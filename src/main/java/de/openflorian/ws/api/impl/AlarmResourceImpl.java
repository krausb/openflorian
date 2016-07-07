package de.openflorian.ws.api.impl;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.WebApplicationException;

import de.openflorian.EventBusAddresses;
import de.openflorian.OpenflorianContext;
import de.openflorian.alarm.AlarmContextVerticle;
import de.openflorian.data.dao.OperationDao;
import de.openflorian.data.model.Operation;
import de.openflorian.ws.api.AlarmResource;

/**
 * Impl of {@link AlarmResource}
 * 
 * @author Bastian Kraus <bofh@k-hive.de>
 */
public class AlarmResourceImpl implements AlarmResource {

	@Override
	public Operation getCurrentOperation() {
		return AlarmContextVerticle.getInstance().getCurrentOperation();
	}

	@Override
	public void incurre(long operationId) {
		try {
			final Operation o = new OperationDao().getById(operationId);
			if (o != null)
				OpenflorianContext.vertx().eventBus().publish(EventBusAddresses.ALARM_INCURRED, o);
			else
				throw new NotFoundException();
		}
		catch (final NotFoundException e) {
			throw new WebApplicationException(
					new IllegalArgumentException("No operation with ID " + operationId + " found."), 404);
		}
		catch (final Exception e) {
			throw new WebApplicationException(e, 500);
		}
	}

	@Override
	public void takeOver() {
		try {
			final Operation o = AlarmContextVerticle.getInstance().getCurrentOperation();
			if (o != null)
				OpenflorianContext.vertx().eventBus().publish(EventBusAddresses.ALARM_TAKENOVER, o);
			else
				throw new NotFoundException();
		}
		catch (final NotFoundException e) {
			throw new WebApplicationException(new IllegalArgumentException("No operation in progress."), 404);
		}
		catch (final Exception e) {
			throw new WebApplicationException(e, 500);
		}
	}

	@Override
	public void dispatch() {
		try {
			final Operation o = AlarmContextVerticle.getInstance().getCurrentOperation();
			if (o != null)
				OpenflorianContext.vertx().eventBus().publish(EventBusAddresses.ALARM_DISPATCHED, o);
			else
				throw new NotFoundException();
		}
		catch (final NotFoundException e) {
			throw new WebApplicationException(new IllegalArgumentException("No operation in progress."), 404);
		}
		catch (final Exception e) {
			throw new WebApplicationException(e, 500);
		}
	}

}
