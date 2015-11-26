package de.openflorian.ui.system;

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
import de.openflorian.ui.ZkGlobals;
import de.openflorian.web.core.AvailableComponent;
import de.openflorian.web.core.ContainerManager;
import de.openflorian.zk.AbstractGuiController;

/**
 * Controller for encrypting text
 * 
 * @author Bastian Kraus <me@bastian-kraus.me>
 */
public class EncryptController extends AbstractGuiController {
	private static final long serialVersionUID = -5618735217657780476L;
	
	private Textbox plain;
	private Textbox encrypted;
	
	@Override
	public String getLoginPage() {
		return ZkGlobals.PAGE_LOGIN;
	}
	
	@Override
	public boolean mayView(Principal user) {
		return true;
	}
	
	@Override
	public boolean mustBeLoggedIn() {
		return false;
	}
	
	@Override
    public void doAfterCompose(Component comp) throws Exception {
    	super.doAfterCompose(comp);
    }
	
	/**
	 * Event-Handler: usersListbox.onClick
	 * @param event
	 */
	public void onClick$encrypt(Event event) {
		CryptCipherService cryptService = (CryptCipherService) ContainerManager
				.getComponent(AvailableComponent.CryptService);
		
		if(cryptService != null) {
			String cryptedStr = cryptService.encrypt(cryptService.encrypt(plain.getText(), CipherTarget.Xor),
					CipherTarget.Blowfish);
			log.debug("crypted string: " + cryptedStr);
			encrypted.setText(cryptedStr);
		}
	}
	
}
