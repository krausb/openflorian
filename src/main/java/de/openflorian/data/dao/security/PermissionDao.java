package de.openflorian.data.dao.security;

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

import javax.persistence.Query;

import de.openflorian.data.jpa.GenericDao;
import de.openflorian.web.user.Permission;
import de.openflorian.web.user.User;

/**
 * {@link Permission} DAO<br/>
 * 
 * @author Bastian Kraus <me@bastian-kraus.me>
 */
public class PermissionDao extends GenericDao<Permission, Long>{

	/**
	 * Get a specific {@link Permission} for a given <code>u</code> and
	 * a given <code>permission</code>.
	 * 
	 * @param u
	 * @param permission
	 * @return
	 * 		{@link Permission}
	 */
	public Permission getByUserAndPermission(User u, String permission) {
    	Query query = getEntityManager().createQuery(
    			"from " + Permission.class.getCanonicalName() + " p " +
    			"WHERE p.user.id = :userid " + 
    			"AND p.permission = :permission");
    	query.setParameter("userid", u.getId());
    	query.setParameter("permission", permission);
    	return (Permission)query.getSingleResult();
    }
	
	
}
