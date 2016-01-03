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

import java.io.FileNotFoundException;
import java.util.Date;

import javax.servlet.ServletContextListener;
import javax.xml.bind.ValidationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.zkoss.zk.ui.WebApps;
import org.zkoss.zk.ui.event.EventQueues;

import de.openflorian.alarm.parser.AlarmFaxParsedEvent;
import de.openflorian.alarm.parser.AlarmFaxParser;
import de.openflorian.alarm.transform.AbstractAlarmFaxTransformator;
import de.openflorian.alarm.transform.AlarmFaxTransformedEvent;
import de.openflorian.config.ConfigurationProvider;
import de.openflorian.data.model.Operation;
import de.openflorian.event.Event;
import de.openflorian.event.EventListener;
import de.openflorian.event.EventQueue;
import de.openflorian.service.OperationService;
import de.openflorian.ui.ZkGlobals;
import de.openflorian.web.core.ContainerManager;

/**
 * Open Florian Alarm Context {@link ServletContextListener} (Singleton)<br/>
 * <br/>
 * {@link InitializingBean} bootstraps the alarming context by starting a listener for
 * monitoring a given directory for new alarming files. 
 * 
 * @author Bastian Kraus <me@bastian-kraus.me>
 */
public final class AlarmContext implements InitializingBean, DisposableBean {

	private final Logger log = LoggerFactory.getLogger(getClass());
	
	private static AlarmContext instance;
	public static AlarmContext getInstance() {
		if(instance == null)
			throw new IllegalStateException("No AlarmContext instance available.");
		return instance;
	}
	
	public Operation getCurrentOperation() {
		return this.currentOperation;
	}
	private Operation currentOperation = null;
	
	@Autowired(required=true)
	private ConfigurationProvider config;
	
	@Autowired(required=true)
	private OperationService operationService;
	
	private AlarmFaxDirectoryObserver alarmFaxDirectoryObserver;
	private Thread alarmFaxDirectoryObserverThread;
	
	@Override
	public void afterPropertiesSet() {
		AlarmContext.instance = this;
		
		log.info("Initialize Alarm Context...");
		
		log.info("-> Setting up AlarmTelefaxDirectoryObserver...");
		this.alarmFaxDirectoryObserver = new AlarmFaxDirectoryObserver(config);
		
		// SETUP AlarmFax Event Listener
		EventQueue.getInstance().subscribe(new EventListener<AlarmFaxIncomingEvent>() {
			
			@Override
			public void onEvent(Event event) {
				log.debug("Event recieved...");
				if(event instanceof AlarmFaxIncomingEvent) {
					log.debug("AlarmFaxIncomingEvent... transforming...");
					AbstractAlarmFaxTransformator transformator = (AbstractAlarmFaxTransformator)ContainerManager.getComponent("alarmFaxTransformator");
					transformator.transform((AlarmFaxIncomingEvent)event);
				} 
			}
			
		});
		
		EventQueue.getInstance().subscribe(new EventListener<AlarmFaxTransformedEvent>() {
			
			@Override
			public void onEvent(Event event) {
				log.debug("Event recieved...");
				if(event instanceof AlarmFaxTransformedEvent) {
					log.debug("AlarmFaxTransformedEvent... parsing...");
					AlarmFaxParser parser = (AlarmFaxParser)ContainerManager.getComponent("alarmFaxParser");
					try {
						Operation op = parser.parse((AlarmFaxTransformedEvent)event);
						op = operationService.persist(op);
						AlarmContext.this.currentOperation = op;
						
						if(op.getId() > 0) {
							EventQueue.getInstance().publish(new AlarmFaxParsedEvent(((AlarmFaxTransformedEvent) event).getResultFile()));
							
							AlarmContext.getInstance().alarmOperation(op);
						}
					} catch (FileNotFoundException e) {
						log.error(e.getMessage(), e);
					} catch (Exception e) {
						log.error(e.getMessage(), e);
					}
				} 
			}
			
		});
		
		EventQueue.getInstance().subscribe(new EventListener<AlarmDispatchedEvent>() {
			
			@Override
			public void onEvent(Event event) {
				log.debug("Event recieved...");
				if(event instanceof AlarmDispatchedEvent) {
					AlarmContext.getInstance().dispatchOperation(getCurrentOperation());
				}
			}
			
		});
		
		this.alarmFaxDirectoryObserverThread = new Thread(this.alarmFaxDirectoryObserver);
		this.alarmFaxDirectoryObserverThread.setDaemon(true);
		this.alarmFaxDirectoryObserverThread.start();
		log.info("-> ... done.");
		
		log.info("Context initialized.");
	}
	
	/**
	 * Trigger a systemwide {@link AlarmEvent} and {@link ZkAlarmEvent} of given <code>o</code>
	 * @param o {@link Operation}
	 */
	public void alarmOperation(Operation o) {
		if(o == null)
			throw new IllegalArgumentException("Given operation is null.");
		
		this.currentOperation = o;
		EventQueue.getInstance().publish(new AlarmEvent(o));
		EventQueues.lookup(ZkGlobals.EVENTS_QUEUE_ALARMEVENT, WebApps.getCurrent(), true)
		  .publish(new ZkAlarmEvent(o));
	}
	
	/**
	 * Trigger a systemwide {@link ZkAlarmDispatchedEvent} of given <code>o</code>
	 * @param o {@link Operation}
	 */
	public void dispatchOperation(Operation o) {
		if(o == null)
			throw new IllegalArgumentException("Given operation is null.");

		try {
			Operation currentOperation = AlarmContext.this.currentOperation;
			currentOperation.setDispatchedAt(new Date());
			operationService.persist(currentOperation);
			
			this.currentOperation = null;
			log.info("Current operation successfuly resetted.");
			
			EventQueues.lookup(ZkGlobals.EVENTS_QUEUE_ALARMEVENT, WebApps.getCurrent(), true)
			  .publish(new ZkAlarmDispatchedEvent(currentOperation));
		} catch (ValidationException e) {
			log.error(e.getMessage(), e);
		}
	}
	
	@Override
	public void destroy() throws Exception {
		log.info("Shutting down AlarmContext...");
		if(this.alarmFaxDirectoryObserverThread.isAlive())
			this.alarmFaxDirectoryObserver.shutdown();
	}
	
}
