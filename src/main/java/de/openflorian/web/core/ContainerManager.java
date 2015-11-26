package de.openflorian.web.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

/**
 * Container Manager<br/>
 * <br/>
 * Brings access to the spring web platform application context
 * 
 * @author Bastian Kraus <me@bastian-kraus.me>
 */
public class ContainerManager {

	private static final Logger log = LoggerFactory.getLogger(ContainerManager.class);
	
	/**
	 * Load component bean from Spring Application Context
	 * @param comp
	 * @return
	 */
	public static Object getComponent(String compId) {
		WebApplicationContext ctx = ContextLoader.getCurrentWebApplicationContext();
		Object component = null;
		try {
			component = ctx.getBean(compId);
		} catch( Exception e ) {
			log.error(e.getMessage(), e);
		} 
		return component;
	}
	
	/**
	 * @see ContainerManager#getComponent(String)
	 * 
	 * @param comp
	 * @return
	 */
	public static Object getComponent(AvailableComponent comp) {
		return getComponent(comp.toString());
	}
	
}
