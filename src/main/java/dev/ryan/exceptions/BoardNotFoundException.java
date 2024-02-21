package dev.ryan.exceptions;

public class BoardNotFoundException  extends RuntimeException {
    public BoardNotFoundException(String message) {
        super(message);
    }
}
