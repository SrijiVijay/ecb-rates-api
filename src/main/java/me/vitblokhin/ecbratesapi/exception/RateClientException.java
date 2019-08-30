package me.vitblokhin.ecbratesapi.exception;

public class RateClientException extends RuntimeException {
    public RateClientException() {
    }

    public RateClientException(String message) {
        super(message);
    }
} // class RateClientException
