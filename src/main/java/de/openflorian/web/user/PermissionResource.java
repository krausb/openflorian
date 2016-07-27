package de.openflorian.web.user;

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

/**
 * REST Resource for {@link Permission}
 * 
 * @author Bastian Kraus <bofh@k-hive.de>
 */
@Path("/user/permission")
public interface PermissionResource {
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/byUserAndPermission")
	public Permission getByUserAndPermission(@QueryParam("userid") long userid, @QueryParam("permission") String permission);
	
	/**
	 * REST Resource to load all {@link Permission} available<br/>
	 * 
	 * @param userid
	 * 		{@link long}
	 * @return
	 * 		{@link Permission}[]
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/all/byUser")
	public Permission[] list(@QueryParam("userid") long userid);
	
	/**
	 * REST Resource to create or persist a single {@link Permission}
	 * 
	 * @param o
	 * 		{@link Permission}
	 * @return
	 * 		{@link Permission}
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/persist")
	public Permission persist(Permission o);

	/**
	 * REST Resource for deleting a specific {@link Permission}
	 * 
	 * @param userid
	 * 		{@link long}
	 * @param permission
	 * 		{@link Permission}
	 */
	@DELETE
	@Path("/remove")
	public void remove(@QueryParam("userid") long userid, @QueryParam("permission") String permission);
	
	/**
	 * REST Resource for deleting a specific {@link Permission}
	 * 
	 * @param id
	 * 		{@link long}
	 */
	@DELETE
	@Path("/remove/byId")
	public void remove(@QueryParam("id") long id);
	
}
