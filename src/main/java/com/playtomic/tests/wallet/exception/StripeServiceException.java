package com.playtomic.tests.wallet.exception;

public class StripeServiceException extends BaseException {

    private static final long serialVersionUID = 1L;
    private static final String MESSAGE = "Stripe service exception.";
    private static final String CODE = "STRIPE_ERROR";

    public StripeServiceException() {
        super(MESSAGE);
        setStatusCode(CODE);
    }

    public StripeServiceException(String message, String... arguments) {
        super(message, arguments);
        setStatusCode(CODE);
    }
}
