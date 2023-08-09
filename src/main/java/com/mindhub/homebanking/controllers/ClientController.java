package com.mindhub.homebanking.controllers;

import java.util.List;
import java.util.stream.Collectors;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ClientController {

    @Autowired
    private ClientRepository clientRepo;

    //Servlet (Escucha y responde peticiones específicas)
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

}
