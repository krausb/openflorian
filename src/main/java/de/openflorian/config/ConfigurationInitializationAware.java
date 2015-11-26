package de.openflorian.config;

/**
 * Configuration Initialization Aware<br/>
 * <br/>
 * A type inheriting {@link ConfigurationInitializationAware} can register itself to {@link ConfigurationProvider}
 * 
 * @author Bastian Kraus <me@bastian-kraus.me>
 *
 */
public interface ConfigurationInitializationAware {

	/**
	 * Method triggered on successful initialization of {@link ConfigurationProvider}.<br/>
	 */
	public void afterConfigurationInitialized();
	
}
