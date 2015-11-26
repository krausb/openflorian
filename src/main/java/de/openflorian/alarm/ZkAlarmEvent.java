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
