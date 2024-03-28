package org.sportradar.exception;

public class InvalidCountryNameException extends Exception {
    public InvalidCountryNameException(final String countryName) {
        super("Invalid country name: " + countryName);
    }
}
