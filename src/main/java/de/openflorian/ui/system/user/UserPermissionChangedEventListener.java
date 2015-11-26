package de.openflorian.ui.system.user;

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
