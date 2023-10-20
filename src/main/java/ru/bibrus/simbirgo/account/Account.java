package ru.bibrus.simbirgo.account;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue
    private Long id;

    private String username;

    private String password;

    @Getter
    @Setter
    private boolean isAdmin;

    private double balance;

    public final void topUpBalance(double amount) {
        this.balance += amount;
    }

}