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

import org.apache.commons.lang3.StringUtils;
import org.zkoss.gmaps.Ginfo;
import org.zkoss.gmaps.Gmaps;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Audio;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Label;

import de.openflorian.alarm.AlarmContext;
import de.openflorian.data.model.Operation;
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

	private Div alarmView;
	
	private Audio alarmsound;
	private Label alarmTime;
	
	private Label keyword;
	private Label buzzword;
	private Label currentTime;
	private Label operationNr;
	private Label positionLongitude;
	private Label positionLatitude;
	private Label object;
	private Hbox objectBox;
	private Label street;
	private Hbox streetBox;
	private Label crossway;
	private Hbox crosswayBox;
	private Label city;
	private Hbox cityBox;
	private Label priority;
	private Label resourcesRaw;
	
	private Gmaps operationMap;
	private Ginfo operationMarker;
	
	private Button arrange;
	
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
    	
    	operationMap.setVisible(true);
    	
    	Operation currentOperation = AlarmContext.getInstance().getCurrentOperation();
    	
    	if(currentOperation.isFireOperation())
    		alarmView.setZclass(Operation.FIRE_ZCLASS);
    	else
    		alarmView.setZclass(Operation.TECHNICAL_ASSISTANCE_ZCLASS);
    	
    	currentTime.setValue(new SimpleDateFormat(ZkGlobals.FORMAT_DATETIME).format(new Date()));
    	
    	//if(currentOperation.getKeyword() != null)
    		//keyword.setValue(currentOperation.getKeyword());
    	
    	if(currentOperation.getBuzzword() != null)
    		buzzword.setValue(currentOperation.getBuzzword());
    	
    	if(!StringUtils.isEmpty(currentOperation.getPriority()))
    		priority.setValue(currentOperation.getPriority());
    	
    	if(currentOperation.getOperationNr() != null)
    		operationNr.setValue(currentOperation.getOperationNr());
    	
    	if(!StringUtils.isEmpty(currentOperation.getObject())) {
    		object.setValue(currentOperation.getObject());
    		objectBox.setVisible(true);
    	} else {
    		objectBox.setVisible(false);
    	}
    	
    	if(currentOperation.getPositionLatitude() != 0)
    		positionLatitude.setValue(String.valueOf(currentOperation.getPositionLatitude()));
    	
    	if(currentOperation.getPositionLongitude() != 0)
    		positionLongitude.setValue(String.valueOf(currentOperation.getPositionLongitude()));
    	
    	if(currentOperation.getPositionLatitude() != 0 && currentOperation.getPositionLongitude() != 0) {
    		initMap(currentOperation.getPositionLongitude(), currentOperation.getPositionLatitude());
    	} else {
    		operationMap.setVisible(false);
    	}
    	
    	if(!StringUtils.isEmpty(currentOperation.getStreet())) {
    		street.setValue(currentOperation.getStreet());
    		streetBox.setVisible(true);
    	} else {
    		streetBox.setVisible(false);
    	}
    	
    	if(!StringUtils.isEmpty(currentOperation.getCrossway())) {
    		crossway.setValue(currentOperation.getCrossway());
    		crosswayBox.setVisible(true);
    	} else {
    		crosswayBox.setVisible(false);
    	}

    	if(!StringUtils.isEmpty(currentOperation.getCity())) {
    		city.setValue(currentOperation.getCity());
    		cityBox.setVisible(true);
    	} else {
    		cityBox.setVisible(false);
    	}
    	
    	if(currentOperation.getResourcesRaw() != null)
    		resourcesRaw.setValue(currentOperation.getResourcesRaw());
    	
		SimpleDateFormat format = new SimpleDateFormat(ZkGlobals.FORMAT_DATETIME);
		if(currentOperation.getIncurredAt() != null)
			alarmTime.setValue(format.format(currentOperation.getIncurredAt()));
    	
    	alarmsound.play();
    }
	
	/**
	 * Helper: Init Map
	 */
	private void initMap(double longVal, double latVal) {
		log.debug("Arrange map with coords long: " + longVal + "; lat: " + latVal);
		
		operationMap.setLng(longVal);
		operationMap.setLat(latVal);
		operationMap.setCenter(latVal, longVal);
		operationMap.setZoom(16);
		
		operationMarker.setLat(latVal);
		operationMarker.setLng(longVal);
		operationMarker.setOpen(true);
	}

}
