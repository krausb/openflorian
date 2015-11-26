package de.openflorian.service;

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

import java.util.List;

import javax.persistence.NoResultException;
import javax.xml.bind.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import de.openflorian.data.dao.security.PermissionDao;
import de.openflorian.web.user.Permission;
import de.openflorian.web.user.User;

/**
 * {@link User} Permission Service
 * 
 * Permission service is responsable for determining and handling
 * (GRANT, REVOKE) a {@link User}s permission.
 * 
 * @author Bastian Kraus <me@bastian-kraus.me>
 */
@Service
public class PermissionService {

	@Autowired
	private PermissionDao dao;

		/**
		 * Get a specific {@link Permission} for a given <code>u</code> and
		 * a given <code>permission</code>.
		 * 
		 * @param user
		 * @param permission
		 * @return
		 */
		@Transactional(propagation=Propagation.REQUIRED,readOnly=true)
		public Permission getByUserAndPermission(User user, String permission){
			return dao.getByUserAndPermission(user, permission);
		}
		
		/**
		 * Get a specific {@link Permission} for a given <code>u</code> and
		 * a given <code>permission</code>.
		 * 
		 * @param user
		 * @param permission
		 * @return
		 */
		@Transactional(propagation=Propagation.REQUIRED,readOnly=true)
		public boolean hasPermission(User user, String permission) {
			try {
				return dao.getByUserAndPermission(user, permission) != null;
			} catch (NoResultException e) {
				// nothing to do here
			}
			return false;
		}
		
		/**
		 * List all permissions of the given <code>userid</code>.
		 * 
		 * @param userid
		 * @return
		 * 		{@link List}<{@link Permission}>
		 */
		public List<Permission> list(long userid){
			return dao.findAllByAttribute("id", userid);
		}
		
		/**
		 * Persist given <code>permission</code>.
		 * 
		 * @param permission
		 * @return
		 * 		{@link Permission}
		 * @throws ValidationException
		 */
		@Transactional(propagation=Propagation.REQUIRES_NEW)
		public Permission persist(Permission permission) throws ValidationException{
			return dao.merge(permission);
		}
		
		/**
		 * Remove a specific {@link Permission} identified by given <code>userid</code>
		 * and <code>permission</code> from persistence context.
		 * 
		 * @param userid
		 * @param permission
		 */
		@Transactional(propagation=Propagation.REQUIRES_NEW)
		public void remove(User user,  String permission){
			Permission p = this.getByUserAndPermission(user, permission);
			if(p != null)
				dao.remove(p.getId());
		}
		
		/**
		 * Remove a specific {@link Permission} identified by given 
		 * <code>id</code> from persistence context.
		 * 
		 * @param id
		 */
		@Transactional(propagation=Propagation.REQUIRES_NEW)
		public void remove( long id){
			dao.remove(id);
		}
	
}
