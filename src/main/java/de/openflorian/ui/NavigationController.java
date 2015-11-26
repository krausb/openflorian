package de.openflorian.ui;

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
	
	public User currentUser;

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
	
}
