package org.example.flashcards.exception;

public class EmptyDictionaryException extends RuntimeException {
    public EmptyDictionaryException(String message) {
        super(message);
    }
}
