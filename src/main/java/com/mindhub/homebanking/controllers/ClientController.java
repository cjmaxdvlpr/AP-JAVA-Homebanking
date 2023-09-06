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
import com.mindhub.homebanking.services.ClientService;
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
    private ClientService clientService;

    //Servlet (listen and response to specific requests)
    @RequestMapping("/clients")
    public List<ClientDTO> getClients() {
        return clientService.getClients();
    }

    @RequestMapping("/clients/{id}")
    public ResponseEntity<ClientDTO> getClient(@PathVariable Long id){
        if(clientService.getClientById(id) != null) {
            return new ResponseEntity<>(clientService.getClientById(id), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping("/clients/current")
    public ResponseEntity<ClientDTO> getCurrentAuthenticatedClient(Authentication authentication){
        if(clientService.getClientByEmail(authentication.getName()) != null) {
            return new ResponseEntity<>(clientService.getClientByEmail(authentication.getName()), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @RequestMapping(path = "/clients", method = RequestMethod.POST)
    public ResponseEntity<String> register(
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String email,
            @RequestParam String password) {

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            String message = (firstName.isEmpty()? "First name. ":"") +
                             (lastName.isEmpty()?"Last name. ":"") +
                             (email.isEmpty()?"Email. ":"") +
                             (password.isEmpty()?"Password. ":"");
            return new ResponseEntity<>("Missing data: " + message, HttpStatus.FORBIDDEN);
        }
        else if (clientService.getClientByEmail(email) !=  null) {
            return new ResponseEntity<>("Name already in use", HttpStatus.FORBIDDEN);
        }
        else {
            return clientService.registerClient(firstName, lastName, email, password);
        }
    }
}
