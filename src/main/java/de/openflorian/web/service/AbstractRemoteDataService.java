package de.openflorian.web.service;

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

import java.util.concurrent.TimeUnit;

import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.jboss.resteasy.client.core.executors.ApacheHttpClient4Executor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import de.openflorian.config.ConfigurationProvider;
import de.openflorian.web.core.BootstrapManager;

/**
 * Abstract Remote Data Service<Br/>
 * <br/>
 * Provides Access to the benefit-data-provisioning Webservices
 * 
 * @author Bastian Kraus <me@bastian-kraus.me>
 */
public abstract class AbstractRemoteDataService implements InitializingBean {
	protected Logger log = LoggerFactory.getLogger(getClass());
	
	protected final int RESTEASY_CONNECTIONPOOL_SIZE = 50;
	protected final int RESTEASY_CONNECTION_TTL = 5;
	protected final TimeUnit RESTEASY_CONNECTION_TTL_TIMEUNIT = TimeUnit.SECONDS;
	
	/**
	 * System Configuration Provider
	 */
	protected ConfigurationProvider configuration = null;
	protected BootstrapManager bootstrapManager = null;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		log.debug("Post initialization...");
	}

	/**
	 * Create a pooled multithread ready httpclient executor
	 * @return
	 */
	protected ApacheHttpClient4Executor getExecutor() {
		PoolingClientConnectionManager connectionManager = new PoolingClientConnectionManager();
		return new ApacheHttpClient4Executor(new DefaultHttpClient(connectionManager));
	}
	
	/**
	 * Dependency injection: {@link ConfigurationProvider}
	 * 
	 * @param config
	 */
	public void setConfigurationProvider(ConfigurationProvider config) {
		log.debug("Injecting ConfigurationProvider...");
		this.configuration = config;
	}
	
	public void setBootstrapManager(BootstrapManager bootstrapManager) {
		log.debug("Injecting BootstrapManager...");
		this.bootstrapManager = bootstrapManager;
	}

}
