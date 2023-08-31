package com.mindhub.homebanking.controllers;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
//import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ClientController {

    @Autowired
    private AccountRepository accountRepo;

    @Autowired
    private ClientRepository clientRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;


    //Servlet (listen and response to specific requests)
    @RequestMapping("/clients")
    public List<ClientDTO> getClients() {

        return clientRepo.findAll()
                .stream()
                .map(client -> new ClientDTO(client))
                .collect(Collectors.toList());

    }

    @RequestMapping("/clients/{id}")
    public ClientDTO getClient(@PathVariable Long id){
        return new ClientDTO(clientRepo.findById(id).orElse(null));

    }

    @RequestMapping("/clients/current")
    public ClientDTO getCurrentAuthenticatedClient(Authentication authentication){
        return new ClientDTO(clientRepo.findByEmail(authentication.getName()));

    }



    @RequestMapping(path = "/clients", method = RequestMethod.POST)
    public ResponseEntity<Object> register(
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String email,
            @RequestParam String password) {

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {

            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);

        }

        if (clientRepo.findByEmail(email) !=  null) {

            return new ResponseEntity<>("Name already in use", HttpStatus.FORBIDDEN);

        }

        Client client = new Client(firstName, lastName, email, passwordEncoder.encode(password));
        clientRepo.save(client);
        int vinNumber = ThreadLocalRandom.current().nextInt(10000000, 99999999 + 1);
        //See if the account number already exists. If it exists, generate a new one.
        String vin = "VIN-"+ vinNumber;
        while(accountRepo.findByNumber(vin) != null){
            vin="VIN-"+ ThreadLocalRandom.current().nextInt(10000000, 99999999 + 1);
        }
        Account account = new Account( vin, LocalDate.now(), 0);
        client.addAccount(account);
        accountRepo.save(account);


        return new ResponseEntity<>(HttpStatus.CREATED);

    }


}
