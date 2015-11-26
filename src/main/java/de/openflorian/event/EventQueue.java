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

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.openflorian.alarm.AlarmFaxEventListener;

/**
 * Event Queue Service (Singleton)<br/>
 * 
 * @author Bastian Kraus <me@bastian-kraus.me>
 */
public class EventQueue {
	
	private Logger log = LoggerFactory.getLogger(getClass());
	
	private static EventQueue instance = null;
	public static EventQueue getInstance() {
		if(instance == null)
			instance = new EventQueue();
		return instance;
	}
	
	private List<EventListener<?>> listener = new LinkedList<EventListener<?>>();
	
	/**
	 * Notify all registered {@link AlarmFaxEventListener}
	 */
	public void publish(Event event) {
		log.debug("Notifying registered listeners for: " + event.getClass().getSimpleName());
		
		listener.stream()
			.filter(l -> l.getEventType().isAssignableFrom(event.getClass()))
			.collect(Collectors.toList())
			.forEach(l -> {
				Thread t = new Thread(new Runnable() {

					@Override
					public void run() {
						l.onEvent(event);
					}
				
				});
				t.setDaemon(true);
				t.start();
			});
	}
	
	/**
	 * Register an {@link EventListener}
	 * 
	 * @param listener
	 */
	public void subscribe(EventListener<?> listener) {
		if(this.listener == null)
			throw new IllegalStateException("No listener container present.");
		
		if(!this.listener.contains(listener))
			this.listener.add(listener);
	}
	
	/**
	 * Remove an {@link EventListener} subscription
	 * 
	 * @param listener
	 */
	public void unsubscribe(EventListener<?> listener) {
		if(this.listener == null)
			throw new IllegalStateException("No listener container present.");
		
		if(this.listener.contains(listener))
			this.listener.remove(listener);
	}
	
}
