package de.openflorian.ui;

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

import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Panel;

import de.openflorian.data.model.security.GlobalPermission;
import de.openflorian.service.PermissionService;
import de.openflorian.web.core.ContainerManager;
import de.openflorian.web.user.AuthenticatedUserSessionLocal;
import de.openflorian.web.user.User;
import de.openflorian.zk.AbstractGuiController;

/**
 * Navigation Controller
 * 
 * @author Bastian Kraus <me@bastian-kraus.me>
 */
public class NavigationController extends AbstractGuiController {
	private static final long serialVersionUID = 1182851061480627950L;

	private Logger log = LoggerFactory.getLogger(getClass());

	/*
	 * System Management
	 */
	private Panel systemManagementPanel;
	private Listitem systemconfigurationButtonItem;
	private Listcell systemconfigurationButton;
	private Listitem systeminformationButtonItem;
	private Listcell systeminformationButton;

	/*
	 * User Management
	 */
	private Listitem usermanagementButtonItem;
	private Listcell usermanagementButton;
	
	/*
	 * Operation Management
	 */
	private Listitem operationsItem;
	private Listcell operationsButton;
	
	private User currentUser;

	@Override
	public String getLoginPage() {
		return ZkGlobals.PAGE_LOGIN;
	}

	@Override
	public boolean mayView(Principal user) {
		return true;
	}

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		log.debug("doAfterCompose(...)...");
		
		currentUser = AuthenticatedUserSessionLocal.get();
		
		// apply permissions
		applySystemModulePermissions();
	}

	/**
	 * Helper: Apply System Module permissions
	 */
	private void applySystemModulePermissions() {
		PermissionService permissionService = (PermissionService)ContainerManager.getComponent("permissionService");
		
		boolean mayAdminister = currentUser != null && permissionService.hasPermission(currentUser, GlobalPermission.ADMINISTER);
		systemManagementPanel.setVisible(mayAdminister);
		usermanagementButtonItem.setVisible(mayAdminister);
		systemconfigurationButtonItem.setVisible(mayAdminister);
		systeminformationButtonItem.setVisible(mayAdminister);
	}
	
	/**
	 * Event-Handler: systemconfigurationButton.onClick
	 * 
	 * @param event
	 */
	public void onClick$systemconfigurationButton(Event event) {
		log.debug("systemconfigurationButton.onClick()");
		setContentZul( ZkGlobals.PAGE_SYSTEM_PROPERTIES );

	}
	
	/**
	 * Event-Handler: usermanagementButton.onClick
	 * 
	 * @param event
	 */
	public void onClick$usermanagementButton(Event event) {
		log.debug("usermanagementButton.onClick()");
		setContentZul( ZkGlobals.PAGE_SYSTEM_USERS );
	}
	
	/**
	 * Event-Handler: systeminformationButton.onClick
	 * 
	 * @param event
	 */
	public void onClick$systeminformationButton(Event event) {
		log.debug("systeminformationButton.onClick()");
		setContentZul( ZkGlobals.PAGE_SYSTEM_INFORMATION );
	}
	
	/**
	 * Event-Handler: operationsButton.onClick
	 * 
	 * @param event
	 */
	public void onClick$operationsButton(Event event) {
		log.debug("operationsButton.onClick()");
		setContentZul( ZkGlobals.PAGE_OPERATION_LIST );
	}
	
}
