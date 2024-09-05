package com.example.demo.bankAccount;

import com.example.demo.user.User;
import com.example.demo.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Service
public class BankAccountService {

    private final BankAccountRepository bankAccountRepository;
    private final UserRepository userRepository;
    private static final Logger logger = LogManager.getLogger(BankAccountService.class);

    @Autowired
    public BankAccountService(BankAccountRepository bankAccountRepository, UserRepository userRepository) {
        this.bankAccountRepository = bankAccountRepository;
        this.userRepository = userRepository;
    }

    public BankAccount createAccount(Long userId, double initialBalance) {
        try {
            logger.debug("Creating bank account for user ID: {} with balance: {}", userId, initialBalance);
            BankAccount account = new BankAccount(userId, initialBalance);  // Assuming BankAccount has a constructor that takes userId and balance
            logger.info("Bank account created for user ID: {}", userId);
            return bankAccountRepository.save(account);
        } catch (IllegalArgumentException e) {
            logger.error("Failed to create account: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("An unexpected error occurred while creating account for user ID: {}", userId, e);
            throw new RuntimeException("Unable to create account at this time. Please try again later.");
        }
    }

    public BankAccount getAccount(Long id, Long userId) {
        try {
            logger.info("Getting account with ID: {}", id);
            BankAccount account = bankAccountRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Account with ID " + id + " not found."));
            if (!account.getUserId().equals(userId)) {
                throw new SecurityException("Account access denied. The account does not belong to the user.");
            }
            return account;
        } catch (IllegalArgumentException e) {
            logger.error("Failed to get account: {}", e.getMessage());
            throw e;
        } catch (SecurityException e) {
            logger.error("Security error: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("An unexpected error occurred while retrieving account with ID: {}", id, e);
            throw new RuntimeException("Unable to retrieve account at this time. Please try again later.");
        }
    }

    @Transactional
    public boolean deposit(Long id, double amount, Long userId) {
        try {
            BankAccount account = getAccount(id, userId);
            logger.info("Trying to deposit {} to account with ID: {}", amount, id);
            return account.deposit(amount);
        } catch (IllegalArgumentException e) {
            logger.error("Failed to deposit to account: {}", e.getMessage());
            throw e;
        } catch (SecurityException e) {
            logger.error("Security error: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("An unexpected error occurred while depositing to account with ID: {}", id, e);
            throw new RuntimeException("Unable to deposit at this time. Please try again later.");
        }
    }

    @Transactional
    public boolean withdraw(Long id, double amount, Long userId) {
        try {
            BankAccount account = getAccount(id, userId);
            logger.info("Trying to withdraw {} from account with ID: {}", amount, id);
            return account.withdraw(amount);
        } catch (IllegalArgumentException e) {
            logger.error("Failed to withdraw from account: {}", e.getMessage());
            throw e;
        } catch (SecurityException e) {
            logger.error("Security error: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("An unexpected error occurred while withdrawing from account with ID: {}", id, e);
            throw new RuntimeException("Unable to withdraw at this time. Please try again later.");
        }
    }

    @Transactional
    public boolean transfer(Long fromAccountId, Long toAccountId, double amount, Long userId) {
        try {
            BankAccount fromAccount = getAccount(fromAccountId, userId);
            BankAccount toAccount = getAccount(toAccountId, userId); // Check if the user has access to the destination account
            logger.info("Trying to transfer {} from account with ID: {} to account with ID: {}", amount, fromAccountId, toAccountId);
            return fromAccount.transfer(amount, toAccount);
        } catch (IllegalArgumentException e) {
            logger.error("Failed to transfer funds: {}", e.getMessage());
            throw e;
        } catch (SecurityException e) {
            logger.error("Security error: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("An unexpected error occurred while transferring funds from account with ID: {} to account with ID: {}", fromAccountId, toAccountId, e);
            throw new RuntimeException("Unable to transfer funds at this time. Please try again later.");
        }
    }
    public BankAccount editAccount(Long accountId, Long userId, BankAccount updatedAccountDetails) {
        BankAccount existingAccount = getAccount(accountId, userId);
        existingAccount.setBalance(updatedAccountDetails.getBalance());
        existingAccount.setUserId(updatedAccountDetails.getUserId());
        return bankAccountRepository.save(existingAccount);
    }

    // Delete Bank Account
    public void deleteAccount(Long accountId, Long userId) {
        BankAccount existingAccount = getAccount(accountId, userId);
        bankAccountRepository.delete(existingAccount);
    }

}
