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

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.StringUtils;

/**
 * Configuration Provider - Default Implementation
 * 
 * Gains access to a in the deployment descriptor as 'java:/comp/env/config.dir' defined directory
 * 
 * @author Bastian Kraus <me@bastian-kraus.me>
 */
public class DefaultConfigurationProvider implements InitializingBean, ConfigurationProvider {
	private Logger log = LoggerFactory.getLogger(getClass());
	
	private static List<ConfigurationInitializationAware> initializationAwares = new LinkedList<ConfigurationInitializationAware>();
	private static boolean initialized = false;
	
	public static final String MAIN_CONFIG_FILE = "main.properties";
	private static String TOMCAT_CONF_DIR;
	private static String APP_DATA_DIR;

    static {
    	Logger log = LoggerFactory.getLogger(DefaultConfigurationProvider.class);
    	InitialContext ctx = null;
    	try {
    		ctx = new InitialContext();
    	} catch (NamingException e) {
    		log.error(e.getMessage(), e);
    	}
    	
    	synchronized (ctx) {
    		try {
    			TOMCAT_CONF_DIR = (String) ctx.lookup( "java:/comp/env/config.dir" );
    			APP_DATA_DIR = (String) ctx.lookup( "java:/comp/env/data.dir" );
    		} catch (NamingException e) {
    			// Config for Tests
    			if(!StringUtils.isEmpty(System.getenv("config.dir")))
    				TOMCAT_CONF_DIR = System.getenv("config.dir");
    			else
    				TOMCAT_CONF_DIR = "src/test/resources/config";
    			if(!StringUtils.isEmpty(System.getenv("config.dir")))
    				APP_DATA_DIR = System.getenv("data.dir");
    			else
    				APP_DATA_DIR = "src/test/resources/testfiles";
    		}
		}
    }
	
	private Properties properties = null;
	private String configFileName = null;
	
	@Override
	public String getProperty(String name) {
		if(!properties.containsKey(name))
			log.warn("Property with name '" + name + "' not available. Maybe missing in configuration file.");
		
		return properties.getProperty(name);
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		log.debug("Initializing ConfigurationProvider...");
		properties = new Properties();
		InputStream input = null;
		String config = TOMCAT_CONF_DIR + System.getProperty("file.separator") + (!StringUtils.isEmpty(configFileName) ? configFileName : MAIN_CONFIG_FILE);
		log.debug("Using config file: " + config);
		try {
			input = new FileInputStream( config );
			properties.load(input);
			initialized = true;
			notifyInitializationAwares();
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * Helper: Notify registered {@link ConfigurationInitializationAware}s for successful<br/>
	 * configuration initialization.
	 */
	private void notifyInitializationAwares() {
		initializationAwares.stream().forEach(aware -> aware.afterConfigurationInitialized());
	}
	
	@Override
	public String getConfigurationPath() {
    	return TOMCAT_CONF_DIR;
    }
    
    @Override
	public String getApplicationDataWorkPath() {
    	return APP_DATA_DIR;
    }
    
    @Override
	public String getPluginDatastore() {
    	return APP_DATA_DIR + "/plugins";
    }
    
    /**
     * Setter: Configuration file name
     * 
     * @param config
     */
    public void setConfigurationFileName(String config) {
    	this.configFileName = config;
    }

	/**
	 * Register given <code>object</code> to be notified on the successful initialization of<br/>
	 * {@link ConfigurationProvider}.
	 * 
	 * @param object
	 */
    @Override
	public void becomeInitializationAware(ConfigurationInitializationAware object) {
		if(!initializationAwares.contains(object))
			initializationAwares.add(object);
		if(initialized)
			object.afterConfigurationInitialized();
	}
    
	@Override
	public boolean isInitialized() {
		// TODO Auto-generated method stub
		return false;
	}
	
}
