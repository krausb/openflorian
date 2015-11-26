package de.openflorian.alarm;

import org.zkoss.zk.ui.event.Event;

import de.openflorian.data.model.Operation;

/**
 * Alarm Event<br/>
 * <br/>
 * Indicates that an {@link Operation} is triggered by an alarm fax.
 * 
 * @author Bastian Kraus <me@bastian-kraus.me>
 */
public class ZkAlarmEvent extends Event {
	private static final long serialVersionUID = -2031391582703470669L;
	
	private final Operation operation;
	
	public ZkAlarmEvent(Operation operation) {
		super("AlarmEvent");
		this.operation = operation;
	}
	
	/**
	 * Get the alarmed {@link Operation}
	 * 
	 * @return
	 * 		{@link Operation}
	 */
	public Operation getOperation() {
		return this.operation;
	}

}
