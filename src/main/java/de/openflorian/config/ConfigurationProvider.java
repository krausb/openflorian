package de.openflorian.config;

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
 * Bean: Configuration Provider
 * 
 * @author Bastian Kraus <me@bastian-kraus.me>
 */
public interface ConfigurationProvider {
	
	public abstract String getProperty(String name);
	
    /** 
     * Get configuration path set in tomcat deployment descriptor
     * 
     * @return 
     * 		{@link String} TOMCAT_CONF_DIR
     */
	public abstract String getConfigurationPath();

    /** 
     * Get application data work path path set in tomcat deployment descriptor
     * 
     * @return 
     * 		{@link String} APP_DATA_DIR
     */
	public abstract String getApplicationDataWorkPath();

    /** 
     * Get plugin datastore path set in tomcat deployment descriptor
     * 
     * @return 
     * 		{@link String} APP_DATA_DIR + "/plugins"
     */
	public abstract String getPluginDatastore();

	/**
	 * Register given <code>object</code> to be notified on the successful initialization of<br/>
	 * {@link ConfigurationProvider}.
	 * 
	 * @param object
	 */
	public void becomeInitializationAware(ConfigurationInitializationAware object);
	
	/**
	 * Checks if the {@link ConfigurationProvider} instance is initialized.
	 * 
	 * @return
	 */
	public abstract boolean isInitialized();
	
}
