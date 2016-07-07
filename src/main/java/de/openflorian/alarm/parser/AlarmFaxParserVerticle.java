package de.openflorian.alarm.parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.openflorian.EventBusAddresses;
import de.openflorian.OpenflorianContext;
import de.openflorian.alarm.AlarmFaxEvent;
import de.openflorian.data.model.Operation;

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

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.eventbus.Message;

/**
 * Alarm Fax Parser<br/>
 * <br/>
 * Processes the given alarm fax with regex patterns and transforms it into
 * tesseract.
 * 
 * @author Bastian Kraus <bofh@k-hive.de>
 */
public class AlarmFaxParserVerticle extends AbstractVerticle {

	private final Logger log = LoggerFactory.getLogger(getClass());

	private OperationNrParserResponsable firstParserResponsable;

	@Override
	public void start(Future<Void> startFuture) {
		log.info("Starting " + getClass().getSimpleName() + " ...");

		initResponsables();

		if (firstParserResponsable == null)
			throw new IllegalStateException("No firstParserResponsable present/injected.");
		log.info(getClass().getSimpleName() + " started.");

		log.info("Registering EventBus consumer...");
		vertx.eventBus().consumer(EventBusAddresses.ALARMFAX_TRANSFORMED, msg -> parse(msg));

		startFuture.complete();
	}

	/**
	 * Helper: Init parser chain of responsibility
	 */
	private void initResponsables() {
		firstParserResponsable = new OperationNrParserResponsable();

		CityParserResponsable cityParser = new CityParserResponsable();
		firstParserResponsable.setNext(cityParser);

		StreetParserResponsable streetParser = new StreetParserResponsable();
		cityParser.setNext(streetParser);

		CrosswayParserResponsable crosswayParser = new CrosswayParserResponsable();
		streetParser.setNext(crosswayParser);

		PriorityParserResponsable priorityParser = new PriorityParserResponsable();
		crosswayParser.setNext(priorityParser);

		ObjectParserResponsable objectParser = new ObjectParserResponsable();
		priorityParser.setNext(objectParser);

		BuzzwordParserResponsable buzzwordParser = new BuzzwordParserResponsable();
		objectParser.setNext(buzzwordParser);

		KeywordParserResponsable keywordParser = new KeywordParserResponsable();
		buzzwordParser.setNext(keywordParser);

		GeoCoordinateParserResponsable coordinateParser = new GeoCoordinateParserResponsable();
		keywordParser.setNext(coordinateParser);

		ResourcesParserResponsable resourcesParser = new ResourcesParserResponsable();
		coordinateParser.setNext(resourcesParser);
	}

	/**
	 * Parses given file from <code>event</code> and extracts
	 * 
	 * @param event
	 * @throws FileNotFoundException
	 * @returns {@link Operation}
	 */
	public void parse(Message<Object> msg) {
		AlarmFaxEvent event = (AlarmFaxEvent) msg.body();

		if (firstParserResponsable == null)
			throw new IllegalStateException("No alarm fax parser responsable chain available.");

		File inputFile = event.getResultFile();
		if (inputFile.exists() && inputFile.canRead()) {
			try {
				log.debug("Parsing file: " + inputFile.getAbsolutePath());
				Operation op = new Operation();

				byte[] encoded = Files.readAllBytes(event.getResultFile().toPath());
				String fax = new String(encoded, "UTF-8");
				parseFax(fax, op);

				OpenflorianContext.vertx().eventBus().send(EventBusAddresses.ARCHIVE_FILE, inputFile.getAbsolutePath());

				OpenflorianContext.vertx().eventBus().publish(EventBusAddresses.ALARM_INCURRED, op);
			} catch (IOException e) {
				log.error(e.getMessage(), e);
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		} else {
			log.error("Given file '" + inputFile.getAbsolutePath() + "' is not readable or does not exist!");
		}
	}

	/**
	 * Helper: Parse given <code>fax</code> into <code>op</code>
	 * 
	 * @param fax
	 * @param op
	 */
	protected void parseFax(String fax, Operation op) {
		firstParserResponsable.parse(fax, op);
		log.debug("Parsed operation: " + op);

		op.setIncurredAt(new Date());

	}

}
