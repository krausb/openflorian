package de.openflorian.web.user;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 * REST Resource for {@link User}
 * 
 * @author Bastian Kraus <me@bastian-kraus.me>
 */
@Path("/user")
public interface UserResource {
	
	/**
	 * Find a specific {@link User} entity by name
	 * 
	 * @param name
	 * @return {@link User}
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/get/byname")
	public User getByName(@QueryParam("name") String name);
	
	/**
	 * REST Resource to load all {@link User} available<br/>
	 * <br/>
	 * HTTP Method: GET
	 * 
	 * @return {@link User}[]
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/all")
	public User[] list();

	/**
	 * Get a paged list of {@link User}
	 * 
	 * @param activePage
	 * @return {@link List}<{@link User}>
	 */
	@GET
    @Produces(MediaType.APPLICATION_JSON)
	@Path("/all/paged")
	public User[] listByPage(@QueryParam("order") String order, 
			@QueryParam("activePage") int activePage, @QueryParam("pageSize") int pageSize);
	
	/**
	 * Count all available entities of the specific {@link T}
	 * 
	 * @return
	 */
	@GET
	@Path("/all/count")
	@Produces(MediaType.TEXT_PLAIN)
	public long count();
	
	/**
	 * Find a specific {@link User} entity by pk id
	 * 
	 * @param id
	 * @return {@link User}
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/get/byid")
	public User getById(@QueryParam("id") Long id);
	
	/**
	 * Get a list of all {@link User} entities filtered by given {@code column} and given {@code filter}
	 * 
	 * @param column
	 * @param filter
	 * @return {@link List}<{@link User}>
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/get/byfilter")
	public User[] search(@QueryParam("column") String column, @QueryParam("filter") String filter);
	
	/**
	 * REST Resource to create or persist a single {@link User}
	 * @param benefit
	 * @return {@link T}
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/persist")
	public User persist(User o);

	/**
	 * REST Resource for deleting a specific {@link User}
	 */
	@DELETE
	@Path("/remove")
	public void remove(@QueryParam("id") long id);
	
}
