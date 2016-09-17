package de.openflorian.ws.api;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import de.openflorian.data.model.Operation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * REST Resource for Alarm Handlinng
 * 
 * @author Bastian kraus <bofh@k-hive.de>
 */
@Api(value = "/alarm", description = "Operations for alarm handling")
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
	@ApiOperation(value = "Incurre alarm")
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
	@ApiOperation(value = "Re-incurre last incurred alarm")
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
	@ApiOperation(value = "Get the current incurred operation", response = Operation.class)
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/current")
	public Operation getCurrentOperation();

	/**
	 * REST Resource to takeOver an {@link Operation} with given <code>operationId</code><br/>
	 * <br/>
	 * HTTP Method: PUT
	 * 
	 * @param operationId
	 * @return
	 */
	@ApiOperation(value = "Takeover current incurred operation")
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
	@ApiOperation(value = "Dispatch current incurred operation")
	@DELETE
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/current")
	public void dispatch();

}
