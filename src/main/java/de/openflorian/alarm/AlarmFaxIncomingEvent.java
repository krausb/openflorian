package de.openflorian.alarm;

import java.io.File;

/**
 * Alarm Fax Incoming Event<Br/>
 * <br/>
 * Event will be triggered if an alarm fax image file is created in the observed directory
 * @author Bastian Kraus <me@bastian-kraus.me>
 *
 */
public class AlarmFaxIncomingEvent extends AlarmFaxEvent {

	public AlarmFaxIncomingEvent(File result) {
		super(result);
	}
	
}
