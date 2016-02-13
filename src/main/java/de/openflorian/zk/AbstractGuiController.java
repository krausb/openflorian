package de.openflorian.zk;

import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.validator.InvalidValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.media.Media;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.event.EventQueue;
import org.zkoss.zk.ui.metainfo.ComponentInfo;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zul.Div;
import org.zkoss.zul.Include;
import org.zkoss.zul.Label;

import de.openflorian.service.PermissionService;
import de.openflorian.web.core.AvailableComponent;
import de.openflorian.web.core.ContainerManager;
import de.openflorian.web.core.WebConstants;
import de.openflorian.web.user.AuthenticatedUserSessionLocal;
import de.openflorian.web.user.User;

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

/**
 * Abstract UI Controller<br/>
 * <br/>
 * Extends the {@link GenericForwardComposer} with UI logic like {@link AbstractGuiController#mayView(Principal)}
 * or {@link AbstractGuiController#getLoginPage()}.
 * 
 * @author Bastian Kraus <me@bastian-kraus.me>
 */
public abstract class AbstractGuiController extends GenericForwardComposer {

	/**
	 * Logger for all Subclasses
	 */
    protected Logger log = LoggerFactory.getLogger(getClass());

    private static final long serialVersionUID = -5002909515906285673L;
    private List<String> affectedFields = new ArrayList<String>();
    protected AnnotateDataBinder dataBinder;

    /**
     * Delegate method for checking users permission to view a ZUL.
     *  
     * @param user
     * @return true: may view - false: access denied
     */
    public abstract boolean mayView(Principal user);
    
    /**
     * Login Page ZUL
     * @return
     */
    public String getLoginPage() {
    	return ZkGlobals.PAGE_LOGIN;
    }
    
    /**
     * About Page ZUL
     * @return
     */
    public String getAboutPage() {
    	return ZkGlobals.PAGE_ABOUT;
    }
    
    /**
     * Gets a name for the {@link EventQueue} for which
     * the current controller may be listening on.
     * 
     * @return
     */
    public String getEventQueue() {
    	return getClass().getName();
    }
    
    /**
     * Gets the current processes entity
     * 
     * @return
     */
    public Object getTargetEntity() {
    	return null;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public ComponentInfo doBeforeCompose(Page page, Component parent, ComponentInfo compInfo) {
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

    /**
     * {@inheritDoc}
     */
    @Override
    public void doAfterCompose(Component comp) throws Exception {
    	super.doAfterCompose(comp);

    	if(mustBeLoggedIn() && AuthenticatedUserSessionLocal.get() != null) {
    		boolean mayView = mayView(getUser());
    		log.debug("User permitted to current view: " + mayView);
    		if(!mayView)
    			setContentZul(getAboutPage());
    	}
    	
    	dataBinder = new AnnotateDataBinder(comp);
    	
    }
    
    public boolean mustBeLoggedIn() {
    	return true;
    }
    
    /**
     * Set error/warning/info message.
     * 
     * @param message Notification message (already translated)
     * @param type GUIConstants.NOTIFY_*
     */
    private void internalSetMessage( String message,
                                     String type ) {
        internalSetMessage(null, message, type);

    }

    /**
     * @param pageName
     * @param message
     * @param type
     */
    private void internalSetMessage( String pageName,
                                     String message,
                                     String type ) {

        // Set message label
        Component notificationBox = null;
        Component error = null;

        if (pageName == null) {
            notificationBox = self.getFellow("notificationBox");
            error = notificationBox.getFellow("notificationMessage");
        } else {
            Page page = desktop.getPage(pageName);
            notificationBox = page.getFellow("notificationBox");
            error = notificationBox.getFellow("notificationMessage");
        }

        ((Label)error).setValue(message);
        ((Label)error).setVisible(true);

        Component notificationWrapper = notificationBox.getFellow("notificationWrapper");
        ((Div)notificationWrapper).setClass(type);
        notificationWrapper.setVisible(true);
    }

    /**
     * Set notification box by String.
     * 
     * @param message Notification message as I18n key
     * @param type GUIConstants.NOTIFY_*
     */
    public void setNotification( String message,
                                    String type ) {
        internalSetMessage(getLabel(message, null), type);
    }

    /**
     * Set notification box by String.
     * 
     * @param message Notification message as I18n key
     * @param type GUIConstants.NOTIFY_*
     */
    protected void setNotification( String message,
                                    String[] values,
                                    String type ) {
        internalSetMessage(getLabel(message, values), type);
    }

    /**
     * Sets notification box in specific page by String
     * 
     * @param message
     * @param type
     * @param pageName
     */
    protected void setNotification( String message,
                                    String type,
                                    String pageName ) {
        internalSetMessage(pageName, getLabel(message, null), type);
    }

    /**
     * Set error message by exception.
     * 
     * @param e - the {@link BLException}.
     */
    public void setError( ZkException e ) {
        setError(e, null);
    }

    /**
     * @param e
     * @param pageName 
     */
    protected void setError( ZkException e,
                             String pageName ) {
        String errorMessage;

        // Special treatment for ValidationException
        if (e instanceof ValidationException) {
            InvalidValue[] values = ((ValidationException)e).getInvalidValues();
            List<String> messages = new ArrayList<String>();
            boolean focusSet = false;
            for (InvalidValue value : values) {
                String field = value.getPropertyName();

                // Mark affected fields
                Component comp = self.getFellowIfAny(field);
                if (comp != null && comp instanceof HtmlBasedComponent) {
                    HtmlBasedComponent htmlComp = (HtmlBasedComponent)comp;
                    affectedFields.add(htmlComp.getId());

                    // set Error Color to Field
                    htmlComp.setZclass("validationError");

                    if (!focusSet) {
                        htmlComp.setFocus(true);
                        focusSet = true;
                    }
                }

                // Unique set of messages
                if (!messages.contains(value.getMessage())) {
                    messages.add(value.getMessage());
                }
            }

            // Append error messages
            StringBuilder messageBuilder = new StringBuilder();
            for (String message : messages) {
                messageBuilder.append(getLabel(message, null));
                messageBuilder.append(" ");
            }
            errorMessage = messageBuilder.toString();
        } else {
            // Standard BL exception
            errorMessage = getLabel(e.getMessage(), e.getValues());
        }
        if (pageName == null) {
            internalSetMessage(errorMessage, ZkGlobals.NOTIFY_ERROR);
        } else {
            internalSetMessage(pageName, errorMessage, ZkGlobals.NOTIFY_ERROR);
        }
    }

    /**
     * Helper method: Return translated error or #marked# error string.
     * 
     * @param message I18n string
     * @param values Values for variable substitution
     * @return Hopefully translated message
     */
    protected String getLabel( String message,
                               String[] values ) {
        String ret;
        if (values == null) {
            ret = Labels.getLabel(message);
        } else {
            ret = Labels.getLabel(message, values);
        }
        if (ret == null || ret.length() == 0) {
            return "#" + message + "#";
        }
        return ret;
    }

    /**
     * Resets error messages for current page.
     */
    protected void resetError() {
        resetError(null);
    }

    /**
     * Resets error messages for specified page.
     * 
     * @param pageName
     */
    protected void resetError( String pageName ) {

        // Reset message on top of screen
        clearNotificationBox(pageName);

        // Set message label
        Component errorLabel = null;

        if (pageName == null) {
            errorLabel = self.getFellowIfAny("notificationBox");
        } else {
            Page page = desktop.getPage(pageName);
            errorLabel = page.getFellow("notificationBox");

        }
        // Set error label
        if (errorLabel != null) {

            Component error = errorLabel.getFellow("notificationMessage");
            ((Label)error).setValue("");
            ((Label)error).setVisible(false);

            Component notificationWrapper = errorLabel.getFellow("notificationWrapper");
            notificationWrapper.setVisible(false);

        }
        // Unset styles
        for (String id : affectedFields) {
            Component comp = self.getFellowIfAny(id);
            if (comp != null && comp instanceof HtmlBasedComponent) {
                HtmlBasedComponent htmlComp = (HtmlBasedComponent)comp;
                htmlComp.setZclass("defaultField");
                // ("border: 1px solid green");
            }
        }
        affectedFields.clear();
    }

    /**
     * Löscht die bestehende Fehlermeldung für die übergebene Seite aus der NotificationBox.
     * 
     * @param pageName
     */
    private void clearNotificationBox( String pageName ) {

        // Set message label
        Component notificationBox = null;
        Component error = null;

        if (pageName == null) {
            notificationBox = self.getFellow("notificationBox");
            error = notificationBox.getFellow("notificationMessage");
        } else {
            Page page = desktop.getPage(pageName);
            notificationBox = page.getFellow("notificationBox");
            error = notificationBox.getFellow("notificationMessage");
        }

        if (error != null) {
            ((Label)error).setValue(null);
            ((Label)error).setVisible(false);

        }
    }

    /**
     * Set zul for center component.
     * 
     * @param zul Zul to set
     */
    protected void setContentZul( String zul ) {
        Page mainPage = desktop.getPage("mainPage");
        Include include = (Include)mainPage.getFellow("content");
        include.setSrc(zul);
        // This line forces a rerender even, if the src stayed the same.
        // A rerender is necessary here because the data may be different now.
        // So we want doAfterCompose to be called again.
        include.invalidate();
    }
    
    /**
     * Get current {@link User}
     * 
     * @return
     */
    protected User getUser() {
    	User user = AuthenticatedUserSessionLocal.get();
    	if ( user == null ) {
    		// Paranoia
    		throw new SecurityException("User not logged in");
    	}
    	if (log.isDebugEnabled())
    		log.debug("Session value:" + user.getId());
    	return user;
    }

    /**
     * Check if current {@link User} is Admin
     * 
     * @return
     */
    public boolean hasPermission(String permission) {
    	PermissionService permissionService = (PermissionService)ContainerManager.getComponent(AvailableComponent.PermissionManager);
    	if(permissionService != null)
    		return permissionService.hasPermission(getUser(), permission);
   		else
    		return true;
    }

    /**
     * Get current Page
     * 
     * @return {@link Page}
     */
    public Page getPageObject() {
    	return page;
    }
    
    /**
     * Get request context path
     * 
     * @return {@link String}
     */
    public String getRequestContextPath() {
    	return Executions.getCurrent().getContextPath(); 
    }
    
    /**
     * Helper method: Convert Media object to Reader, independently of mime type.
     * 
     * @param media
     * @return Reader
     */
    protected Reader getReaderFromMedia( Media media ) throws ZkException {
        if (media.isBinary()) {
            try {
            	log.debug("open file with encoding: " + WebConstants.MEDIA_CHARSET);
                return new InputStreamReader(media.getStreamData(), WebConstants.MEDIA_CHARSET);
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("Unexpected unsupported charset", e);
            } catch (Exception e) {
            	throw new ZkException(e.getMessage(), e);
            }
        } else {
            return media.getReaderData();
        }
    }
    
}
