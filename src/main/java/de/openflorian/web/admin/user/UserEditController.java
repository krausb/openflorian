package de.openflorian.web.admin.user;

import java.security.Principal;

import com.vaadin.ui.UI;

import de.openflorian.web.AbstractBrowser;
import de.openflorian.web.WebGlobals;
import de.openflorian.web.user.User;

/**
 * Controller for Editing / Adding a {@link User}
 * 
 * ZUL: {@link WebGlobals#PAGE_SYSTEM_USERS_EDIT}
 * 
 * @author Bastian Kraus <bofh@k-hive.de>
 */
public class UserEditController extends AbstractBrowser {
	public UserEditController(UI view) {
		super(view);
		// TODO Auto-generated constructor stub
	}

	private static final long serialVersionUID = -9103899038654112897L;

	// private Window userEditWindow;
	//
	// private Button saveButton;
	// private Button cancelButton;
	// private Button applyButton;
	// private Button deleteButton;
	//
	// private long userId;
	//
	// private Textbox password;
	// private Textbox confirmPassword;
	// private String tmpPassword;
	//
	// private Textbox name;
	// private Textbox firstname;
	// private Textbox lastname;
	// private Textbox email;
	//
	// private Tabbox permissionsTab;

	@Override
	public boolean mayView(Principal user) {
		// PermissionService ps = (PermissionService)
		// ContainerManager.getComponent("permissionService");
		// return user != null && ps.hasPermission((User) user,
		// GlobalPermission.ADMINISTER);
		return true;
	}

	// @Override
	// public void init() {
	//// super.doAfterCompose(comp);
	//
	// User selectedUser = (User)
	// execution.getAttribute(WebGlobals.REQUEST_ENTITY);
	//
	// if (selectedUser != null) {
	// log.debug("Recieved user to select: " + selectedUser.toString());
	// fillZulFromUser(selectedUser);
	// deleteButton.setDisabled(false);
	// } else {
	// deleteButton.setDisabled(true);
	// }
	// }
	//
	// /**
	// * Event-Handler: cancelButton.onClick @param event @throws
	// */
	// public void onClick$applyButton(Event event) {
	// try {
	// User u = getUserFromZul();
	//
	// UserService userService = (UserService)
	// ContainerManager.getComponent("userService");
	// u = userService.persist(u);
	// if (u.getId() > 0) {
	// this.userId = u.getId();
	// execution.setAttribute(WebGlobals.REQUEST_ENTITY, u);
	// fillZulFromUser(u);
	// }
	//
	// Clients.showNotification(Labels.getLabel("admin.user.edit.msg.usersaved"),
	// "info", applyButton,
	// "top_center", 2000);
	// } catch (WrongValueException e) {
	// log.debug(e.getMessage(), e);
	// setError(new ZkException(Labels.getLabel("user.model." +
	// e.getComponent().getId()) + ": " + e.getMessage(),
	// e));
	// } catch (ZkException e) {
	// log.error(e.getMessage(), e);
	// setError(e);
	// } catch (GeneralSecurityException e) {
	// log.error(e.getMessage(), e);
	// setError(new ZkException(e.getMessage(), e));
	// } catch (ValidationException e) {
	// log.error(e.getMessage(), e);
	// setError(new ZkException(e.getMessage(), e));
	// }
	// }
	//
	// /**
	// * Event-Handler: saveButton.onClick <br/>
	// * Redirects to User List ZUL
	// *
	// * @param event
	// */
	// public void onClick$saveButton(Event event) {
	// try {
	// User u = getUserFromZul();
	//
	// UserService userService = (UserService)
	// ContainerManager.getComponent("userService");
	// u = userService.persist(u);
	//
	// // redirect to user list
	// setContentZul(WebGlobals.PAGE_SYSTEM_USERS);
	// } catch (WrongValueException e) {
	// log.debug(e.getMessage(), e);
	// setError(new ZkException(e.getMessage(), e));
	// } catch (ZkException e) {
	// log.error(e.getMessage(), e);
	// setError(e);
	// } catch (GeneralSecurityException e) {
	// log.error(e.getMessage(), e);
	// setError(new ZkException(e.getMessage(), e));
	// } catch (ValidationException e) {
	// log.error(e.getMessage(), e);
	// setError(new ZkException(e.getMessage(), e));
	// }
	// }
	//
	// /**
	// * Event-Handler: deleteButton.onClick
	// *
	// * @param event
	// */
	// public void onClick$deleteButton(Event event) {
	// if (userId > 0) {
	// Messagebox.show(Labels.getLabel("admin.user.edit.msg.deleteuser", new
	// String[] { name.getText() }),
	// Labels.getLabel("global.question"),
	// new Messagebox.Button[] { Messagebox.Button.YES, Messagebox.Button.NO },
	// Messagebox.QUESTION,
	// new EventListener<ClickEvent>() {
	//
	// public void onEvent(ClickEvent event) throws Exception {
	// if (event.getButton() == Messagebox.Button.YES)
	// UserEditController.this.deleteCurrentUser();
	// }
	//
	// });
	// } else {
	// Clients.showNotification(Labels.getLabel("admin.user.edit.msg.nouserselected"),
	// "info", applyButton,
	// "top_center", 2000);
	// }
	// }
	//
	// /**
	// * Event-Handler: cancelButton.onClick
	// *
	// * @param event
	// */
	// public void onClick$cancelButton(Event event) {
	// setContentZul(WebGlobals.PAGE_SYSTEM_USERS);
	// }
	//
	// /**
	// * Helper: Fill .zul with data from given {@code u}
	// *
	// * @param u
	// * {@link User}
	// */
	// private void fillZulFromUser(User u) {
	// userId = u.getId();
	//
	// // fill core data
	// name.setText(u.getName());
	// password.setText("");
	// confirmPassword.setText("");
	// tmpPassword = u.getPassword();
	//
	// // fill contact data
	// try {
	// firstname.setText(u.getFirstname());
	// } catch (WrongValueException e) {
	// }
	// try {
	// lastname.setText(u.getLastname());
	// } catch (WrongValueException e) {
	// }
	//
	// email.setText(u.getEmail());
	//
	// renderPermissionTabs();
	// }
	//
	// /**
	// * Helper: Read data from .zul and fill it into a {@link User} object.
	// *
	// * @return
	// * @throws CoreException
	// */
	// private User getUserFromZul() throws GeneralSecurityException,
	// ZkException {
	// User zu = new User();
	//
	// // set core data
	// zu.setId(userId);
	// zu.setName(name.getValue());
	// if (!StringUtils.isEmpty(password.getValue())) {
	// if (password.getText().equals(confirmPassword.getText())) {
	// CryptCipherService cryptService = (CryptCipherService) ContainerManager
	// .getComponent(AvailableComponent.CryptService.toString());
	// zu.setPassword(cryptService.encrypt(cryptService.encrypt(password.getText(),
	// CipherTarget.Xor),
	// CipherTarget.Blowfish));
	// } else {
	// throw new ZkException("admin.user.edit.validation.password");
	// }
	// } else {
	// if (tmpPassword == null)
	// throw new ZkException("user.error.passwordempty");
	// zu.setPassword(tmpPassword);
	// }
	// zu.setFirstname(firstname.getText());
	// zu.setLastname(lastname.getText());
	// zu.setEmail(email.getText());
	//
	// return zu;
	// }
	//
	// /**
	// * Helper: Delete current set user
	// */
	// private void deleteCurrentUser() {
	// if (userId > 0) {
	// UserService userService = (UserService)
	// ContainerManager.getComponent("userService");
	// userService.remove(userId);
	//
	// setContentZul(WebGlobals.PAGE_SYSTEM_USERS);
	// }
	// }
	//
	// /**
	// * Helper: Render Tabpanels for all systemwide available permission sets
	// */
	// @SuppressWarnings("unused")
	// private void renderPermissionTabs() {
	// try {
	// Reflections refl = new Reflections("de.openflorian.data");
	//
	// Set<Class<? extends PermissionSet>> permissionSetClasses =
	// refl.getSubTypesOf(PermissionSet.class);
	//
	// for (Class<? extends PermissionSet> clazz : permissionSetClasses) {
	// permissionsTab.getTabs().appendChild(
	// new Tab(Labels.getLabel("permissions.sets." +
	// clazz.getSimpleName().toLowerCase())));
	// permissionsTab.getTabpanels()
	// .appendChild(new UserPermissionTabpanelRenderer(getUserFromZul(),
	// clazz).render());
	// }
	// } catch (ZkException e) {
	// log.error(e.getMessage(), e);
	// setError(e);
	// } catch (Exception e) {
	// log.error(e.getMessage(), e);
	// setError(new ZkException(e.getMessage(), e));
	// }
	// }

	@Override
	public void init() {
		// TODO Auto-generated method stub

	}

	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

}
