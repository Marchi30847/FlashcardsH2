package org.example.flashcards.exception;

public class NonexistentCardException extends RuntimeException {
    public NonexistentCardException(String message) {
        super(message);
    }
}
