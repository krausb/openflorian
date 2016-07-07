package de.openflorian.weather.impl;

import org.bitpipeline.lib.owm.OwmClient;
import org.bitpipeline.lib.owm.StatusWeatherData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.openflorian.config.OpenflorianConfig;
import de.openflorian.weather.Weather;
import de.openflorian.weather.Weather.PressureState;
import de.openflorian.weather.Weather.Source;
import de.openflorian.weather.WeatherSource;

public class OpenWeatherMapSource implements WeatherSource {

	private static final Logger log = LoggerFactory.getLogger(OpenWeatherMapSource.class);

	private OwmClient owm = new OwmClient();

	@Override
	public Weather getCurrentWeather() throws Exception {
		owm.setAPPID(OpenflorianConfig.config().weather.openWeatherMapApi.apiKey);

		Weather w = new Weather();

		StatusWeatherData currentWeather = owm
				.currentWeatherAtCity(OpenflorianConfig.config().weather.openWeatherMapApi.cityId);

		// temperature from kelvin to celsius
		w.setTemperature(currentWeather.getTemp() - 273.15);
		w.setPressure(currentWeather.getPressure());
		w.setHumidity(currentWeather.getHumidity());
		w.setWindDirection(currentWeather.getWindDeg());
		w.setWindSpeed(currentWeather.getWindSpeed());
		w.setPressureState(PressureState.STEADY);
		w.setConditionCode(currentWeather.getWeatherConditions().get(0).getCode().getId());
		w.setSource(Source.OPENWEATHERMAP);
		w.setTimestamp(System.currentTimeMillis());

		return w;
	}

}
