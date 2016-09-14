package de.openflorian.ws.api;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import de.openflorian.data.model.Operation;

/**
 * REST Resource for Operation Handlinng
 * 
 * @author Bastian kraus <bofh@k-hive.de>
 */
@Path("/operation")
public interface OperationResource {

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
	 * REST Resource to load a {@link List}<{@link Operation}>
	 * 
	 * @return {@link List}<{@link Operation}>
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/last")
	public List<Operation> getLastOperations(@QueryParam("limit") int limit);

}
