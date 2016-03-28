package de.openflorian.web.admin.system;

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

import java.security.Principal;

import com.vaadin.ui.UI;

import de.openflorian.web.AbstractBrowser;

/**
 * System Properties UI Controller<br/>
 * <Br/>
 * ZUL: webapp/system/properties.zul
 * 
 * @author Bastian Kraus <bofh@k-hive.de>
 */
public class PropertiesController extends AbstractBrowser {

	public PropertiesController(UI view) {
		super(view);
		// TODO Auto-generated constructor stub
	}

	private static final long serialVersionUID = 3698439925438248515L;

	@Override
	public boolean mayView(Principal user) {
		// PermissionService ps =
		// (PermissionService)ContainerManager.getComponent("permissionService");
		// return user != null && ps.hasPermission((User) user,
		// GlobalPermission.ADMINISTER);
		return true;
	}

	@Override
	public void init() {
		// super.doAfterCompose(comp);
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

}
