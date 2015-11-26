package de.openflorian.web.core;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Locale;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

/**
 * I18n Provider<br/>
 * <br/>
 * Provides internationalized text labels from i3-label.properties / i3-label_[LANG].properties
 * 
 * @author Bastian Kraus <me@bastian-kraus.me>
 */
public class DefaultI18nProvider implements InitializingBean, I18nProvider {

	private Logger log = LoggerFactory.getLogger(getClass());

	private BootstrapManager bootstrapManager;

	private URLClassLoader i18nClassLoader;

	@Override
	public String getText(String label) {
		try {
			// http://docs.oracle.com/javase/7/docs/api/java/util/Properties.html#load%28java.io.InputStream%29
			// we use UTF-8 for properties - so we need to convert from ISO-8859-1 to UTF-8 here.
			return new String(getCurrentResourceBundle().getString(label).getBytes("ISO-8859-1"), "UTF-8");
		} catch (Exception e) {
			log.error("Error retrieving i18n value for key '" + label + "': " + e.getMessage(), e);
			return label;
		}
	}

	@Override
	public String getText(String label, String replacement) {
		try {
			// http://docs.oracle.com/javase/7/docs/api/java/util/Properties.html#load%28java.io.InputStream%29
			// we use UTF-8 for properties - so we need to convert from ISO-8859-1 to UTF-8 here.
			return new String(getCurrentResourceBundle().getString(label).getBytes("ISO-8859-1"), "UTF-8").replace("{0}", replacement);
		} catch (Exception e) {
			log.error("Error retrieving i18n value for key '" + label + "': " + e.getMessage(), e);
			return label;
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Locale.setDefault(I18nLocaleProvider.getDefaultLocale());
		
		URL[] urls = {new File(bootstrapManager.getRealPathForVirtual("/WEB-INF/")).toURI().toURL()};
		i18nClassLoader = new URLClassLoader(urls);
	}

	/**
	 * Load current resource bundle
	 * 
	 * @return
	 */
	private ResourceBundle getCurrentResourceBundle() {
		Locale rLocale = I18nLocaleProvider.getCurrentLocale();
		
		ResourceBundle bundle = null;
		try {
			bundle = ResourceBundle.getBundle("i3-label", rLocale, i18nClassLoader);
		} catch(Exception e) {
			log.error(e.getMessage(), e);
		}
		
		return bundle; 
	}
	
	/**
	 * Dependency injection of {@link DefaultBootstrapManager}
	 * @param o
	 */
	public void setBootstrapManager(BootstrapManager o) {
		this.bootstrapManager = o;
	}

}
