package de.openflorian.config;


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
