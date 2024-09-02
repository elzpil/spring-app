package com.example.demo.tests;


import com.example.demo.bankAccount.BankAccount;
import com.example.demo.bankAccount.BankAccountRepository;
import com.example.demo.bankAccount.BankAccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class BankAccountServiceTests {

    @Mock
    private BankAccountRepository bankAccountRepository;

    @InjectMocks
    private BankAccountService bankAccountService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createAccount_ShouldReturnCreatedAccount() {
        // Arrange
        String id = "id1";
        double initialBalance = 100.0;
        BankAccount account = new BankAccount(id, initialBalance);

        when(bankAccountRepository.save(any(BankAccount.class))).thenReturn(account);

        // Act
        BankAccount createdAccount = bankAccountService.createAccount(id, initialBalance);

        // Assert
        assertNotNull(createdAccount);
        assertEquals(id, createdAccount.getId());
        assertEquals(initialBalance, createdAccount.getBalance());
    }

    @Test
    void deposit_ShouldIncreaseBalance() {
        // Arrange
        String id = "id1";
        double initialBalance = 100.0;
        double depositAmount = 50.0;
        BankAccount account = new BankAccount(id, initialBalance);

        when(bankAccountRepository.findById(id)).thenReturn(java.util.Optional.of(account));

        // Act
        bankAccountService.deposit(id, depositAmount);

        // Assert
        assertEquals(150.0, account.getBalance());
    }

    @Test
    void withdraw_ShouldDecreaseBalance() {
        // Arrange
        String id = "id1";
        double initialBalance = 100.0;
        double withdrawAmount = 50.0;
        BankAccount account = new BankAccount(id, initialBalance);

        when(bankAccountRepository.findById(id)).thenReturn(java.util.Optional.of(account));

        // Act
        bankAccountService.withdraw(id, withdrawAmount);

        // Assert
        assertEquals(50.0, account.getBalance());
    }

    @Test
    void transfer_ShouldTransferFundsBetweenAccounts() {
        // Arrange
        String fromAccountId = "id1";
        String toAccountId = "id2";
        double transferAmount = 50.0;

        BankAccount fromAccount = new BankAccount(fromAccountId, 100.0);
        BankAccount toAccount = new BankAccount(toAccountId, 50.0);

        when(bankAccountRepository.findById(fromAccountId)).thenReturn(java.util.Optional.of(fromAccount));
        when(bankAccountRepository.findById(toAccountId)).thenReturn(java.util.Optional.of(toAccount));

        // Act
        bankAccountService.transfer(fromAccountId, toAccountId, transferAmount);

        // Assert
        assertEquals(50.0, fromAccount.getBalance());
        assertEquals(100.0, toAccount.getBalance());
    }
}
