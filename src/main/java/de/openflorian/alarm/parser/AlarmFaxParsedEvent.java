package de.openflorian.alarm.parser;

import java.io.File;

import de.openflorian.alarm.AlarmFaxEvent;

/**
 * Alarm Fax Parsed Event<Br/>
 * <br/>
 * Event will be triggered if an alarm fax file is successfuly parsed in the observed directory
 * @author Bastian Kraus <me@bastian-kraus.me>
 *
 */
public class AlarmFaxParsedEvent extends AlarmFaxEvent {

	public AlarmFaxParsedEvent(File result) {
		super(result);
	}
	
}
