package de.openflorian.alarm;

/**
 * Alarm Fax Event Listener
 * 
 * @author Bastian Kraus <me@bastian-kraus.me>
 */
public interface AlarmFaxEventListener {

	/**
	 * Notify target about {@link AlarmFaxEvent}
	 * @param event
	 * 		{@link AlarmFaxEvent}
	 */
	public void notify(AlarmFaxEvent event);
	
}
