package de.openflorian.event;

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

import java.lang.reflect.ParameterizedType;

/**
 * EventListener<br/>
 * <br/>
 * An event listener that will be notified when an event occurs, if
 * registered to {@link EventQueue}.
 * 
 * @author Bastian Kraus <me@bastian-kraus.me>
 * @param <T>
 */
public abstract class EventListener<T extends Event> {

	protected Class<?> genericType;
	
	@SuppressWarnings("unchecked")
	public EventListener() {
		genericType = (Class<T>)((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}
	
	/**
	 * Notifies the listener that an event occurs.
	 * @param event
	 */
	public abstract void onEvent(Event event);
	
	/**
	 * Get the generic type argument class
	 * @return
	 */
	public Class<?> getEventType() {
		return genericType;
	}
	
}
