package com.playtomic.tests.wallet.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.validator.constraints.CreditCardNumber;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;


@Getter
@AllArgsConstructor
public class RechargeRequestDto {

    @NotNull(message = "Walled id must be provided.")
    private Long walletId;

    @DecimalMin(value = "0.0", inclusive = false, message = "Please recharge your wallet with amount bigger than 0.")
    private BigDecimal amount;

    @CreditCardNumber(ignoreNonDigitCharacters = true, message = "Please enter valid card number.")
    private String cardNumber;

    //TODO: We can add this option in order to choose payment provider for recharging the wallet.
//    @NotNull(message = "Payment provider must be added.")
//    private PaymentProviderType paymentProviderType;

}