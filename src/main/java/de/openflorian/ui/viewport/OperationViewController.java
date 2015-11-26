package de.openflorian.ui.viewport;

import java.security.Principal;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.WebApps;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zul.Include;

import de.openflorian.alarm.AlarmContext;
import de.openflorian.alarm.ZkAlarmDispatchedEvent;
import de.openflorian.alarm.ZkAlarmEvent;
import de.openflorian.data.model.Operation;
import de.openflorian.ui.ZkGlobals;
import de.openflorian.zk.AbstractGuiController;

/**
 * Operation View Controller<br/>
 * <Br/>
 * ZUL: webapp/operation/viewport.zul
 * 
 * @author Bastian Kraus <me@bastian-kraus.me>
 */
public class OperationViewController extends AbstractGuiController {
	private static final long serialVersionUID = 3698439925438248515L;

	private Include view;
	
	@Override
	public String getLoginPage() {
		return ZkGlobals.OPERATION_VIEWPORT;
	}
	
	@Override
	public boolean mustBeLoggedIn() {
		return false;
	}
	
	@Override
	public boolean mayView(Principal user) {
		return true;
	}
	
	@Override
    public void doAfterCompose(Component comp) throws Exception {
    	super.doAfterCompose(comp);
    	
    	EventQueues.lookup(ZkGlobals.EVENTS_QUEUE_ALARMEVENT, WebApps.getCurrent(), true).subscribe(
    			new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				if(event instanceof ZkAlarmEvent) {
					OperationViewController.this.log.debug("ZkAlarmEvent catched...");
					if(((ZkAlarmEvent) event).getOperation() != null)
						showAlarmView(((ZkAlarmEvent) event).getOperation());
				} else if (event instanceof ZkAlarmDispatchedEvent) {
					OperationViewController.this.log.debug("ZkAlarmDispatchedEvent catched...");
					showWeatherView();
				}
			}
    		
		});
    	
    	Operation operation = AlarmContext.getInstance().getCurrentOperation();
    	if(operation != null)
    		showAlarmView(operation);
    	else
    		showWeatherView();
    }
	
	/**
	 * Helper: Show {@link ZkGlobals#OPERATION_VIEW_ALARMVIEW}
	 * 
	 * @param
	 * 		{@link Operation}
	 */
	private void showAlarmView(Operation operation) {
		Executions.getCurrent().setAttribute(ZkGlobals.REQUEST_OPERATION, operation);
		view.setSrc(null);
		view.setSrc(ZkGlobals.OPERATION_VIEW_ALARMVIEW);
	}
	
	/**
	 * Helper: Show {@link ZkGlobals#OPERATION_VIEW_WEATHERVIEW}
	 */
	private void showWeatherView() {
		view.setSrc(ZkGlobals.OPERATION_VIEW_WEATHERVIEW);
	}
	
}
