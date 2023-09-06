package com.mindhub.homebanking.services;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestParam;

public interface TransactionService {

    public ResponseEntity<String> makeTransaction(String email,
                                                  Double amount,
                                                  String description,
                                                  String fromAccountNumber,
                                                  String toAccountNumber);
}
