package eirb.pg203.parser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import eirb.pg203.parser.DayForecast;
import eirb.pg203.parser.WeatherParser;
import eirb.pg203.parser.WeatherData;

public class OpenMeteoParser implements WeatherParser {

    @Override
    public WeatherData parse(JSONObject data, int nbDays) {
        try {

            /* System.out.println("OpenMeteo \n");
            System.out.println(data); */
            JSONObject daily = data.getJSONObject("daily");
            JSONArray dates = daily.getJSONArray("time");
            JSONArray minTemps = daily.getJSONArray("temperature_2m_min");
            JSONArray maxTemps = daily.getJSONArray("temperature_2m_max");
            JSONArray weatherCode = daily.getJSONArray("weathercode"); 
            JSONArray ventDirections = daily.getJSONArray("winddirection_10m_dominant"); 
            JSONArray ventVitesses = daily.getJSONArray("windspeed_10m_max"); 

            List<DayForecast> forecasts = new ArrayList<>();
            int limit = Math.min(nbDays, dates.length());
            for (int i = 0; i < limit; i++) {
                String date = dates.getString(i);
                double minTemp = minTemps.getDouble(i);
                double maxTemp = maxTemps.getDouble(i);
                String condition = getWeatherDescription(weatherCode.getInt(i)); 
                int ventDirection = ventDirections.getInt(i); 
                double ventVitesse = ventVitesses.getDouble(i); 
                

                forecasts.add(new DayForecast(
                    date,
                    maxTemp,
                    minTemp,
                    condition,
                    ventDirection,
                    ventVitesse
                ));
            }

            return new WeatherData(
                "OpenMeteo",
                "N/A",       
                "N/A",       
                "N/A",       
                forecasts   
            );

        } catch (Exception e) {
            throw new RuntimeException("Erreur lors du parsing des données OpenMeteo : " + e.getMessage(), e);
        }
    }

    

    @Override
    public String getWeatherDescription(int weatherCode) {
        switch (weatherCode) {
            case 0: return "Sunny"; // Clear sky
            case 1: return "Sunny"; // Mainly clear
            case 2: return "Partly Cloudy"; // Partly cloudy
            case 3: return "Overcast"; // Overcast
            case 45: return "Fog"; // Fog
            case 48: return "Fog"; // Depositing rime fog
            case 51: return "Light freezing rain"; // Light drizzle
            case 53: return "Moderate rain"; // Moderate drizzle
            case 55: return "Moderate rain"; // Dense drizzle
            case 56: return "Light freezing rain"; // Freezing drizzle
            case 57: return "Light freezing rain"; // Freezing drizzle
            case 61: return "Patchy rain nearby"; // Slight rain
            case 63: return "Moderate rain"; // Moderate rain
            case 65: return "Heavy rain"; // Heavy rain
            case 66: return "Light freezing rain"; // Freezing rain
            case 67: return "Heavy freezing rain"; // Heavy freezing rain
            case 71: return "Light snow"; // Light snow
            case 73: return "Heavy snow"; // Heavy snow
            case 75: return "Heavy snow"; // Very heavy snow
            case 77: return "Snow"; // Snow grains
            case 80: return "Rain"; // Rain showers
            case 81: return "Rain"; // Moderate rain showers
            case 82: return "Heavy rain"; // Violent rain showers
            case 85: return "Light snow"; // Light snow showers
            case 86: return "Heavy snow"; // Heavy snow showers
            case 95: return "Thunderstorm"; // Slight thunderstorm
            case 96: return "Thunderstorm"; // Thunderstorm with hail
            case 99: return "Thunderstorm"; // Thunderstorm with heavy hail
            default:
                return "Unknown weather condition";
        }
    }

}


    

