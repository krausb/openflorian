package de.openflorian.alarm.parser;

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.stream.Stream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.openflorian.config.ConfigurationProvider;
import de.openflorian.data.model.Operation;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/config/alarmFaxParserContext.xml" })
public class AlarmParserTest {

	protected static Logger log = LoggerFactory.getLogger(AlarmParserTest.class);
	
	@Autowired
	private ConfigurationProvider config;
	
	@Autowired
	private OperationNrParserResponsable firstParserResponsable;
	
	@Test
	public void parseTest() throws IOException {
		
		assertNotNull(config);
		assertNotNull(firstParserResponsable);
		
		File inputFile = new File("src/test/resources/testfiles/alarmfax-test.txt");
		if(inputFile.exists() && inputFile.canRead()) {
			Operation op = new Operation();
			
			byte[] encoded = Files.readAllBytes(inputFile.toPath());
			String fax = new String(encoded, "UTF-8");
			  
			firstParserResponsable.parse(fax, op);
			log.debug("Parsed operation: " + op);
		}
	}
	
}
