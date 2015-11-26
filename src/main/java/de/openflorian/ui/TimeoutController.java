package de.openflorian.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.util.GenericForwardComposer;

import de.openflorian.web.user.AuthenticatedUserSessionLocal;

/**
 * Session Timeout Controller<br/>
 * <br/>
 * ZUL: /timeout.zul
 * 
 * @author Bastian Kraus <me@bastian-kraus.me>
 */
public class TimeoutController extends GenericForwardComposer {
	private static final long serialVersionUID = 8133567451702629273L;
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	
    @Override
    public void doAfterCompose( Component comp ) throws Exception {
        super.doAfterCompose(comp);
        
        if(AuthenticatedUserSessionLocal.get() != null) {
        	AuthenticatedUserSessionLocal.set(null);
        	execution.sendRedirect(ZkGlobals.PAGE_LOGIN);
        } else {
        	execution.sendRedirect("/index.zul");
        }
    }
}
