package de.openflorian.web.admin.system;

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

import com.vaadin.ui.UI;

import de.openflorian.web.AbstractBrowser;

/**
 * Controller System Information
 * 
 * @author Bastian Kraus <bofh@k-hive.de>
 */
public class InformationController extends AbstractBrowser {

	public InformationController(UI view) {
		super(view);
		// TODO Auto-generated constructor stub
	}

	private static final long serialVersionUID = -9103899038654112897L;

	// private Listbox entityList;
	// private DataListModel<Entry<String, String>> entityListModel;
	//
	// private Timer refreshTimer;

	@Override
	public boolean mayView(Principal user) {
		return true;
		// PermissionService ps =
		// (PermissionService)ContainerManager.getComponent("permissionService");
		// return user != null && ps.hasPermission((User) user,
		// GlobalPermission.ADMINISTER);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void init() {
		// super.doAfterCompose(comp);
		//
		// entityList.setItemRenderer(new InformationListItemRenderer());
		//
		// // add new event listener for page refreshing
		// refreshTimer.addEventListener("onTimer", new EventListener() {
		// public void onEvent(Event event) throws Exception {
		// InformationController.this.refreshSystemInformation();
		// }
		// });
		//
		// refreshSystemInformation();
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

	// /**
	// * Helper: Gather {@link SystemInformation#getSystemInformationList()} and
	// refresh listbox
	// */
	// private void refreshSystemInformation() {
	// SystemInformation info = new SystemInformation();
	//
	// List<Entry<String, String>> infoEntryList = new LinkedList<Entry<String,
	// String>>();
	// for(Entry<String, String> entry:
	// info.getSystemInformationList().entrySet()) {
	// infoEntryList.add(entry);
	// }
	//
	// entityListModel = new DataListModel<Entry<String,String>>(infoEntryList);
	// entityList.setModel(entityListModel);
	// }

}
