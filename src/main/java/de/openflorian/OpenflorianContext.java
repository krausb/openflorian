package de.openflorian;

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

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.openflorian.alarm.*;
import de.openflorian.alarm.archive.AlarmFaxArchiver;
import de.openflorian.alarm.parser.AlarmFaxParserVerticle;
import de.openflorian.alarm.transform.BinaryAlarmFaxTransformator;
import de.openflorian.data.DatabaseConnectionKeepaliveVerticle;
import de.openflorian.data.model.Operation;
import de.openflorian.weather.WeatherProvisioningVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;

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

import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.MessageCodec;

/**
 * Openflorian Context<br/>
 * <br/>
 * Responsable for bootstrapping the whole application context.
 * 
 * @author Bastian Kraus <bofh@k-hive.de>
 */
public class OpenflorianContext implements ServletContextListener {

	private final Logger log = LoggerFactory.getLogger(getClass());

	private static Vertx vertx;

	public static Vertx vertx() {
		return vertx;
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		log.info("Initializing OpenFlorian Context...");

		log.info("Creating Vert.X context...");
		vertx = Vertx.vertx();

		registerMessageCodecs();
		deployVerticles();

		log.info("... Context initialized!");
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		log.info("Shutting down OpenFlorian Context...");

		log.info("Releasing Vert.X resources...");
		vertx.close();

		vertx.close(new Handler<AsyncResult<Void>>() {

			@Override
			public void handle(AsyncResult<Void> event) {
				if (event.succeeded())
					log.info("Shutdown complete. Bye :-)");
				else
					log.error(event.cause().getMessage(), event.cause());
			}
		});

	}

	/**
	 * Helper: Deploy Verticles to VertX Context
	 */
	private void deployVerticles() {
		log.info("Deploying Verticles...");

		vertx.deployVerticle(new DatabaseConnectionKeepaliveVerticle());
		vertx.deployVerticle(new AlarmContextVerticle());
		vertx.deployVerticle(new FaxDirectoryObserverVerticle());
		vertx.deployVerticle(new BinaryAlarmFaxTransformator());
		vertx.deployVerticle(new AlarmFaxParserVerticle());
		vertx.deployVerticle(new AlarmFaxArchiver());
		vertx.deployVerticle(new WeatherProvisioningVerticle());
	}

	/**
	 * Helper: Register {@link MessageCodec}s to the {@link EventBus} for
	 * decoding and encoding custom message objects
	 */
	private void registerMessageCodecs() {
		log.info("Register MessageCodes for Vert.X EventBus Message Transport...");
		vertx.eventBus().registerDefaultCodec(Operation.class, new OperationMessageCodec());
		vertx.eventBus().registerDefaultCodec(AlarmFaxEvent.class, new AlarmFaxEventMessageCodec());
	}

}
