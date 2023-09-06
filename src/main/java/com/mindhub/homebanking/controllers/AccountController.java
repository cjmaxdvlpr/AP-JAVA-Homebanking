package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static java.lang.Long.parseLong;

@RestController
@RequestMapping("/api")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private ClientService clientService;


    @RequestMapping("/accounts")
    public List<AccountDTO> getAccounts() {
        return accountService.getAccounts();
    }

    @RequestMapping("/accounts/{id}")
    public ResponseEntity<AccountDTO> getAccount(Authentication authentication, @PathVariable Long id){
        if(clientService.getClientById(id) != null) {
            return new ResponseEntity<>(accountService.getAccountById(authentication.getName(),id), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping("/clients/current/accounts")
    public ResponseEntity<Set<AccountDTO>> getAccounts(Authentication authentication) {
        if(clientService.getClientByEmail(authentication.getName()) != null) {
            return new ResponseEntity<>(accountService.getAccountsByEmail(authentication.getName()),HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @RequestMapping(path = "/clients/current/accounts", method = RequestMethod.POST)
    public ResponseEntity<String> addNewAccount( Authentication authentication){
        if(clientService.getClientByEmail(authentication.getName()) != null) {
            return accountService.addNewAccount(authentication.getName());
        }else{
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

}
