package de.openflorian.media;

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