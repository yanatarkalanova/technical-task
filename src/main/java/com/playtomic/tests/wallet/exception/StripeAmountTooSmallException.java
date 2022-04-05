package com.playtomic.tests.wallet.exception;

public class StripeAmountTooSmallException extends StripeServiceException {

    private static final long serialVersionUID = 1L;
    private static final String MESSAGE = "Stripe amount too small.";
    private static final String CODE = "AMOUNT_TOO_SMALL";

    public StripeAmountTooSmallException() {
        super(MESSAGE);
        setStatusCode(CODE);
    }

    public StripeAmountTooSmallException(String message, String... arguments) {
        super(message, arguments);
        setStatusCode(CODE);
    }
}
