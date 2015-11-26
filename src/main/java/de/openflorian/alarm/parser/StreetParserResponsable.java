package de.openflorian.alarm.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.openflorian.data.model.Operation;

/**
 * Street Parser Responsable
 * 
 * @author Bastian Kraus <me@bastian-kraus.me>
 */
class StreetParserResponsable extends AlarmFaxParserPatternMatcherResponsable {

	public static final String CONFIG_PATTERN = "alarm.parser.pattern.street";
		
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
			operation.setStreet(m.group(1));
		}
		
		if(getNext() != null)
			getNext().parse(alarmfax, operation);
	}

}
