package de.openflorian.alarm.parser;

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

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.openflorian.data.model.Operation;

/**
 * Alarm Parser Test
 * 
 * @author Bastian Kraus <bofh@k-hive.de>
 */
public class AlarmParserTest {

	protected static Logger log = LoggerFactory.getLogger(AlarmParserTest.class);

	private AlarmFaxParserVerticle alarmFaxParserVerticle;

	@Test
	public void parseTest() throws IOException {

		assertNotNull(alarmFaxParserVerticle);

		File inputFile = new File("src/test/resources/testfiles/alarmfax-test.txt");
		if (inputFile.exists() && inputFile.canRead()) {
			Operation op = new Operation();

			byte[] encoded = Files.readAllBytes(inputFile.toPath());
			String fax = new String(encoded, "UTF-8");

			alarmFaxParserVerticle.parseFax(fax, op);
			log.debug("Parsed operation: " + op);
		}
	}

}
