package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
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

        //return null;
    }

    @Override
    public ClientDTO getClientById(Long id) {

        return new ClientDTO(clientRepo.findById(id).orElse(null));
        //return null;
    }

    @Override
    public ClientDTO getClientByEmail(String email) {

        return new ClientDTO(clientRepo.findByEmail(email));
        //return null;

    }

    @Override
    public ResponseEntity<String> registerClient(String firstName, String lastName, String email, String password) {

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }

        if (clientRepo.findByEmail(email) !=  null) {
            return new ResponseEntity<>("Name already in use", HttpStatus.FORBIDDEN);
        }

        Client client = new Client(firstName, lastName, email, passwordEncoder.encode(password));
        clientRepo.save(client);

        int vinNumber = ThreadLocalRandom.current().nextInt(10000000, 99999999 + 1);
        String vin = "VIN-"+ vinNumber;
        //See if the account number already exists. If it exists, generate a new one.
        while(accountRepo.findByNumber(vin) != null){
            vin="VIN-"+ ThreadLocalRandom.current().nextInt(10000000, 99999999 + 1);
        }

        Account account = new Account( vin, LocalDate.now(), 0);
        client.addAccount(account);
        accountRepo.save(account);

        return new ResponseEntity<>(HttpStatus.CREATED);

        //return null;
    }
}
