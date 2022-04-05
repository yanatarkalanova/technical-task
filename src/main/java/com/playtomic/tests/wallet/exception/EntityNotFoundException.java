package com.playtomic.tests.wallet.exception;

public class EntityNotFoundException extends BaseException {

    private static final long serialVersionUID = 1L;
    private static final String MESSAGE = "Entity not found.";
    private static final String CODE = "ENTITY_NOT_FOUND";

    public EntityNotFoundException() {
        super(MESSAGE);
        setStatusCode(CODE);
    }

    public EntityNotFoundException(String message, String... arguments) {
        super(message, arguments);
        setStatusCode(CODE);
    }
}