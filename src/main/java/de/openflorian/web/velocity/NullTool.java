package de.openflorian.web.velocity;

import org.apache.velocity.context.Context;

/**
 * Velocity Null Tool<br/>
 * <Br/>
 * Tool for checking of null values in velocity templates.
 * 
 * @author Bastian Kraus <bk@pogo-systems.de>
 */
public class NullTool {

	public NullTool() {
	}

	/**
	 * Sets the given VTL reference back to <code>null</code>.
	 * 
	 * @param context
	 *            the current Context
	 * @param key
	 *            the VTL reference to set back to <code>null</code>.
	 */
	public void setNull(Context context, String key) {
		if (this.isNull(context)) {
			return;
		}
		context.remove(key);
	}

	/**
	 * Sets the given VTL reference to the given value. If the value is
	 * <code>null</code>, the VTL reference is set to <code>null</code>.
	 * 
	 * @param context
	 *            the current Context
	 * @param key
	 *            the VTL reference to set.
	 * @param value
	 *            the value to set the VTL reference to.
	 */
	public void set(Context context, String key, Object value) {
		if (this.isNull(context)) {
			return;
		}
		if (this.isNull(value)) {
			this.setNull(context, key);
			return;
		}
		context.put(key, value);
	}

	/**
	 * Checks if a VTL reference is <code>null</code>.
	 * 
	 * @param object
	 *            the VTL reference to check.
	 * @return <code>true</code> if the VTL reference is <code>null</code>,
	 *         <code>false</code> if otherwise.
	 */
	public boolean isNull(Object object) {
		return object == null;
	}

	/**
	 * Checks if a VTL reference is not <code>null</code>.
	 * 
	 * @param object
	 *            the VTL reference to check.
	 * @return <code>true</code> if the VTL reference is not <code>null</code>,
	 *         <code>false</code> if otherwise.
	 */
	public boolean isNotNull(Object object) {
		return !this.isNull(object);
	}

	/**
	 * A convinient method which returns <code>null</code>. Actually, this tool
	 * will work the same without this method, because Velocity treats
	 * non-existing methods as null. :)
	 * 
	 * @return <code>null</code>
	 */
	public Object getNull() {
		return null;
	}

}