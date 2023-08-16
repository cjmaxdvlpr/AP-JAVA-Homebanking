package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(ClientRepository clientRepo,
									  AccountRepository accountRepo,
									  TransactionRepository transactionRepo,
									  LoanRepository loanRepo,
									  ClientLoanRepository clientLoanRepo,
									  CardRepository cardRepo) {
		return (args) -> {

			Loan loan1 = new Loan("Hipotecario", 500000.0, List.of(12,24,36,48,60));
			Loan loan2 = new Loan("Personal", 100000.0, List.of(6,12,24));
			Loan loan3 = new Loan("Automotriz", 300000.0, List.of(6,12,24,36));
			loanRepo.save(loan1);
			loanRepo.save(loan2);
			loanRepo.save(loan3);


			Client client1 = new Client("Melba", "Morel", "melba@mindhub.com");
			clientRepo.save(client1);
			Account account1 = new Account("VIN001", LocalDate.now(), 5000);
			Account account2 = new Account("VIN002", LocalDate.now().plusDays(1), 7500);
			Transaction transaction1 = new Transaction( TransactionType.CREDIT, 100000, "Transacción crédito 1");
			Transaction transaction2 = new Transaction( TransactionType.DEBIT, -15500, "Transacción débito 1");
			Transaction transaction3 = new Transaction( TransactionType.DEBIT, -7800, "Transacción débito 2");
			Transaction transaction4 = new Transaction( TransactionType.CREDIT, 100000, "Transacción crédito 1");
			Transaction transaction5 = new Transaction( TransactionType.DEBIT, -15500, "Transacción débito 1");
			Transaction transaction6 = new Transaction( TransactionType.DEBIT, -7800, "Transacción débito 2");
			account1.addTransaction(transaction1);
			account1.addTransaction(transaction2);
			account1.addTransaction(transaction3);
			account2.addTransaction(transaction4);
			account2.addTransaction(transaction5);
			account2.addTransaction(transaction6);
			client1.addAccount(account1);
			client1.addAccount(account2);
			accountRepo.save(account1);
			accountRepo.save(account2);
			transactionRepo.save(transaction1);
			transactionRepo.save(transaction2);
			transactionRepo.save(transaction3);
			transactionRepo.save(transaction4);
			transactionRepo.save(transaction5);
			transactionRepo.save(transaction6);

			ClientLoan clientLoan1 = new ClientLoan(400000.00,60,client1,loan1);
			ClientLoan clientLoan2 = new ClientLoan(50000.00,12,client1,loan2);
			loan1.addClientLoan(clientLoan1);
			loan2.addClientLoan(clientLoan2);
			client1.addClientLoan(clientLoan1);
			client1.addClientLoan(clientLoan2);
			clientLoanRepo.save(clientLoan1);
			clientLoanRepo.save(clientLoan2);

			Card card1 = new Card(CardType.DEBIT, CardColor.GOLD, "4654-2912-8915-5836", 563, LocalDate.now(), LocalDate.now().plusYears(5));
			Card card2 = new Card(CardType.CREDIT, CardColor.TITANIUM, "4611-5678-5920-7418", 169, LocalDate.now(), LocalDate.now().plusYears(5));
			client1.addCard(card1);
			client1.addCard(card2);
			cardRepo.save(card1);
			cardRepo.save(card2);


			Client client2 = new Client("Anna", "Haberk", "anna@mindhub.com");
			clientRepo.save(client2);
			Account account3 = new Account("VIN003", LocalDate.now(), 6000);
			Account account4 = new Account("VIN004", LocalDate.now().plusDays(1), 8500);
			Transaction transaction7 = new Transaction( TransactionType.CREDIT, 100000, "Transacción crédito 1");
			Transaction transaction8 = new Transaction( TransactionType.DEBIT, -15500, "Transacción débito 1");
			Transaction transaction9 = new Transaction( TransactionType.DEBIT, -7800, "Transacción débito 2");
			Transaction transaction10 = new Transaction( TransactionType.CREDIT, 100000, "Transacción crédito 1");
			Transaction transaction11 = new Transaction( TransactionType.DEBIT, -15500, "Transacción débito 1");
			Transaction transaction12 = new Transaction( TransactionType.DEBIT, -7800, "Transacción débito 2");
			account3.addTransaction(transaction7);
			account3.addTransaction(transaction8);
			account3.addTransaction(transaction9);
			account4.addTransaction(transaction10);
			account4.addTransaction(transaction11);
			account4.addTransaction(transaction12);
			client2.addAccount(account3);
			client2.addAccount(account4);
			accountRepo.save(account3);
			accountRepo.save(account4);
			transactionRepo.save(transaction7);
			transactionRepo.save(transaction8);
			transactionRepo.save(transaction9);
			transactionRepo.save(transaction10);
			transactionRepo.save(transaction11);
			transactionRepo.save(transaction12);

			ClientLoan clientLoan3 = new ClientLoan(100000.00,24,client2,loan2);
			ClientLoan clientLoan4 = new ClientLoan(200000.00,36,client2,loan3);
			loan2.addClientLoan(clientLoan3);
			loan3.addClientLoan(clientLoan4);
			client2.addClientLoan(clientLoan3);
			client2.addClientLoan(clientLoan4);
			clientLoanRepo.save(clientLoan3);
			clientLoanRepo.save(clientLoan4);

			Card card3 = new Card(CardType.CREDIT, CardColor.SILVER, "4304-3297-7715-4129", 836, LocalDate.now(), LocalDate.now().plusYears(5));
			client2.addCard(card3);
			cardRepo.save(card3);


		};
	}
}
