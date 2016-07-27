package de.openflorian.weather.impl;

/*
 * This file is part of Openflorian.
 * 
 * Copyright (C) 2016  Bastian Kraus
 * 
 * Openflorian is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version)
 *     
 * Openflorian is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *     
 * You should have received a copy of the GNU General Public License
 * along with Openflorian.  If not, see <http://www.gnu.org/licenses/>.
 */

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

	private final OwmClient owm = new OwmClient();

	@Override
	public Weather getCurrentWeather() throws Exception {
		owm.setAPPID(OpenflorianConfig.config().weather.openWeatherMapApi.apiKey);

		final Weather w = new Weather();

		final StatusWeatherData currentWeather = owm
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
