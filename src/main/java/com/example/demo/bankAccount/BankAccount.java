package com.example.demo.bankAccount;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "bank_account")
public class BankAccount {
    @Id
    private String id;
    private double balance;
    public BankAccount() {
    }

    public BankAccount(String id, double balance) {
        this.id = id;
        this.balance = balance;
    }

    public String getId() {
        return id;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public boolean deposit(double amount) {
        if (amount <= 0) {
            System.out.println("Amount must be more than zero");
            return false;
        }
        balance += amount;
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
        balance -= amount;
        System.out.println("Withdrawal complete: " + amount);
        return true;
    }

    public boolean transfer(double amount, BankAccount targetAccount) {
        if (amount <= 0) {
            System.out.println("Amount must be more than zero");
            return false;
        }
        if (amount > balance) {
            System.out.println("Insufficient funds");
            return false;
        }
        this.withdraw(amount);
        targetAccount.deposit(amount);
        System.out.println("Transfer completed: " + amount);
        return true;
    }
}
