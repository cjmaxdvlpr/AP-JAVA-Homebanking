package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;

import java.time.LocalDate;

public class CardDTO {

    private Long id;

    private String cardHolder;

    //private String type;
    private CardType type;

    //private String color;
    private CardColor color;

    private String number;

    private Integer cvv;

    //private String fromDate;
    private LocalDate fromDate;

    //private String thruDate;
    private LocalDate thruDate;

    public CardDTO(Card card) {
        this.id = card.getId();
        this.cardHolder = card.getCardHolder();
        //this.type = card.getType().toString();
        this.type = card.getType();
        //this.color = card.getColor().toString();
        this.color = card.getColor();
        this.number = card.getNumber();
        this.cvv = card.getCvv();
        //this.fromDate = card.getFromDate().toString();
        this.fromDate = card.getFromDate();
        //this.thruDate = card.getThruDate().toString();
        this.thruDate = card.getThruDate();
    }

    public Long getId() { return id; }

    public String getCardHolder() { return cardHolder; }

    //public String getType() { return type; }
    public CardType getType() { return type; }

    //public String getColor() { return color; }
    public CardColor getColor() { return color; }

    public String getNumber() { return number; }

    public Integer getCvv() { return cvv; }

    //public String getFromDate() { return fromDate; }
    public LocalDate getFromDate() { return fromDate; }

    //public String getThruDate() { return thruDate; }
    public LocalDate getThruDate() { return thruDate; }

}
