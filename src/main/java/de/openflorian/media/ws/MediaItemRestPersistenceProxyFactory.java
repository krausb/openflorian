package de.openflorian.media.ws;

import java.util.concurrent.TimeUnit;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.openflorian.media.MediaItemPersistenceManager;
import de.openflorian.media.MediaItemPersistenceProxyFactory;
import de.openflorian.media.model.MediaItem;

/**
 * Factory for creation of {@link MediaItemResource} REST Persistence Proxy<br/>
 * for {@link MediaItem} persistence.
 * 
 * @author Bastian Kraus <me@bastian-kraus.me>
 */
public class MediaItemRestPersistenceProxyFactory implements MediaItemPersistenceProxyFactory {
	
	protected static final int RESTEASY_CONNECTIONPOOL_SIZE = 50;
	protected static final int RESTEASY_CONNECTION_TTL = 5;
	protected static final TimeUnit RESTEASY_CONNECTION_TTL_TIMEUNIT = TimeUnit.SECONDS;
	
	private Logger log = LoggerFactory.getLogger(MediaItemRestPersistenceProxyFactory.class);
	
	private static MediaItemResource resource = null;

	@Override
	public MediaItemResource getInstance() {
		if(resource == null) {
	    	// throw error when PERSISTENCE_REST_PATH is not configured properly
	    	if(MediaItemPersistenceManager.getPersistenceRestPath() == null || MediaItemPersistenceManager.getPersistenceRestPath().length() == 0) {
	    		log.error("MediaManager.CONTEXT_MEDIA_PERSISTENCE_PATH not found in initial context!");
	    		throw new RuntimeException("MediaManager.CONTEXT_MEDIA_PERSISTENCE_PATH not found in initial context!");
	    	}
			
			// Initializing REST Interface Proxy
			ResteasyClient client = new ResteasyClientBuilder()
				.connectionPoolSize(RESTEASY_CONNECTIONPOOL_SIZE)
				.connectionTTL(RESTEASY_CONNECTION_TTL, RESTEASY_CONNECTION_TTL_TIMEUNIT)
				.build();
			ResteasyWebTarget target = null;
			try {
				target = client.target(MediaItemPersistenceManager.getPersistenceRestPath());
			} catch (Exception e) {}
			
			resource = (MediaItemResource) target.proxy(MediaItemResource.class);
		}
		
		return resource;
	}
	
}
