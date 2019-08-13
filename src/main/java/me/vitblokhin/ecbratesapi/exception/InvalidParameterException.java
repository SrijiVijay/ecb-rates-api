package me.vitblokhin.ecbratesapi.exception;

public class InvalidParameterException extends ServerException {
    public InvalidParameterException() {
    }

    public InvalidParameterException(String message) {
        super(message);
    }
} // class InvalidParameterException
