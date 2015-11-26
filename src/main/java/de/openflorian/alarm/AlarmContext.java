package de.openflorian.alarm;

import java.io.FileNotFoundException;

import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.WebApp;
import org.zkoss.zk.ui.WebApps;
import org.zkoss.zk.ui.event.EventQueues;

import de.openflorian.alarm.parser.AlarmFaxParsedEvent;
import de.openflorian.alarm.parser.AlarmFaxParser;
import de.openflorian.alarm.transform.AlarmFaxTransformator;
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
					AlarmFaxTransformator transformator = (AlarmFaxTransformator)ContainerManager.getComponent("alarmFaxTransformator");
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
							EventQueue.getInstance().publish(new AlarmEvent(op));
							
							// publish to ZK event queues
							EventQueues.lookup(ZkGlobals.EVENTS_QUEUE_ALARMEVENT, WebApps.getCurrent(), true)
							  .publish(new ZkAlarmEvent(op));
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
					Operation currentOperation = AlarmContext.this.currentOperation; 
					AlarmContext.this.currentOperation = null;
					log.info("Current operation successfuly resetted.");
					
					// publish to ZK event queues
					EventQueues.lookup(ZkGlobals.EVENTS_QUEUE_ALARMEVENT, WebApps.getCurrent(), true)
					  .publish(new ZkAlarmDispatchedEvent(currentOperation));
				}
			}
			
		});
		
		this.alarmFaxDirectoryObserverThread = new Thread(this.alarmFaxDirectoryObserver);
		this.alarmFaxDirectoryObserverThread.setDaemon(true);
		this.alarmFaxDirectoryObserverThread.start();
		log.info("-> ... done.");
		
		log.info("Context initialized.");
	}
	
	@Override
	public void destroy() throws Exception {
		log.info("Shutting down AlarmContext...");
		if(this.alarmFaxDirectoryObserverThread.isAlive())
			this.alarmFaxDirectoryObserver.shutdown();
	}
	
}
