package ru.bibrus.simbirgo.payment;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import ru.bibrus.simbirgo.account.Account;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/api/Payment/Hesoyam/{accountId}")
    public ResponseEntity<Account> topUpAccountBalance(@PathVariable long accountId) {
        Account account = paymentService.topUpAccountBalanceById(accountId);
        return ResponseEntity.ok(account);
    }

}