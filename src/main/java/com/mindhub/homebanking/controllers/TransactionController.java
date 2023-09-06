package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ClientService clientService;

    @Transactional
    @RequestMapping(path = "/transactions", method = RequestMethod.POST)
    public ResponseEntity<String> performTransaction(Authentication authentication,
                                                     @RequestParam Double amount,
                                                     @RequestParam String description,
                                                     @RequestParam String fromAccountNumber,
                                                     @RequestParam String toAccountNumber){
        if(clientService.getClientByEmail(authentication.getName()) != null){
            if(amount.isNaN() || description.isEmpty() || fromAccountNumber.isEmpty() || toAccountNumber.isEmpty()){
                String message = (amount.isNaN()? "Not valid amount. ":"") +
                                 (description.isEmpty()?"Description. ":"") +
                                 (fromAccountNumber.isEmpty()?"Origin account number. ":"") +
                                 (toAccountNumber.isEmpty()?"Destiny account number. ":"");
                return new ResponseEntity<>("Missing data: " + message, HttpStatus.FORBIDDEN);
            }
            if(fromAccountNumber.equals(toAccountNumber)){
                return new ResponseEntity<>("Origin and destiny accounts are the same", HttpStatus.FORBIDDEN);
            }
            if(accountService.getAccountByNumber(fromAccountNumber) == null){
                return new ResponseEntity<>("Origin account not found", HttpStatus.FORBIDDEN);
            }

            if(!clientService.getClientAccountsIds(authentication.getName())
                    .contains(accountService.getAccountByNumber(fromAccountNumber).getId())){
                return new ResponseEntity<>("Origin account does not belong to authenticated client", HttpStatus.FORBIDDEN);
            };

            if(accountService.getAccountByNumber(toAccountNumber) == null){
                return new ResponseEntity<>("Destiny account not found", HttpStatus.FORBIDDEN);
            }
            if(accountService.getAccountByNumber(toAccountNumber).getBalance() < amount){
                return new ResponseEntity<>("Origin account founds not enough", HttpStatus.FORBIDDEN);
            }
            transactionService.makeTransaction(authentication.getName(),
                                               amount,
                                               description,
                                               fromAccountNumber,
                                               toAccountNumber);

            return new ResponseEntity<>("Success transaction",HttpStatus.CREATED);
        }else{
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

    }

}
