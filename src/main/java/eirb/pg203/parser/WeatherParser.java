package eirb.pg203.parser;

import org.json.JSONObject;

public interface WeatherParser {
    WeatherData parse(JSONObject data, int nbDays);

    String getWeatherDescription(int weatherCode); 
}
