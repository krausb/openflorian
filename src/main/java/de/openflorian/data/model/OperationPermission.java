package de.openflorian.data.model;

import java.util.Collection;
import java.util.LinkedList;

import de.openflorian.data.model.security.PermissionSet;

/**
 * Operations Module Permissions
 * 
 *  * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!<br/>
 * !!!!!BIG RED FLASHING NOTE!!!!!!!!!!!!<br/>
 * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!<br/>
 * If you change or add permissions keep sure that 
 * you update {@link PermissionSet#PERMISSION_TYPES()} method.
 * 
 * @author Bastian Kraus <me@bastian-kraus.me>
 */
public class OperationPermission implements PermissionSet {

	public static final String ACCESS = "OPERATION";
	
	public static final String ADMINISTER = "OPERATION_ADMINISTER";
	
	public static Collection<String> PERMISSION_TYPES() {
		Collection<String> col = new LinkedList<String>();
		col.add(ACCESS);
		col.add(ADMINISTER);
		
		return col;
	}
	
}
