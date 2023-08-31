package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
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
    private AccountRepository accountRepo;

    @Autowired
    private ClientRepository clientRepo;

    @RequestMapping("/accounts")
    public List<AccountDTO> getAccounts() {

        return accountRepo.findAll()
                .stream()
                .map(account -> new AccountDTO(account))
                .collect(Collectors.toList());

    }

    /*@RequestMapping("/accounts/{id}")
    public AccountDTO getAccount(@PathVariable Long id){
        return new AccountDTO(accountRepo.findById(id).orElse(null));
    }*/

    @RequestMapping("/accounts/{id}")
    public AccountDTO getAccount(Authentication authentication, @PathVariable Long id){
        Client client = clientRepo.findByEmail(authentication.getName());
        List<Long> clientAccountIds = client.getAccountsIds();
        if(clientAccountIds.contains(id)){
            return new AccountDTO(accountRepo.findById(id).orElse(null));
        }else{
            return null;
            //return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @RequestMapping("/clients/current/accounts")
    public Set<AccountDTO> getAccounts(Authentication authentication) {
        Client client =  clientRepo.findByEmail(authentication.getName());
        return new ClientDTO(client).getAccounts();
    }


    @RequestMapping(path = "/clients/current/accounts", method = RequestMethod.POST)
    public ResponseEntity<Object> addNewAccount( Authentication authentication){
        //ClientDTO clientDTO = new ClientDTO(clientRepo.findByEmail(authentication.getName()));
        Client client =  clientRepo.findByEmail(authentication.getName());
        if(client.getAccounts().size() < 3 ){
            int vinNumber = ThreadLocalRandom.current().nextInt(10000000, 99999999 + 1);
            //See if the account number already exists. If it exists, generate a new one.
            String vin = "VIN-"+ vinNumber;
            while(accountRepo.findByNumber(vin) != null){
                vin="VIN-"+ ThreadLocalRandom.current().nextInt(10000000, 99999999 + 1);
            }
            Account account = new Account( vin, LocalDate.now(), 0);
            client.addAccount(account);
            accountRepo.save(account);
            return new ResponseEntity<>("Account created success", HttpStatus.CREATED);

        }else{
            return new ResponseEntity<>("Limit of three accounts reached", HttpStatus.FORBIDDEN);

        }


    }


}
