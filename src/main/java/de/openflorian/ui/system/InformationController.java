package de.openflorian.ui.system;

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
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Timer;

import de.openflorian.data.model.security.GlobalPermission;
import de.openflorian.service.PermissionService;
import de.openflorian.ui.ZkGlobals;
import de.openflorian.web.core.ContainerManager;
import de.openflorian.web.system.SystemInformation;
import de.openflorian.web.user.User;
import de.openflorian.zk.AbstractGuiController;
import de.openflorian.zk.model.DataListModel;

/**
 * Controller System Information
 * 
 * @author Bastian Kraus <me@bastian-kraus.me>
 */
public class InformationController extends AbstractGuiController {
	private static final long serialVersionUID = -9103899038654112897L;
	
	private Listbox entityList;
	private DataListModel<Entry<String, String>> entityListModel;
	
	private Timer refreshTimer;
	
	@Override
	public String getLoginPage() {
		return ZkGlobals.PAGE_LOGIN;
	}
	
	@Override
	public boolean mayView(Principal user) {
		PermissionService ps = (PermissionService)ContainerManager.getComponent("permissionService");
		return user != null && ps.hasPermission((User) user, GlobalPermission.ADMINISTER);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
    public void doAfterCompose(Component comp) throws Exception {
    	super.doAfterCompose(comp);
    	
    	entityList.setItemRenderer(new InformationListItemRenderer());
    	
    	// add new event listener for page refreshing
    	refreshTimer.addEventListener("onTimer", new EventListener() {
			public void onEvent(Event event) throws Exception {
				InformationController.this.refreshSystemInformation();
			}
		});
    	
    	refreshSystemInformation();
    }
	
	/**
	 * Helper: Gather {@link SystemInformation#getSystemInformationList()} and refresh listbox
	 */
	private void refreshSystemInformation() {
		SystemInformation info = new SystemInformation();
		
		List<Entry<String, String>> infoEntryList = new LinkedList<Entry<String, String>>();
		for(Entry<String, String> entry: info.getSystemInformationList().entrySet()) {
			infoEntryList.add(entry);
		}
		
		entityListModel = new DataListModel<Entry<String,String>>(infoEntryList);
		entityList.setModel(entityListModel);
	}
	
}
