package de.openflorian.alarm.parser;

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

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jfree.util.Log;
import org.springframework.beans.factory.annotation.Autowired;

import de.openflorian.OpenflorianContext;
import de.openflorian.data.model.Operation;
import de.openflorian.data.model.OperationResource;
import de.openflorian.service.OperationResourceService;
import de.openflorian.util.StringUtils;

/**
 * Street Parser Responsable
 * 
 * @author Bastian Kraus <me@bastian-kraus.me>
 */
class ResourcesParserResponsable extends AlarmFaxParserPatternMatcherResponsable {

	public static final String CONFIG_PATTERN = "alarm.parser.pattern.resources";
	public static final String CONFIG_STATION_RESOURCE_PATTERN = "alarm.parser.pattern.stationresource";
	public static final String CONFIG_RESOURCE_CALLNAME_PATTERN = "alarm.parser.pattern.resourcecallname";
	
	protected Pattern callNamePattern = null;
	protected String stationresourcePattern = null;
	
	@Autowired(required=true)
	private OperationResourceService resourceService;
	
	public ResourcesParserResponsable() {
		super();
		
		try {
			String callNamePatternStr = OpenflorianContext.getConfig().getProperty(CONFIG_RESOURCE_CALLNAME_PATTERN);
			if(!StringUtils.isEmpty(callNamePatternStr)) {
				callNamePattern = Pattern.compile(callNamePatternStr);
				log.info(CONFIG_RESOURCE_CALLNAME_PATTERN + ": " + callNamePatternStr);
			}
			
			stationresourcePattern = OpenflorianContext.getConfig().getProperty(CONFIG_STATION_RESOURCE_PATTERN);
			if(StringUtils.isEmpty(stationresourcePattern))
				throw new IllegalStateException(CONFIG_STATION_RESOURCE_PATTERN + " not set in configuration file.");
			else
				log.info(CONFIG_STATION_RESOURCE_PATTERN + ": " + stationresourcePattern);
		} catch (Exception e) {
			Log.error(e.getMessage(), e);
		}
		
	}
	
	@Override
	public String getConfigurationProperty() {
		return CONFIG_PATTERN;
	}

	@Override
	public Pattern getPattern() {
		return this.parserPattern;
	}

	@Override
	public void parse(String alarmfax, Operation operation) {
		Matcher m = getPattern().matcher(alarmfax);
		StringBuffer sb = new StringBuffer();
		while(m.find()) {
			sb.append(m.group(1));
			sb.append(System.getProperty("line.separator"));
			
			if(resourceService != null) {
				if(m.group(1).contains(stationresourcePattern)) {
					Matcher callNameMatcher = callNamePattern.matcher(m.group(1));
					if(callNameMatcher.find()) {
						OperationResource resource = resourceService.getResourceByCallname(callNameMatcher.group(1));
						if(operation.getResources() == null) operation.setResources(new ArrayList<OperationResource>());
						operation.getResources().add(resource);
					}
				}
			}
		}
		operation.setResourcesRaw(sb.toString());
		
		if(getNext() != null)
			getNext().parse(alarmfax, operation);
	}

}
