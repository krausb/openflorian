package de.openflorian.media.ws;

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

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import de.openflorian.media.model.MediaItem;

/**
 * REST Resource for {@link MediaItem} persistence<br/>
 * 
 * @author Bastian Kraus <me@bastian-kraus.me>
 */
@Path("/media")
public interface MediaItemResource {
	
	/**
	 * Find a specific {@link MediaItem} entity by pk id
	 * 
	 * @param id
	 * @return {@link MediaItem}
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/get/byid")
	public MediaItem getById(@QueryParam("id") long id);
	
	/**
	 * Find a specific {@link MediaItem} entity by unique identifier (uuid)
	 * 
	 * @param id
	 * @return {@link MediaItem}
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/get/byidentifier")
	public MediaItem getByIdentifier(@QueryParam("identifier") String identifier);	
	
	/**
	 * REST Resource to create or persist a single {@link MediaItem}
	 * @param Action
	 * @return {@link MediaItem}
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/persist")
	public MediaItem persist(MediaItem o);

	/**
	 * REST Resource for deleting a specific {@link MediaItem}
	 */
	@DELETE
	@Path("/remove")
	public void remove(@QueryParam("id") long id);
	
}
