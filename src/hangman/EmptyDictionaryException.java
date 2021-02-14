package hangman;

public class EmptyDictionaryException extends Exception {
    //Thrown when dictionary file is empty or no words in dictionary match the length asked for

    public EmptyDictionaryException() {
    }

    public EmptyDictionaryException(String message) {
        super(message);
    }

    public EmptyDictionaryException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmptyDictionaryException(Throwable cause) {
        super(cause);
    }

    public EmptyDictionaryException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
