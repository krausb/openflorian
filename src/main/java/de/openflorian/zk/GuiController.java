package de.openflorian.zk;

import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.security.Principal;

import org.zkoss.util.media.Media;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.event.EventQueue;
import org.zkoss.zk.ui.util.Composer;
import org.zkoss.zul.Include;

import de.openflorian.web.core.AvailableComponent;
import de.openflorian.web.core.ContainerManager;
import de.openflorian.web.core.WebConstants;
import de.openflorian.web.user.AuthenticatedUserSessionLocal;
import de.openflorian.web.user.PermissionService;
import de.openflorian.web.user.User;

/**
 * Generic ZK GUI Controller Interface<Br/>
 * <Br/>
 * 
 * @author Bastian Kraus <me@bastian-kraus.me>
 */
public interface GuiController {
	
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
	public default String getLoginPage() {
		return ZkGlobals.PAGE_LOGIN;
	}

	/**
	 * About Page ZUL
	 * @return
	 */
	public default String getAboutPage() {
		return ZkGlobals.PAGE_ABOUT;
	}

	/**
	 * Gets a name for the {@link EventQueue} for which
	 * the current controller may be listening on.
	 * 
	 * @return
	 */
	public default String getEventQueue() {
		return getClass().getName();
	}

	/**
	 * Gets the current processes entity
	 * 
	 * @return
	 */
	public default Object getTargetEntity() {
		return null;
	}

	/**
	 * Tells the parent {@link Composer} wether there has to be a valid
	 * {@link AuthenticatedUserSessionLocal#get()} not <code>null</code>.
	 * 
	 * @return
	 */
	public default boolean mustBeLoggedIn() {
		return true;
	}

	/**
	 * Set notification box by String.
	 * 
	 * @param message Notification message as I18n key
	 * @param type GUIConstants.NOTIFY_*
	 */
	public abstract void setNotification(String message, String type);

	/**
	 * Set error message by exception.
	 * 
	 * @param e - the {@link ZkException}.
	 */
	public abstract void setError(ZkException e);

	/**
     * Set zul for center component.
     * 
     * @param zul Zul to set
     */
    public default void setContentZul( String zul ) {
        Page mainPage = Executions.getCurrent().getDesktop().getPage("mainPage");
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
     * 		{@link User}
     */
    public default User getUser() {
    	User user = AuthenticatedUserSessionLocal.get();
    	if ( user == null ) {
    		// Paranoia
    		throw new SecurityException("User not logged in");
    	}
    	return user;
    }

    /**
     * Check a given <code>permission</code> for {@link GuiController#getUser()}
     * @param permission
     * @return
     */
	public default boolean hasPermission(String permission) {
    	PermissionService permissionService = (PermissionService)ContainerManager.getComponent(AvailableComponent.PermissionManager);
    	if(permissionService != null)
    		return permissionService.hasPermission(getUser(), permission);
   		else
    		return true;
    }

    /**
     * Get the current set {@link Page} object
     */
	public abstract Page getPageObject();
    
    /* (non-Javadoc)
	 * @see de.pogosystems.zk.GuiController#getRequestContextPath()
	 */
	public default String getRequestContextPath() {
    	return Executions.getCurrent().getContextPath(); 
    }
    
    /**
     * Helper method: Convert Media object to Reader, independently of mime type.
     * 
     * @param media
     * @return Reader
     */
    public default Reader getReaderFromMedia( Media media ) throws ZkException {
        if (media.isBinary()) {
            try {
                return new InputStreamReader(media.getStreamData(), WebConstants.MEDIA_CHARSET);
            } catch (UnsupportedEncodingException e) {
                // Should never happen
                throw new RuntimeException("Unexpected unsupported charset", e);
            } catch (Exception e) {
            	throw new ZkException(e.getMessage(), e);
            }
        } else {
            return media.getReaderData();
        }
    }
    
    /**
     * Helper method: Return translated error or #marked# error string.
     * 
     * @param message I18n string
     * @param values Values for variable substitution
     * @return Hopefully translated message
     */
    public default String getLabel( String message,
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

}
