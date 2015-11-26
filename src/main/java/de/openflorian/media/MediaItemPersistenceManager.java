package de.openflorian.media;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import de.openflorian.media.model.MediaItem;
import de.openflorian.media.ws.MediaItemResource;

/**
 * Persisting {@link MediaManager} extending {@link DefaultMediaManager}<br/>
 * <br/>
 * Enriches {@link DefaultMediaManager} with Data Persistence of {@link MediaItem}s
 *  
 * @author Bastian Kraus <bk@pogo-systems.de>
 */
public class MediaItemPersistenceManager implements InitializingBean {

	private static String PERSISTENCE_REST_PATH = "";
	public static String getPersistenceRestPath() {
		return PERSISTENCE_REST_PATH;
	}
	
	private MediaItemPersistenceProxyFactory persistenceProxyFactory;	
	private MediaItemResource persistenceResource;
	
	static {
    	Logger log = LoggerFactory.getLogger(DefaultMediaManager.class);
    	InitialContext ctx = null;
    	try {
    		ctx = new InitialContext();
    	} catch (NamingException e) {
    		log.error(e.getMessage(), e);
    	}

    	synchronized (ctx) {
    		try {
    			PERSISTENCE_REST_PATH = (String) ctx.lookup( MediaManager.CONTEXT_MEDIA_PERSISTENCE_PATH );
    		} catch (NamingException e) {
    			// Config for Tests
    			PERSISTENCE_REST_PATH = "http://yourserver.com/rest/persistence";
    		}
		}
    	
    	// throw error when PERSISTENCE_REST_PATH is not configured properly
    	if(PERSISTENCE_REST_PATH == null || PERSISTENCE_REST_PATH.length() == 0) {
    		log.error("MediaManager.CONTEXT_MEDIA_PERSISTENCE_PATH not configured properly!");
    		throw new RuntimeException("MediaManager.CONTEXT_MEDIA_PERSISTENCE_PATH not configured properly!");
    	} else {
    		log.info("Configured persistence path: " + PERSISTENCE_REST_PATH);
    	}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if(persistenceProxyFactory != null)
			persistenceResource = persistenceProxyFactory.getInstance();
	}
	
	/**
	 * Persist given <code>item</code> from persistence layer
	 * @param item
	 * @return
	 */
	public MediaItem store(MediaItem item) {
		if(persistenceResource != null)
			return persistenceResource.persist(item);
		else
			return null;
	}

	/**
	 * Remove given <code>item</code> from given persistence layer
	 * @param item
	 */
	public void remove(MediaItem item) {
		if(persistenceResource != null)
			persistenceResource.remove(item.getMediaItemId());
	}	
	
	/**
	 * Get a {@link MediaItem} by <code>identifier</code>
	 * 
	 * @param identifier
	 * @return
	 * 		{@link MediaItem}
	 */
	public MediaItem get(String identifier) {
		if(persistenceResource != null)
			return persistenceResource.getByIdentifier(identifier);
		else
			return null;
	}
	
	/**
	 * Get a {@link MediaItem} by <code>id</code>
	 * 
	 * @param id
	 * @return
	 * 		{@link MediaItem}
	 */
	public MediaItem get(long id) {
		if(persistenceResource != null)
			return persistenceResource.getById(id);
		else
			return null;
	}

	/**
	 * Dependency injection: {@link MediaItemPersistenceManager}
	 * @param persistenceManager
	 */
	public void setMediaItemPersistenceProxyFactory(MediaItemPersistenceProxyFactory proxyFactory) {
		this.persistenceProxyFactory = proxyFactory;
	}
	
}
