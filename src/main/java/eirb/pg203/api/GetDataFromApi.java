package eirb.pg203.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;

//Interface des fonctions que doivent implémenter les classes propres à Chaque Api 
public abstract class GetDataFromApi {

    public GetDataFromApi() {}

    public abstract URL getURL(String city, int nbDays) throws IOException;

    public JSONObject getWeatherData(String city, int nbDays) throws IOException {
            URL url = getURL(city, nbDays);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            StringBuilder result = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }        
            }
            return new JSONObject(result.toString());    
    }
}



