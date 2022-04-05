package com.playtomic.tests.wallet.dto;

import com.playtomic.tests.wallet.enums.PaymentProviderType;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Currency;

@Getter
public class PayRequestDto {

    private Long walletId;
    private BigDecimal amount;
    private String payTo;
    private Currency currency;
    private PaymentProviderType paymentProviderType;

}