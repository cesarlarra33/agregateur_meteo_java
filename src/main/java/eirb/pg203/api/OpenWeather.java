
package eirb.pg203.api;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;

public class OpenWeather extends GetDataFromApi{
    private static String API_KEY;

    public OpenWeather() {
        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream(".config")) {
            properties.load(fis);
            API_KEY = properties.getProperty("API_KEY_OPENWEATHER");
            if (API_KEY == null || API_KEY.isEmpty()) {
                throw new IllegalStateException("Clé API manquante dans le fichier .config");
            }
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors du chargement du fichier .config : " + e.getMessage());
        }
    }

    

    public URL getURL(String city, int nbDays) throws IOException {
        GetCoordinates getCoordinates = new GetCoordinates(city, API_KEY);
        double coordinates[] = getCoordinates.getCoordinates();
        try {
            URI uri = new URI(
                "https://api.openweathermap.org/data/2.5/forecast?" +
                "lat=" + coordinates[0] +
                "&lon=" + coordinates[1] +
                "&appid=" + API_KEY +
                "&cnt=" + (nbDays * 8) +  // fournit des prévisions toutes les 3h, donc 8 par jour
                "&units=metric"          // T° en Celsius
            );
            return uri.toURL();
        } catch (URISyntaxException e) {
            System.out.println("URI syntax error: " + e.getMessage());
            return null;
        }
    }

    
}