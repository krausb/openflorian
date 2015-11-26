package de.openflorian.event;

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
