package com.example.demo.tests;

import com.example.demo.bankAccount.*;
import com.example.demo.config.SecurityConfig;
import com.example.demo.config.auth.TokenProvider;
import com.example.demo.user.UserRepository;
import com.example.demo.user.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import({SecurityConfig.class, TokenProvider.class})
@WebMvcTest(BankAccountController.class)
public class BankAccountControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BankAccountService bankAccountService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private UserService userService;

    private String generateToken() {
        // You should implement token generation with valid credentials
        // This is a placeholder example
        return "Bearer your_valid_token_here";
    }

    @Test
    void createAccount_ShouldReturnCreatedAccount() throws Exception {
        // Arrange
        BankAccount account = new BankAccount(1L, 100.0);
        when(bankAccountService.createAccount(any(Long.class), any(Double.class)))
                .thenReturn(account);

        // Act & Assert
        mockMvc.perform(post("/api/v1/bankAccount/create")
                        .header("Authorization", generateToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(account)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(account)));
    }

    @Test
    void deposit_ShouldReturnSuccessMessage() throws Exception {
        // Arrange
        DepositRequest depositRequest = new DepositRequest();
        depositRequest.setId(1L);
        depositRequest.setAmount(50.0);

        // Act & Assert
        mockMvc.perform(post("/api/v1/bankAccount/deposit")
                        .header("Authorization", generateToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(depositRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("Deposit successful"));
    }

    @Test
    void transfer_ShouldReturnSuccessMessage() throws Exception {
        // Arrange
        TransferRequest transferRequest = new TransferRequest();
        transferRequest.setFromAccountId(1L);
        transferRequest.setToAccountId(2L);
        transferRequest.setAmount(50.0);

        // Act & Assert
        mockMvc.perform(post("/api/v1/bankAccount/transfer")
                        .header("Authorization", generateToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transferRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("Transfer successful"));
    }
}
