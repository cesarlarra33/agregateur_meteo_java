package eirb.pg203.api;


import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Properties;



public class WeatherApi extends GetDataFromApi {

    private String API_KEY;
    
    public WeatherApi() {
        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream(".config")) {
            properties.load(fis);
            this.API_KEY = properties.getProperty("API_KEY_WeatherAPI");
            if (API_KEY == null || API_KEY.isEmpty()) {
                throw new IllegalStateException("Clé API manquante dans le fichier .config");
            }
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors du chargement du fichier .config : " + e.getMessage());
        }
    }

    @Override
    public URL getURL(String city, int nbDays ) throws IOException {
        try {
            String encodedCity = URLEncoder.encode(city, StandardCharsets.UTF_8.toString());
            URI uri = new URI("http://api.weatherapi.com/v1/forecast.json?key=" + API_KEY + "&q=" + encodedCity + "&days=" + nbDays+"&aqi=no&alerts=no");
            return uri.toURL();

        }   catch (URISyntaxException e) {
            System.out.println("URI syntax error: " + e.getMessage());
            return null;
        }
    }
    
}
