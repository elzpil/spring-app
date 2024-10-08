package com.example.demo.bankAccount;

import com.example.demo.user.User;
import jakarta.persistence.*;

@Entity
@Table(name = "bank_account")
public class BankAccount {

    @Id
    @SequenceGenerator(name = "account_sequence", sequenceName = "account_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_sequence")
    private Long id;
    private Long userId;
    private double balance;

    public BankAccount() {
    }


    public BankAccount(Long userId, double balance) {
        this.userId = userId;
        this.balance = balance;
    }
    private double roundToTwoDecimalPlaces(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    public Long getId() {
        return id;
    }

    public double getBalance() {
        return balance;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }



    public boolean deposit(double amount) {
        if (amount <= 0) {
            System.out.println("Amount must be more than zero");
            return false;
        }
        this.balance = roundToTwoDecimalPlaces(this.balance + amount);
        System.out.println("Deposit complete: " + amount);
        return true;
    }

    public boolean withdraw(double amount) {
        if (amount <= 0) {
            System.out.println("Withdrawal amount must be positive");
            return false;
        }
        if (amount > balance) {
            System.out.println("Insufficient funds");
            return false;
        }
        this.balance = roundToTwoDecimalPlaces(this.balance - amount);
        System.out.println("Withdrawal complete: " + amount);
        return true;
    }

    public boolean transfer(double amount, BankAccount toAccount) {
        if (this.withdraw(amount)) {
            return toAccount.deposit(amount);
        }
        return false;
    }
}
