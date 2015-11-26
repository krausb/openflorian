package de.openflorian.ui.viewport;

import java.security.Principal;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WebApps;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zul.Button;

import de.openflorian.alarm.AlarmContext;
import de.openflorian.alarm.AlarmDispatchedEvent;
import de.openflorian.alarm.ZkAlarmDispatchedEvent;
import de.openflorian.alarm.ZkAlarmEvent;
import de.openflorian.data.model.Operation;
import de.openflorian.event.EventQueue;
import de.openflorian.ui.ZkGlobals;
import de.openflorian.zk.AbstractGuiController;

/**
 * Operation View Controller<br/>
 * <Br/>
 * ZUL: webapp/operation/viewport.zul
 * 
 * @author Bastian Kraus <me@bastian-kraus.me>
 */
public class OperationDispatcherController extends AbstractGuiController {
	private static final long serialVersionUID = -6907197092954813302L;
	
	private Button dispatch;
	
	@Override
	public String getLoginPage() {
		return ZkGlobals.OPERATION_DISPATCHER;
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
    	

    }
	
	/**
	 * Event-Handler: dispatch.onClick
	 * 
	 * @param
	 * 		{@link Event}
	 */
	public void onClick$dispatch(Event event) {
		EventQueue.getInstance().publish(new AlarmDispatchedEvent(AlarmContext.getInstance().getCurrentOperation()));
	}
	
}
