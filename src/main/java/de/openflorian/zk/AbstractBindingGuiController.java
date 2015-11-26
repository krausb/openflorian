package de.openflorian.zk;

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

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.bind.BindComposer;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.metainfo.ComponentInfo;

import de.openflorian.web.user.AuthenticatedUserSessionLocal;

/**
 * BindComposer Extension
 * 
 * @author Bastian Kraus <me@bastian-kraus.me>
 */
public abstract class AbstractBindingGuiController extends BindComposer implements GuiController {
	private static final long serialVersionUID = 4407462108300419571L;

	/**
	 * Applied Page
	 */
	protected Page page;
	
	/**
	 * Applied component
	 */
	protected Component self;
	
	/**
	 * Logger for all Subclasses
	 */
    protected Logger log = LoggerFactory.getLogger(getClass());
    protected List<String> affectedFields = new ArrayList<String>();
    
    @Override
    public ComponentInfo doBeforeCompose(Page page, Component parent, ComponentInfo compInfo) {
    	this.page = page;
    	if(AuthenticatedUserSessionLocal.get() != null)
    		log.debug("Current AuthenticatedUserSessionLocal: " + AuthenticatedUserSessionLocal.get());
    	else
    		log.debug("Current AuthenticatedUserSessionLocal not set.");
    	
    	if ( mustBeLoggedIn() &&
    			AuthenticatedUserSessionLocal.get() == null ) {
    		Executions.sendRedirect(getLoginPage());
    	}
    
    	return compInfo;
    }

    @SuppressWarnings("unchecked")
	@Override
    public void doAfterCompose(Component comp) throws Exception {
    	super.doAfterCompose(comp);
    	this.self = comp;

    	if(mustBeLoggedIn() && AuthenticatedUserSessionLocal.get() != null) {
    		boolean mayView = mayView(getUser());
    		log.debug("User permitted to current view: " + mayView);
    		if(!mayView)
    			setContentZul(getAboutPage());
    	}
    }

}
