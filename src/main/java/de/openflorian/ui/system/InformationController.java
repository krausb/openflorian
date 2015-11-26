package de.openflorian.ui.system;

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
