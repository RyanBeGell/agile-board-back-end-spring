package dev.ryan.AgileBoardBackEndSpring.exceptions;
public class CardNotFoundException extends RuntimeException {
    public CardNotFoundException(String message) {
        super(message);
    }
}
