package com.anyMind.application;

import com.anyMind.application.wallet.Wallet;
import com.anyMind.application.wallet.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

import static java.lang.Double.valueOf;

@SpringBootApplication
public class Application {

	@Autowired
	WalletRepository repository;

	// For test
	@PostConstruct
	public void initialDataForTesting() {
		Wallet w = new Wallet();
		w.setBalance( valueOf(1000));
		w.setAmount( valueOf(1000));
		w.setDatetime(ZonedDateTime.now());
		repository.save(w);
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
