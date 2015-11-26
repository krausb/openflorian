package de.openflorian.web.core;

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
