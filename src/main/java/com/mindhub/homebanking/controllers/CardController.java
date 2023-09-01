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
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class CardController {

    @Autowired
    private CardService cardService;

    /*@Autowired
    private CardRepository cardRepo;

    @Autowired
    private ClientRepository clientRepo;*/

    @RequestMapping("/clients/current/cards")
    public Set<CardDTO> getCards(Authentication authentication) {

        return cardService.getCardsByEmail(authentication.getName());

        /*Client client =  clientRepo.findByEmail(authentication.getName());
        return new ClientDTO(client).getCards();*/
    }

    @RequestMapping(path = "/clients/current/cards", method = RequestMethod.POST)
    public ResponseEntity<String> addNewCard(Authentication authentication,
                                             @RequestParam CardType type,
                                             @RequestParam CardColor color){


        return cardService.addNewCard(authentication.getName(),type,color);

        /*Client client =  clientRepo.findByEmail(authentication.getName());
        if(client.getCardsByType(type).size() < 3 ){
            int cvv = ThreadLocalRandom.current().nextInt(0, 999 + 1);
            String cardNumber = ThreadLocalRandom.current().nextInt(0, 9999 + 1) +
                                "-" +
                                ThreadLocalRandom.current().nextInt(0, 9999 + 1) +
                                "-" +
                                ThreadLocalRandom.current().nextInt(0, 9999 + 1) +
                                "-" +
                                ThreadLocalRandom.current().nextInt(0, 9999 + 1);
            //See if the account number already exists. If it exists, generate a new one.
            while(cardRepo.findByNumber(cardNumber) != null){
                cardNumber = ThreadLocalRandom.current().nextInt(0, 9999 + 1) +
                             "-" +
                             ThreadLocalRandom.current().nextInt(0, 9999 + 1) +
                             "-" +
                             ThreadLocalRandom.current().nextInt(0, 9999 + 1) +
                             "-" +
                             ThreadLocalRandom.current().nextInt(0, 9999 + 1);
            }
            Card card = new Card(client.getFullName(), type, color, cardNumber, cvv,
                                LocalDate.now(), LocalDate.now().plusYears(5));
            client.addCard(card);
            cardRepo.save(card);
            return new ResponseEntity<>("Card created success", HttpStatus.CREATED);

        }else{
            return new ResponseEntity<>(
                    type.equals(CardType.DEBIT)?
                            "Limit of three debit cards reached":
                            "Limit of three credit cards reached",
                    HttpStatus.FORBIDDEN);
        }*/

    }

}
