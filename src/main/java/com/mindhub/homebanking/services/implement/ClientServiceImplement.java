package com.mindhub.homebanking.services.implement;

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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
public class ClientServiceImplement implements ClientService {

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountRepository accountRepo;

    @Autowired
    private ClientRepository clientRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<ClientDTO> getClients() {
        return clientRepo.findAll()
                .stream()
                .map(client -> new ClientDTO(client))
                .collect(Collectors.toList());
    }

    @Override
    public ClientDTO getClientById(Long id) {
        if(clientRepo.findById(id).orElse(null) != null) {
            return new ClientDTO(clientRepo.findById(id).orElse(null));
        }
        else {
            return null;
        }
    }

    @Override
    public ClientDTO getClientByEmail(String email) {
        if(clientRepo.findByEmail(email) != null) {
            return new ClientDTO(clientRepo.findByEmail(email));
        }else{
            return null;
        }
    }

    @Override
    public List<Long> getClientAccountsIds(String email){
        if(clientRepo.findByEmail(email) != null) {
            return clientRepo.findByEmail(email).getAccountsIds();
        }else {
            return null;
        }
    }

    @Override
    public ResponseEntity<String> registerClient(String firstName, String lastName, String email, String password) {
        Client client = new Client(firstName, lastName, email, passwordEncoder.encode(password));
        clientRepo.save(client);
        accountService.addNewAccount(email);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
