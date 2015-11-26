package de.openflorian.ui.system.user;

import java.lang.reflect.Method;
import java.util.Collection;

import javax.persistence.NoResultException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Tabpanel;

import de.openflorian.data.model.security.PermissionSet;
import de.openflorian.service.PermissionService;
import de.openflorian.web.core.ContainerManager;
import de.openflorian.web.user.User;
import de.openflorian.zk.renderer.ComponentRenderer;

/**
 * {@link UserPermissionTabpanelRenderer}<br/>
 * <br/>
 * Renders a {@link PermissionSet} {@link Tabpanel}. The permissions will be
 * represented with {@link Checkbox}es and have an {@link UserPermissionChangedEventListener} 
 * attached which handles the permission changes automatically.
 * 
 * @author Bastian Kraus <me@bastian-kraus.me>
 */
public class UserPermissionTabpanelRenderer implements ComponentRenderer {
	
	private final Logger log = LoggerFactory.getLogger(getClass());

	private final Class<? extends PermissionSet> permissionSetClazz;
	private final User user;
	
	public UserPermissionTabpanelRenderer(User user, Class<? extends PermissionSet> permissionSetClazz) {
		this.permissionSetClazz = permissionSetClazz;
		this.user = user;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Component render() {
		Grid g = new Grid();
		Rows rows = new Rows();
		rows.setParent(g);

		// get result of PERMISSION_TYPES()
		Collection<String> permissionTypes = null;
		try {
			Method permissionTypesMethod = permissionSetClazz.getMethod("PERMISSION_TYPES");
			permissionTypes = (Collection<String>) permissionTypesMethod.invoke(null);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}

		PermissionService permissionService = (PermissionService)ContainerManager.getComponent("permissionService");
		if(permissionService == null)
			throw new IllegalStateException("No permission service bean availalbe!");
		
		for(String permission: permissionTypes) {
			Row permissionRow = new Row();
			
			Label label = new Label(Labels.getLabel("permissions.label." + permission.toLowerCase()));
			label.setTooltip(Labels.getLabel("permissions.description." + permission.toLowerCase()));
			label.setParent(permissionRow);
			
			Checkbox checkbox = new Checkbox();
			checkbox.setAttribute("permission", permission);
			checkbox.addEventListener("onCheck", new UserPermissionChangedEventListener(user, permission));
			try {
				checkbox.setChecked(permissionService.getByUserAndPermission(user, permission) != null);
			} catch (NoResultException e) {
				checkbox.setChecked(false);
			}
			checkbox.setParent(permissionRow);
			
			permissionRow.setParent(rows);
		}
		
		Tabpanel p = new Tabpanel();
		g.setParent(p);
		
		return p;
	}

}
