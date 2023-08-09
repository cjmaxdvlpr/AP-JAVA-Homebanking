package com.mindhub.homebanking;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.time.LocalDateTime;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(ClientRepository clientRepo, AccountRepository accountRepo) {
		return (args) -> {
			Client client1 = new Client("Melba", "Morel", "melba@mindhub.com");
			clientRepo.save(client1);
			Account account1 = new Account("VIN001", LocalDate.now(), 5000);
			Account account2 = new Account("VIN002", LocalDate.now().plusDays(1), 7500);
			client1.addAccount(account1);
			client1.addAccount(account2);
			accountRepo.save(account1);
			accountRepo.save(account2);

			Client client2 = new Client("Melba2", "Morel", "melba2@mindhub.com");
			clientRepo.save(client2);
			Account account3 = new Account("VIN003", LocalDate.now(), 6000);
			Account account4 = new Account("VIN004", LocalDate.now().plusDays(1), 8500);
			client2.addAccount(account3);
			client2.addAccount(account4);
			accountRepo.save(account3);
			accountRepo.save(account4);

		};
	}
}
