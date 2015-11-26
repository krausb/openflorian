package de.openflorian.data.model;

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
