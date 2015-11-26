package de.openflorian.alarm.transform;

import java.io.File;

import de.openflorian.alarm.AlarmFaxEvent;

/**
 * Alarm Fax Parsed Event<Br/>
 * <br/>
 * Event will be triggered if an alarm fax file is successfuly parsed in the observed directory
 * @author Bastian Kraus <me@bastian-kraus.me>
 *
 */
public class AlarmFaxTransformedEvent extends AlarmFaxEvent {

	public AlarmFaxTransformedEvent(File result) {
		super(result);
	}

}
