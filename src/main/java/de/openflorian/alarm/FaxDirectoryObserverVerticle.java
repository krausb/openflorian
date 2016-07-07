package de.openflorian.alarm;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.openflorian.EventBusAddresses;
import de.openflorian.config.OpenflorianConfig;

/*
 * This file is part of Openflorian.
 * 
 * Copyright (C) 2015  Bastian Kraus
 * 
 * Openflorian is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version)
 *     
 * Openflorian is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *     
 * You should have received a copy of the GNU General Public License
 * along with Openflorian.  If not, see <http://www.gnu.org/licenses/>.
 */

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Handler;

/**
 * Alarm Telefax Directory Observer<br/>
 * <br/>
 * Observes a given directory for
 * 
 * @author Bastian Kraus <bofh@k-hive.de>
 */
public class FaxDirectoryObserverVerticle extends AbstractVerticle {

	private final Logger log = LoggerFactory.getLogger(getClass());

	public static final String CONFIG_OBSERVING_DIRECTORY = "alarm.observer.faxdir";

	private final File observingDirectory;

	private WatchService watcher;
	private WatchKey key;

	public FaxDirectoryObserverVerticle() {
		String dir = OpenflorianConfig.config().faxObserver.observerDir;
		log.debug(CONFIG_OBSERVING_DIRECTORY + ": " + dir);
		this.observingDirectory = new File(dir);

		if (this.observingDirectory == null || !this.observingDirectory.isDirectory())
			throw new IllegalStateException("Configuration does not contain '" + CONFIG_OBSERVING_DIRECTORY + "'");
		else
			log.info("Observing directory: " + this.observingDirectory.getAbsolutePath());
	}

	@Override
	public void start(Future<Void> startFuture) {

		log.info("Starting Directory Observer Verticle...");

		// SETUP
		watcher = null;
		try {
			watcher = FileSystems.getDefault().newWatchService();
			this.observingDirectory.toPath().register(watcher, StandardWatchEventKinds.ENTRY_CREATE);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}

		if (watcher == null)
			throw new IllegalStateException("No FileSystem WatcherService could be created.");

		vertx.setPeriodic(500, new Handler<Long>() {

			@Override
			public void handle(Long arg0) {
				try {
					processEvents();
				} catch (IOException e) {
					log.error(e.getMessage(), e);
				} catch (Exception e) {
					log.error(e.getMessage(), e);
				}
			}

		});

		startFuture.complete();
	}

	@Override
	public void stop() {
		log.info("Stopping Directory Observer Verticle...");
		try {
			if (key != null) {
				key.cancel();
				key.pollEvents();
			}
			watcher.close();
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
	}

	/**
	 * Helper: Process directory events
	 */
	private synchronized void processEvents() throws IOException, Exception {

		while (true) {
			// wait for key to be signaled
			key = watcher.poll();
			if (key == null)
				break;

			List<WatchEvent<?>> events = key.pollEvents();
			for (WatchEvent<?> event : events) {
				WatchEvent.Kind<?> kind = event.kind();

				if (kind == StandardWatchEventKinds.OVERFLOW) {
					continue;
				} else if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
					log.debug("ENTRY_CREATE event triggered...");
					@SuppressWarnings("unchecked")
					WatchEvent<Path> ev = (WatchEvent<Path>) event;
					Path filename = ev.context();

					Path child = this.observingDirectory.toPath().resolve(filename);
					log.debug("File: " + child.toFile().getAbsolutePath());

					final String contentType = Files.probeContentType(child);
					if (contentType != null && contentType.startsWith("image/")) {
						AlarmFaxEvent alarmFaxEvent = new AlarmFaxEvent(child.toFile());
						vertx.eventBus().send(EventBusAddresses.ALARMFAX_NEWFAX, alarmFaxEvent);
						log.debug("Successfuly published to " + EventBusAddresses.ALARMFAX_NEWFAX + ".");
						break;
					}
				}
			}

			if (key != null) {
				boolean valid = key.reset();
				key.pollEvents();
				if (!valid) {
					break;
				}
			}
		}
	}

}
