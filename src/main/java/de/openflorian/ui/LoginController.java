package de.openflorian.ui;

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
import org.zkoss.zul.Textbox;

import de.openflorian.crypt.CryptCipherService;
import de.openflorian.crypt.CryptCipherService.CipherTarget;
import de.openflorian.service.UserService;
import de.openflorian.web.core.AvailableComponent;
import de.openflorian.web.core.ContainerManager;
import de.openflorian.web.user.AuthenticatedUserSessionLocal;
import de.openflorian.web.user.User;
import de.openflorian.zk.AbstractGuiController;
import de.openflorian.zk.ZkException;

/**
 * Controller for Login Mask
 * 
 * @author Bastian Kraus <me@bastian-kraus.me>
 */
public class LoginController extends AbstractGuiController {
	private static final long serialVersionUID = 7907222989847151064L;

	private Textbox username;
	private Textbox password;

	@Override
	public boolean mayView(Principal user) {
		return true;
	}

	@Override
	public boolean mustBeLoggedIn() {
		return false;
	};

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
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
	 * @throws
	 * @throws WrongValueException
	 */
	public void onClick$login(Event event) {
		resetError();

		CryptCipherService cryptService = (CryptCipherService) ContainerManager
				.getComponent(AvailableComponent.CryptService);
		UserService userService = (UserService) ContainerManager.getComponent("userService");
		// PermissionService pemService =
		// (PermissionService)ContainerManager.getComponent("permissionService");

		try {
			User u = userService.getByName(username.getText());
			String cryptedPw = cryptService.encrypt(cryptService.encrypt(password.getText(), CipherTarget.Xor),
					CipherTarget.Blowfish);
			
			log.trace("given password: " + cryptedPw);
			if (u != null && u.isDeleted()) {
				setError(new ZkException("login.error.accountdeleted"));
				password.setValue("");
				password.setFocus(true);
			} else if(u != null && !u.isActive()) {
				setError(new ZkException("login.error.accountnotactive"));
				password.setValue("");
				password.setFocus(true);
			} else if (u != null && u.getPassword().equals(cryptedPw)) {
				AuthenticatedUserSessionLocal.set(u);

				execution.sendRedirect(ZkGlobals.PAGE_MAIN);
			} else {
				setError(new ZkException("login.error.wrongcredentials"));
				password.setValue("");
				password.setFocus(true);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			setError(new ZkException("login.error.generalerror", e));
			username.setValue("");
			username.setFocus(true);
			password.setValue("");
		}

	}

}
