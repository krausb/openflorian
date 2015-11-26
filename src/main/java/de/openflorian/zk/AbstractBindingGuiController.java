package de.openflorian.zk;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.bind.BindComposer;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.metainfo.ComponentInfo;

import de.openflorian.web.user.AuthenticatedUserSessionLocal;

/**
 * BindComposer Extension
 * 
 * @author Bastian Kraus <me@bastian-kraus.me>
 */
public abstract class AbstractBindingGuiController extends BindComposer implements GuiController {
	private static final long serialVersionUID = 4407462108300419571L;

	/**
	 * Applied Page
	 */
	protected Page page;
	
	/**
	 * Applied component
	 */
	protected Component self;
	
	/**
	 * Logger for all Subclasses
	 */
    protected Logger log = LoggerFactory.getLogger(getClass());
    protected List<String> affectedFields = new ArrayList<String>();
    
    @Override
    public ComponentInfo doBeforeCompose(Page page, Component parent, ComponentInfo compInfo) {
    	this.page = page;
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
    	this.self = comp;

    	if(mustBeLoggedIn() && AuthenticatedUserSessionLocal.get() != null) {
    		boolean mayView = mayView(getUser());
    		log.debug("User permitted to current view: " + mayView);
    		if(!mayView)
    			setContentZul(getAboutPage());
    	}
    }

}
