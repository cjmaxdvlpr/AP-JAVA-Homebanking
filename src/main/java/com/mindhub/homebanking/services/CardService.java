package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Set;

public interface CardService {

    public Set<CardDTO> getCardsByEmail(String email);

    public ResponseEntity<String> addNewCard(String email, CardType type, CardColor color);


}
