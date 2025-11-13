package eirb.pg203.api;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.json.JSONArray;
import org.json.JSONObject;

public class GetCoordinates {
    private String city;
    private final String API_KEY;

    public GetCoordinates(String city, String API_KEY) {
        this.city = city;
        this.API_KEY = API_KEY;
    }

    //utile pour les tests
    public String getCity() {
        return city;
    }
    

    public double[] getCoordinates() {
        double[] coordinates = new double[2];
        HttpURLConnection conn = null;
        StringBuilder result = new StringBuilder();

        try {
            // Encoder la ville pour l'URL
            String encodedCity = URLEncoder.encode(city, StandardCharsets.UTF_8.toString());
            URI uri = new URI("http://api.openweathermap.org/geo/1.0/direct?q=" + encodedCity + "&appid=" + API_KEY);
            URL url = uri.toURL();

            // Ouvrir la connexion HTTP
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            // Lire la réponse
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
            }

            // Parser la réponse JSON
            JSONArray jsonArray = new JSONArray(result.toString());
            if (jsonArray.length() > 0) {
                JSONObject firstResult = jsonArray.getJSONObject(0);
                coordinates[0] = firstResult.getDouble("lat");
                coordinates[1] = firstResult.getDouble("lon");
            } else {
                System.out.println("No results found for the city: " + city);
                return null;
            }

        } catch (URISyntaxException e) {
            System.out.println("URI syntax error: " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.out.println("Error while fetching coordinates: " + e.getMessage());
            return null;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        return coordinates;
    }
}

