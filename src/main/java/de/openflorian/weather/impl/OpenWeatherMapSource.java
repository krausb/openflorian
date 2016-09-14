package de.openflorian.weather.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.openflorian.config.OpenflorianConfig;
import de.openflorian.weather.Weather;
import de.openflorian.weather.Weather.PressureState;
import de.openflorian.weather.Weather.Source;
import de.openflorian.weather.WeatherSource;
import net.aksingh.owmjapis.CurrentWeather;
import net.aksingh.owmjapis.OpenWeatherMap;
import net.aksingh.owmjapis.OpenWeatherMap.Units;

public class OpenWeatherMapSource implements WeatherSource {

	private static final Logger log = LoggerFactory.getLogger(OpenWeatherMapSource.class);

	private final OpenWeatherMap owm = new OpenWeatherMap(Units.METRIC,
			OpenflorianConfig.config().weather.openWeatherMapApi.apiKey);
	private final long cityId = OpenflorianConfig.config().weather.openWeatherMapApi.cityId;

	@Override
	public Weather getCurrentWeather() throws Exception {

		final CurrentWeather currentWeather = owm.currentWeatherByCityCode(cityId);

		final Weather w = new Weather();

		// temperature from kelvin to celsius
		w.setTemperature(currentWeather.getMainInstance().getTemperature());
		w.setPressure(currentWeather.getMainInstance().getPressure());
		w.setHumidity(currentWeather.getMainInstance().getHumidity());
		if (currentWeather.getWindInstance().getWindDegree() != Float.NaN)
			w.setWindDirection(Math.round(currentWeather.getWindInstance().getWindDegree()));
		else
			w.setWindDirection(0);
		w.setWindSpeed(currentWeather.getWindInstance().getWindSpeed());
		w.setPressureState(PressureState.STEADY);
		if (currentWeather.getWeatherCount() > 0)
			w.setConditionCode(currentWeather.getWeatherInstance(0).hasWeatherCode()
					? currentWeather.getWeatherInstance(0).getWeatherCode() : Integer.MIN_VALUE);
		w.setSource(Source.OPENWEATHERMAP);
		w.setTimestamp(System.currentTimeMillis());

		return w;
	}

}
