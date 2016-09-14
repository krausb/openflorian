package de.openflorian.ws.api.impl;

import java.util.List;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.WebApplicationException;

import de.openflorian.data.dao.OperationDao;
import de.openflorian.data.model.Operation;
import de.openflorian.ws.api.OperationResource;

/**
 * Impl of {@link OperationResource}
 * 
 * @author Bastian Kraus <bofh@k-hive.de>
 */
public class OperationResourceImpl implements OperationResource {

	@Override
	public List<Operation> getAllOperations() {
		try {
			return new OperationDao().list();
		}
		catch (final NotFoundException e) {
			throw new WebApplicationException(new IllegalArgumentException("No operation found."), 404);
		}
		catch (final Exception e) {
			throw new WebApplicationException(e, 500);
		}
	}

	@Override
	public List<Operation> getLastOperations(int limit) {
		try {
			return new OperationDao().listLast(limit);
		}
		catch (final NotFoundException e) {
			throw new WebApplicationException(new IllegalArgumentException("No operation found."), 404);
		}
		catch (final Exception e) {
			throw new WebApplicationException(e, 500);
		}
	}

}
