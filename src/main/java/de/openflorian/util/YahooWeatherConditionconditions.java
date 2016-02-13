package de.openflorian.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Mapping Factory<br/>
 * <br/>
 * Maps yahoo weather api condition codes to 
 * weather icon css classes.<br/>
 * <br/>
 * See: {@link https://developer.yahoo.com/weather/documentation.html#codes}
 * @author Bastian Kraus <me@bastian-kraus.me>
 */
public class YahooWeatherConditionconditions {
	
	private static Map<Integer,String> conditions;
	
	public static String map(int conditionCode) {
		if(conditions == null) initMapping();
		return conditions.get(conditionCode);
	}
	
	/**
	 * Helper: Initialize mapping for weather conditions
	 */
	private static void initMapping() {
		conditions = new HashMap<Integer, String>();
		conditions.put(0,	"wi-tornado");
		conditions.put(1,	"wi-hurricane");
		conditions.put(2,	"wi-hurricane");
		conditions.put(3,	"wi-thunderstorm");
		conditions.put(4,	"wi-thunderstorm");
		conditions.put(5,	"wi-sleet");
		conditions.put(6,	"wi-sleet");
		conditions.put(7,	"wi-sleet");
		conditions.put(8,	"wi-sleet");
		conditions.put(9,	"wi-sleet wi-snowflake-cold");
		conditions.put(10,	"wi-showers wi-snowflake-cold");
		conditions.put(11,	"wi-showers");
		conditions.put(12,	"wi-showers");
		conditions.put(13,	"wi-strong-wind wi-snow");
		conditions.put(14,	"wi-snow");
		conditions.put(15,	"wi-strong-wind wi-snow");
		conditions.put(16,	"wi-snow");
		conditions.put(17,	"wi-hail");
		conditions.put(18,	"wi-sleet");
		conditions.put(19,	"wi-dust");
		conditions.put(20,	"wi-dust");
		conditions.put(21,	"wi-dust");
		conditions.put(22,	"wi-smog");
		conditions.put(23,	"wi-cloudy-gusts");
		conditions.put(24,	"wi-strong-wind");
		conditions.put(25,	"wi-snowflake-cold");
		conditions.put(26,	"wi-cloudy");
		conditions.put(27,	"wi-night-alt-cloudy");
		conditions.put(28,	"wi-day-cloudy");
		conditions.put(29,	"wi-night-alt-cloudy");
		conditions.put(30,	"wi-day-cloudy");
		conditions.put(31,	"wi-stars");
		conditions.put(32,	"wi-day-sunny");
		conditions.put(33,	"wi-night-clear");
		conditions.put(34,	"wi-day-sunny");
		conditions.put(35,	"wi-hail");
		conditions.put(36,	"wi-thermometer");
		conditions.put(37,	"wi-storm-showers");
		conditions.put(38,	"wi-storm-showers");
		conditions.put(39,	"wi-storm-showers");
		conditions.put(40,	"wi-showers");
		conditions.put(41,	"wi-snow-wind");
		conditions.put(42,	"wi-snow");
		conditions.put(43,	"wi-snow");
		conditions.put(44,	"wi-cloudy");
		conditions.put(45,	"wi-storm-showers");
		conditions.put(46,	"wi-snow");
		conditions.put(47,	"wi-storm-showers");
	}
	
}
