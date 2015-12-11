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
import java.util.Set;

import javax.xml.bind.ValidationException;

import org.apache.commons.lang.StringUtils;
import org.reflections.Reflections;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Messagebox.ClickEvent;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import de.openflorian.crypt.CryptCipherService;
import de.openflorian.crypt.CryptCipherService.CipherTarget;
import de.openflorian.data.model.security.GlobalPermission;
import de.openflorian.data.model.security.PermissionSet;
import de.openflorian.service.PermissionService;
import de.openflorian.service.UserService;
import de.openflorian.ui.ZkGlobals;
import de.openflorian.web.core.AvailableComponent;
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
	
	private Window userEditWindow;
	
	private Button saveButton;
	private Button cancelButton;
	private Button applyButton;
	private Button deleteButton;
	
	private long userId;
	
	private Textbox password;
	private Textbox confirmPassword;
	private String tmpPassword;
	
	private Textbox name;
	private Textbox firstname;
	private Textbox lastname;
	private Textbox email;
	
	private Tabbox permissionsTab;
	
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

    	User selectedUser = (User)execution.getAttribute(ZkGlobals.REQUEST_ENTITY);

    	if(selectedUser != null) {
    		log.debug("Recieved user to select: " + selectedUser.toString());
    		fillZulFromUser(selectedUser);
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
			User u = getUserFromZul();
			
			UserService userService = (UserService)ContainerManager.getComponent("userService");
			u = userService.persist(u);
			if(u.getId() > 0) {
				this.userId = u.getId();
				execution.setAttribute(ZkGlobals.REQUEST_ENTITY,u);
				fillZulFromUser(u);
			}
			
			Clients.showNotification(Labels.getLabel("admin.user.edit.msg.usersaved"),"info", applyButton,"top_center", 2000);
		} catch (WrongValueException e) {
			log.debug(e.getMessage(), e);
			setError(new ZkException(Labels.getLabel("user.model." + e.getComponent().getId()) + ": " + e.getMessage(), e));			
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
	 * Redirects to User List ZUL
	 * 
	 * @param event
	 */
	public void onClick$saveButton(Event event) {		
		try {
			User u = getUserFromZul();
			
			UserService userService = (UserService)ContainerManager.getComponent("userService");
			u = userService.persist(u);
			
			// redirect to user list
			setContentZul(ZkGlobals.PAGE_SYSTEM_USERS);
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
		if(userId > 0) {
			Messagebox.show(
					Labels.getLabel("admin.user.edit.msg.deleteuser", new String[]{name.getText()}), 
					Labels.getLabel("global.question"), 
					new Messagebox.Button[]{Messagebox.Button.YES, Messagebox.Button.NO},
					Messagebox.QUESTION,
					new EventListener<ClickEvent>() {
						
						public void onEvent(ClickEvent event) throws Exception {
							if(event.getButton() == Messagebox.Button.YES)
								OperationEditController.this.deleteCurrentUser();
						}
						
					});
		} else {
			Clients.showNotification(Labels.getLabel("admin.user.edit.msg.nouserselected"),"info", applyButton,"top_center", 2000);
		}
	}
	
	/**
	 * Event-Handler: cancelButton.onClick
	 * @param event
	 */
	public void onClick$cancelButton(Event event) {
		setContentZul(ZkGlobals.PAGE_SYSTEM_USERS);
	}
	
	
	/**
	 * Helper: Fill .zul with data from given {@code u} 
	 * @param u {@link User}
	 */
	private void fillZulFromUser(User u) {
		userId = u.getId();
		
		// fill core data
		name.setText(u.getName());
		password.setText("");
		confirmPassword.setText("");
		tmpPassword = u.getPassword();
		
		// fill contact data
		try {
			firstname.setText(u.getFirstname());
		} catch (WrongValueException e) {}
		try {
			lastname.setText(u.getLastname());
		} catch (WrongValueException e) {}

		email.setText(u.getEmail());
		
	}
	
	/**
	 * Helper: Read data from .zul and fill it into a {@link User} object.
	 * @return
	 * @throws CoreException 
	 */
	private User getUserFromZul() throws GeneralSecurityException, ZkException {
		User zu = new User();
		
		// set core data
		zu.setId(userId);
		zu.setName(name.getValue());
		if(!StringUtils.isEmpty(password.getValue())) {
			if(password.getText().equals(confirmPassword.getText())) {
				CryptCipherService cryptService = (CryptCipherService)ContainerManager.getComponent(AvailableComponent.CryptService.toString());
				zu.setPassword(cryptService.encrypt(cryptService.encrypt(password.getText(), CipherTarget.Xor), CipherTarget.Blowfish));
			} else {
				throw new ZkException("admin.user.edit.validation.password");
			}
		} else {
			if(tmpPassword == null)
				throw new ZkException("user.error.passwordempty");
			zu.setPassword(tmpPassword);
		}
		zu.setFirstname(firstname.getText());
		zu.setLastname(lastname.getText());
		zu.setEmail(email.getText());

		return zu;
	}

	/**
	 * Helper: Delete current set operation
	 */
	private void deleteCurrentOperation() {
		if(userId > 0) {
			UserService userService = (UserService)ContainerManager.getComponent("userService");
			userService.remove(userId);
			
			setContentZul(ZkGlobals.PAGE_SYSTEM_USERS);
		}
	}

}
