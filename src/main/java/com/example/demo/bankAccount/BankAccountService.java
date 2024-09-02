package com.example.demo.bankAccount;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@Service
public class BankAccountService {

    private final BankAccountRepository bankAccountRepository;
    private static final Logger logger = LogManager.getLogger(BankAccountService.class);

    @Autowired
    public BankAccountService(BankAccountRepository bankAccountRepository) {
        this.bankAccountRepository = bankAccountRepository;
    }

    public BankAccount createAccount(String id, double initialBalance) {
        logger.debug("Creating bank account with ID: {} and balance: {}", id, initialBalance);
        BankAccount account = new BankAccount(id, initialBalance);
        logger.info("Bank account created with ID: {}", id);
        return bankAccountRepository.save(account);
    }


    public BankAccount getAccount(String id) {
        logger.info("Getting account with ID: {}", id);
        return bankAccountRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Bank account not found"));
    }

    @Transactional
    public boolean deposit(String id, double amount) {
        BankAccount account = getAccount(id);
        logger.info("Trying to deposit {} to account with ID: {}", amount, id);
        return account.deposit(amount);
    }


    @Transactional
    public boolean withdraw(String id, double amount) {
        BankAccount account = getAccount(id);
        logger.info("Trying to withdraw {} from account with ID: {}", amount, id);
        return account.withdraw(amount);
    }

    @Transactional
    public boolean transfer(String fromAccountId, String toAccountId, double amount) {
        BankAccount fromAccount = getAccount(fromAccountId);
        BankAccount toAccount = getAccount(toAccountId);
        logger.info("Trying to transfer {} from account with ID: {} to account with ID: {}", amount, fromAccountId, toAccountId);
        return fromAccount.transfer(amount, toAccount);
    }
}
