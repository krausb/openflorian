package de.openflorian.web.core;

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
