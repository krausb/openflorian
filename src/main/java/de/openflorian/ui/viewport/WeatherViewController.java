package de.openflorian.ui.viewport;

/*
 * This file is part of Openflorian.
 * 
 * Copyright (C) 2015  Bastian Kraus
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

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Label;
import org.zkoss.zul.Timer;

import com.github.fedy2.weather.YahooWeatherService;
import com.github.fedy2.weather.data.Channel;
import com.github.fedy2.weather.data.unit.BarometricPressureState;
import com.github.fedy2.weather.data.unit.DegreeUnit;

import de.openflorian.ui.ZkGlobals;
import de.openflorian.util.YahooWeatherConditionconditions;
import de.openflorian.zk.AbstractGuiController;

/**
 * Operation View Controller<br/>
 * <Br/>
 * ZUL: webapp/operation/viewport.zul
 * 
 * @author Bastian Kraus <me@bastian-kraus.me>
 */
public class WeatherViewController extends AbstractGuiController {
	private static final long serialVersionUID = 3698439925438248515L;

	private Timer refreshTimer;
	private Label weather;
	
	private Label temperature;
	private Label condition;
	private Label humidity;
	private Label pressure;
	private Label rising;
	private Label windSpeed;
	private Label windDirection;
	
	private Label currentTime;
	
	@Override
	public String getLoginPage() {
		return ZkGlobals.OPERATION_VIEWPORT;
	}
	
	@Override
	public boolean mustBeLoggedIn() {
		return false;
	}
	
	@Override
	public boolean mayView(Principal user) {
		return true;
	}
	
	@Override
    public void doAfterCompose(Component comp) throws Exception {
    	super.doAfterCompose(comp);
    	
    	refreshTimer.addEventListener("onTimer", new EventListener<Event>() {
			@Override
			public void onEvent(Event event) throws Exception {
				printWeather();
			}
		});
    	
    	printWeather();
    }
	
	private void printWeather() {
		try {
			YahooWeatherService service = new YahooWeatherService();
			
			Channel c = service.getForecast("12836980", DegreeUnit.CELSIUS);
			
			temperature.setValue(c.getItem().getCondition().getTemp() + " °C");
			condition.setSclass("wi weather-icon " + YahooWeatherConditionconditions.map(c.getItem().getCondition().getCode()));
			humidity.setValue(String.valueOf(c.getAtmosphere().getHumidity()) + " %");
			pressure.setValue(String.valueOf(c.getAtmosphere().getPressure()) + " hPa");
			
			if(c.getAtmosphere().getRising() == BarometricPressureState.RISING) {
				rising.setSclass("wi pressure wi-direction-up-right");
			} else if(c.getAtmosphere().getRising() == BarometricPressureState.FALLING) {
				rising.setSclass("wi pressure wi-direction-down-right");
			} else if(c.getAtmosphere().getRising() == BarometricPressureState.STEADY) {
				rising.setSclass("wi pressure wi-direction-right");
			}
			
			windSpeed.setValue(c.getWind().getSpeed() + " km/h");
			windDirection.setSclass("wi windspeed wi-wind towards-" + c.getWind().getDirection() + "-deg");

			currentTime.setValue(new SimpleDateFormat(ZkGlobals.FORMAT_DATETIME).format(new Date()));
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}
	
}
