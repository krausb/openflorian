package de.openflorian.zk;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.validator.InvalidValue;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.http.ExecutionImpl;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;

/**
 * GUI Notification Handler
 * 
 * @author Bastian Kraus <bk@pogo-systems.de>
 */
public abstract class AbstractGuiNotificationHandler implements GuiController {

	protected List<String> affectedFields = new ArrayList<String>();
	
	@Override
	public Page getPageObject() {
		return ((ExecutionImpl)Executions.getCurrent()).getCurrentPage();
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
            notificationBox = getPageObject().getFellow("notificationBox");
            error = notificationBox.getFellow("notificationMessage");
        } else {
            Page page = Executions.getCurrent().getDesktop().getPage(pageName);
            notificationBox = page.getFellow("notificationBox");
            error = notificationBox.getFellow("notificationMessage");
        }

        ((Label)error).setValue(message);
        ((Label)error).setVisible(true);

        Component notificationWrapper = notificationBox.getFellow("notificationWrapper");
        ((Div)notificationWrapper).setClass(type);
        notificationWrapper.setVisible(true);
    }

    /* (non-Javadoc)
	 * @see de.pogosystems.zk.GuiController#setNotification(java.lang.String, java.lang.String)
	 */
    @Override
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

    /* (non-Javadoc)
	 * @see de.pogosystems.zk.GuiController#setError(de.pogosystems.zk.ZkException)
	 */
    @Override
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
                Component comp = getPageObject().getFellowIfAny(field);
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
                // TODO List
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
            errorLabel = getPageObject().getFellowIfAny("notificationBox");
        } else {
            Page page = Executions.getCurrent().getDesktop().getPage(pageName);
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
            Component comp = getPageObject().getFellowIfAny(id);
            if (comp != null && comp instanceof HtmlBasedComponent) {
                HtmlBasedComponent htmlComp = (HtmlBasedComponent)comp;
                // TODO Something more useful (unset of style does not seem possible). Maybe style classes?
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
            notificationBox = getPageObject().getFellow("notificationBox");
            error = notificationBox.getFellow("notificationMessage");
        } else {
            Page page = Executions.getCurrent().getDesktop().getPage(pageName);
            notificationBox = page.getFellow("notificationBox");
            error = notificationBox.getFellow("notificationMessage");
        }

        if (error != null) {
            ((Label)error).setValue(null);
            ((Label)error).setVisible(false);

        }
    }
	
}
