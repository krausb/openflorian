package de.openflorian.event;

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
