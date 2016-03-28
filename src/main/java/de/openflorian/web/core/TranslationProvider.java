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

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Locale;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * I18n Provider<br/>
 * <br/>
 * Provides internationalized text labels from i3-label.properties /
 * i3-label_[LANG].properties
 * 
 * @author Bastian Kraus <bofh@k-hive.de>
 */
public class TranslationProvider {

	private static final Logger log = LoggerFactory.getLogger(TranslationProvider.class);

	private URLClassLoader i18nClassLoader;

	private static TranslationProvider instance;

	public static TranslationProvider provider() {
		if (instance == null)
			instance = new TranslationProvider();
		return instance;
	}

	/**
	 * Singleton CTOR
	 */
	private TranslationProvider() {
		Locale.setDefault(LocaleProvider.getDefaultLocale());

		try {
			URL[] urls = { new File(BootstrapManager.manager().getRealPathForVirtual("/WEB-INF/")).toURI().toURL() };
			i18nClassLoader = new URLClassLoader(urls);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new IllegalStateException("Error on initialization of TranslationProvder.");
		}
	}

	public String getText(String label) {
		try {
			// http://docs.oracle.com/javase/7/docs/api/java/util/Properties.html#load%28java.io.InputStream%29
			// we use UTF-8 for properties - so we need to convert from
			// ISO-8859-1 to UTF-8 here.
			return new String(getCurrentResourceBundle().getString(label).getBytes("ISO-8859-1"), "UTF-8");
		} catch (Exception e) {
			log.error("Error retrieving i18n value for key '" + label + "': " + e.getMessage(), e);
			return label;
		}
	}

	public String getText(String label, String replacement) {
		try {
			// http://docs.oracle.com/javase/7/docs/api/java/util/Properties.html#load%28java.io.InputStream%29
			// we use UTF-8 for properties - so we need to convert from
			// ISO-8859-1 to UTF-8 here.
			return new String(getCurrentResourceBundle().getString(label).getBytes("ISO-8859-1"), "UTF-8")
					.replace("{0}", replacement);
		} catch (Exception e) {
			log.error("Error retrieving i18n value for key '" + label + "': " + e.getMessage(), e);
			return label;
		}
	}

	/**
	 * Load current resource bundle
	 * 
	 * @return
	 */
	private ResourceBundle getCurrentResourceBundle() {
		Locale rLocale = LocaleProvider.getCurrentLocale();

		ResourceBundle bundle = null;
		try {
			bundle = ResourceBundle.getBundle("i3-label", rLocale, i18nClassLoader);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		return bundle;
	}

}
