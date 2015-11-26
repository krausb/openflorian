package de.openflorian.web.service;

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
