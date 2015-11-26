package de.openflorian.zk;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.metainfo.ComponentInfo;
import org.zkoss.zk.ui.util.GenericForwardComposer;

import de.openflorian.web.user.AuthenticatedUserSessionLocal;


/**
 * Abstract UI Controller
 * 
 * @author Bastian Kraus <me@bastian-kraus.me>
 */
@SuppressWarnings("rawtypes")
public abstract class AbstractGenericForwardController extends GenericForwardComposer implements GuiController {

	/**
	 * Logger for all Subclasses
	 */
    protected Logger log = LoggerFactory.getLogger(getClass());

    private static final long serialVersionUID = -5002909515906285673L;
    
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

    @SuppressWarnings("unchecked")
	@Override
    public void doAfterCompose(Component comp) throws Exception {
    	super.doAfterCompose(comp);

    	if(mustBeLoggedIn() && AuthenticatedUserSessionLocal.get() != null) {
    		boolean mayView = mayView(getUser());
    		log.debug("User permitted to current view: " + mayView);
    		if(!mayView)
    			setContentZul(getAboutPage());
    	}
    }
    
}
