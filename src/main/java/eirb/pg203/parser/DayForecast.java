package eirb.pg203.parser;

public class DayForecast {
    private final String date;
    private final double maxTemp;
    private final double minTemp;
    private final String condition;
    private final int ventDirection; 
    private final double ventVitesse; 

    public DayForecast(String date, double maxTemp, double minTemp, String condition, int ventDirection, double ventVitesse) {
        this.date = date;
        this.maxTemp = maxTemp;
        this.minTemp = minTemp;
        this.condition = condition;
        this.ventDirection = ventDirection;
        this.ventVitesse = ventVitesse; 
    }

    public String getDate() {
        return date;
    }

    public double getMaxTemp() {
        return maxTemp;
    }

    public double getMinTemp() {
        return minTemp;
    }

    public String getCondition() {
        return condition;
    }

    public int getVentDirection() {
        return ventDirection;
    }

    public double getVentVitesse() {
        return ventVitesse; 
    }
}