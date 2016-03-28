package de.openflorian.web.admin.operation;

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
 * Controller for {@link Operation} Management List
 * 
 * @author Bastian Kraus <bofh@k-hive.de>
 */
public class OperationListController extends AbstractBrowser {

	public OperationListController(UI view) {
		super(view);
		// TODO Auto-generated constructor stub
	}

	private static final long serialVersionUID = -9103899038654112897L;

	// private Listbox entityList;
	//
	// private Paging paging;
	//
	// private OperationPagingDataListModel listModel;

	@Override
	public boolean mayView(Principal user) {
		// PermissionService ps =
		// (PermissionService)ContainerManager.getComponent("permissionService");
		// return user != null && ps.hasPermission((User) user,
		// GlobalPermission.ADMINISTER);
		return true;
	}

	@Override
	public void init() {
		//
		// execution.removeAttribute(WebGlobals.REQUEST_ENTITY);
		//
		// listModel = new OperationPagingDataListModel("id DESC", 15);
		// listModel.init();
		//
		// long totalDataListSize = listModel.getTotalSize();
		//
		// paging.setTotalSize(Long.valueOf(totalDataListSize).intValue());
		//
		// paging.addEventListener("onPaging", new EventListener() {
		// public void onEvent(Event event) {
		// if(event instanceof PagingEvent) {
		// int pageno = ((PagingEvent)event).getActivePage();
		// listModel.setActivePage(pageno);
		// listModel.refresh();
		// entityList.setModel(listModel.getCurrentData());
		// }
		// }
		// });
		//
		// if(!listModel.hasMoreThanOnePage()) {
		// paging.setVisible(false);
		// }
		//
		// entityList.setModel(listModel.getCurrentData());
		// entityList.setItemRenderer(new OperationListitemRenderer());
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

	// /**
	// * Event-Handler: usersListbox.onClick
	// * @param event
	// */
	// public void onClick$entityList(Event event) {
	// Listitem selectedItem = entityList.getSelectedItem();
	//
	// if(selectedItem != null) {
	// execution.setAttribute(WebGlobals.REQUEST_ENTITY,
	// selectedItem.getValue());
	// setContentZul(WebGlobals.PAGE_OPERATION_EDIT);
	// }
	// }
	//
	// /**
	// * Event-Handler: newUserButton.onClick
	// * @param event
	// */
	// public void onClick$newOperationButton(Event event) {
	// execution.removeAttribute(WebGlobals.REQUEST_ENTITY);
	// setContentZul(WebGlobals.PAGE_OPERATION_EDIT);
	// }

}
