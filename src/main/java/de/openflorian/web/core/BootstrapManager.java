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

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Bean: Bootstrap Manager
 * 
 * Provides basic configuration, e.g. Context Path, Configuration Path, ...
 * 
 * @author Bastian Kraus <bofh@k-hive.de>
 */
public class BootstrapManager implements ServletContextListener {

	private static final Logger log = LoggerFactory.getLogger(BootstrapManager.class);

	private static BootstrapManager instance;

	public static BootstrapManager manager() {
		if (instance == null)
			throw new IllegalStateException(
					"Missplacement of BootstrapManager Servlet Context Listener. No instance available.");
		return instance;
	}

	private ServletContext context;

	public ServletContext getCurrentContext() {
		return context;
	}

	public String getContextPath() {
		return context.getContextPath();
	}

	public String getRealPath() {
		return getRealPathForVirtual("/");
	}

	public String getRealPathForVirtual(String virtualPath) {
		return context.getRealPath(virtualPath);
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		log.info("Bootstrap Manager initializing...");

		instance = this;
		this.context = sce.getServletContext();

		log.info("Webapp context path: " + getContextPath());
		log.info("Real path for '/': " + getRealPath());

		log.info("... intialization finished");
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// nothing to do here
	}

}
