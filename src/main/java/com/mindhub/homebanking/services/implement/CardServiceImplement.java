package com.mindhub.homebanking.services.implement;

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
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class CardServiceImplement implements CardService {

    @Autowired
    private CardRepository cardRepo;

    @Autowired
    private ClientRepository clientRepo;

    @Override
    public Set<CardDTO> getCardsByEmail(String email) {
        Client client =  clientRepo.findByEmail(email);
        return new ClientDTO(client).getCards();
    }

    @Override
    public ResponseEntity<String> addNewCard(String email, CardType type, CardColor color) {

        Client client =  clientRepo.findByEmail(email);
        if(client.getCardsByType(type).size() < 3 ){
            int cvv = generateCardCvv();
            //Generate a new card number.See if the card number already exists.
            // If it exists, generate a new one.
            String cardNumber;
            do{cardNumber=generateCardNumber();}
            while(cardRepo.findByNumber(cardNumber) != null);

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
        }
    }

    private String generateCardNumber(){
        StringBuilder cardNumber = new StringBuilder();
        for(int n=0;n<4;n++){
            if(n!=0) cardNumber.append("-");
            cardNumber.append(ThreadLocalRandom.current().nextInt(0, 9999 + 1));
        }
        return cardNumber.toString();
    }

    private int generateCardCvv(){
        return ThreadLocalRandom.current().nextInt(0, 999 + 1);
    }

}
