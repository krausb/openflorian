package de.openflorian.alarm;

/*
 * This file is part of Openflorian.
 * 
 * Copyright (C) 2015  Bastian Kraus
 * 
 * Openflorian is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version)
 *     
 * Openflorian is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *     
 * You should have received a copy of the GNU General Public License
 * along with Openflorian.  If not, see <http://www.gnu.org/licenses/>.
 */

import io.vertx.core.eventbus.Message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.WebApp;
import org.zkoss.zk.ui.WebApps;
import org.zkoss.zk.ui.event.EventQueue;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.util.WebAppInit;

import de.openflorian.EventBusAdresses;
import de.openflorian.OpenflorianContext;
import de.openflorian.data.model.Operation;
import de.openflorian.ui.ZkGlobals;

/**
 * ZK Alarm Event Bridge Verticle<br/>
 * <br/>
 * Listens to {@link EventBusAdresses#ALARM_INCURRED} and bridges it
 * to {@link ZkGlobals.EVENTS_QUEUE_ALARMEVENT}
 * 
 * @author Bastian Kraus <me@bastian-kraus.me>
 */
public class ZkAlarmEventBridgeListener implements WebAppInit {

	private final Logger log = LoggerFactory.getLogger(getClass());
	
	@SuppressWarnings("rawtypes")
	private EventQueue eventQueue;
	
	@Override
	public void init(WebApp wapp) {
		log.info("Starting " + getClass().getSimpleName() + " ...");
		
		eventQueue = EventQueues.lookup(ZkGlobals.EVENTS_QUEUE_ALARMEVENT, WebApps.getCurrent(), true);
		
		log.info("Listening to: " + EventBusAdresses.ALARM_INCURRED);
		OpenflorianContext.vertx().eventBus().consumer(EventBusAdresses.ALARM_INCURRED, msg -> alarm(msg));
		log.info("Listening to: " + EventBusAdresses.ALARM_TAKENOVER);
		OpenflorianContext.vertx().eventBus().consumer(EventBusAdresses.ALARM_TAKENOVER, msg -> takenOver(msg));
		log.info("Listening to: " + EventBusAdresses.ALARM_DISPATCHED);
		OpenflorianContext.vertx().eventBus().consumer(EventBusAdresses.ALARM_DISPATCHED, msg -> dispatched(msg));
		
		log.info(getClass().getSimpleName() + " started!");
	}

	@SuppressWarnings("unchecked")
	private void alarm(Message<Object> msg) {
		if(log.isDebugEnabled())
			log.debug("alarm(...): " + msg.body());
		if(msg.body() instanceof Operation) {
			Operation o = (Operation) msg.body();
			log.debug("Bridging to ZkAlarmEvent...");
			eventQueue.publish(new ZkAlarmEvent(o));
		} else {
			log.error("Wrong object: no Operation object provided!!!");
		}
	}
	
	private void takenOver(Message<Object> msg) {
		if(log.isDebugEnabled())
			log.debug("takenOver(...): " + msg.body());
		// nothing to do here
	}
	
	@SuppressWarnings("unchecked")
	private void dispatched(Message<Object> msg) {
		if(log.isDebugEnabled())
			log.debug("dispatched(...): " + msg.body());
		if(msg.body() instanceof Operation) {
			Operation o = (Operation) msg.body();
			eventQueue.publish(new ZkAlarmDispatchedEvent(o));
		} else {
			log.error("Wrong object: no Operation object provided!!!");
		}
	}
	
}
