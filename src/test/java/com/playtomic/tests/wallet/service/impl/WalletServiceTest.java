package com.playtomic.tests.wallet.service.impl;

import com.playtomic.tests.wallet.dto.RechargeRequestDto;
import com.playtomic.tests.wallet.entity.Wallet;
import com.playtomic.tests.wallet.exception.EntityNotFoundException;
import com.playtomic.tests.wallet.exception.StripeAmountTooSmallException;
import com.playtomic.tests.wallet.service.WalletService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Profile({"test"})
@AutoConfigureMockMvc
public class WalletServiceTest {

    private static final String CARD_NUMBER = "4242 4242 4242 4242";
    private static final String INVALID_CARD_NUMBER = "abc";
    private static Validator validator;

    @Autowired
    private WalletService walletService;

    @BeforeAll
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void test_existingWallet() {
        Wallet wallet = walletService.getWallet(1L);
        assertNotNull(wallet);
    }

    @Test
    public void test_notExistingWallet() {
        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            walletService.getWallet(110L);
        });
    }

    @Test
    public void test_rechargeWalletAmountTooSmall() {
        Assertions.assertThrows(StripeAmountTooSmallException.class, () -> {
            walletService.rechargeWallet(new RechargeRequestDto(1L, BigDecimal.valueOf(3), CARD_NUMBER));
        });
    }

    @Test
    public void test_rechargeWalletInvalidCard() {
        Set<ConstraintViolation<RechargeRequestDto>> violations = validator.validate(new RechargeRequestDto(1L, BigDecimal.valueOf(15), INVALID_CARD_NUMBER));
        Assertions.assertFalse(violations.isEmpty());
    }

    @Test
    public void test_rechargeNotExistingWallet() {
        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            walletService.rechargeWallet(new RechargeRequestDto(66L, BigDecimal.valueOf(15), CARD_NUMBER));
        });
    }

    @Test
    public void test_rechargeWalletOk() throws InterruptedException {
        Long walletId = 1L;
        long iterations = 5;
        long transactionAmount = 10;

        for (int i = 1; i < iterations; i++) {
            new Thread(() -> {
                walletService.rechargeWallet(new RechargeRequestDto(walletId, BigDecimal.valueOf(transactionAmount), CARD_NUMBER));
            }).start();
        }

        Thread.sleep(10000L);
        Wallet updatedWallet = walletService.getWallet(walletId);
        assertNotNull(updatedWallet);
        assertEquals(updatedWallet.getBalance(), BigDecimal.valueOf(iterations * transactionAmount).setScale(2, RoundingMode.UNNECESSARY));
    }

}