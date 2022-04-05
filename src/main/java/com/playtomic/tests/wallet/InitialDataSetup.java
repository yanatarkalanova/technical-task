package com.playtomic.tests.wallet;

import com.playtomic.tests.wallet.entity.Wallet;
import com.playtomic.tests.wallet.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Currency;
import java.util.List;

@Component
@Profile({ "develop", "test" })
public class InitialDataSetup implements CommandLineRunner {
	
	@Autowired
    private WalletRepository walletRepository;

	@Override
	public void run(String... args) {
		List<Wallet> walletList = Arrays.asList(
		Wallet.builder().balance(BigDecimal.valueOf(10)).currency(Currency.getInstance("USD")).build(),
		Wallet.builder().balance(BigDecimal.valueOf(15)).currency(Currency.getInstance("USD")).build(),
		Wallet.builder().balance(BigDecimal.valueOf(22)).currency(Currency.getInstance("USD")).build(),
		Wallet.builder().balance(BigDecimal.valueOf(56)).currency(Currency.getInstance("USD")).build());
		walletRepository.saveAll(walletList);
	}

}