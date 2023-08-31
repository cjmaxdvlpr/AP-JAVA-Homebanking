package com.mindhub.homebanking.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;
    private String number;
    private LocalDate creationDate;
    private double balance;

    @ManyToOne(fetch = FetchType.EAGER)
    //@JoinColumn(name="owner")
    private Client client;

    @OneToMany(mappedBy="account", fetch=FetchType.EAGER)
    private Set<Transaction> transactions = new HashSet<>();

    public Account() { }

    public Account(String num, LocalDate date, double bal) {
        this.number = num;
        this.creationDate = date;
        this.balance = bal;

    }

    public Long getId() { return id; }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public LocalDate getCreationDate() { return creationDate; }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
    //@JsonIgnore
    public Client getClient() {
        return client;
    }

    public void setClient(Client owner) {
        this.client = owner;
    }

    public Set<Transaction> getTransactions() { return transactions; }

    public void addTransaction(Transaction transaction){
        transaction.setAccount(this);
        transactions.add(transaction);
    }

    public String toString() {
        return number + " " + creationDate + " " + balance;
    }

}
