package ru.bibrus.simbirgo.payment;

import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import ru.bibrus.simbirgo.account.Account;
import ru.bibrus.simbirgo.account.AccountRepository;

@RestController
public class PaymentController {

    private static final long MONETARY_UNITS = 250_000;

    private final AccountRepository accountRepository;

    public PaymentController(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Transactional
    @PostMapping("/api/Payment/Hesoyam/{accountId}")
    public ResponseEntity<Account> topUpAccountBalance(@PathVariable long accountId) {
        Account account = accountRepository.getReferenceById(accountId);
        if (account.isAdmin()) {
            account.topUpBalance(MONETARY_UNITS);
            accountRepository.save(account);
        }
        return ResponseEntity.ok(account);
    }

}