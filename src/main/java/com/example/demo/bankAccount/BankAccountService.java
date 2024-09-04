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
        try {
            if (bankAccountRepository.existsById(id)) {
                logger.warn("Account with ID: {} already exists.", id);
                throw new IllegalArgumentException("Account with ID " + id + " already exists.");
            }

            logger.debug("Creating bank account with ID: {} and balance: {}", id, initialBalance);
            BankAccount account = new BankAccount(id, initialBalance);
            logger.info("Bank account created with ID: {}", id);
            return bankAccountRepository.save(account);
        } catch (IllegalArgumentException e) {
            logger.error("Failed to create account: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("An unexpected error occurred while creating account with ID: {}", id, e);
            throw new RuntimeException("Unable to create account at this time. Please try again later.");
        }
    }

    public BankAccount getAccount(String id) {
        try {
            logger.info("Getting account with ID: {}", id);
            return bankAccountRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Account with ID " + id + " not found."));
        } catch (IllegalArgumentException e) {
            logger.error("Failed to get account: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("An unexpected error occurred while retrieving account with ID: {}", id, e);
            throw new RuntimeException("Unable to retrieve account at this time. Please try again later.");
        }
    }

    @Transactional
    public boolean deposit(String id, double amount) {
        try {
            BankAccount account = getAccount(id);
            logger.info("Trying to deposit {} to account with ID: {}", amount, id);
            return account.deposit(amount);
        } catch (IllegalArgumentException e) {
            logger.error("Failed to deposit to account: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("An unexpected error occurred while depositing to account with ID: {}", id, e);
            throw new RuntimeException("Unable to deposit at this time. Please try again later.");
        }
    }

    @Transactional
    public boolean withdraw(String id, double amount) {
        try {
            BankAccount account = getAccount(id);
            logger.info("Trying to withdraw {} from account with ID: {}", amount, id);
            return account.withdraw(amount);
        } catch (IllegalArgumentException e) {
            logger.error("Failed to withdraw from account: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("An unexpected error occurred while withdrawing from account with ID: {}", id, e);
            throw new RuntimeException("Unable to withdraw at this time. Please try again later.");
        }
    }

    @Transactional
    public boolean transfer(String fromAccountId, String toAccountId, double amount) {
        try {
            BankAccount fromAccount = getAccount(fromAccountId);
            BankAccount toAccount = getAccount(toAccountId);
            logger.info("Trying to transfer {} from account with ID: {} to account with ID: {}", amount, fromAccountId, toAccountId);
            return fromAccount.transfer(amount, toAccount);
        } catch (IllegalArgumentException e) {
            logger.error("Failed to transfer funds: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("An unexpected error occurred while transferring funds from account with ID: {} to account with ID: {}", fromAccountId, toAccountId, e);
            throw new RuntimeException("Unable to transfer funds at this time. Please try again later.");
        }
    }
}


