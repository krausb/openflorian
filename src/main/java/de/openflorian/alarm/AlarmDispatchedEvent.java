package de.openflorian.alarm;

import de.openflorian.data.model.Operation;
import de.openflorian.event.Event;

/**
 * Alarm Dispatched Event<br/>
 * <br/>
 * Indicates that an {@link Operation} is dispatched by the crew.
 * 
 * @author Bastian Kraus <me@bastian-kraus.me>
 */
public class AlarmDispatchedEvent extends Event {

	public AlarmDispatchedEvent(Operation operation) {
		super("AlarmDispatchedEvent", operation);
	}
	
	/**
	 * Get the alarmed {@link Operation}
	 * 
	 * @return
	 * 		{@link Operation}
	 */
	public Operation getOperation() {
		if(this.data != null && this.data instanceof Operation)
			return (Operation)this.data;
		else
			return null;
	}

}
