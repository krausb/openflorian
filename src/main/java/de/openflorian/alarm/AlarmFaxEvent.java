package de.openflorian.alarm;

import java.io.File;

import de.openflorian.event.Event;

/**
 * Alarm Fax Event
 * 
 * @author Bastian Kraus <me@bastian-kraus.me>
 */
public abstract class AlarmFaxEvent extends Event {
	
	public AlarmFaxEvent(File result) {
		super("AlarmFaxEvent", result);
	}

	/**
	 * Get the resulting file of the triggered event.
	 * 
	 * @return
	 * 		{@link File}
	 */
	public File getResultFile() {
		return (File)this.data;
	}
	
}
