package eirb.pg203.exceptions; 

public class WeatherFetchingException extends Exception {
    public WeatherFetchingException(String message) {
        super(message);
    }

    public WeatherFetchingException(String message, Throwable cause) {
        super(message, cause);
    }
}
