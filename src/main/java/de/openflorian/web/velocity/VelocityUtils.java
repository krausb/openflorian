package de.openflorian.web.velocity;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.runtime.RuntimeConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.openflorian.web.core.BootstrapManager;

/**
 * Velocity Template Rendering Utils
 * 
 * @author Bastian Kraus <bofh@k-hive.de>
 */
public class VelocityUtils {

	public static Logger log = LoggerFactory.getLogger(VelocityUtils.class);

	private static VelocityEngine engine = null;

	/**
	 * Get a preconfigured instance of {@link VelocityEngine}
	 * 
	 * @return
	 * @throws Exception
	 */
	public static VelocityEngine getVelocityEngine() throws Exception {
		if (engine == null) {
			log.debug("Initializing velocity engine first time...");

			File velocityProperties = new File(
					BootstrapManager.manager().getRealPathForVirtual("/WEB-INF/velocity.properties"));
			log.debug("velocity.properties: " + velocityProperties.getAbsolutePath() + "; exists: "
					+ velocityProperties.exists());
			Properties prop = new Properties();
			prop.load(new FileReader(velocityProperties));

			// instantiate engine and init with given properties
			try {
				engine = new VelocityEngine();
				engine.setApplicationAttribute("javax.servlet.ServletContext",
						BootstrapManager.manager().getCurrentContext());
				engine.init(prop);
			} catch (NullPointerException e) {
				log.error(e.getMessage(), e);
			}

			log.debug("instance stored. using now :-)");
		}
		return engine;
	}

	/**
	 * Gets a Velocity Template from preconfigured
	 * {@link VelocityUtils#getVelocityEngine()}
	 * 
	 * @param templateName
	 * @return
	 * @throws Exception
	 */
	public static Template getTemplate(String templateName) throws Exception {
		return getVelocityEngine().getTemplate(templateName, StandardCharsets.UTF_8.toString());
	}

	/**
	 * Gets a velocity template from preconfigured
	 * {@link VelocityUtils#getVelocityEngine()} and merges a localized
	 * properties context.
	 * 
	 * @param templateName
	 * @return
	 * @throws Exception
	 * @throws ParseErrorException
	 * @throws Exception
	 */
	public static String renderTemplate(String templateName) throws Exception {
		Template t = getTemplate(templateName);

		StringWriter sw = new StringWriter();
		t.merge(new VelocityContext(getDefaultVelocityContext()), sw);

		return sw.toString();
	}

	/**
	 * Get rendered template as {@link InputStream}
	 * 
	 * @param templateName
	 * @return {@link InputStream}
	 * @throws Exception
	 */
	public static InputStream getRenderedTemplate(String templateName) throws Exception {
		return new ByteArrayInputStream(renderTemplate(templateName).getBytes());
	}

	/**
	 * Gets a Veloctiy Template from external base path location
	 * 
	 * @param basePath
	 * @param templateName
	 * @return
	 * @throws Exception
	 */
	public static Template getExternalTemplate(String basePath, String templateName) throws Exception {
		VelocityEngine e = new VelocityEngine();
		e.setProperty(RuntimeConstants.FILE_RESOURCE_LOADER_PATH, basePath);
		e.init();
		return e.getTemplate(templateName, StandardCharsets.UTF_8.toString());
	}

	/**
	 * 
	 * @return
	 */
	public static Map<String, Object> getDefaultVelocityContext() {
		Map<String, Object> context = new HashMap<>();

		context.put("tool", new VelocityTool());

		return context;
	}

}
