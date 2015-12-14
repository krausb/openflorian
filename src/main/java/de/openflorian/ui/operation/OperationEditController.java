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

import java.security.GeneralSecurityException;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.ValidationException;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WebApps;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.EventQueues;
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
import de.openflorian.alarm.AlarmEvent;
import de.openflorian.alarm.ZkAlarmEvent;
import de.openflorian.data.model.Operation;
import de.openflorian.data.model.security.GlobalPermission;
import de.openflorian.event.EventQueue;
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
			}
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
		
		incurredAt.setValue(o.getIncurredAt());
		incurredAt.setFormat(ZkGlobals.FORMAT_DATETIME);

		SimpleDateFormat format = new SimpleDateFormat(ZkGlobals.FORMAT_DATETIME);
		takenOverAtLabel.setValue(format.format(o.getIncurredAt()));
		takenOverAt = o.getTakenOverAt();
		dispatchedAtLabel.setValue(format.format(o.getIncurredAt()));
		dispatchedAt = o.getDispatchedAt();
		
		
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

}
