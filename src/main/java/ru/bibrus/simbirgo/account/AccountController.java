package ru.bibrus.simbirgo.account;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ru.bibrus.simbirgo.authentication.AuthenticationService;
import ru.bibrus.simbirgo.authentication.SignInRequest;
import ru.bibrus.simbirgo.authentication.SignInResponse;
import ru.bibrus.simbirgo.authentication.SignUpRequest;
import ru.bibrus.simbirgo.authentication.SignUpResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/Account")
public class AccountController {

    private final AccountService accountService;
    private final AuthenticationService authenticationService;

    @PostMapping("/SignUp")
    public ResponseEntity<SignUpResponse> signUp(@RequestBody SignUpRequest request) {
        return new ResponseEntity<>(authenticationService.signUp(request), HttpStatus.CREATED);
    }

    @PostMapping("/SignIn")
    public ResponseEntity<SignInResponse> signIn(@RequestBody SignInRequest request) {
        return new ResponseEntity<>(authenticationService.signIn(request), HttpStatus.OK);
    }

    @GetMapping("/Me")
    public ResponseEntity<AccountInformationResponse> getInformationAboutMe(HttpServletRequest request) {
        return new ResponseEntity<>(accountService.getAuthorizedAccount(request), HttpStatus.OK);

    }

    @PutMapping("/Update")
    public ResponseEntity<String> updateAccount(UpdateAccountRequest request) throws Exception {
        return accountService.updateAccountInformation(request);
    }

}