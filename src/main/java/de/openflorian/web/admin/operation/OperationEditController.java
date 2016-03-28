package de.openflorian.web.admin.operation;

import java.security.Principal;
import java.util.Date;

import com.vaadin.ui.*;

import de.openflorian.web.AbstractBrowser;
import de.openflorian.web.WebGlobals;
import de.openflorian.web.user.User;

/**
 * Controller for Editing / Adding a {@link User}
 * 
 * ZUL: {@link WebGlobals#PAGE_SYSTEM_USERS_EDIT}
 * 
 * @author Bastian Kraus <bofh@k-hive.de>
 */
public class OperationEditController extends AbstractBrowser {

	private static final long serialVersionUID = -9103899038654112897L;

	private Window operationEditWindow;

	private VerticalLayout resourcesTabPanel;
	private VerticalLayout dispatchTabPanel;
	private VerticalLayout mapTabPanel;

	private Button saveButton;
	private Button cancelButton;
	private Button applyButton;
	private Button deleteButton;

	private Button alarmButton;
	private Button dispatchButton;
	private Button takeOverButton;

	private long operationId;

	private TextField operationNr;
	private TextField positionLongitude;
	private TextField positionLatitude;
	private TextField object;
	private TextField street;
	private TextField city;
	private TextField crossway;
	private TextField priority;
	private TextField keyword;
	private TextField buzzword;
	private TextField resourcesRaw;
	private DateField incurredAt;
	private Label takenOverAtLabel;
	private Date takenOverAt;
	private Label dispatchedAtLabel;
	private Date dispatchedAt;

	private TabSheet operationsTabbox;

	// private Gmaps operationMap;
	// private Ginfo operationMarker;

	private Button arrange;

	public OperationEditController(UI view) {
		super(view);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean mayView(Principal user) {
		return true;
		// PermissionService ps = (PermissionService)
		// ContainerManager.getComponent("permissionService");
		// return user != null && ps.hasPermission((User) user,
		// GlobalPermission.ADMINISTER);
	}

	@Override
	public void init() {

		// operationMap.setVisible(true);
		//
		// Operation selectedOperation = (Operation)
		// execution.getAttribute(WebGlobals.REQUEST_ENTITY);
		//
		// if (selectedOperation != null) {
		// log.debug("Recieved operation to select: " +
		// selectedOperation.toString());
		// fillZul(selectedOperation);
		// deleteButton.setDisabled(false);
		// } else {
		// deleteButton.setDisabled(true);
		// }
	}

	// /**
	// * Event-Handler: arrangeButton.onClick
	// *
	// * @param event
	// */
	// public void onClick$arrangeButton(Event event) {
	// Double longVal = positionLongitude.getValue();
	// Double latVal = positionLatitude.getValue();
	// initMap(longVal, latVal);
	// }
	//
	// /**
	// * Event-Handler: cancelButton.onClick @param event @throws
	// */
	// public void onClick$applyButton(Event event) {
	// try {
	// Operation o = getFromZul();
	//
	// OperationService operationService = (OperationService)
	// ContainerManager.getComponent("operationService");
	// o = operationService.persist(o);
	// if (o.getId() > 0) {
	// this.operationId = o.getId();
	// execution.setAttribute(WebGlobals.REQUEST_ENTITY, o);
	// fillZul(o);
	// }
	//
	// Clients.showNotification(Labels.getLabel("admin.user.edit.msg.usersaved"),
	// "info", applyButton,
	// "top_center", 2000);
	// } catch (WrongValueException e) {
	// log.debug(e.getMessage(), e);
	// setError(new ZkException(
	// Labels.getLabel("operation.model." + e.getComponent().getId()) + ": " +
	// e.getMessage(), e));
	// } catch (ZkException e) {
	// log.error(e.getMessage(), e);
	// setError(e);
	// } catch (GeneralSecurityException e) {
	// log.error(e.getMessage(), e);
	// setError(new ZkException(e.getMessage(), e));
	// } catch (ValidationException e) {
	// log.error(e.getMessage(), e);
	// setError(new ZkException(e.getMessage(), e));
	// }
	// }
	//
	// /**
	// * Event-Handler: saveButton.onClick <br/>
	// * Redirects to Operation List ZUL
	// *
	// * @param event
	// */
	// public void onClick$saveButton(Event event) {
	// try {
	// Operation o = getFromZul();
	//
	// OperationService operationService = (OperationService)
	// ContainerManager.getComponent("operationService");
	// o = operationService.persist(o);
	//
	// // redirect to user list
	// setContentZul(WebGlobals.PAGE_OPERATION_LIST);
	// } catch (WrongValueException e) {
	// log.debug(e.getMessage(), e);
	// setError(new ZkException(e.getMessage(), e));
	// } catch (ZkException e) {
	// log.error(e.getMessage(), e);
	// setError(e);
	// } catch (GeneralSecurityException e) {
	// log.error(e.getMessage(), e);
	// setError(new ZkException(e.getMessage(), e));
	// } catch (ValidationException e) {
	// log.error(e.getMessage(), e);
	// setError(new ZkException(e.getMessage(), e));
	// }
	// }
	//
	// /**
	// * Event-Handler: deleteButton.onClick
	// *
	// * @param event
	// */
	// public void onClick$deleteButton(Event event) {
	// if (operationId > 0) {
	// Messagebox.show(Labels.getLabel("operation.edit.msg.delete", new String[]
	// { operationNr.getText() }),
	// Labels.getLabel("global.question"),
	// new Messagebox.Button[] { Messagebox.Button.YES, Messagebox.Button.NO },
	// Messagebox.QUESTION,
	// new EventListener<ClickEvent>() {
	//
	// public void onEvent(ClickEvent event) throws Exception {
	// if (event.getButton() == Messagebox.Button.YES)
	// OperationEditController.this.deleteCurrentOperation();
	// }
	//
	// });
	// } else {
	// Clients.showNotification(Labels.getLabel("operation.edit.msg.noselected"),
	// "info", applyButton,
	// "top_center", 2000);
	// }
	// }
	//
	// /**
	// * Event-Handler: cancelButton.onClick
	// *
	// * @param event
	// */
	// public void onClick$cancelButton(Event event) {
	// setContentZul(WebGlobals.PAGE_OPERATION_LIST);
	// }
	//
	// /**
	// * Event-Handler: alarmButton.onClick
	// *
	// * @param event
	// */
	// public void onClick$alarmButton(Event event) {
	// if (operationId > 0) {
	// try {
	// OpenflorianContext.vertx().eventBus().send(EventBusAdresses.ALARM_INCURRED,
	// getFromZul());
	// } catch (ZkException e) {
	// log.error(e.getMessage(), e);
	// setError(e);
	// } catch (GeneralSecurityException e) {
	// log.error(e.getMessage(), e);
	// setError(new ZkException(e.getMessage(), e));
	// } catch (Exception e) {
	// log.error(e.getMessage(), e);
	// setError(new ZkException(e.getMessage(), e));
	// }
	// }
	// }
	//
	// /**
	// * Event-Handler: dispatchButton.onClick
	// *
	// * @param event
	// */
	// public void onClick$dispatchButton(Event event) {
	// if (operationId > 0) {
	// if (AlarmContextVerticle.getInstance().getCurrentOperation() != null
	// && operationId ==
	// AlarmContextVerticle.getInstance().getCurrentOperation().getId()) {
	//
	// try {
	// OpenflorianContext.vertx().eventBus().send(EventBusAdresses.ALARM_DISPATCHED,
	// getFromZul());
	// } catch (ZkException e) {
	// log.error(e.getMessage(), e);
	// setError(e);
	// } catch (GeneralSecurityException e) {
	// log.error(e.getMessage(), e);
	// setError(new ZkException(e.getMessage(), e));
	// } catch (Exception e) {
	// log.error(e.getMessage(), e);
	// setError(new ZkException(e.getMessage(), e));
	// }
	// } else {
	// Clients.showNotification(Labels.getLabel("operation.edit.msg.wrongoperation"),
	// "info", dispatchButton,
	// "top_center", 2000);
	// }
	// } else {
	// Clients.showNotification(Labels.getLabel("operation.edit.msg.noselected"),
	// "info", dispatchButton,
	// "top_center", 2000);
	// }
	// }
	//
	// /**
	// * Helper: Fill .zul with data from given <code>o</code>.
	// *
	// * @param o
	// * {@link Operation}
	// */
	// private void fillZul(Operation o) {
	// operationId = o.getId();
	//
	// // fill core data
	// try {
	// operationNr.setText(o.getOperationNr());
	// } catch (WrongValueException e) {
	// }
	//
	// try {
	// positionLongitude.setValue(o.getPositionLongitude());
	// } catch (WrongValueException e) {
	// }
	//
	// try {
	// positionLatitude.setValue(o.getPositionLatitude());
	// } catch (WrongValueException e) {
	// }
	//
	// try {
	// object.setText(o.getObject());
	// } catch (WrongValueException e) {
	// }
	//
	// try {
	// street.setText(o.getStreet());
	// } catch (WrongValueException e) {
	// }
	//
	// try {
	// city.setText(o.getCity());
	// } catch (WrongValueException e) {
	// }
	//
	// try {
	// crossway.setText(o.getCrossway());
	// } catch (WrongValueException e) {
	// }
	//
	// try {
	// priority.setText(o.getPriority());
	// } catch (WrongValueException e) {
	// }
	//
	// try {
	// keyword.setText(o.getKeyword());
	// } catch (WrongValueException e) {
	// }
	//
	// try {
	// buzzword.setText(o.getBuzzword());
	// } catch (WrongValueException e) {
	// }
	//
	// try {
	// resourcesRaw.setText(o.getResourcesRaw());
	// } catch (WrongValueException e) {
	// }
	//
	// incurredAt.setValue(o.getIncurredAt());
	// incurredAt.setFormat(WebGlobals.FORMAT_DATETIME);
	//
	// SimpleDateFormat format = new
	// SimpleDateFormat(WebGlobals.FORMAT_DATETIME);
	//
	// if (o.getTakenOverAt() != null)
	// takenOverAtLabel.setValue(format.format(o.getTakenOverAt()));
	// takenOverAt = o.getTakenOverAt();
	//
	// if (o.getDispatchedAt() != null)
	// dispatchedAtLabel.setValue(format.format(o.getDispatchedAt()));
	// dispatchedAt = o.getDispatchedAt();
	//
	// if (o.getPositionLatitude() != 0 && o.getPositionLongitude() != 0) {
	// initMap(o.getPositionLongitude(), o.getPositionLatitude());
	// } else {
	// operationMap.setVisible(false);
	// }
	// }
	//
	// /**
	// * Helper: Read data from .zul and fill it into a {@link User} object.
	// *
	// * @return
	// * @throws CoreException
	// */
	// private Operation getFromZul() throws GeneralSecurityException,
	// ZkException {
	// Operation o = new Operation();
	//
	// // set core data
	// o.setId(operationId);
	// o.setOperationNr(operationNr.getValue());
	// o.setPositionLongitude(positionLongitude.getValue());
	// o.setPositionLatitude(positionLatitude.getValue());
	// o.setCity(city.getText());
	// o.setStreet(street.getText());
	// o.setObject(object.getText());
	// o.setCrossway(crossway.getText());
	// o.setPriority(priority.getText());
	// o.setKeyword(keyword.getText());
	// o.setBuzzword(buzzword.getText());
	// o.setIncurredAt(incurredAt.getValue());
	// o.setTakenOverAt(takenOverAt);
	// o.setDispatchedAt(dispatchedAt);
	//
	// o.setResourcesRaw(resourcesRaw.getText());
	//
	// return o;
	// }
	//
	// /**
	// * Helper: Delete current set operation
	// */
	// private void deleteCurrentOperation() {
	// if (operationId > 0) {
	// OperationService operationService = (OperationService)
	// ContainerManager.getComponent("operationService");
	// operationService.remove(operationId);
	//
	// setContentZul(WebGlobals.PAGE_OPERATION_LIST);
	// }
	// }
	//
	// /**
	// * Helper: Init Map
	// */
	// private void initMap(double longVal, double latVal) {
	// log.debug("Arrange map with coords long: " + longVal + "; lat: " +
	// latVal);
	//
	// operationMap.setLng(longVal);
	// operationMap.setLat(latVal);
	// operationMap.setCenter(latVal, longVal);
	// operationMap.setZoom(16);
	//
	// operationMarker.setLat(latVal);
	// operationMarker.setLng(longVal);
	// operationMarker.setOpen(true);
	// }

	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

}
