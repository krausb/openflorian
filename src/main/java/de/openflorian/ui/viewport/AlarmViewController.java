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
import java.text.SimpleDateFormat;
import java.util.Date;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Audio;
import org.zkoss.zul.Label;

import de.openflorian.alarm.AlarmContext;
import de.openflorian.alarm.AlarmDispatchedEvent;
import de.openflorian.data.model.Operation;
import de.openflorian.event.Event;
import de.openflorian.event.EventListener;
import de.openflorian.event.EventQueue;
import de.openflorian.ui.ZkGlobals;
import de.openflorian.zk.AbstractGuiController;

/**
 * Operation View Controller<br/>
 * <Br/>
 * ZUL: webapp/operation/views/alarm.zul
 * 
 * @author Bastian Kraus <me@bastian-kraus.me>
 */
public class AlarmViewController extends AbstractGuiController {
	private static final long serialVersionUID = -3937904151161574461L;

	private Audio alarmsound;
	
	private Label keyword;
	private Label buzzword;
	private Label currentTime;
	private Label operationNr;
	private Label object;
	private Label street;
	private Label crossway;
	private Label city;
	private Label priority;
	private Label resourcesRaw;
	
	@Override
	public String getLoginPage() {
		return ZkGlobals.OPERATION_VIEWPORT;
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
    
    	Operation currentOperation = AlarmContext.getInstance().getCurrentOperation();
    	
    	currentTime.setValue(new SimpleDateFormat(ZkGlobals.FORMAT_DATETIME).format(new Date()));
    	
    	keyword.setValue(currentOperation.getKeyword());
    	buzzword.setValue(currentOperation.getBuzzword());
    	operationNr.setValue(currentOperation.getOperationNr());
    	object.setValue(currentOperation.getObject());
    	street.setValue(currentOperation.getStreet());
    	crossway.setValue(currentOperation.getCrossway());
    	city.setValue(currentOperation.getCity());
    	priority.setValue(currentOperation.getPriority());
    	resourcesRaw.setValue(currentOperation.getResourcesRaw());
    	
    	alarmsound.play();
    }
	
}
