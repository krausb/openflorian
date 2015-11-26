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
 * Bootstrap Manager for handling everything related to Apache Tomcat and Environment.
 * 
 * @author Bastian Kraus <me@bastian-kraus.me>
 */
public interface BootstrapManager {

	/**
	 * Configuration path in $CATALINA_HOME/conf/CONFIG
	 * 
	 * @return {@link String}
	 */
	public abstract String getConfigurationPath();

	/**
	 * Application data work directory $CATALINA_HOME/data
	 * 
	 * @return {@link String}
	 */
	public abstract String getApplicationDataWorkPath();

	/**
	 * Plugin data store path
	 * @return {@link String}
	 */
	public abstract String getPluginDatastore();
	
	/**
	 * Web Context Path - http://serveraddress:8080/CONTEXTPATH/
	 * 
	 * @return String Web Context Path
	 */
	public abstract String getContextPath();

	/**
	 * Real Path for "/"
	 * 
	 * @return {@link String}
	 */
	public abstract String getRealPath();

	/**
	 * Real Path for given String
	 * 
	 * @param virtualPath {@link String} virtual path
	 * @return
	 */
	public abstract String getRealPathForVirtual(String virtualPath);

}