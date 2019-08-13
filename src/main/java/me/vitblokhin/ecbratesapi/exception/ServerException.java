package me.vitblokhin.ecbratesapi.exception;

public class ServerException extends RuntimeException {
    public ServerException() {
    }

    public ServerException(String message) {
        super(message);
    }
} // class ServerException
