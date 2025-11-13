package eirb.pg203;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import eirb.pg203.display.DisplayService;
import eirb.pg203.parser.*;
import eirb.pg203.api.*;
import eirb.pg203.cache.*;

public class Main {

    public static void main(String[] args) throws IOException {
        if (args.length < 2) {
            System.out.println("usage : ./gradlew run --args='CITY_NAME DAYS'");
            return;
        }

        String city = args[0];
        int nbDays = Integer.parseInt(args[1]);

        // Initialisation du cache
        Cache cache = new Cache("cache.json", 86400 * 1000); // Expiration en secondes
        System.out.println("Contenu initial du cache : " + cache.getCacheList().size() + " éléments.");


        try {
            // api utilisées et leurs services associés
            String[] apiNames = {"WeatherApi", "OpenMeteo", "OpenWeather"};
            WeatherService[] weatherServices = {
                new WeatherService(new WeatherApi(), new WeatherApiParser()),
                new WeatherService(new OpenMeteo(), new OpenMeteoParser()),
                new WeatherService(new OpenWeather(), new OpenWeatherParser())
            };

            List<WeatherData> weatherDataList = new ArrayList<>();
            DisplayService displayService = new DisplayService();

            // On boucle sur les services pour recup les données pour chaque service, c'est ici qu'on teste si c'est déjà dans le cache ou non
            for (int i = 0; i < weatherServices.length; i++) {

                // on regarde si cette recherche a déjà été faite recemment, si oui on recupère cette recherche
                CacheElement cachedElement = cache.isInCache(city, apiNames[i]); 
                
                WeatherData weatherData;
                
                if (cachedElement != null) { // Si trouvé on recup les données
                    weatherData = cachedElement.getData();
                } else { // Si pas trouvé on fetch les données depuis les api les met dans un cacheElement et on l'ajoute au cache. 
                    weatherData = weatherServices[i].fetchWeatherData(city, nbDays);
                    CacheElement cacheElement = new CacheElement(city, apiNames[i], System.currentTimeMillis());
                    cacheElement.setData(weatherData);
                    cache.addToCache(cacheElement);
                }
                weatherDataList.add(weatherData);
                
                // Affichage dans le terminal
                System.out.println("\n" + apiNames[i] + " :\n");
                displayService.displayWeatherDataTerminal(weatherData);
            }

            // Affichage dans le HTML
            displayService.displayWeatherDataHTML(weatherDataList, "src/main/ressources/index.html", "src/main/ressources/template.html");

        } catch (IOException e) {
            System.out.println("Failed to get the weather data: " + e.getMessage());
        }
    }
}
