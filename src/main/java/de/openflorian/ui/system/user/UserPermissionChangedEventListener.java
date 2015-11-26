package de.openflorian.ui.system.user;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.event.CheckEvent;
import org.zkoss.zk.ui.event.EventListener;

import de.openflorian.service.PermissionService;
import de.openflorian.web.core.ContainerManager;
import de.openflorian.web.user.AuthenticatedUserSessionLocal;
import de.openflorian.web.user.Permission;
import de.openflorian.web.user.User;

/**
 * {@link EventListener} for User Permission Checkbox Changed listener
 * 
 * @author Bastian Kraus <me@bastian-kraus.me>
 */
public class UserPermissionChangedEventListener implements EventListener<CheckEvent> {
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	private final User user;
	private final String permission;
	
	public UserPermissionChangedEventListener(User user, String permission) {
		this.user = user;
		this.permission = permission;
	}
	
	@Override
	public void onEvent(CheckEvent event) throws Exception {
		PermissionService permissionService = (PermissionService)ContainerManager.getComponent("permissionService");
		if(permissionService == null)
			throw new IllegalStateException("No permission service bean availalbe!");
		
		if(event.isChecked()) {
			log.info(String.format("userId=%d GRANTed permission: userId=%d; permission=%s", AuthenticatedUserSessionLocal.get().getId(), user.getId(), permission));
			permissionService.persist(Permission.fromUserAndPermission(user, permission));			
		} else {
			log.info(String.format("userId=%d REVOKEd permission: userId=%d; permission=%s", AuthenticatedUserSessionLocal.get().getId(), user.getId(), permission));
			permissionService.remove(user, permission);
		}
	}

}
