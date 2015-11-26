package de.openflorian.alarm;

import de.openflorian.data.model.Operation;
import de.openflorian.event.Event;

/**
 * Alarm Event<br/>
 * <br/>
 * Indicates that an {@link Operation} is triggered by an alarm fax.
 * 
 * @author Bastian Kraus <me@bastian-kraus.me>
 */
public class AlarmEvent extends Event {

	public AlarmEvent(Operation operation) {
		super("AlarmEvent", operation);
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
