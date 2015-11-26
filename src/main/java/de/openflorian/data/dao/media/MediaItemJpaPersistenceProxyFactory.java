package de.openflorian.data.dao.media;

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
