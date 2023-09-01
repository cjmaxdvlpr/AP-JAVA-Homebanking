package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.AccountDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Set;

public interface AccountService {

    public List<AccountDTO> getAccounts();

    public AccountDTO getAccountById(String email, Long id);

    public Set<AccountDTO> getAccountsByEmail(String email);

    public ResponseEntity<String> addNewAccount(String email);

    }
