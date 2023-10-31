package ru.bibrus.simbirgo.payment;

import org.springframework.stereotype.Service;
import ru.bibrus.simbirgo.account.Account;
import ru.bibrus.simbirgo.account.AccountRepository;

@Service
public class PaymentService {

    private static final long MONETARY_UNITS = 250_000;

    private final AccountRepository accountRepository;

    public PaymentService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account topUpAccountBalanceById(long accountId) {
        Account account = accountRepository.getReferenceById(accountId);
        if (account.isAdmin()) {
            account.topUpBalance(MONETARY_UNITS);
            accountRepository.save(account);
        }
        return account;
    }

}
