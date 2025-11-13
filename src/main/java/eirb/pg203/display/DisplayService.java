package eirb.pg203.display;

import java.util.List;

import eirb.pg203.parser.DayForecast;
import eirb.pg203.parser.WeatherData;

public class DisplayService {
    public void displayWeatherDataTerminal(WeatherData data) {
        System.out.println("City: " + data.getCity());
        System.out.println("Region: " + data.getRegion());
        System.out.println("Pays: " + data.getCountry());
        System.out.println("Weather forecast for the next " + data.getForecasts().size() + " days:");
        
        for (DayForecast forecast : data.getForecasts()) {
            System.out.println("\nDate: " + forecast.getDate());
            System.out.println("Max Temperature: " + forecast.getMaxTemp() + "°C");
            System.out.println("Min Temperature: " + forecast.getMinTemp() + "°C");
            System.out.println("Condition: " + forecast.getCondition());
            System.out.println("Directiont vent: "+ forecast.getVentDirection()+ "°");
            System.out.println("Vitesse vent: "+ forecast.getVentVitesse() + "km/h");
        }
    }

    public void displayWeatherDataHTML(List<WeatherData> weatherDataList, String filepath, String fileTemplate) {
        // On reset le fichier des modfis des executions precedentes
        ModifHTML.resetHtmlFile(filepath, fileTemplate); 
        for (WeatherData data : weatherDataList) {
            //On recup les données de ville pays et region pour WeatherApi uniquement 
            if(data.apiName.equals("WeatherApi")){

    

                //Modif pour ville region pays et nb de jours
                String city = data.getCity();
                String region = data.getRegion();
                String country = data.getCountry(); 

                

                new ModifHTML(filepath, "Ville : <span id='ville'>" + city + "</span>", "pville");
                new ModifHTML(filepath, "Region :  <span>" + region + "</span>", "pregion");
                new ModifHTML(filepath, "Pays :   <span>" + country + "</span>", "ppays");
            }
            
            int size = data.getForecasts().size();  
            for (int i = size - 1; i >= 0; i--) {
                DayForecast forecast = data.getForecasts().get(i);
                String dateLabel;

                if (i == 0) {
                    dateLabel = "Aujourd'hui";
                } else if (i == 1) {
                    dateLabel = "Demain";
                } else {
                    dateLabel = "Date: " + forecast.getDate();
                } 
                int directionToDisplay = forecast.getVentDirection() - 180; 
                String infocontent = "<div class='infobox fade-div'>" +
                "<div class='date data'>" + dateLabel + "</div>" +
                "<div class ='tempventcontainer'>" +
                    "<div class='tempcontainer'>" +
                        "<div class='TMax data'>  Max T°: <span>" + forecast.getMaxTemp() + "°C</span></div>" +
                        "<div class='TMin data'>  Min T°: <span>" + forecast.getMinTemp() + "°C</span></div>" +
                    "</div>" +
                    "<div class='ventcontainer'>" +
                        "<div class='wind-direction' style='transform: rotate("+ directionToDisplay+"deg);'>" +
                            "<div class='arrow'></div>" +
                        "</div>" +
                        "<span class='wind-speed'>" + forecast.getVentVitesse() + " km/h</span>" +
                    "</div>" +
                "</div>" +
                "<div class='condition data'>Condition: <span><br>" + forecast.getCondition() + "</span></div>" +
            "</div>";

                String containerId = data.apiName.toLowerCase(); 
                new ModifHTML(filepath, infocontent, containerId);
            }
            if(data.apiName.equals("OpenMeteo")) {
                int numberOfDays = data.getForecasts().size();
                //System.out.println(numberOfDays);
                // Mise à jour de la valeur par défaut du sélecteur de jours
                new ModifHTML(filepath, ""+numberOfDays, "nbjours-selector");
            }
           
        }
        //Décommenter si on veut que le fichier s'ouvre à l'execution
        //OpenHTML.openHtmlFile(filepath);
    }
}

