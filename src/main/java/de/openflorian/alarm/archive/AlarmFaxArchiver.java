package de.openflorian.alarm.archive;

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

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.eventbus.Message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import de.openflorian.EventBusAdresses;
import de.openflorian.OpenflorianContext;
import de.openflorian.alarm.FaxDirectoryObserverVerticle;
import de.openflorian.alarm.parser.AlarmFaxParsedEvent;
import de.openflorian.config.ConfigurationProvider;
import de.openflorian.util.StringUtils;

/**
 * Alarm Fax Archiver<br/>
 * <br/>
 * Puts the transformed and parsed Alarm Fax after {@link AlarmFaxParsedEvent}
 * into {@link AlarmFaxArchiver#CONFIG_ARCHIVATION_DIRECTORY}.
 * 
 * @author Bastian Kraus <me@bastian-kraus.me>
 */
public class AlarmFaxArchiver extends AbstractVerticle {

	protected final Logger log = LoggerFactory.getLogger(getClass());
	
	public static final String CONFIG_ARCHIVATION_DIRECTORY = "alarm.observer.archivedir";
	
	private String faxObervationDirectory;
	private String faxArchivationDirectory;
	
	@Override
	public void start(Future<Void> startFuture) {
		ConfigurationProvider config = OpenflorianContext.getConfig();
		if(config == null)
			throw new IllegalStateException("No ConfigurationProvider present/injected.");
		
		log.info("Setting up AlarmFaxArchiver...");
 
		faxArchivationDirectory = config.getProperty(CONFIG_ARCHIVATION_DIRECTORY);
		faxObervationDirectory = config.getProperty(FaxDirectoryObserverVerticle.CONFIG_OBSERVING_DIRECTORY);
		
		if(StringUtils.isEmpty(faxArchivationDirectory))
			throw new IllegalStateException("faxArchivationDirectory faxArchivationDirectory '" + CONFIG_ARCHIVATION_DIRECTORY + "' is missing.");
		else if(StringUtils.isEmpty(faxObervationDirectory)) 
			throw new IllegalStateException("Fax observing directory '" + FaxDirectoryObserverVerticle.CONFIG_OBSERVING_DIRECTORY + "' is missing.");
		else {
			log.info(CONFIG_ARCHIVATION_DIRECTORY + ": " + faxArchivationDirectory);
			log.info(FaxDirectoryObserverVerticle.CONFIG_OBSERVING_DIRECTORY + ": " + faxObervationDirectory);
		}
		
		log.info("Registering EventBus consumers...");
		vertx.eventBus().consumer(EventBusAdresses.ALARMFAX_PARSED, msg -> archive(msg));
		
		startFuture.complete();
	}

	/**
	 * Takes given <code>event</code> and moves the parsed and transformed
	 * Alarm Fax TIF and TXT files to the {@link AlarmFaxArchiver#CONFIG_ARCHIVATION_DIRECTORY}
	 *  
	 * @param event
	 */
	public void archive(Message<Object> msg) {
		
	}
}
