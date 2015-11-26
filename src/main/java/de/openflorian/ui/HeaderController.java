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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;

import de.openflorian.web.user.AuthenticatedUserSessionLocal;
import de.openflorian.web.user.User;
import de.openflorian.zk.AbstractGuiController;

/**
 * Controller for Layout Header Bar
 * 
 * @author Bastian Kraus <bk@pogo-systems.de>
 */
public class HeaderController extends AbstractGuiController {
	private static final long serialVersionUID = 1182851061480627950L;

	private Logger log = LoggerFactory.getLogger(getClass());
	
	private Label logout;
	private Label loggedInAs;
	
	private Listbox languages;
	
	private Image stageImage;
	
	@Override
	public String getLoginPage() {
		return ZkGlobals.PAGE_LOGIN;
	}
	
	@Override
	public boolean mayView(Principal user) {
		return user != null;
	}
	
    @Override
    public void doAfterCompose(Component comp) throws Exception {
    	super.doAfterCompose(comp);
    	
    	User user = AuthenticatedUserSessionLocal.get();
    	
    	if(user != null)
    		loggedInAs.setValue(user.getName() + " (" + user.getLastname() + ", " + user.getFirstname() + ")");
    	
    }
    
    /**
     * Event-Handler: logout.onClick
     * @param event
     */
    public void onClick$logout(Event event) {
    	AuthenticatedUserSessionLocal.set(null);
    	
    	execution.sendRedirect(ZkGlobals.PAGE_LOGIN);
    }
    
    /**
     * Property: Application Version
     * @return
     */
    public String getVersion() {
    	return "0.1";
    }
    
    /**
     * Property: Test Mode of Application active or not
     * @return
     */
    public boolean getMode() {
    	return false;
    }
    	
}
