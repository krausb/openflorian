package de.openflorian.media;

import de.openflorian.media.ws.MediaItemResource;

/**
 * Media Item Persistence Proxy Factory<br/>
 * <br/>
 * Interface for creating media 
 * 
 * @author Bastian Kraus <me@bastian-kraus.me>
 */
public interface MediaItemPersistenceProxyFactory {

	/**
	 * Create a {@link ResteasyWebTarget} based {@link MediaItemResource} client proxy instance.
	 * The premise: {@link PERSISTENCE_REST_PATH} has to be configured in the initial context of the<br/>
	 * current web application.
	 * 
	 * @return
	 * 		{@link MediaItemResource} ready to use client proxy
	 */
	public abstract MediaItemResource getInstance();

}