package com.example.demo.bankAccount;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
public class BankAccountService {

    private final BankAccountRepository bankAccountRepository;

    @Autowired
    public BankAccountService(BankAccountRepository bankAccountRepository) {
        this.bankAccountRepository = bankAccountRepository;
    }

    public BankAccount createAccount(String id, double initialBalance) {
        BankAccount account = new BankAccount(id, initialBalance);
        return bankAccountRepository.save(account);
    }


    public BankAccount getAccount(String id) {
        return bankAccountRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Bank account not found"));
    }

    @Transactional
    public void deposit(String id, double amount) {
        BankAccount account = getAccount(id);
        account.deposit(amount);
    }

    @Transactional
    public void withdraw(String id, double amount) {
        BankAccount account = getAccount(id);
        account.withdraw(amount);
    }

    @Transactional
    public void transfer(String fromAccountId, String toAccountId, double amount) {
        BankAccount fromAccount = getAccount(fromAccountId);
        BankAccount toAccount = getAccount(toAccountId);
        fromAccount.transfer(amount, toAccount);
    }
}
