package de.openflorian.weather;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.openflorian.config.OpenflorianConfig;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;

/**
 * Verticle for provisioning weather
 * 
 * @author Bastian Kraus <bofh@k-hive.de>
 */
public class WeatherProvisioningVerticle extends AbstractVerticle {

	private static final Logger log = LoggerFactory.getLogger(WeatherProvisioningVerticle.class);

	private WeatherSource weatherSource;

	private static Weather currentWeather = null;

	public static Weather getCurrentWeather() {
		return currentWeather;
	}

	@Override
	public void start() throws Exception {
		log.info("Initialize " + getClass().getSimpleName() + "...");

		// start weather polling
		weatherSource = WeatherSourceFactory.get(Weather.Source.OPENWEATHERMAP);
		currentWeather = weatherSource.getCurrentWeather();

		vertx.setPeriodic(OpenflorianConfig.config().weather.loadPeriod * 1000, new Handler<Long>() {

			@Override
			public void handle(Long event) {
				if (weatherSource == null) {
					log.error("No weather source present.");
					return;
				}
				try {
					if (log.isDebugEnabled())
						log.debug("Loading weather with " + weatherSource.getClass().getSimpleName());
					currentWeather = weatherSource.getCurrentWeather();
					if (log.isDebugEnabled())
						log.debug("Current weather: " + currentWeather);
				} catch (Exception e) {
					log.error(e.getMessage(), e);
				}
			}
		});

		log.info(getClass().getSimpleName() + " started!");

		super.start();
	}

}
