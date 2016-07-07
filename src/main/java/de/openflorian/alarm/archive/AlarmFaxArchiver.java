package de.openflorian.alarm.archive;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.openflorian.EventBusAddresses;
import de.openflorian.alarm.parser.AlarmFaxParsedEvent;
import de.openflorian.config.OpenflorianConfig;
import de.openflorian.util.StringUtils;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.eventbus.Message;

/*
 * This file is part of Openflorian.
 * 
 * Copyright (C) 2016  Bastian Kraus
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

/**
 * Alarm Fax Archiver<br/>
 * <br/>
 * Puts the transformed and parsed Alarm Fax after {@link AlarmFaxParsedEvent}
 * into {@link AlarmFaxArchiver#CONFIG_ARCHIVATION_DIRECTORY}.
 * 
 * @author Bastian Kraus <bofh@k-hive.de>
 */
public class AlarmFaxArchiver extends AbstractVerticle {

	private static final Logger log = LoggerFactory.getLogger(AlarmFaxArchiver.class);

	private String faxObervationDirectory;
	private String faxArchivationDirectory;

	@Override
	public void start(Future<Void> startFuture) {
		log.info("Setting up AlarmFaxArchiver...");

		faxArchivationDirectory = OpenflorianConfig.config().faxObserver.archiveDir;
		faxObervationDirectory = OpenflorianConfig.config().faxObserver.observerDir;

		if (StringUtils.isEmpty(faxArchivationDirectory))
			throw new IllegalStateException("faxArchivationDirectory faxArchivationDirectory is missing/empty.");
		else if (StringUtils.isEmpty(faxObervationDirectory))
			throw new IllegalStateException("Fax observing directory is missing.");
		else {
			log.info("Fax Archive Directory: " + faxArchivationDirectory);
			log.info("Fax Observation Directory: " + faxObervationDirectory);
		}

		log.info("Registering EventBus consumers...");
		vertx.eventBus().consumer(EventBusAddresses.ARCHIVE_FILE, msg -> archive(msg));

		startFuture.complete();
	}

	/**
	 * Takes given <code>event</code> and moves the parsed and transformed Alarm
	 * Fax TIF and TXT files to the
	 * {@link AlarmFaxArchiver#CONFIG_ARCHIVATION_DIRECTORY}
	 * 
	 * @param event
	 */
	private void archive(Message<Object> msg) {
		final String toArchive = msg.body().toString();

		if (log.isDebugEnabled())
			log.debug("Archiving file: " + toArchive);

		final File toArchiveFile = new File(toArchive);

		if (toArchiveFile.exists()) {
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss");
			try {
				final String archiveTarget = String.format("%s%s%s_%s", faxArchivationDirectory, File.separator,
						format.format(new Date()), toArchiveFile.getName());
				if (log.isDebugEnabled())
					log.debug("Archiving target: " + archiveTarget);

				FileUtils.moveFile(toArchiveFile, new File(archiveTarget));
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		} else {
			log.error("File does not exist: " + toArchive);
		}
	}
}
