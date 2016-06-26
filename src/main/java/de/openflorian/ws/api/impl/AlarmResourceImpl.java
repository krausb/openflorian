package de.openflorian.ws.api.impl;

import javax.persistence.EntityNotFoundException;
import javax.ws.rs.WebApplicationException;

import de.openflorian.EventBusAddresses;
import de.openflorian.OpenflorianContext;
import de.openflorian.alarm.AlarmContextVerticle;
import de.openflorian.data.model.Operation;
import de.openflorian.service.OperationService;
import de.openflorian.ws.api.AlarmResource;

/**
 * Impl of {@link AlarmResource}
 * 
 * @author Bastian Kraus <bofh@k-hive.de>
 */
public class AlarmResourceImpl implements AlarmResource {

	private final OperationService service = OperationService.transactional();

	@Override
	public Operation getCurrentOperation() {
		return AlarmContextVerticle.getInstance().getCurrentOperation();
	}

	@Override
	public void incurre(long operationId) {
		try {
			Operation o = service.getById(operationId);
			OpenflorianContext.vertx().eventBus().publish(EventBusAddresses.ALARM_INCURRED, o);
		} catch (EntityNotFoundException e) {
			throw new WebApplicationException(
					new IllegalArgumentException("No operation with ID " + operationId + " found."), 404);
		} catch (Exception e) {
			throw new WebApplicationException(e, 500);
		}
	}

	@Override
	public void takeOver() {
		try {
			Operation o = AlarmContextVerticle.getInstance().getCurrentOperation();
			if (o != null)
				OpenflorianContext.vertx().eventBus().publish(EventBusAddresses.ALARM_TAKENOVER, o);
			else
				throw new EntityNotFoundException();
		} catch (EntityNotFoundException e) {
			throw new WebApplicationException(new IllegalArgumentException("No operation in progress."), 404);
		} catch (Exception e) {
			throw new WebApplicationException(e, 500);
		}
	}

	@Override
	public void dispatch() {
		try {
			Operation o = AlarmContextVerticle.getInstance().getCurrentOperation();
			if (o != null)
				OpenflorianContext.vertx().eventBus().publish(EventBusAddresses.ALARM_DISPATCHED, o);
			else
				throw new EntityNotFoundException();
		} catch (EntityNotFoundException e) {
			throw new WebApplicationException(new IllegalArgumentException("No operation in progress."), 404);
		} catch (Exception e) {
			throw new WebApplicationException(e, 500);
		}
	}

}
