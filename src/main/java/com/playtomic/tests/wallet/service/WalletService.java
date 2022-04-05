package com.playtomic.tests.wallet.service;

import com.playtomic.tests.wallet.dto.PayRequestDto;
import com.playtomic.tests.wallet.dto.RechargeRequestDto;
import com.playtomic.tests.wallet.entity.Payment;
import com.playtomic.tests.wallet.entity.Transaction;
import com.playtomic.tests.wallet.entity.Wallet;
import com.playtomic.tests.wallet.enums.PaymentProviderType;
import com.playtomic.tests.wallet.exception.EntityNotFoundException;
import com.playtomic.tests.wallet.exception.StripeServiceException;
import com.playtomic.tests.wallet.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;
    private final StripeService stripeService;

    public Wallet getWallet(Long walletId) {
        return walletRepository.findById(walletId).orElseThrow(() -> {
            throw new EntityNotFoundException("There is no wallet with id: " + walletId);
        });
    }

    public List<Wallet> getWallets() {
        return walletRepository.findAll();
    }

    @Transactional
    public synchronized Wallet rechargeWallet(RechargeRequestDto request) {

        Wallet wallet = getWallet(request.getWalletId());
        try {
            Payment payment = chargeWithStripe(request.getCardNumber(), request.getAmount());

            BigDecimal newBalance = wallet.getBalance().add(request.getAmount());
            wallet.setBalance(newBalance);

            Transaction successfulTransaction = Transaction.builder().paymentProviderId(payment.getId())
                    .paymentProvider(PaymentProviderType.STRIPE).amount(request.getAmount()).build();
            wallet.addTransaction(successfulTransaction);

            return walletRepository.saveAndFlush(wallet);
        } catch (ObjectOptimisticLockingFailureException e) {
            log.error("Somebody has already updated the amount for wallet:{} in concurrent transaction. Please try again...", request.getWalletId());
        }
        return wallet;

    }

    @Retryable(value = StripeServiceException.class, maxAttempts = 5, backoff = @Backoff(delay = 3000))
    private Payment chargeWithStripe(String creditCardNumber, BigDecimal amount) {
        return stripeService.charge(creditCardNumber, amount);
    }

    public Transaction pay(PayRequestDto payRequestDto) {
        // TODO: implement and use all data in PayRequestDto.class
        return null;
    }

    public Transaction refund(String transactionId) {
        // TODO: by transactionId we will find the wallet and
        return null;
    }

}