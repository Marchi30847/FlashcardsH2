package org.example.flashcards.exception;

public class IncorrectInputException extends RuntimeException {
    public IncorrectInputException(String message) {
        super(message);
    }
}
