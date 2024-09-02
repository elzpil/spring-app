package com.example.demo.bankAccount;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/bankAccount")
public class BankAccountController {

    private final BankAccountService bankAccountService;

    @Autowired
    public BankAccountController(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }



    @PostMapping("/create")
    public ResponseEntity<BankAccount> createAccount(@RequestBody BankAccount bankAccount) {
        System.out.println("==================id " + bankAccount.getId() +" ==================== " + bankAccount.getBalance());
        BankAccount createdAccount = bankAccountService.createAccount(bankAccount.getId(), bankAccount.getBalance());
        return ResponseEntity.ok(createdAccount);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BankAccount> getAccount(@PathVariable String id) {
        BankAccount account = bankAccountService.getAccount(id);
        return ResponseEntity.ok(account);
    }

    @PostMapping("/deposit")
    public ResponseEntity<String> deposit(@RequestBody DepositRequest depositRequest) {
        bankAccountService.deposit(depositRequest.getId(), depositRequest.getAmount());
        return ResponseEntity.ok("Deposit successful");
    }


    @PostMapping("/withdraw")
    public ResponseEntity<String> withdraw(@RequestBody DepositRequest depositRequest) {
        bankAccountService.withdraw(depositRequest.getId(), depositRequest.getAmount());
        return ResponseEntity.ok("Withdrawal successful");
    }

    @PostMapping("/transfer")
    public ResponseEntity<String> transfer(@RequestBody TransferRequest transferRequest) {
        bankAccountService.transfer(
                transferRequest.getFromAccountId(),
                transferRequest.getToAccountId(),
                transferRequest.getAmount()
        );
        return ResponseEntity.ok("Transfer successful");
    }

}
