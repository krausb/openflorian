package de.openflorian.web.admin;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.event.UIEvents.PollEvent;
import com.vaadin.event.UIEvents.PollListener;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;

import de.openflorian.weather.Weather;
import de.openflorian.weather.Weather.PressureState;
import de.openflorian.weather.Weather.Source;
import de.openflorian.weather.WeatherConditionMapping;
import de.openflorian.weather.WeatherProvisioningVerticle;
import de.openflorian.web.AbstractBrowser;
import de.openflorian.web.WebGlobals;

/**
 * Weather View Browser<br/>
 * <Br/>
 * URL: Viewport
 * 
 * @author Bastian Kraus <bofh@k-hive.de>
 */
public final class WeatherViewBrowser extends AbstractBrowser implements PollListener {
	private static final long serialVersionUID = 4786328707332997870L;

	private static final Logger log = LoggerFactory.getLogger(WeatherViewBrowser.class);

	private Label weather;

	private Label temperature = new Label();
	private Label condition = new Label();
	private Label humidity = new Label();
	private Label pressure = new Label();
	private Label rising = new Label();
	private Label windSpeed = new Label();
	private Label windDirection = new Label();

	private Label currentTime = new Label();

	private CustomLayout layout = new CustomLayout("weather");

	public WeatherViewBrowser(UI view) {
		super(view);
	}

	@Override
	public void init() {
		if (log.isDebugEnabled())
			log.debug("init()...");

		view.setPollInterval(15000);
		view.addPollListener(this);

		// init layout
		layout.addComponent(temperature, "temperature");
		layout.addComponent(condition, "condition");
		layout.addComponent(humidity, "humidity");
		layout.addComponent(pressure, "pressure");
		layout.addComponent(rising, "rising");
		layout.addComponent(windSpeed, "windSpeed");
		layout.addComponent(windDirection, "windDirection");
		layout.addComponent(currentTime, "currentTime");

		this.addComponent(layout);
		printWeather();
	}

	/**
	 * Helper: print out current weather
	 */
	private void printWeather() {
		view.access(new Runnable() {

			@Override
			public void run() {
				try {
					Weather w = WeatherProvisioningVerticle.getCurrentWeather();

					if (w != null) {
						temperature.setValue(String.format("%.1f", w.getTemperature()) + " °C");

						if (w.getSource().equals(Source.YAHOO))
							condition.setStyleName(
									"wi weather-icon " + WeatherConditionMapping.mapYahooCode(w.getConditionCode()));
						else
							condition.setStyleName("wi weather-icon "
									+ WeatherConditionMapping.mapOpenWeatherMapCode(w.getConditionCode()));

						humidity.setValue(String.format("%.0f", w.getHumidity()) + " %");
						pressure.setValue(String.format("%.0f", w.getPressure()) + " hPa");

						if (w.getPressureState() == PressureState.RISING) {
							rising.setStyleName("wi pressure wi-direction-up-right");
						} else if (w.getPressureState() == PressureState.FALLING) {
							rising.setStyleName("wi pressure wi-direction-down-right");
						} else if (w.getPressureState() == PressureState.STEADY) {
							rising.setStyleName("wi pressure wi-direction-right");
						}

						windSpeed.setValue(String.format("%.1f", w.getWindSpeed()) + " km/h");
						windDirection.setStyleName("wi windspeed wi-wind towards-" + w.getWindDirection() + "-deg");
						currentTime.setValue(
								new SimpleDateFormat(WebGlobals.FORMAT_DATETIME).format(new Date(w.getTimestamp())));
					} else {
						temperature.setValue("n/a °C");
						condition.setStyleName(
								"wi weather-icon " + WeatherConditionMapping.mapOpenWeatherMapCode(Integer.MIN_VALUE));
						humidity.setValue("n/a %");
						pressure.setValue("n/a hPa");
						rising.setStyleName("wi pressure wi-direction-right");
						windSpeed.setValue("n/a km/h");
						windDirection.setStyleName("wi windspeed wi-wind towards-0-deg");
						currentTime.setValue(new SimpleDateFormat(WebGlobals.FORMAT_DATETIME).format(new Date()));
					}

				} catch (Exception e) {
					log.error(e.getMessage(), e);
				}
			}

		});
	}

	@Override
	public void close() {
		if (log.isDebugEnabled())
			log.debug("close()...");
		view.removePollListener(this);
	}

	@Override
	public void poll(PollEvent event) {
		if (log.isTraceEnabled())
			log.trace("printWeather()...");
		printWeather();
	}

}
