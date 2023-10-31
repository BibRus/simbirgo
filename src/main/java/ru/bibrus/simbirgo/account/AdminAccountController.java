package ru.bibrus.simbirgo.account;

import org.springframework.data.domain.Page;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/Admin/")
@RequiredArgsConstructor
public class AdminAccountController {

    private final AccountService accountService;

    @GetMapping("/Account")
    public Page<AccountInformationResponse> getInformationAllAccounts(
            @RequestParam("start") int start,
            @RequestParam("count") int count) {
        return accountService.findAll(start, count);
    }

    @GetMapping("/Account/{id}")
    public ResponseEntity<AccountInformationResponse> getInformationAccountById(@PathVariable long id) {
        return new ResponseEntity<>(accountService.findById(id), HttpStatus.OK);
    }

    @DeleteMapping("/Account/{id}")
    public void deleteById(@PathVariable long id) {
        accountService.deleteById(id);
    }



}