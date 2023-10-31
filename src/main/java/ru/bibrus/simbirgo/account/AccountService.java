package ru.bibrus.simbirgo.account;

import jakarta.servlet.http.HttpServletRequest;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;

    public AccountInformationResponse getAuthorizedAccount(HttpServletRequest request) {
        String username = request.getUserPrincipal().getName();
        Account account = getAccountByUsername(username);
        return AccountInformationResponse
                .builder()
                .id(account.getId())
                .username(account.getUsername())
                .role(account.getRole())
                .balance(account.getBalance())
                .build();
    }

    @Deprecated(since = "0.0.1")
    public ResponseEntity<String> updateAccountInformation(UpdateAccountRequest request) throws Exception {
        Optional<Account> existAccount = accountRepository.getAccountByUsername(request.getUsername());
        Optional<Account> authorizedAccount = accountRepository.getAccountByUsername(SecurityContextHolder
                .getContext().getAuthentication().getName());
        String password = passwordEncoder.encode(request.getPassword());
        if (authorizedAccount.isPresent()) {
            var account = authorizedAccount.get();
            if (passwordEncoder.matches(account.getPassword(), request.getPassword())) {
                throw new Exception("Passwords match");
            }
            if (request.getPassword().equals(account.getUsername())) {
                throw new Exception("Usernames match");
            }
            account.setPassword(passwordEncoder.encode(request.getPassword()));
            accountRepository.save(account);
        }
        return ResponseEntity.ok("Password changed successfully");
    }

    private Account getAccountByUsername(String username) {
        return accountRepository.getAccountByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Account %s not found".formatted(username)));
    }

    public AccountInformationResponse findById(long id) {
        Account account = accountRepository.getReferenceById(id);
        return AccountInformationResponse
                .builder()
                .id(account.getId())
                .username(account.getUsername())
                .role(account.getRole())
                .balance(account.getBalance())
                .build();
    }

    public void deleteById(long id) {
        accountRepository.deleteById(id);
    }

    public Page<AccountInformationResponse> findAll(int start, int count) {
        Page<Account> accounts = accountRepository.findAll(PageRequest.of(start, count, Sort.Direction.ASC));
        return accounts.map(account -> AccountInformationResponse
                .builder()
                .id(account.getId())
                .username(account.getUsername())
                .role(account.getRole())
                .balance(account.getBalance())
                .build());
    }

}