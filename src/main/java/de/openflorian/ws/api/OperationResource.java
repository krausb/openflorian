package de.openflorian.ws.api;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import de.openflorian.data.model.Operation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * REST Resource for Operation Handlinng
 * 
 * @author Bastian kraus <bofh@k-hive.de>
 */
@Api(value = "/operation", description = "API functions for managing operations")
@Path("/operation")
public interface OperationResource {

	/**
	 * REST Resource to load a {@link List}<{@link Operation}>
	 * 
	 * @return {@link List}<{@link Operation}>
	 */
	@ApiOperation(value = "List all operations", response = Operation.class, responseContainer = "List")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/all")
	public List<Operation> getAllOperations();

	/**
	 * REST Resource to load a {@link List}<{@link Operation}>
	 * 
	 * @return {@link List}<{@link Operation}>
	 */
	@ApiOperation(value = "List all operations sorted by id descending with given limit", response = Operation.class, responseContainer = "List")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/last")
	public List<Operation> getLastOperations(@QueryParam("limit") int limit);

}
