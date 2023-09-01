package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
public class AccountServiceImplement implements AccountService {

    @Autowired
    private AccountRepository accountRepo;

    @Autowired
    private ClientRepository clientRepo;


    @Override
    public List<AccountDTO> getAccounts() {
        return accountRepo.findAll()
                .stream()
                .map(account -> new AccountDTO(account))
                .collect(Collectors.toList());

    }

    @Override
    public AccountDTO getAccountById(String email, Long id){
        Client client = clientRepo.findByEmail(email);
        List<Long> clientAccountIds = client.getAccountsIds();
        if(clientAccountIds.contains(id)){
            return new AccountDTO(accountRepo.findById(id).orElse(null));
        }else{
            return null;
            //return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @Override
    public Set<AccountDTO> getAccountsByEmail(String email){
        Client client =  clientRepo.findByEmail(email);
        return new ClientDTO(client).getAccounts();
    }

    @Override
    //public ResponseEntity<Object> addNewAccount(Authentication authentication){
    public ResponseEntity<String> addNewAccount(String email){
        //ClientDTO clientDTO = new ClientDTO(clientRepo.findByEmail(email));
        Client client =  clientRepo.findByEmail(email);
        if(client.getAccounts().size() < 3 ){
            //Generate account number See if the account number already exists. If it exists, generate a new one.
            String accountNumber;
            do{accountNumber=generateAccountNumber();}
            while(accountRepo.findByNumber(accountNumber) != null);
            Account account = new Account( accountNumber, LocalDate.now(), 0);
            client.addAccount(account);
            accountRepo.save(account);
            return new ResponseEntity<>("Account created success", HttpStatus.CREATED);
        }else{
            return new ResponseEntity<>("Limit of three accounts reached", HttpStatus.FORBIDDEN);
        }
    }

    private String generateAccountNumber(){
        return "VIN-" + ThreadLocalRandom.current().nextInt(10000000, 99999999 + 1);
    }

}
