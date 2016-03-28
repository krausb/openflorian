package de.openflorian.web.velocity;

import java.util.Locale;

import de.openflorian.web.core.*;
import de.openflorian.web.user.User;

/**
 * Velocity Tool<br/>
 * <br/>
 * Extends the velocity context inside velocity templates<br/>
 * to provide access to for e.g. the current action, i18n, ...
 * 
 * @author Bastian Kraus <bk@absout-pogo.de>
 */
public class VelocityTool {

	/**
	 * Give access to {@link BootstrapManager} functionality
	 * 
	 * @return {@link BootstrapManager}
	 */
	public BootstrapManager getBootstrapManager() {
		return BootstrapManager.manager();
	}

	/**
	 * Give access to {@link I18nProvider}
	 * 
	 * @return {@link I18nProvider}
	 */
	public TranslationProvider getTranslationProvider() {
		return TranslationProvider.provider();
	}

	/**
	 * Get current locale
	 * 
	 * @return
	 */
	public Locale getLocaleProvider() {
		return LocaleProvider.getCurrentLocale();
	}

	/**
	 * Get authenticated {@link User}
	 */
	public User getCurrentUser() {
		return (User) RequestDataProvider.getCurrentHttpRequest().getSession().getAttribute(WebConstants.SESSION_USER);
	}

	/**
	 * Get current Http Addr
	 */
	public String getHttpAddr() {
		return RequestDataProvider.getCurrentHttpRequest().getRequestURI();
	}

	/**
	 * Get configured base url from
	 * {@link ConfigurationProperties#SystemBasePath}
	 * 
	 * @return
	 */
	public String getBaseUrl() {
		return BootstrapManager.manager().getContextPath();
	}

	/**
	 * Determines if the current HTTP Request is secure (SSL) or not
	 * 
	 * @return <code>true</code> if secure (SSL)
	 */
	public boolean isRequestSecure() {
		return RequestDataProvider.getCurrentHttpRequest().isSecure();
	}

}
