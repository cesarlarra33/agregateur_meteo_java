package eirb.pg203.parser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import eirb.pg203.parser.DayForecast;
import eirb.pg203.parser.WeatherParser;
import eirb.pg203.parser.WeatherData;

public class OpenWeatherParser implements WeatherParser {

    @Override
    public WeatherData parse(JSONObject data, int nbDays) {
        try {
            String city = data.getJSONObject("city").getString("name");
            String country = data.getJSONObject("city").getString("country");
            String region = "N/A"; 

            
            List<DayForecast> forecasts = new ArrayList<>();

            
            JSONArray list = data.getJSONArray("list");
            for (int i = 0; i < list.length(); i += 8) { // l'api fournit des report de toutes les 3h donc on en prend 1/8 pour faire une journée
                JSONObject day = list.getJSONObject(i);

                //extraire les données pour chaque jour
                String date = day.getString("dt_txt").split(" ")[0];
                double minTemp = day.getJSONObject("main").getDouble("temp_min");
                double maxTemp = day.getJSONObject("main").getDouble("temp_max");

                int weatherCode = day.getJSONArray("weather").getJSONObject(0).getInt("id");
                String condition = getWeatherDescription(weatherCode);  // Utilisation de la méthode getWeatherDescription

                int ventDirection = day.getJSONObject("wind").getInt("deg"); 
                double ventVitesse = Math.round(day.getJSONObject("wind").getDouble("speed")*3.6*10)/10.0 ; // x3.6 pour passer des m/s en km/h 

                // Ajouter la prévision à la liste
                forecasts.add(new DayForecast(date, maxTemp, minTemp, condition, ventDirection, ventVitesse));
            }

            // Construire l'objet WeatherData
            return new WeatherData("OpenWeather",city, region, country, forecasts);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors du parsing des données OpenWeather : " + e.getMessage(), e);
        }
    }

    @Override
    public String getWeatherDescription(int weatherCode) {
        // Utilisation des codes météorologiques pour retourner les descriptions
        switch (weatherCode) {
            case 800: return "Sunny"; // Clear sky
            case 801: return "Partly Cloudy"; // Few clouds
            case 802: return "Partly Cloudy"; // Scattered clouds
            case 803: return "Cloudy"; // Broken clouds
            case 804: return "Overcast"; // Overcast clouds
            case 500: return "Patchy rain nearby"; // Light rain
            case 501: return "Moderate rain"; // Moderate rain
            case 502: return "Heavy rain"; // Heavy rain
            case 503: return "Heavy rain"; // Very heavy rain
            case 504: return "Heavy rain"; // Extreme rain
            case 300: return "Light drizzle"; // Light drizzle
            case 301: return "Light drizzle"; // Drizzle
            case 302: return "Moderate rain"; // Heavy drizzle
            case 600: return "Light snow"; // Light snow
            case 601: return "Snow"; // Snow
            case 602: return "Heavy snow"; // Heavy snow
            case 701: return "Fog"; // Mist
            case 711: return "Fog"; // Smoke
            case 721: return "Fog"; // Haze
            case 731: return "Fog"; // Dust
            case 741: return "Fog"; // Fog
            case 751: return "Fog"; // Sand
            case 761: return "Fog"; // Dust
            case 762: return "Fog"; // Volcanic ash
            case 771: return "Thunderstorm"; // Squalls
            case 781: return "Thunderstorm"; // Tornado
            case 200: return "Thunderstorm"; // Thunderstorm with light rain
            case 201: return "Thunderstorm"; // Thunderstorm with rain
            case 202: return "Thunderstorm"; // Thunderstorm with heavy rain
            case 210: return "Thunderstorm"; // Light thunderstorm
            case 211: return "Thunderstorm"; // Thunderstorm
            case 212: return "Thunderstorm"; // Heavy thunderstorm
            case 221: return "Thunderstorm"; // Ragged thunderstorm
            case 230: return "Thunderstorm"; // Thunderstorm with light drizzle
            case 231: return "Thunderstorm"; // Thunderstorm with drizzle
            case 232: return "Thunderstorm"; // Thunderstorm with heavy drizzle
            default: return "Unknown weather condition";
        }
    }

}
