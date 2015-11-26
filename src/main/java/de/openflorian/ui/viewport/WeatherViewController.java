package de.openflorian.ui.viewport;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Timer;

import com.github.fedy2.weather.YahooWeatherService;
import com.github.fedy2.weather.data.Channel;
import com.github.fedy2.weather.data.unit.BarometricPressureState;
import com.github.fedy2.weather.data.unit.DegreeUnit;

import de.openflorian.ui.ZkGlobals;
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
	private Label visibility;
	private Image rising;
	private Label windSpeed;
	private Label windDirection;
	private Label windChill;
	
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
			weather.setValue(c.getDescription());
			temperature.setValue(c.getItem().getCondition().getTemp() + " °C");
			condition.setValue(c.getItem().getCondition().getText());
			humidity.setValue(String.valueOf(c.getAtmosphere().getHumidity()) + " %");
			pressure.setValue(String.valueOf(c.getAtmosphere().getPressure()) + " hPa");
			visibility.setValue(String.valueOf(c.getAtmosphere().getVisibility()));
			
			if(c.getAtmosphere().getRising() == BarometricPressureState.RISING) {
				rising.setSrc(ZkGlobals.ICON_SMALL_UP);
			} else if(c.getAtmosphere().getRising() == BarometricPressureState.FALLING) {
				rising.setSrc(ZkGlobals.ICON_SMALL_DOWN);
			} else if(c.getAtmosphere().getRising() == BarometricPressureState.STEADY) {
				rising.setSrc(ZkGlobals.ICON_SMALL_STEADY);
			}
			
			windSpeed.setValue(c.getWind().getSpeed() + " km/h");
			windDirection.setValue(String.valueOf(c.getWind().getDirection()));
			windChill.setValue(String.valueOf(c.getWind().getChill()) + " °C");

			currentTime.setValue(new SimpleDateFormat(ZkGlobals.FORMAT_DATETIME).format(new Date()));
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}
	
}
