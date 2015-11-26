package de.openflorian.alarm.parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import de.openflorian.alarm.transform.AlarmFaxTransformedEvent;
import de.openflorian.config.ConfigurationProvider;
import de.openflorian.data.model.Operation;

/**
 * Alarm Fax Parser<br/>
 * <br/>
 * Processes the given alarm fax with regex patterns and transforms it into
 * tesseract.
 * 
 * @author Bastian Kraus <me@bastian-kraus.me>
 */
public class AlarmFaxParser implements InitializingBean {
	
	private final Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private ConfigurationProvider config;
	
	@Autowired
	private OperationNrParserResponsable firstParserResponsable;
	
	@Override
	public void afterPropertiesSet() {
		if(config == null)
			throw new IllegalStateException("No ConfigurationProvider present/injected.");
		if(firstParserResponsable == null)
			throw new IllegalStateException("No firstParserResponsable present/injected.");
	}
	
	/**
	 * Parses given file from <code>event</code> and extracts 
	 * @param event
	 * @throws FileNotFoundException
	 * @returns
	 * 		{@link Operation} 
	 */
	public Operation parse(AlarmFaxTransformedEvent event) throws Exception, FileNotFoundException {
		if(firstParserResponsable == null)
			throw new IllegalStateException("No alarm fax parser responsable chain available.");
		
		File inputFile = event.getResultFile();
		if(inputFile.exists() && inputFile.canRead()) {
			Operation op = new Operation();
			
			byte[] encoded = Files.readAllBytes(event.getResultFile().toPath());
			String fax = new String(encoded, "UTF-8");
			
			firstParserResponsable.parse(fax, op);
			log.debug("Parsed operation: " + op);
			
			return op;
		} else {
			throw new FileNotFoundException("Given file '" + inputFile.getAbsolutePath() + "' is not readable or does not exist!");
		}

	}
	
}
