package de.openflorian.ui;

import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;

import de.openflorian.web.user.AuthenticatedUserSessionLocal;
import de.openflorian.web.user.User;
import de.openflorian.zk.AbstractGuiController;

/**
 * Controller for Layout Header Bar
 * 
 * @author Bastian Kraus <bk@pogo-systems.de>
 */
public class HeaderController extends AbstractGuiController {
	private static final long serialVersionUID = 1182851061480627950L;

	private Logger log = LoggerFactory.getLogger(getClass());
	
	private Label logout;
	private Label loggedInAs;
	
	private Listbox languages;
	
	private Image stageImage;
	
	@Override
	public String getLoginPage() {
		return ZkGlobals.PAGE_LOGIN;
	}
	
	@Override
	public boolean mayView(Principal user) {
		return user != null;
	}
	
    @Override
    public void doAfterCompose(Component comp) throws Exception {
    	super.doAfterCompose(comp);
    	
    	User user = AuthenticatedUserSessionLocal.get();
    	
    	if(user != null)
    		loggedInAs.setValue(user.getName() + " (" + user.getLastname() + ", " + user.getFirstname() + ")");
    	
    }
    
    /**
     * Event-Handler: logout.onClick
     * @param event
     */
    public void onClick$logout(Event event) {
    	AuthenticatedUserSessionLocal.set(null);
    	
    	execution.sendRedirect(ZkGlobals.PAGE_LOGIN);
    }
    
    /**
     * Property: Application Version
     * @return
     */
    public String getVersion() {
    	return "0.1";
    }
    
    /**
     * Property: Test Mode of Application active or not
     * @return
     */
    public boolean getMode() {
    	return false;
    }
    	
}
