package de.openflorian.ws.api;

import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import de.openflorian.data.model.Operation;

/**
 * REST Resource for Alarm Handlinng
 * 
 * @author Bastian kraus <bofh@k-hive.de>
 */
@Path("/alarm")
public interface AlarmResource {

	/**
	 * REST Resource to incurre an {@link Operation} with given <code>operationId</code><br/>
	 * <br/>
	 * HTTP Method: POST
	 * 
	 * @param operationId
	 * @return
	 */
	@POST
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/")
	public void incurre(@QueryParam("operationId") long operationId);

	/**
	 * REST Resource to incurre the last incurred operation
	 * 
	 * HTTP Method: POST
	 * 
	 * @return
	 */
	@POST
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/last")
	public void incurreLast();

	/**
	 * REST Resource to load all running content crawler available<br/>
	 * <br/>
	 * HTTP Method: GET
	 * 
	 * @return {@link Operation}
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/current")
	public Operation getCurrentOperation();

	/**
	 * REST Resource to load a {@link List}<{@link Operation}>
	 * 
	 * @return {@link List}<{@link Operation}>
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/all")
	public List<Operation> getAllOperations();

	/**
	 * REST Resource to takeOver an {@link Operation} with given <code>operationId</code><br/>
	 * <br/>
	 * HTTP Method: PUT
	 * 
	 * @param operationId
	 * @return
	 */
	@PUT
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/current")
	public void takeOver();

	/**
	 * REST Resource to takeOver an {@link Operation} with given <code>operationId</code><br/>
	 * <br/>
	 * HTTP Method: DELETE <br/>
	 * HTTP Response:<br/>
	 * 200 - Successfuly dispatched 404 - Currently no operation
	 * 
	 * @param operationId
	 * @return
	 */
	@DELETE
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/current")
	public void dispatch();

}
