package de.openflorian.data.model.security;

import java.util.Collection;
import java.util.LinkedList;

/**
 * "Abstract" Permission Set Interface<br/>
 * <Br/>
 * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!<br/>
 * !!!!!BIG RED FLASHING NOTE!!!!!!!!!!!!<br/>
 * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!<br/>
 * Child classes MUST override {@link PermissionSet#PERMISSON_TYPES()}. 
 * 
 * @author Bastian Kraus <me@bastian-kraus.me>
 */
public interface PermissionSet {

	public static Collection<String> PERMISSION_TYPES() { 
		return new LinkedList<String>();
	}
	
}
