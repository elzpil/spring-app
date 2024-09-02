package com.example.demo.tests;

import com.example.demo.bankAccount.*;
import com.example.demo.config.SecurityConfig;
import com.example.demo.config.auth.SecurityFilter;
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

@Import({SecurityConfig.class, TokenProvider.class, SecurityFilter.class})
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

    @Test
    void createAccount_ShouldReturnCreatedAccount() throws Exception {
        // Arrange
        BankAccount account = new BankAccount("id1", 100.0);
        when(bankAccountService.createAccount(any(String.class), any(Double.class)))
                .thenReturn(account);

        // Act & Assert
        mockMvc.perform(post("/api/v1/bankAccount/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(account)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(account)));
    }



    @Test
    void deposit_ShouldReturnSuccessMessage() throws Exception {
        // Arrange
        DepositRequest depositRequest = new DepositRequest();
        depositRequest.setId("id1");
        depositRequest.setAmount(50.0);

        // Act & Assert
        mockMvc.perform(post("/api/v1/bankAccount/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(depositRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("Deposit successful"));
    }

    @Test
    void transfer_ShouldReturnSuccessMessage() throws Exception {
        // Arrange
        TransferRequest transferRequest = new TransferRequest();
        transferRequest.setFromAccountId("id1");
        transferRequest.setToAccountId("id2");
        transferRequest.setAmount(50.0);

        // Act & Assert
        mockMvc.perform(post("/api/v1/bankAccount/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transferRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("Transfer successful"));
    }
}
