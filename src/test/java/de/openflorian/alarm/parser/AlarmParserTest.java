package de.openflorian.alarm.parser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.openflorian.data.model.Operation;
import io.vertx.core.Vertx;

/**
 * Alarm Parser Test
 * 
 * @author Bastian Kraus <bofh@k-hive.de>
 */
public class AlarmParserTest {

	protected static Logger log = LoggerFactory.getLogger(AlarmParserTest.class);

	private static Vertx vertx = null;
	private AlarmFaxParserVerticle alarmFaxParserVerticle = null;

	@BeforeClass
	public static void bootstrap() throws Exception {
		vertx = Vertx.vertx();
	}

	@Before
	public void prepare() throws Exception {
		alarmFaxParserVerticle = new AlarmFaxParserVerticle();
		vertx.deployVerticle(alarmFaxParserVerticle);
	}

	@Test
	public void parseTest() throws IOException {

		final File inputFile = new File("src/test/resources/testfiles/alarmfax-test.txt");
		if (inputFile.exists() && inputFile.canRead()) {
			final Operation op = new Operation();

			final byte[] encoded = Files.readAllBytes(inputFile.toPath());
			final String fax = new String(encoded, "UTF-8");

			alarmFaxParserVerticle.parseFax(fax, op);
			log.debug("Parsed operation: " + op);
		}
	}

}
