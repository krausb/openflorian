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

import de.openflorian.config.OpenflorianConfig;
import de.openflorian.data.model.Operation;
import de.openflorian.data.model.OperationResource;
import de.openflorian.service.OperationResourceService;
import de.openflorian.util.StringUtils;

/**
 * Street Parser Responsable
 * 
 * @author Bastian Kraus <bofh@k-hive.de>
 */
class ResourcesParserResponsable extends AlarmFaxParserPatternMatcherResponsable {

	protected Pattern callNamePattern = null;
	protected String stationresourcePattern = null;

	private OperationResourceService resourceService = OperationResourceService.transactional();

	public ResourcesParserResponsable() {
		super();

		try {
			String callNamePatternStr = OpenflorianConfig.config().faxParser.patterns.resourcecallnamePattern;
			if (!StringUtils.isEmpty(callNamePatternStr)) {
				callNamePattern = Pattern.compile(callNamePatternStr);
				log.info("Resource callName pattern: " + callNamePatternStr);
			} else {
				throw new IllegalStateException("Resource callName pattern not set in configuration file.");
			}

			stationresourcePattern = OpenflorianConfig.config().faxParser.patterns.stationresourcePattern;
			if (StringUtils.isEmpty(stationresourcePattern))
				throw new IllegalStateException("Station resource pattern not set in configuration file.");
			else
				log.info("Station resource pattern: " + stationresourcePattern);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

	}

	@Override
	public Pattern getPattern() {
		return Pattern.compile(OpenflorianConfig.config().faxParser.patterns.resourcesPattern,
				Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.UNICODE_CASE);
	}

	@Override
	public void parse(String alarmfax, Operation operation) {
		Matcher m = getPattern().matcher(alarmfax);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			sb.append(m.group(1));
			sb.append(System.getProperty("line.separator"));

			if (m.group(1).contains(stationresourcePattern)) {
				Matcher callNameMatcher = callNamePattern.matcher(m.group(1));
				if (callNameMatcher.find()) {
					OperationResource resource = resourceService.getResourceByCallname(callNameMatcher.group(1));
					if (operation.getResources() == null)
						operation.setResources(new ArrayList<OperationResource>());
					operation.getResources().add(resource);
				}
			}
		}
		operation.setResourcesRaw(sb.toString());

		if (getNext() != null)
			getNext().parse(alarmfax, operation);
	}

}
