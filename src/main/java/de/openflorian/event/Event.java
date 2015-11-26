package de.openflorian.event;

/**
 * Base Event<br/>
 * <br/>
 * An event for OpenFlorian Platform.
 * 
 * @author Bastian Kraus <me@bastian-kraus.me>
 */
public class Event {

	protected final String name;
	protected Object data = null;
	protected Object target = null;
	
	/**
	 * Constructs a simple event with a given <code>name</code>
	 * 
	 * @param name
	 */
	public Event(String name) {
		this.name = name;
	}
	
	/**
	 * Constructs a simple event with a given <code>name</code> and <code>data</code>
	 * @param name
	 * @param data
	 */
	public Event(String name, Object data) {
		this(name);
		this.data = data;
	}
	
	/**
	 * Constructs a simple event with a given <code>name</code> and <code>data</code> related to
	 * a <code>target</code>
	 * 
	 * @param name
	 * @param data
	 * @param target
	 */
	public Event(String name, Object data, Object target) {
		this(name, data);
		this.target = target;
	}
	
	/**
	 * Returns the data accompanies with this event, or null if not available.
	 * 
	 * @return
	 * 		{@link Object}
	 */
	public Object getData() {
		return this.data;
	}
	
	/**
	 * Returns the target that receives this event, or null if broadcast.
	 * 
	 * @return
	 */
	public Object getTarget() {
		return this.target;
	}
	
}
