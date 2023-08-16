package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Card;

public class CardDTO {

    private Long id;

    private String cardHolder;

    private String type;

    private String color;

    private String number;

    private Integer cvv;

    private String fromDate;

    private String thruDate;

    public CardDTO(Card card) {
        this.id = card.getId();
        this.cardHolder = card.getCardHolder();
        this.type = card.getType().toString();
        this.color = card.getColor().toString();
        this.number = card.getNumber();
        this.cvv = card.getCvv();
        this.fromDate = card.getFromDate().toString();
        this.thruDate = card.getThruDate().toString();
    }

    public Long getId() { return id; }

    public String getCardHolder() { return cardHolder; }

    public String getType() { return type; }

    public String getColor() { return color; }

    public String getNumber() { return number; }

    public Integer getCvv() { return cvv; }

    public String getFromDate() { return fromDate; }

    public String getThruDate() { return thruDate; }

}
