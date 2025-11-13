package eirb.pg203.cache;

import eirb.pg203.parser.WeatherData;

public class CacheElement {
    private String city;
    private String api;
    private long timestamp;
    private WeatherData data;

    public CacheElement(String city, String api, long timestamp) {
        this.city = city;
        this.api = api;
        this.timestamp = timestamp;
    }

    public String getCity() {
        return city;
    }

    public String getApi() {
        return api;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public WeatherData getData() {
        return data;
    }

    public void setData(WeatherData data){
        this.data = data; 
    }


    public boolean isStillValid(long expirationTime_s) {
        long currentTime = System.currentTimeMillis();
        long ageInSeconds = (currentTime - timestamp) / 1000; // age en secondes
        System.out.println("Vérification de validité : âge de l'élément = " + ageInSeconds + " secondes.");
        return ageInSeconds <= expirationTime_s; // comparaison en secondes
    }
}
