package eirb.pg203.api;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;

public class OpenMeteo extends GetDataFromApi{
    private static String API_KEY;

    public OpenMeteo() {
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
                "https://api.open-meteo.com/v1/forecast?" +
                "latitude=" + coordinates[0] + 
                "&longitude=" + coordinates[1] + 
                "&daily=temperature_2m_min,temperature_2m_max,windspeed_10m_max,winddirection_10m_dominant,weathercode" +
                "&timezone=auto"
            );
            return uri.toURL();
        } catch (URISyntaxException e) {
            System.out.println("URI syntax error: " + e.getMessage());
            return null;
        }
    }

    
}