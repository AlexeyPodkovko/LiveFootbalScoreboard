package org.sportradar.exception;

public class DuplicateCountryException extends Exception {
    public DuplicateCountryException() {
        super("Duplicate country names");
    }
}
