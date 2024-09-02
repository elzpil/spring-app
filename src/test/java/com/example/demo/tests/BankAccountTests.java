package com.example.demo.tests;



import com.example.demo.bankAccount.BankAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BankAccountTests {

    private BankAccount bankAccount;

    @BeforeEach
    void setUp() {
        bankAccount = new BankAccount("id1", 100.0);
    }

    @Test
    void deposit_ShouldIncreaseBalance_WhenAmountIsPositive() {
        // Act
        bankAccount.deposit(50.0);

        // Assert
        assertEquals(150.0, bankAccount.getBalance());
    }

    @Test
    void deposit_ShouldNotChangeBalance_WhenAmountIsZeroOrNegative() {
        // Act
        bankAccount.deposit(0.0);
        bankAccount.deposit(-50.0);

        // Assert
        assertEquals(100.0, bankAccount.getBalance());
    }

    @Test
    void withdraw_ShouldDecreaseBalance_WhenAmountIsPositiveAndLessThanOrEqualToBalance() {
        // Act
        bankAccount.withdraw(50.0);

        // Assert
        assertEquals(50.0, bankAccount.getBalance());
    }

    @Test
    void withdraw_ShouldNotChangeBalance_WhenAmountIsZeroOrNegative() {
        // Act
        bankAccount.withdraw(0.0);
        bankAccount.withdraw(-50.0);

        // Assert
        assertEquals(100.0, bankAccount.getBalance());
    }

    @Test
    void withdraw_ShouldNotChangeBalance_WhenAmountExceedsBalance() {
        // Act
        bankAccount.withdraw(150.0);

        // Assert
        assertEquals(100.0, bankAccount.getBalance());
    }

    @Test
    void transfer_ShouldDecreaseBalanceOfSourceAccountAndIncreaseBalanceOfTargetAccount_WhenAmountIsValid() {
        // Arrange
        BankAccount targetAccount = new BankAccount("id2", 50.0);

        // Act
        bankAccount.transfer(50.0, targetAccount);

        // Assert
        assertEquals(50.0, bankAccount.getBalance());
        assertEquals(100.0, targetAccount.getBalance());
    }

    @Test
    void transfer_ShouldNotChangeBalances_WhenAmountIsZeroOrNegative() {
        // Arrange
        BankAccount targetAccount = new BankAccount("id2", 50.0);

        // Act
        bankAccount.transfer(0.0, targetAccount);
        bankAccount.transfer(-50.0, targetAccount);

        // Assert
        assertEquals(100.0, bankAccount.getBalance());
        assertEquals(50.0, targetAccount.getBalance());
    }

    @Test
    void transfer_ShouldNotChangeBalances_WhenAmountExceedsSourceAccountBalance() {
        // Arrange
        BankAccount targetAccount = new BankAccount("id2", 50.0);

        // Act
        bankAccount.transfer(150.0, targetAccount);

        // Assert
        assertEquals(100.0, bankAccount.getBalance());
        assertEquals(50.0, targetAccount.getBalance());
    }
}
