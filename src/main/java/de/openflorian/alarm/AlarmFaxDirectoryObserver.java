package de.openflorian.alarm;

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

import java.io.File;
import java.io.IOException;
import java.nio.file.ClosedWatchServiceException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.openflorian.alarm.parser.AlarmFaxParsedEvent;
import de.openflorian.config.ConfigurationProvider;
import de.openflorian.event.EventQueue;

/**
 * Alarm Telefax Directory Observer<br/>
 * <br/>
 * Observes a given directory for
 * 
 * @author Bastian Kraus <me@bastian-kraus.me>
 */
public class AlarmFaxDirectoryObserver implements Runnable {

	private final Logger log = LoggerFactory.getLogger(getClass());

	public static final String CONFIG_OBSERVING_DIRECTORY = "alarm.observer.faxdir";

	private final File observingDirectory;

	private WatchService watcher;
	private WatchKey key;
	
	private boolean isRunning = false;
	private boolean shutdown = false;

	public AlarmFaxDirectoryObserver( ConfigurationProvider config) {
		if (config == null)
			throw new IllegalStateException("No ConfigurationProvider in the Spring Context available.");

		String dir = config.getProperty(CONFIG_OBSERVING_DIRECTORY);
		log.debug(CONFIG_OBSERVING_DIRECTORY + ": " + dir);
		this.observingDirectory = new File(dir);
		
		if (this.observingDirectory == null || !this.observingDirectory.isDirectory())
			throw new IllegalStateException("Configuration does not contain '"
					+ CONFIG_OBSERVING_DIRECTORY + "'");
		else
			log.info("Observing directory: " + this.observingDirectory.getAbsolutePath());
	}

	@Override
	public void run() {
		isRunning = true;

		log.info("Observer running: " + isRunning);
		
		// SETUP
		watcher = null;
		try {
			watcher = FileSystems.getDefault().newWatchService();
			this.observingDirectory.toPath().register(watcher, StandardWatchEventKinds.ENTRY_CREATE);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
		
		if (watcher == null)
			throw new IllegalStateException(
					"No FileSystem WatcherService could be created.");

		while (true) {
			key = null;
			try {
				if (shutdown) {
					break;
				}

				// wait for key to be signaled
				try {
					key = watcher.take();
				} catch (InterruptedException x) {
					continue;
				}

				for (WatchEvent<?> event : key.pollEvents()) {
					WatchEvent.Kind<?> kind = event.kind();

					if (kind == StandardWatchEventKinds.OVERFLOW) {
						continue;
					} else if(kind == StandardWatchEventKinds.ENTRY_CREATE) {
						log.debug("ENTRY_CREATE event triggered...");
						@SuppressWarnings("unchecked")
						WatchEvent<Path> ev = (WatchEvent<Path>) event;
						Path filename = ev.context();
	
						try {
							Path child = this.observingDirectory.toPath().resolve(filename);
							log.debug("File: " + child.toFile().getAbsolutePath());
							
							AlarmFaxEvent alarmFaxEvent = null;
							if (Files.probeContentType(child).startsWith("image/")) {
								log.debug("Image detected... triggering AlarmFaxIncomingEvent...");
								alarmFaxEvent = new AlarmFaxIncomingEvent(child.toFile());
							} else if (Files.probeContentType(child).startsWith("text/plain")) {
								log.debug("Textfile detected... triggering AlarmFaxParsedEvent...");
								alarmFaxEvent = new AlarmFaxParsedEvent(child.toFile());
							}
							if(alarmFaxEvent != null)
								EventQueue.getInstance().publish(alarmFaxEvent);
						} catch (IOException e) {
							log.error(e.getMessage(), e);
							continue;
						}
					}
				}
			} catch (ClosedWatchServiceException e) {
				log.error(e.getMessage(), e);
				break;
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
			
			if(key != null) {
			    boolean valid = key.reset();
			    if (!valid) {
			        break;
			    }
			}
		}

		isRunning = false;
	}

	/**
	 * Determines wether the observer is running or not;
	 * 
	 * @return
	 */
	public boolean isRunning() {
		return this.isRunning;
	}

	/**
	 * Trys to shutdown the observer
	 */
	public void shutdown() {
		this.shutdown = true;
		try {
			watcher.close();
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
		if(key != null)
			key.cancel();
	}
	
}
