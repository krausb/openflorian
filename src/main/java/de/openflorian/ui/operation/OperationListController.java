package de.openflorian.ui.operation;

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

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Paging;
import org.zkoss.zul.event.PagingEvent;

import de.openflorian.data.model.security.GlobalPermission;
import de.openflorian.service.PermissionService;
import de.openflorian.ui.ZkGlobals;
import de.openflorian.ui.model.UserPagingDataListModel;
import de.openflorian.ui.renderer.UserListitemRenderer;
import de.openflorian.web.core.ContainerManager;
import de.openflorian.web.user.User;
import de.openflorian.zk.AbstractGuiController;

/**
 * Controller for User Management List
 * 
 * @author Bastian Kraus <me@bastian-kraus.me>
 */
public class OperationListController extends AbstractGuiController {
	private static final long serialVersionUID = -9103899038654112897L;
	
	private Listbox entityList;
	
	private Paging paging;
	
	private UserPagingDataListModel listModel;
	
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
    	
    	execution.removeAttribute(ZkGlobals.REQUEST_ENTITY);
    	
    	listModel = new UserPagingDataListModel("name ASC", 15);
    	listModel.init();
    	
    	long totalDataListSize = listModel.getTotalSize();
    	
    	paging.setTotalSize(Long.valueOf(totalDataListSize).intValue());
    	
    	paging.addEventListener("onPaging", new EventListener() {
    		public void onEvent(Event event) {
    			if(event instanceof PagingEvent) {
    				int pageno = ((PagingEvent)event).getActivePage();
    				listModel.setActivePage(pageno);
    				listModel.refresh();
    				entityList.setModel(listModel.getCurrentData());
    			}
    		}
    	});
    	
    	if(!listModel.hasMoreThanOnePage()) {
    		paging.setVisible(false);
    	}
    	
    	entityList.setModel(listModel.getCurrentData());
    	entityList.setItemRenderer(new UserListitemRenderer());
    }
	
	/**
	 * Event-Handler: usersListbox.onClick
	 * @param event
	 */
	public void onClick$entityList(Event event) {
		Listitem selectedItem = entityList.getSelectedItem();
		
		if(selectedItem != null) {
			execution.setAttribute(ZkGlobals.REQUEST_ENTITY, selectedItem.getValue());
			setContentZul(ZkGlobals.PAGE_SYSTEM_USERS_EDIT);
		}
	}
	
	/**
	 * Event-Handler: newUserButton.onClick
	 * @param event
	 */
	public void onClick$newUserButton(Event event) {
		execution.removeAttribute(ZkGlobals.REQUEST_ENTITY);
		setContentZul(ZkGlobals.PAGE_SYSTEM_USERS_EDIT);
	}
	
}
