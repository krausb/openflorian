package de.openflorian.web.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.ui.Button;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;

import de.openflorian.web.AbstractBrowser;

/**
 * Controller for Login Mask
 * 
 * @author Bastian Kraus <bofh@k-hive.de>
 */
public class LoginBrowser extends AbstractBrowser {
	private static final long serialVersionUID = -3645856142775830614L;

	private static final Logger log = LoggerFactory.getLogger(LoginBrowser.class);

	private TextField username;
	private TextField password;
	private Button loginButton;

	public LoginBrowser(UI view) {
		super(view);
	}

	/**
	 * Event-Handler: password.onOK
	 */
	public void onOK$password(Event event) {
		onClick$login(event);
	}

	/**
	 * Event-Handler: loginButton.onClick
	 * 
	 * @throws @throws
	 *             WrongValueException
	 */
	public void onClick$login(Event event) {
		// resetError();
		//
		// CryptCipherService cryptService = (CryptCipherService)
		// ContainerManager
		// .getComponent(AvailableComponent.CryptService);
		// UserService userService = (UserService)
		// ContainerManager.getComponent("userService");
		// // PermissionService pemService =
		// //
		// (PermissionService)ContainerManager.getComponent("permissionService");
		//
		// try {
		// User u = userService.getByName(username.getText());
		// String cryptedPw =
		// cryptService.encrypt(cryptService.encrypt(password.getText(),
		// CipherTarget.Xor),
		// CipherTarget.Blowfish);
		//
		// log.trace("given password: " + cryptedPw);
		// if (u != null && u.isDeleted()) {
		// setError(new ZkException("login.error.accountdeleted"));
		// password.setValue("");
		// password.setFocus(true);
		// } else if (u != null && !u.isActive()) {
		// setError(new ZkException("login.error.accountnotactive"));
		// password.setValue("");
		// password.setFocus(true);
		// } else if (u != null && u.getPassword().equals(cryptedPw)) {
		// AuthenticatedUserSessionLocal.set(u);
		//
		// execution.sendRedirect(ZkGlobals.PAGE_MAIN);
		// } else {
		// setError(new ZkException("login.error.wrongcredentials"));
		// password.setValue("");
		// password.setFocus(true);
		// }
		// } catch (Exception e) {
		// log.error(e.getMessage(), e);
		// setError(new ZkException("login.error.generalerror", e));
		// username.setValue("");
		// username.setFocus(true);
		// password.setValue("");
		// }

	}

	@Override
	public void init() {
		// TODO Auto-generated method stub

	}

	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

}
