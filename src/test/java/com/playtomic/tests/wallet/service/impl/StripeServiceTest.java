package com.playtomic.tests.wallet.service.impl;

import com.playtomic.tests.wallet.exception.StripeAmountTooSmallException;
import com.playtomic.tests.wallet.exception.StripeServiceException;
import com.playtomic.tests.wallet.service.StripeService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

import java.math.BigDecimal;
import java.net.URI;

/**
 * This test is failing with the current implementation.
 * <p>
 * How would you test this?
 */
@SpringBootTest
@Profile({"test"})
public class StripeServiceTest {

    private static final String CARD_NUMBER = "4242 4242 4242 4242";

    @Value("${stripe.simulator.charges-uri}")
    private URI chargesUri;

    @Value("${stripe.simulator.refunds-uri}")
    private URI refundsUri;

    @Autowired
    private StripeService stripeService;

    @Test
    public void test_exception() {
        Assertions.assertThrows(StripeAmountTooSmallException.class, () -> {
            stripeService.charge(CARD_NUMBER, new BigDecimal(5));
        });
    }

    @Test
    public void test_ok() throws StripeServiceException {
        stripeService.charge(CARD_NUMBER, new BigDecimal(15));
    }

}