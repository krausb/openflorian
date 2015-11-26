package de.openflorian.web.core;

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
 * Available components in spring application context
 * 
 * @author Bastian Kraus <me@bastian-kraus.me>
 */
public enum AvailableComponent {
	BootstrapManager("bootstrapManager"),
	ConfigurationProvider("configurationProvider"),
	CryptService("cryptService"),
	I18nProvider("i18n"),
	PermissionManager("permissionManager"),
	PluginContext("pluginContext"),
	SecurityService("securityService"),
	UserService("userService");
	
	private final String comp;
	private AvailableComponent(String comp) {
		this.comp = comp;
	}
	
	public String toString() {
		return comp;
	}
	
}
