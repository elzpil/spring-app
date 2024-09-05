package com.example.demo.bankAccount;

import com.example.demo.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/bankAccount")
public class BankAccountController {

    private final BankAccountService bankAccountService;
    private final JwtUtil jwtUtil;

    @Autowired
    public BankAccountController(BankAccountService bankAccountService, JwtUtil jwtUtil) {
        this.bankAccountService = bankAccountService;
        this.jwtUtil = jwtUtil;
    }

    private Long getUserIdFromToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return null;  // Authorization header is missing or invalid
        }

        String token = authorizationHeader.substring(7);
        return jwtUtil.extractUserId(token);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createAccount(HttpServletRequest request, @RequestBody BankAccount bankAccount) {
        Long userId = getUserIdFromToken(request);
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authorization header is missing or invalid.");
        }
        BankAccount createdAccount = bankAccountService.createAccount(userId, bankAccount.getBalance());
        return ResponseEntity.ok(createdAccount);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAccount(@PathVariable Long id, HttpServletRequest request) {
        Long userId = getUserIdFromToken(request);
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authorization header is missing or invalid.");
        }
        try {
            BankAccount account = bankAccountService.getAccount(id, userId);
            return ResponseEntity.ok(account);
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied: The account does not belong to the user.");
        }
    }

    @PostMapping("/deposit")
    public ResponseEntity<?> deposit(@RequestBody DepositRequest depositRequest, HttpServletRequest request) {
        Long userId = getUserIdFromToken(request);
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authorization header is missing or invalid.");
        }
        boolean success = bankAccountService.deposit(depositRequest.getId(), depositRequest.getAmount(), userId);
        if (success) {
            return ResponseEntity.ok("Deposit successful");
        } else {
            return ResponseEntity.badRequest().body("Deposit unsuccessful: Amount must be more than zero");
        }
    }

    @PostMapping("/withdraw")
    public ResponseEntity<?> withdraw(@RequestBody DepositRequest depositRequest, HttpServletRequest request) {
        Long userId = getUserIdFromToken(request);
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authorization header is missing or invalid.");
        }
        boolean success = bankAccountService.withdraw(depositRequest.getId(), depositRequest.getAmount(), userId);
        if (success) {
            return ResponseEntity.ok("Withdrawal successful");
        } else {
            return ResponseEntity.badRequest().body("Withdrawal unsuccessful");
        }
    }

    @PostMapping("/transfer")
    public ResponseEntity<?> transfer(@RequestBody TransferRequest transferRequest, HttpServletRequest request) {
        Long userId = getUserIdFromToken(request);
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authorization header is missing or invalid.");
        }
        boolean success = bankAccountService.transfer(
                transferRequest.getFromAccountId(),
                transferRequest.getToAccountId(),
                transferRequest.getAmount(),
                userId
        );
        if (success) {
            return ResponseEntity.ok("Transfer successful");
        } else {
            return ResponseEntity.badRequest().body("Transfer unsuccessful");
        }
    }
    @PutMapping("/edit")
    public ResponseEntity<?> editAccount(@RequestBody BankAccount bankAccount, HttpServletRequest request) {
        Long userId = getUserIdFromToken(request);
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authorization header is missing or invalid.");
        }

        try {
            BankAccount updatedAccount = bankAccountService.editAccount(bankAccount.getId(), userId, bankAccount);
            return ResponseEntity.ok(updatedAccount);
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied: The account does not belong to the user.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteAccount(@PathVariable Long id, HttpServletRequest request) {
        Long userId = getUserIdFromToken(request);
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authorization header is missing or invalid.");
        }

        try {
            bankAccountService.deleteAccount(id, userId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();  // Return no content on successful deletion
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied: The account does not belong to the user.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
