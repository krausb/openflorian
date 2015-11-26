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

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ServletContextAware;

/**
 * Bean: Bootstrap Manager
 * 
 * Provides basic configuration, e.g. Context Path, Configuration Path, ...
 * 
 * @author Bastian Kraus <me@bastian-kraus.me>
 */
public class DefaultBootstrapManager implements ServletContextAware, BootstrapManager {
	
	private static Logger log = LoggerFactory.getLogger(DefaultBootstrapManager.class);
	
	private static String TOMCAT_CONF_DIR = "src/props/";
	private static String APP_DATA_DIR = "data/";
	
	private ServletContext context = null;
	
    static {
    	Logger log = LoggerFactory.getLogger(DefaultBootstrapManager.class);
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
    			TOMCAT_CONF_DIR = "src/props";
    			APP_DATA_DIR = "data";
    		}
		}
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
    
    @Override
	public String getContextPath() {
    	return context.getContextPath();
    }
    
    @Override
	public String getRealPath() {
    	return getRealPathForVirtual("/");
    }
    
    @Override
	public String getRealPathForVirtual(String virtualPath) {
    	return context.getRealPath(virtualPath);
    }
    
    /**
     * Dependency injection of {@link ServletContext}
     * @param ctx {@link ServletContext}
     */
    @Override
	public void setServletContext(ServletContext ctx) {
		log.info("Bootstrap Manager initializing...");
		this.context = ctx;
		
		log.info("Tomcat config dir: " + getConfigurationPath());
		log.info("Webapp context path: " + getContextPath());
		log.info("Real path for '/': " + getRealPath());
		
		log.info("... intialization finished");
	}
	
}
