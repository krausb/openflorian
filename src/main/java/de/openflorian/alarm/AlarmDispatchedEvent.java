package de.openflorian.alarm;

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
