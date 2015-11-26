package de.openflorian.alarm.transform;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import de.openflorian.alarm.AlarmFaxDirectoryObserver;
import de.openflorian.alarm.AlarmFaxIncomingEvent;
import de.openflorian.config.ConfigurationProvider;
import de.openflorian.event.EventQueue;

/**
 * Alarm Fax Transformator<br/>
 * <br/>
 * Processes the given alarm fax with an OCR binary like
 * tesseract.
 * 
 * @author Bastian Kraus <me@bastian-kraus.me>
 */
public class AlarmFaxTransformator implements InitializingBean {
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	public static final String CONFIG_TRANSFORMATION_CMD = "alarm.transform.cmd";

	@Autowired(required=true)
	private ConfigurationProvider config;
	
	private String transformationCmd;
	private String faxObervationDirectory;
	
	@Override
	public void afterPropertiesSet() {
		if(config == null)
			throw new IllegalStateException("No ConfigurationProvider present/injected.");
		
		log.info("Setting up AlarmFaxTransformator...");
		transformationCmd = config.getProperty(CONFIG_TRANSFORMATION_CMD);
		faxObervationDirectory = config.getProperty(AlarmFaxDirectoryObserver.CONFIG_OBSERVING_DIRECTORY);
		
		if(StringUtils.isEmpty(transformationCmd))
			throw new IllegalStateException("Transformation command '" + CONFIG_TRANSFORMATION_CMD + "' is missing.");
		else if(StringUtils.isEmpty(faxObervationDirectory)) 
			throw new IllegalStateException("Fax observing directory '" + AlarmFaxDirectoryObserver.CONFIG_OBSERVING_DIRECTORY + "' is missing.");
		else {
			log.info(CONFIG_TRANSFORMATION_CMD + ": " + transformationCmd);
			log.info(AlarmFaxDirectoryObserver.CONFIG_OBSERVING_DIRECTORY + ": " + faxObervationDirectory);
		}
	}
	
	/**
	 * Transform the incoming file from {@link AlarmFaxIncomingEvent#getResultFile()}
	 * with 
	 * @param event
	 */
	public void transform(AlarmFaxIncomingEvent event) {
		Runtime rt = Runtime.getRuntime();
		
		try {
			String resultTextFile = String.format("%s/%s",
					faxObervationDirectory,
					event.getResultFile().getName());
			String cmd = String.format("\"%s\" \"%s\" \"%s\"", 
					transformationCmd,
					event.getResultFile().getAbsolutePath(),
					resultTextFile);
			log.debug("Running cmd: " + cmd);
			
			Process ps = rt.exec(cmd);
			
			int exitCode = ps.waitFor();
			if(exitCode == 0) {
				log.debug(String.format("Successful transformed '%s' to '%s'. Exit code: %d",
						event.getResultFile().getAbsolutePath(),
						resultTextFile,
						exitCode));
				EventQueue.getInstance().publish(new AlarmFaxTransformedEvent(new File(resultTextFile + ".txt")));
			} else {
				log.error(String.format("An error occured transforming '%s'. Exit code: %d",
						event.getResultFile().getAbsolutePath(),
						exitCode));
				log.error(IOUtils.toString(ps.getErrorStream(), "UTF-8"));
			}
			ps.destroy();
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		} catch (InterruptedException e) {
			log.error(e.getMessage(), e);
		}
	}
	
}
