package eirb.pg203.parser;

import java.util.List;

import eirb.pg203.cache.CacheElement;


public class WeatherData {
    public final String apiName; 
    private final String city;
    private final String region;
    private final String country; 
    private final List<DayForecast> forecasts;

    public WeatherData(String apiName,String city,String region, String country, List<DayForecast> forecasts) {
        this.apiName = apiName; 
        this.city = city;
        this.region = region;
        this.country = country; 
        this.forecasts = forecasts;
    }

    public String getApiName(){
        return apiName; 
    }

    public String getCity() {
        return city;
    }

    public String getRegion() {
        return region;
    }

    public String getCountry() {
        return country;
    }

    public List<DayForecast> getForecasts() {
        return forecasts;
    }
}

