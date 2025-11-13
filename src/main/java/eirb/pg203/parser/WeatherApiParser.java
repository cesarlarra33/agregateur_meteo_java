package eirb.pg203.parser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import eirb.pg203.parser.DayForecast;
import eirb.pg203.parser.WeatherParser;
import eirb.pg203.parser.WeatherData;

public class WeatherApiParser implements WeatherParser {
    @Override
    public WeatherData parse(JSONObject data, int nbDays) {
        try {
            String city = data.getJSONObject("location").getString("name");
            String region = data.getJSONObject("location").getString("region");
            String country = data.getJSONObject("location").getString("country");

            JSONArray forecastDays = data.getJSONObject("forecast").getJSONArray("forecastday");
            List<DayForecast> forecasts = new ArrayList<>();

            for (int i = 0; i < nbDays; i++) {
                if(i<forecastDays.length() ){
                    JSONObject day = forecastDays.getJSONObject(i);

                    forecasts.add(new DayForecast(
                        day.getString("date"),
                        day.getJSONObject("day").getDouble("maxtemp_c"),
                        day.getJSONObject("day").getDouble("mintemp_c"),
                        day.getJSONObject("day").getJSONObject("condition").getString("text"),
                        

                        day.getJSONArray("hour").getJSONObject(LocalTime.now().getHour()).getInt("wind_degree"),
                        day.getJSONArray("hour").getJSONObject(LocalTime.now().getHour()).getDouble("wind_kph")
                    ));
                } else {
                    forecasts.add(new DayForecast(
                        " X ",
                        0.0,
                        0.0,
                        "Weather Api ne<br>fournit que 3 jours",
                        

                        0,
                        0.0
                    ));
                }
            }

            return new WeatherData("WeatherApi",city, region, country, forecasts);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors du parsing des données WeatherApi : " + e.getMessage(), e);
        }
    }

    // Je l'implémente parce que sinon ca compile pas mais on en a pas besoin vu que cette api renvoie pas de code mais direct une description
    @Override
    public String getWeatherDescription(int weatherCode) {
        return ""; 
    }
}
