package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

@Service
public class TransactionServiceImplement implements TransactionService {

    @Autowired
    private AccountRepository accountRepo;

    @Autowired
    private ClientRepository clientRepo;

    @Autowired
    private TransactionRepository transactionRepo;


    @Override
    public ResponseEntity<String> makeTransaction(String email,
                                                  Double amount,
                                                  String description,
                                                  String fromAccountNumber,
                                                  String toAccountNumber){
        Account fromAccount = accountRepo.findByNumber(fromAccountNumber);
        Account toAccount = accountRepo.findByNumber(toAccountNumber);
        if(fromAccount.getBalance() < amount){
            return new ResponseEntity<String>("Not enough founds on origin account", HttpStatus.FORBIDDEN);
        }
        Transaction fromAccountTransaction = new Transaction( TransactionType.DEBIT, -amount, description + " - " + toAccountNumber);
        Transaction toAccountTransaction = new Transaction( TransactionType.CREDIT, amount, description + " - " + fromAccountNumber);
        fromAccount.addTransaction(fromAccountTransaction);
        toAccount.addTransaction(toAccountTransaction);
        transactionRepo.save(fromAccountTransaction);
        transactionRepo.save(toAccountTransaction);
        fromAccount.setBalance(fromAccount.getBalance()-amount);
        toAccount.setBalance(toAccount.getBalance()+amount);
        accountRepo.save(fromAccount);
        accountRepo.save(toAccount);
        return new ResponseEntity<String>("Success transaction", HttpStatus.OK);
    };


}
