package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.CardService;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class CardController {

    @Autowired
    private CardService cardService;

    @Autowired
    private ClientService clientService;

    @RequestMapping("/clients/current/cards")
    public ResponseEntity<Set<CardDTO>> getCards(Authentication authentication) {
        if(clientService.getClientByEmail(authentication.getName()) != null) {
            return new ResponseEntity<>(cardService.getCardsByEmail(authentication.getName()),HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @RequestMapping(path = "/clients/current/cards", method = RequestMethod.POST)
    public ResponseEntity<String> addNewCard(Authentication authentication,
                                             @RequestParam CardType type,
                                             @RequestParam CardColor color){

        if(clientService.getClientByEmail(authentication.getName()) != null) {
            System.out.println("UNO");
            if(!(type.equals(CardType.DEBIT)) && !(type.equals(CardType.CREDIT))){
                System.out.println("DOS");
                String message = (Objects.isNull(type)? "Type. ":"") +
                        (Objects.isNull(color)?"Color. ":"");
                return new ResponseEntity<>("Missing data: " + message, HttpStatus.FORBIDDEN);
            }
            else {
                return cardService.addNewCard(authentication.getName(), type, color);
            }

        }else{
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }



    }

}
