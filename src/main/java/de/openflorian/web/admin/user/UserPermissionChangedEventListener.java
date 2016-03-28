package de.openflorian.web.admin.user;

/**
 * {@link EventListener} for User Permission Checkbox Changed listener
 * 
 * @author Bastian Kraus <bofh@k-hive.de>
 */
public class UserPermissionChangedEventListener {

	// private final Logger log = LoggerFactory.getLogger(getClass());
	//
	// private final User user;
	// private final String permission;
	//
	// public UserPermissionChangedEventListener(User user, String permission) {
	// this.user = user;
	// this.permission = permission;
	// }
	//
	// @Override
	// public void onEvent(CheckEvent event) throws Exception {
	// PermissionService permissionService =
	// (PermissionService)ContainerManager.getComponent("permissionService");
	// if(permissionService == null)
	// throw new IllegalStateException("No permission service bean availalbe!");
	//
	// if(event.isChecked()) {
	// log.info(String.format("userId=%d GRANTed permission: userId=%d;
	// permission=%s", AuthenticatedUserSessionLocal.get().getId(),
	// user.getId(), permission));
	// permissionService.persist(Permission.fromUserAndPermission(user,
	// permission));
	// } else {
	// log.info(String.format("userId=%d REVOKEd permission: userId=%d;
	// permission=%s", AuthenticatedUserSessionLocal.get().getId(),
	// user.getId(), permission));
	// permissionService.remove(user, permission);
	// }
	// }

}
