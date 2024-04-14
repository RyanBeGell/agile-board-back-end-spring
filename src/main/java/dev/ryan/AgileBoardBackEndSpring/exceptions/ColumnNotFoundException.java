package dev.ryan.AgileBoardBackEndSpring.exceptions;


public class ColumnNotFoundException extends RuntimeException {
    public ColumnNotFoundException(String message) {
        super(message);
    }
}
