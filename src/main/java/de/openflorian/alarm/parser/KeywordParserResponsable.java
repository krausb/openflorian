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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.openflorian.data.model.Operation;

/**
 * Keyword Parser Responsable
 * 
 * @author Bastian Kraus <me@bastian-kraus.me>
 */
class KeywordParserResponsable extends AlarmFaxParserPatternMatcherResponsable {

	public static final String CONFIG_PATTERN = "alarm.parser.pattern.keyword";
	
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
		if(m.find()) {
			operation.setKeyword(m.group(1));
		}
		
		if(getNext() != null)
			getNext().parse(alarmfax, operation);
	}

}
