package de.openflorian.data.dao.media;

import de.openflorian.media.MediaItemPersistenceManager;
import de.openflorian.media.MediaItemPersistenceProxyFactory;
import de.openflorian.media.model.MediaItem;
import de.openflorian.media.ws.MediaItemResource;

/**
 * JPA Persistence Proxy Factory for {@link MediaItem}
 * 
 * @author Bastian Kraus <me@bastian-kraus.me>
 */
public class MediaItemJpaPersistenceProxyFactory implements MediaItemPersistenceProxyFactory {

	private MediaItemResource mediaItemService;
	
	/**
	 * Create a JPA Persistence Proxy for {@link MediaItemPersistenceManager}
	 */
	public MediaItemResource getInstance() {
		if(mediaItemService == null)
			throw new IllegalStateException("No MediaItemService present.");
		return mediaItemService;
	}

	/**
	 * Dependency Injection: {@link MediaItemService}
	 * @param service
	 */
	public void setMediaItemService(MediaItemResource service) {
		this.mediaItemService = service;
	}

}
