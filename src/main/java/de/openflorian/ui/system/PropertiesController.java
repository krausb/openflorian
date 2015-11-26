package de.openflorian.ui.system;

import java.security.Principal;

import org.zkoss.zk.ui.Component;

import de.openflorian.data.model.security.GlobalPermission;
import de.openflorian.service.PermissionService;
import de.openflorian.ui.ZkGlobals;
import de.openflorian.web.core.ContainerManager;
import de.openflorian.web.user.User;
import de.openflorian.zk.AbstractGuiController;

/**
 * System Properties UI Controller<br/>
 * <Br/>
 * ZUL: webapp/system/properties.zul
 * 
 * @author Bastian Kraus <me@bastian-kraus.me>
 */
public class PropertiesController extends AbstractGuiController {
	private static final long serialVersionUID = 3698439925438248515L;

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
    }
	
}
