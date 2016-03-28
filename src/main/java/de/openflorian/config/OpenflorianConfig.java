package de.openflorian.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * JSON Configuration Wrapper for Openflorian
 * 
 * @author Bastian Kraus <bofh@k-hive.de>
 */
public class OpenflorianConfig {

	private static final Logger log = LoggerFactory.getLogger(OpenflorianConfig.class);

	public static final String CONFIG_FILE_PARAM = "configfile";

	private static final String CONFIG_FILE = "/config.json";

	private static Config instance = null;

	public synchronized static Config config() {
		if (instance == null) {
			InputStream fis = null;

			try {
				final String externalConfig = System.getProperty(CONFIG_FILE_PARAM);
				if (externalConfig != null && !externalConfig.isEmpty()) {
					File externalConfigFile = new File(externalConfig);
					log.info("Use external config: " + externalConfigFile.getAbsolutePath());
					fis = new FileInputStream(externalConfigFile);
				} else {
					log.info("Use internal config: " + CONFIG_FILE);
					fis = OpenflorianConfig.class.getResourceAsStream(CONFIG_FILE);
				}
				ObjectMapper mapper = new ObjectMapper();
				instance = mapper.readValue(fis, Config.class);
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				throw new IllegalStateException(e);
			}
		}
		return instance;
	}

	@JsonInclude(JsonInclude.Include.NON_NULL)
	public static class Config {

		@JsonProperty("cipherKeys")
		public CipherKeys cipherKeys;
		@JsonProperty("faxObserver")
		public FaxObserver faxObserver;
		@JsonProperty("faxTransformer")
		public FaxTransformer faxTransformer;
		@JsonProperty("faxParser")
		public FaxParser faxParser;
		@JsonProperty("weather")
		public Weather weather;
	}

	@JsonInclude(JsonInclude.Include.NON_NULL)
	public static class Weather {
		@JsonProperty(value = "loadPeriod")
		public int loadPeriod;
		@JsonProperty(value = "yahooWeatherApi", required = false)
		public YahooWeatherApi yahooWeatherApi;
		@JsonProperty(value = "openWeatherMapApi", required = false)
		public OpenWeatherMapApi openWeatherMapApi;
	}

	@JsonInclude(JsonInclude.Include.NON_NULL)
	public static class YahooWeatherApi {
		@JsonProperty("consumerKey")
		public String consumerKey;
		@JsonProperty("consumerSecret")
		public String consumerSecret;
		@JsonProperty("woeid")
		public String woeid;
	}

	@JsonInclude(JsonInclude.Include.NON_NULL)
	public static class OpenWeatherMapApi {
		@JsonProperty("apiKey")
		public String apiKey;
		@JsonProperty("cityId")
		public int cityId;
	}

	@JsonInclude(JsonInclude.Include.NON_NULL)
	public static class CipherKeys {

		@JsonProperty("blowfish")
		public String blowfish;
		@JsonProperty("xor")
		public String xor;

	}

	@JsonInclude(JsonInclude.Include.NON_NULL)
	public static class FaxObserver {

		@JsonProperty("observerDir")
		public String observerDir;
		@JsonProperty("archiveDir")
		public String archiveDir;

	}

	@JsonInclude(JsonInclude.Include.NON_NULL)
	public static class FaxParser {

		@JsonProperty("currentStation")
		public String currentStation;
		@JsonProperty("patterns")
		public Patterns patterns;
		@JsonProperty("geocoordinates")
		public Geocoordinates geocoordinates;

	}

	@JsonInclude(JsonInclude.Include.NON_NULL)
	public static class FaxTransformer {

		@JsonProperty("cmd")
		public String cmd;
		@JsonProperty("vars")
		public Vars vars;

	}

	@JsonInclude(JsonInclude.Include.NON_NULL)
	public static class Geocoordinates {

		@JsonProperty("source")
		public String source;
		@JsonProperty("target")
		public String target;
		@JsonProperty("targetParams")
		public String targetParams;

	}

	@JsonInclude(JsonInclude.Include.NON_NULL)
	public static class Patterns {

		@JsonProperty("operationNrPattern")
		public String operationNrPattern;
		@JsonProperty("objectPattern")
		public String objectPattern;
		@JsonProperty("streetPattern")
		public String streetPattern;
		@JsonProperty("crosswayPattern")
		public String crosswayPattern;
		@JsonProperty("cityPattern")
		public String cityPattern;
		@JsonProperty("geocoordinatesPattern")
		public String geocoordinatesPattern;
		@JsonProperty("priorityPattern")
		public String priorityPattern;
		@JsonProperty("keywordPattern")
		public String keywordPattern;
		@JsonProperty("buzzwordPattern")
		public String buzzwordPattern;
		@JsonProperty("resourcesPattern")
		public String resourcesPattern;
		@JsonProperty("stationresourcePattern")
		public String stationresourcePattern;
		@JsonProperty("resourcecallnamePattern")
		public String resourcecallnamePattern;

	}

	@JsonInclude(JsonInclude.Include.NON_NULL)
	public static class Vars {

		@JsonProperty("inputVar")
		public String inputVar;
		@JsonProperty("outputVar")
		public String outputVar;

	}
}