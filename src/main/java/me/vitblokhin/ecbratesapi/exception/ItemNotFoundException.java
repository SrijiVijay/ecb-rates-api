package me.vitblokhin.ecbratesapi.exception;

public class ItemNotFoundException extends ServerException {
    public ItemNotFoundException() {
    }

    public ItemNotFoundException(String message) {
        super(message);
    }
} // class ItemNotFoundException
