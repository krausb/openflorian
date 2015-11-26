package de.openflorian.web.core;

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