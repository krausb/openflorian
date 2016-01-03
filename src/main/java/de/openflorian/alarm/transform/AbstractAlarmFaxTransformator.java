package de.openflorian.alarm.transform;

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

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import de.openflorian.alarm.AlarmFaxDirectoryObserver;
import de.openflorian.alarm.AlarmFaxIncomingEvent;
import de.openflorian.config.ConfigurationProvider;

/**
 * Alarm Fax Transformator<br/>
 * <br/>
 * Processes the given alarm fax with an OCR binary like
 * tesseract.
 * 
 * @author Bastian Kraus <me@bastian-kraus.me>
 */
public abstract class AbstractAlarmFaxTransformator implements InitializingBean {

	protected final Logger log = LoggerFactory.getLogger(getClass());
	
	public static final String CONFIG_TRANSFORMATION_CMD = "alarm.transform.cmd";
	public static final String CONFIG_TRANSFORMATION_VAR_OUTPUT = "alarm.transform.var.output";
	public static final String CONFIG_TRANSFORMATION_VAR_INPUT = "alarm.transform.var.input";

	@Autowired(required=true)
	protected ConfigurationProvider config;
	
	protected String transformationCmd;
	protected String faxObervationDirectory;
	
	protected String transformationCmdVarInput;
	protected String transformationCmdVarOutput;
	
	@Override
	public void afterPropertiesSet() {
		if(config == null)
			throw new IllegalStateException("No ConfigurationProvider present/injected.");
		
		log.info("Setting up AlarmFaxTransformator...");
		
		transformationCmd = config.getProperty(CONFIG_TRANSFORMATION_CMD);
		transformationCmdVarInput = config.getProperty(CONFIG_TRANSFORMATION_VAR_INPUT);
		transformationCmdVarOutput = config.getProperty(CONFIG_TRANSFORMATION_VAR_OUTPUT);
		faxObervationDirectory = config.getProperty(AlarmFaxDirectoryObserver.CONFIG_OBSERVING_DIRECTORY);
		
		if(StringUtils.isEmpty(transformationCmd))
			throw new IllegalStateException("Transformation command '" + CONFIG_TRANSFORMATION_CMD + "' is missing.");
		else if(StringUtils.isEmpty(faxObervationDirectory)) 
			throw new IllegalStateException("Fax observing directory '" + AlarmFaxDirectoryObserver.CONFIG_OBSERVING_DIRECTORY + "' is missing.");
		else {
			log.info(CONFIG_TRANSFORMATION_CMD + ": " + transformationCmd);
			log.info(CONFIG_TRANSFORMATION_VAR_INPUT + ": " + transformationCmdVarInput);
			log.info(CONFIG_TRANSFORMATION_VAR_OUTPUT + ": " + transformationCmdVarOutput);
			log.info(AlarmFaxDirectoryObserver.CONFIG_OBSERVING_DIRECTORY + ": " + faxObervationDirectory);
		}
	}
	
	/**
	 * Transform the incoming file from {@link AlarmFaxIncomingEvent#getResultFile()}
	 * with 
	 * @param event
	 */
	public abstract void transform(AlarmFaxIncomingEvent event);

}