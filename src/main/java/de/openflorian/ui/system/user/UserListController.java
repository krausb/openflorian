package de.openflorian.ui.system.user;

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
public class UserListController extends AbstractGuiController {
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
