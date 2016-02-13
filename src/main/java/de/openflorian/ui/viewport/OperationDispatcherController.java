package de.openflorian.ui.viewport;

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
import org.zkoss.zul.Button;

import de.openflorian.alarm.AlarmContextVerticle;
import de.openflorian.ui.ZkGlobals;
import de.openflorian.zk.AbstractGuiController;

/**
 * Operation View Controller<br/>
 * <Br/>
 * ZUL: webapp/operation/viewport.zul
 * 
 * @author Bastian Kraus <me@bastian-kraus.me>
 */
public class OperationDispatcherController extends AbstractGuiController {
	private static final long serialVersionUID = -6907197092954813302L;
	
	private Button dispatch;
	
	@Override
	public String getLoginPage() {
		return ZkGlobals.OPERATION_DISPATCHER;
	}
	
	@Override
	public boolean mustBeLoggedIn() {
		return false;
	}
	
	@Override
	public boolean mayView(Principal user) {
		return true;
	}
	
	@Override
    public void doAfterCompose(Component comp) throws Exception {
    	super.doAfterCompose(comp);
    }
	
	/**
	 * Event-Handler: dispatch.onClick
	 * 
	 * @param
	 * 		{@link Event}
	 */
	public void onClick$dispatch(Event event) {
		AlarmContextVerticle.getInstance().dispatchOperation(AlarmContextVerticle.getInstance().getCurrentOperation());
	}
	
}
