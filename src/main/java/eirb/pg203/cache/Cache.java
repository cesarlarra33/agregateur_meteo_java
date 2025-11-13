package eirb.pg203.cache; 

import org.json.JSONArray;
import org.json.JSONObject;
import eirb.pg203.parser.DayForecast;
import eirb.pg203.parser.WeatherData;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Cache {
    private String cacheFilePath;
    private List<CacheElement> cacheList;
    long expirationTime_ms; 

    

    public Cache(String cacheFilePath, long expirationTime_ms) {
        this.cacheFilePath = cacheFilePath;
        System.out.println("Chemin du fichier cache : " + new java.io.File(cacheFilePath).getAbsolutePath());
    
        // Vérifie si le fichier existe
        if (!new java.io.File(cacheFilePath).exists()) {
            System.out.println("Fichier cache.json non trouvé, un nouveau sera créé.");
        } else {
            System.out.println("Fichier cache.json trouvé.");
        }
        this.expirationTime_ms = expirationTime_ms; 
        this.cacheList = loadCache(expirationTime_ms); 
    }

    public List<CacheElement> getCacheList() {
        return cacheList;
    }

    private List<CacheElement> loadCache(long expirationTime_ms) {
        List<CacheElement> cacheElements = new ArrayList<>();
        int expiredEntriesCount = 0;
        System.out.println("Temps d'epiration des prévisions : " + expirationTime_ms + " secondes.");
    
        try (BufferedReader reader = new BufferedReader(new FileReader(cacheFilePath))) {
            StringBuilder jsonContent = new StringBuilder();
            String line;
    
            while ((line = reader.readLine()) != null) {
                jsonContent.append(line);
            }
    
            if (jsonContent.length() == 0) {
                System.out.println("Fichier cache.json est vide. Aucun élément chargé.");
                return cacheElements;
            }
    
            JSONArray cacheArray = new JSONArray(jsonContent.toString());
    
            for (int i = 0; i < cacheArray.length(); i++) {
                JSONObject jsonObject = cacheArray.getJSONObject(i);
                CacheElement element = jsonToCacheElement(jsonObject);
    

                if (element.isStillValid(expirationTime_ms)) {
                    cacheElements.add(element);
                } else {
                    System.out.println("Entrée expirée supprimée : " + element.getCity() + " via API " + element.getApi());
                    expiredEntriesCount++;
                }
            }
    
            System.out.println("Cache chargé depuis cache.json : " + cacheElements.size() + " éléments valides.");
            System.out.println("Nombre d'entrées expirées : " + expiredEntriesCount);
    
        } catch (IOException e) {
            System.out.println("Erreur lors de la lecture du cache : " + e.getMessage());
            e.printStackTrace();
        }
    
        // Sauvegarde le cache après suppression des expirés
        saveCleanedCache(cacheElements);
        return cacheElements;
    }
    
    private void saveCleanedCache(List<CacheElement> cacheElements) {
    JSONArray jsonArray = new JSONArray();

    for (CacheElement element : cacheElements) {
        JSONObject jsonObject = cacheElementToJson(element);
        jsonArray.put(jsonObject);
    }

    try (FileWriter writer = new FileWriter(cacheFilePath)) {
        writer.write(jsonArray.toString(4)); 
        System.out.println("Cache nettoyé et sauvegardé : " + cacheElements.size() + " éléments restants.");
    } catch (IOException e) {
        System.out.println("Erreur lors de la sauvegarde du cache nettoyé : " + e.getMessage());
        e.printStackTrace();
    }
}

    

    // Convertit un JSONObject lu dans cache.json en un Objet Cache Element
    public CacheElement jsonToCacheElement(JSONObject jsonObject) {
        String city = jsonObject.getString("city");
        String api = jsonObject.getString("api");
        long timestamp = jsonObject.getLong("timestamp");
    
        WeatherData data = null; 
        if (jsonObject.has("data")) { 
            JSONObject dataObject = jsonObject.getJSONObject("data");
            data = parseWeatherDataFromJSON(dataObject); 
        } else {
            System.out.println("Champ 'data' manquant dans l'objet : " + jsonObject.toString());
        }
    
        CacheElement element = new CacheElement(city, api, timestamp);
        element.setData(data); 
        return element;
    }


    

    // Convertit un objet json data lu dans un json object lu dans cache.json en Object weather data pret à etre l'attribut data d'un objet CachrElement
    private WeatherData parseWeatherDataFromJSON(JSONObject json) {
        String apiName = json.getString("apiName");
        String city = json.getString("city");
        String region = json.getString("region");
        String country = json.getString("country");

        List<DayForecast> forecasts = new ArrayList<>();
        JSONArray forecastsArray = json.getJSONArray("forecasts");
        for (int i = 0; i < forecastsArray.length(); i++) {
            JSONObject forecastJson = forecastsArray.getJSONObject(i);
            forecasts.add(parseDayForecastFromJSON(forecastJson));
        }

        return new WeatherData(apiName, city, region, country, forecasts);
    }

    // idem mais pour dayForcecast
    private DayForecast parseDayForecastFromJSON(JSONObject json) {
        String date = json.getString("date");
        double maxTemp = json.getDouble("maxTemp");
        double minTemp = json.getDouble("minTemp");
        String condition = json.getString("condition"); 
        int ventDirection = json.getInt("ventDirection"); 
        double ventVitesse = json.getDouble("ventVitesse"); 

        return new DayForecast(date, maxTemp, minTemp, condition, ventDirection, ventVitesse); 
    }

    // Cherche si element correspond à un elt du cache si oui retourne l'element correspondant, si non, null. Le cherche en fonction de l'api la ville et s'il est pas expiré
    public CacheElement isInCache(String city, String apiName) {
        for (int i = 0; i < cacheList.size(); i++) {
            CacheElement cachedElement = cacheList.get(i);
            
            if (cachedElement.getCity().equals(city) &&
                cachedElement.getApi().equals(apiName) && 
                cachedElement.isStillValid(expirationTime_ms))
            { 
                
                System.out.println("\n        Prévision trouvée dans le cache à l'indice : " + i);
                return cachedElement;
            }
        }
        System.out.println("\n        Prévision non trouvée dans le cache");
        return null;
    }
    



    // Ajoute un element au cache java (pas json)
    public void addToCache(CacheElement element) {
        if (element.getData() == null) {
            throw new IllegalArgumentException("Cannot add CacheElement with null data to cache");
        }
    
        cacheList.add(element);
        System.out.println("Nouvel élément ajouté au cache : " + element.getCity() + " via API " + element.getApi());
        saveCache();
    }
    

    // Save dans le cache json l'état actuel du cache java
    // ==> Il faudra faire les conversions d'objets dans l'autre sens du coup
    private void saveCache() {
        JSONArray jsonArray = new JSONArray();
        if (cacheList != null) {
            for (CacheElement element : cacheList) {
                JSONObject jsonObject = cacheElementToJson(element);
                jsonArray.put(jsonObject);
            }
        }
    
        try (java.io.FileWriter writer = new java.io.FileWriter(cacheFilePath)) {
            writer.write(jsonArray.toString(4)); // Format JSON avec indentation
            System.out.println("Cache sauvegardé dans cache.json : " + cacheList.size() + " éléments.");
        } catch (IOException e) {
            System.out.println("Erreur lors de la sauvegarde dans cache.json : " + e.getMessage());
            e.printStackTrace();
        }
    }

    // convertit unu Cache Element en JsonObject
    public JSONObject cacheElementToJson(CacheElement element) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("city", element.getCity());
        jsonObject.put("api", element.getApi());
        jsonObject.put("timestamp", element.getTimestamp());

        if (element.getData() != null) {
            jsonObject.put("data", weatherDataToJson((WeatherData) element.getData()));
        }

        return jsonObject;
    }

    // convertit uns WeatherData en JSONObject
    private JSONObject weatherDataToJson(WeatherData data) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("apiName", data.getApiName());
        jsonObject.put("city", data.getCity());
        jsonObject.put("region", data.getRegion());
        jsonObject.put("country", data.getCountry());

        JSONArray forecastsArray = new JSONArray();
        for (DayForecast forecast : data.getForecasts()) {
            forecastsArray.put(dayForecastToJson(forecast));
        }
        jsonObject.put("forecasts", forecastsArray);

        return jsonObject;
    }

    // conveetit DayForecast en JSONObject
    private JSONObject dayForecastToJson(DayForecast forecast) {
        JSONObject jsonObject = new JSONObject();
        
        
        jsonObject.put("date", forecast.getDate());
        jsonObject.put("maxTemp", forecast.getMaxTemp());  
        jsonObject.put("minTemp", forecast.getMinTemp());  
        jsonObject.put("condition", forecast.getCondition()); 
        jsonObject.put("ventDirection", forecast.getVentDirection()); 
        jsonObject.put("ventVitesse", forecast.getVentVitesse());  
        
        return jsonObject;
    }
}
