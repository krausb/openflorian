package de.openflorian.ui.operation;

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

import static org.zkoss.openlayers.util.Helper.pair;
import static org.zkoss.openlayers.util.Helper.toMap;

import java.security.GeneralSecurityException;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.ValidationException;

import org.zkoss.openlayers.Openlayers;
import org.zkoss.openlayers.base.Icon;
import org.zkoss.openlayers.base.LonLat;
import org.zkoss.openlayers.base.Pixel;
import org.zkoss.openlayers.base.Projection;
import org.zkoss.openlayers.base.Size;
import org.zkoss.openlayers.layer.Google;
import org.zkoss.openlayers.layer.Markers;
import org.zkoss.openlayers.marker.Marker;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Messagebox.ClickEvent;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import de.openflorian.alarm.AlarmContext;
import de.openflorian.data.model.Operation;
import de.openflorian.data.model.security.GlobalPermission;
import de.openflorian.service.OperationService;
import de.openflorian.service.PermissionService;
import de.openflorian.ui.ZkGlobals;
import de.openflorian.web.core.ContainerManager;
import de.openflorian.web.user.User;
import de.openflorian.zk.AbstractGuiController;
import de.openflorian.zk.ZkException;

/**
 * Controller for Editing / Adding a {@link User}
 * 
 * ZUL:
 * {@link ZkGlobals#PAGE_SYSTEM_USERS_EDIT}
 * 
 * @author Bastian Kraus <me@bastian-kraus.me>
 */
public class OperationEditController extends AbstractGuiController implements EventListener {
	private static final long serialVersionUID = -9103899038654112897L;
	
	private Window 		operationEditWindow;
	
	private Tab			resourcesTab;
	private Tabpanel	resourcesTabPanel;
	
	private Tab			dispatchTab;
	private Tabpanel	dispatchTabPanel;
	
	private Tab			mapTab;
	private Tabpanel	mapTabPanel;
	
	private Button 		saveButton;
	private Button 		cancelButton;
	private Button		applyButton;
	private Button		deleteButton;
	
	private Button		alarmButton;
	private	Button		dispatchButton;
	private Button		takeOverButton;
	
	private long 		operationId;
	
	private Textbox 	operationNr;
	private Doublebox 	positionLongitude;
	private Doublebox 	positionLatitude;
	private Textbox 	object;
	private Textbox		street;
	private Textbox		city;
	private Textbox		crossway;
	private Textbox		priority;
	private Textbox		keyword;
	private Textbox		buzzword;
	private Textbox		resourcesRaw;
	private Datebox		incurredAt;
	private Label		takenOverAtLabel;
	private Date		takenOverAt;
	private Label		dispatchedAtLabel;
	private Date		dispatchedAt;
	
	private Tabbox operationsTabbox;
	
	private Openlayers map;
	private Markers markers;
	private Marker marker;
	
	private Button arrange;
	
	@Override
	public String getLoginPage() {
		return ZkGlobals.PAGE_LOGIN;
	}

	@Override
	public boolean mayView(Principal user) {
		PermissionService ps = (PermissionService)ContainerManager.getComponent("permissionService");
		return user != null && ps.hasPermission((User) user, GlobalPermission.ADMINISTER);
	}
	
	@Override
    public void doAfterCompose(Component comp) throws Exception {
    	super.doAfterCompose(comp);

    	map.addLayer(new Google("Google Satellite", toMap(pair("numZoomLevels", 20))));
    	map.setVisible(true);
    	
    	markers = new Markers("Markers");
    	map.addLayer(markers);
    	
    	Operation selectedOperation = (Operation)execution.getAttribute(ZkGlobals.REQUEST_ENTITY);

    	if(selectedOperation != null) {
    		log.debug("Recieved operation to select: " + selectedOperation.toString());
    		fillZul(selectedOperation);
    		deleteButton.setDisabled(false);
    	} else {
    		deleteButton.setDisabled(true);
    	}
    }
	
	/**
	 * Event-Handler: arrangeButton.onClick
	 * 
	 * @param event
	 */
	public void onClick$arrangeButton(Event event) {
		Double longVal = positionLongitude.getValue();
		Double latVal = positionLatitude.getValue();
		initMap(longVal, latVal);
	}
	
	/**
	 * Event-Handler: cancelButton.onClick
	 * @param event
	 * @throws
	 */
	public void onClick$applyButton(Event event) {
		try {
			Operation o = getFromZul();
			
			OperationService operationService = (OperationService)ContainerManager.getComponent("operationService");
			o = operationService.persist(o);
			if(o.getId() > 0) {
				this.operationId = o.getId();
				execution.setAttribute(ZkGlobals.REQUEST_ENTITY,o);
				fillZul(o);
			}
			
			Clients.showNotification(Labels.getLabel("admin.user.edit.msg.usersaved"),"info", applyButton,"top_center", 2000);
		} catch (WrongValueException e) {
			log.debug(e.getMessage(), e);
			setError(new ZkException(Labels.getLabel("operation.model." + e.getComponent().getId()) + ": " + e.getMessage(), e));			
		} catch (ZkException e) {
			log.error(e.getMessage(), e);
			setError(e);
		} catch (GeneralSecurityException e) {
			log.error(e.getMessage(), e);
			setError(new ZkException(e.getMessage(), e));
		} catch (ValidationException e) {
			log.error(e.getMessage(), e);
			setError(new ZkException(e.getMessage(), e));
		}
	}
	
	/**
	 * Event-Handler: saveButton.onClick
	 * <br/>
	 * Redirects to Operation List ZUL
	 * 
	 * @param event
	 */
	public void onClick$saveButton(Event event) {		
		try {
			Operation o = getFromZul();
			
			OperationService operationService = (OperationService)ContainerManager.getComponent("operationService");
			o = operationService.persist(o);
			
			// redirect to user list
			setContentZul(ZkGlobals.PAGE_OPERATION_LIST);
		} catch (WrongValueException e) {
			log.debug(e.getMessage(), e);
			setError(new ZkException(e.getMessage(), e));			
		} catch (ZkException e) {
			log.error(e.getMessage(), e);
			setError(e);
		} catch (GeneralSecurityException e) {
			log.error(e.getMessage(), e);
			setError(new ZkException(e.getMessage(), e));
		} catch (ValidationException e) {
			log.error(e.getMessage(), e);
			setError(new ZkException(e.getMessage(), e));
		}
	}
	
	/**
	 * Event-Handler: deleteButton.onClick
	 * @param event
	 */
	public void onClick$deleteButton(Event event) {
		if(operationId > 0) {
			Messagebox.show(
					Labels.getLabel("operation.edit.msg.delete", new String[]{operationNr.getText()}), 
					Labels.getLabel("global.question"), 
					new Messagebox.Button[]{Messagebox.Button.YES, Messagebox.Button.NO},
					Messagebox.QUESTION,
					new EventListener<ClickEvent>() {
						
						public void onEvent(ClickEvent event) throws Exception {
							if(event.getButton() == Messagebox.Button.YES)
								OperationEditController.this.deleteCurrentOperation();
						}
						
					});
		} else {
			Clients.showNotification(Labels.getLabel("operation.edit.msg.noselected"),"info", applyButton,"top_center", 2000);
		}
	}
	
	/**
	 * Event-Handler: cancelButton.onClick
	 * @param event
	 */
	public void onClick$cancelButton(Event event) {
		setContentZul(ZkGlobals.PAGE_OPERATION_LIST);
	}
	
	/**
	 * Event-Handler: alarmButton.onClick
	 * @param event
	 */
	public void onClick$alarmButton(Event event) {
		if(operationId > 0) {
			try {
				Operation op = getFromZul();
				
				AlarmContext.getInstance().alarmOperation(op);
			} catch (ZkException e) {
				log.error(e.getMessage(), e);
				setError(e);
			} catch (GeneralSecurityException e) {
				log.error(e.getMessage(), e);
				setError(new ZkException(e.getMessage(), e));
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				setError(new ZkException(e.getMessage(), e));
			}
		}
	}
	
	/**
	 * Event-Handler: dispatchButton.onClick
	 * @param event
	 */
	public void onClick$dispatchButton(Event event) {
		if(operationId > 0) {
			if(AlarmContext.getInstance().getCurrentOperation() != null && 
					operationId == AlarmContext.getInstance().getCurrentOperation().getId()) {
				
				try {
					AlarmContext.getInstance().dispatchOperation(getFromZul());
				} catch (ZkException e) {
					log.error(e.getMessage(), e);
					setError(e);
				} catch (GeneralSecurityException e) {
					log.error(e.getMessage(), e);
					setError(new ZkException(e.getMessage(), e));
				} catch (Exception e) {
					log.error(e.getMessage(), e);
					setError(new ZkException(e.getMessage(), e));
				}
			} else {
				Clients.showNotification(Labels.getLabel("operation.edit.msg.wrongoperation"),"info", dispatchButton,"top_center", 2000);
			}
		} else {
			Clients.showNotification(Labels.getLabel("operation.edit.msg.noselected"),"info", dispatchButton,"top_center", 2000);
		}
	}
	
	
	/**
	 * Helper: Fill .zul with data from given <code>o</code>.
	 *  
	 * @param o {@link Operation}
	 */
	private void fillZul(Operation o) {
		operationId = o.getId();
		
		// fill core data
		try {
			operationNr.setText(o.getOperationNr());
		} catch (WrongValueException e) {}
		
		try {
			positionLongitude.setValue(o.getPositionLongitude());
		} catch (WrongValueException e) {}

		try {
			positionLatitude.setValue(o.getPositionLatitude());
		} catch (WrongValueException e) {}
		
		try {
			object.setText(o.getObject());
		} catch (WrongValueException e) {}
		
		try {
			street.setText(o.getStreet());
		} catch (WrongValueException e) {}
		
		try {
			city.setText(o.getCity());
		} catch (WrongValueException e) {}
		
		try {
			crossway.setText(o.getCrossway());
		} catch (WrongValueException e) {}
		
		try {
			priority.setText(o.getPriority());
		} catch (WrongValueException e) {}
		
		try {
			keyword.setText(o.getKeyword());
		} catch (WrongValueException e) {}
		
		try {
			buzzword.setText(o.getBuzzword());
		} catch (WrongValueException e) {}
		
		try {
			resourcesRaw.setText(o.getResourcesRaw());
		} catch (WrongValueException e) {}
	
		incurredAt.setValue(o.getIncurredAt());
		incurredAt.setFormat(ZkGlobals.FORMAT_DATETIME);

		SimpleDateFormat format = new SimpleDateFormat(ZkGlobals.FORMAT_DATETIME);
		
		if(o.getTakenOverAt() != null)
			takenOverAtLabel.setValue(format.format(o.getTakenOverAt()));
		takenOverAt = o.getTakenOverAt();
		
		if(o.getDispatchedAt() != null)
			dispatchedAtLabel.setValue(format.format(o.getDispatchedAt()));
		dispatchedAt = o.getDispatchedAt();
		
    	if(o.getPositionLatitude() != 0 && o.getPositionLongitude() != 0) {
    		initMap(o.getPositionLongitude(), o.getPositionLatitude());
    	} else {
    		map.setVisible(false);
    	}
	}
	
	/**
	 * Helper: Read data from .zul and fill it into a {@link User} object.
	 * @return
	 * @throws CoreException 
	 */
	private Operation getFromZul() throws GeneralSecurityException, ZkException {
		Operation o = new Operation();
		
		// set core data
		o.setId(operationId);
		o.setOperationNr(operationNr.getValue());
		o.setPositionLongitude(positionLongitude.getValue());
		o.setPositionLatitude(positionLatitude.getValue());
		o.setCity(city.getText());
		o.setStreet(street.getText());
		o.setObject(object.getText());
		o.setCrossway(crossway.getText());
		o.setPriority(priority.getText());
		o.setKeyword(keyword.getText());
		o.setBuzzword(buzzword.getText());
		o.setIncurredAt(incurredAt.getValue());
		o.setTakenOverAt(takenOverAt);
		o.setDispatchedAt(dispatchedAt);
		
		o.setResourcesRaw(resourcesRaw.getText());
		
		return o;
	}

	/**
	 * Helper: Delete current set operation
	 */
	private void deleteCurrentOperation() {
		if(operationId > 0) {
			OperationService operationService = (OperationService)ContainerManager.getComponent("operationService");
			operationService.remove(operationId);
			
			setContentZul(ZkGlobals.PAGE_OPERATION_LIST);
		}
	}
	
	/**
	 * Helper: Init Map
	 */
	private void initMap(double longVal, double latVal) {
		log.debug("Arrange map with coords long: " + longVal + "; lat: " + latVal);
		
		LonLat currentPosition = new LonLat(longVal,latVal).transform(new Projection("EPSG:4326"), map.getProjection());
		map.setCenter(currentPosition, 15);
		
        Size size = new Size(21,25);
        Pixel offset = new Pixel(-(size.getWidth()/2), -size.getHeight());
        Icon icon = new Icon("/openflorian/includes/img/icon/marker.png",size,offset);
        
        marker = new Marker(currentPosition,icon);
        markers.addMarker(marker);
	}

}
