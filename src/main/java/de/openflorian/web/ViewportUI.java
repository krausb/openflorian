package de.openflorian.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.ui.Transport;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import de.openflorian.EventBusAddresses;
import de.openflorian.OpenflorianContext;
import de.openflorian.alarm.AlarmContextVerticle;
import de.openflorian.data.model.Operation;
import de.openflorian.web.viewport.AlarmViewBrowser;
import de.openflorian.web.viewport.WeatherViewBrowser;
import io.vertx.core.eventbus.Message;
import io.vertx.core.eventbus.MessageConsumer;

/**
 * 
 * @author Bastian Kraus <ceth@k-hive.de>
 */
@Theme("ofviewport")
@Push(transport = Transport.LONG_POLLING)
@PreserveOnRefresh
public class ViewportUI extends UI {
	private static final long serialVersionUID = -4716521084775334616L;

	private static final Logger log = LoggerFactory.getLogger(ViewportUI.class);

	private VerticalLayout viewContainer;

	private AbstractBrowser currentView = null;

	private MessageConsumer<Object> alarmIncurredMessageConsumer;
	private MessageConsumer<Object> alarmDispatchedMessageConsumer;

	@Override
	protected void init(VaadinRequest request) {
		setWidth("100%");
		setHeight("100%");
		viewContainer = new VerticalLayout();
		setContent(viewContainer);

		alarmIncurredMessageConsumer = OpenflorianContext.vertx().eventBus().consumer(EventBusAddresses.ALARM_INCURRED,
				message -> alarmIncurred(message));
		alarmDispatchedMessageConsumer = OpenflorianContext.vertx().eventBus()
				.consumer(EventBusAddresses.ALARM_DISPATCHED, message -> alarmDispatched(message));

		refresh(request);
	}

	@Override
	protected void refresh(VaadinRequest request) {
		Operation currentOperation = AlarmContextVerticle.getInstance().getCurrentOperation();

		if (log.isDebugEnabled())
			log.debug("Current operation: " + currentOperation);

		if (currentOperation != null)
			showView(new AlarmViewBrowser(this, currentOperation));
		else
			showView(new WeatherViewBrowser(this));
	}

	@Override
	public void detach() {
		if (log.isTraceEnabled())
			log.trace("Detach from session...");

		if (alarmIncurredMessageConsumer != null)
			alarmIncurredMessageConsumer.unregister();
		if (alarmDispatchedMessageConsumer != null)
			alarmDispatchedMessageConsumer.unregister();
	}

	/**
	 * Show given component
	 * 
	 * @param component
	 */
	private void showView(AbstractBrowser component) {
		if (currentView != null)
			viewContainer.removeComponent(currentView);

		currentView = component;
		currentView.init();
		viewContainer.addComponent(currentView);
	}

	/**
	 * Event-Handler: {@link EventBusAddresses#ALARM_INCURRED}
	 * 
	 * @param msg
	 */
	private void alarmIncurred(Message<Object> msg) {
		Operation o = (Operation) msg.body();

		if (log.isTraceEnabled())
			log.trace("Switching View to AlarmViewBrowser. Incurred: " + o);
		this.access(() -> showView(new AlarmViewBrowser(ViewportUI.this, o)));

	}

	/**
	 * Event-Handler: {@link EventBusAddresses#ALARM_DISPATCHED}
	 * 
	 * @param msg
	 */
	private void alarmDispatched(Message<Object> msg) {
		if (log.isTraceEnabled())
			log.trace("Switching View to WeatherViewBrowser. Dispatched...");

		this.access(() -> showView(new WeatherViewBrowser(ViewportUI.this)));
	}
}
