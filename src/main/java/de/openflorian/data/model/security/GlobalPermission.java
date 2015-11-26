package de.openflorian.data.model.security;

import java.util.Collection;
import java.util.LinkedList;

/**
 * Global Permission Set
 * 
 * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!<br/>
 * !!!!!BIG RED FLASHING NOTE!!!!!!!!!!!!<br/>
 * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!<br/>
 * If you change or add permissions keep sure that 
 * you update {@link PermissionSet#PERMISSION_TYPES()} method.
 * 
 * @author Bastian Kraus <me@bastian-kraus.me>
 */
public class GlobalPermission implements PermissionSet {

	public static final String ACCESS = "SYSTEM_ACCESS";
	
	public static final String ADMINISTER = "SYSTEM_ADMINISTER";
	
	public static Collection<String> PERMISSION_TYPES() {
		Collection<String> col = new LinkedList<String>();
		col.add(ACCESS);
		col.add(ADMINISTER);
		return col;
	}
	
}
