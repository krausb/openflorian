package de.openflorian.media.ws;

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
