package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.ClientDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface ClientService {

    public List<ClientDTO> getClients();

    public ClientDTO getClientById(Long id);

    public ClientDTO getClientByEmail(String email);

    public ResponseEntity<String> registerClient(String firstName,
                                                 String lastName,
                                                 String email,
                                                 String password);

}
