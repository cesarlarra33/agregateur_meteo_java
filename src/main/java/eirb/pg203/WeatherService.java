package eirb.pg203;

import org.json.JSONObject;

import java.io.IOException;

import eirb.pg203.api.*;
import eirb.pg203.parser.*;

public class WeatherService {                                                               //Provide a service to fetch weather data from an API
    private final GetDataFromApi getdatafromAPI;
    private final WeatherParser parser;

    
    public WeatherService(GetDataFromApi weatherAPI, WeatherParser parser) { 
        this.getdatafromAPI = weatherAPI;
        this.parser = parser;

    }

    public WeatherData fetchWeatherData(String city, int nbDays) throws IOException {
        JSONObject jsonData = getdatafromAPI.getWeatherData(city, nbDays);
        return parser.parse(jsonData,nbDays);
    }
    
}
