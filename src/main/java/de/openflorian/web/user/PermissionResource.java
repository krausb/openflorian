package de.openflorian.web.user;

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
 * @author Bastian Kraus <me@bastian-kraus.me>
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
