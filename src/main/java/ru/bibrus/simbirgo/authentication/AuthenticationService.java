package ru.bibrus.simbirgo.authentication;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import ru.bibrus.simbirgo.account.Account;
import ru.bibrus.simbirgo.account.AccountRepository;
import ru.bibrus.simbirgo.account.Role;
import ru.bibrus.simbirgo.configuration.JwtService;
import ru.bibrus.simbirgo.exception.AccountExistsException;
import ru.bibrus.simbirgo.token.Token;
import ru.bibrus.simbirgo.token.TokenRepository;
import ru.bibrus.simbirgo.token.TokenType;

import java.io.IOException;
import java.util.Objects;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

import static ru.bibrus.simbirgo.configuration.JwtService.TOKEN_INDEX_IN_HEADER;
import static ru.bibrus.simbirgo.configuration.JwtService.TOKEN_TYPE;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final JwtService jwtService;
    private final AccountRepository accountRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    private static final Role DEFAULT_ROLE = Role.USER;

    public SignUpResponse signUp(SignUpRequest request) {
        var account = Account.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(DEFAULT_ROLE)
                .build();
        if (accountRepository.existsAccountByUsername(request.getUsername())) {
            throw new AccountExistsException();
        }
        accountRepository.save(account);
        return SignUpResponse
                .builder()
                .message("User has been successfully registered")
                .build();
    }

    public SignInResponse signIn(SignInRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        var account = accountRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        var jwtToken = jwtService.generateToken(account);
        var refreshToken = jwtService.generateRefreshToken(account);
        this.revokeAllAccountsTokens(account);
        this.saveAccountToken(account, jwtToken);
        return SignInResponse
                .builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    private void saveAccountToken(Account account, String jwtToken) {
        var token = Token.builder()
                .account(account)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllAccountsTokens(Account account) {
        var validUserTokens = tokenRepository.findAllValidTokenByAccount(account);
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String username;
        if (Objects.isNull(authHeader) || !authHeader.startsWith(TOKEN_TYPE)) {
            return;
        }
        refreshToken = authHeader.substring(TOKEN_INDEX_IN_HEADER);
        username = jwtService.extractUsername(refreshToken);
        if (Objects.nonNull(username)) {
            var user = this.accountRepository.getAccountByUsername(username).orElseThrow();
            if (jwtService.isTokenValid(refreshToken, user)) {
                var accessToken = jwtService.generateToken(user);
                revokeAllAccountsTokens(user);
                saveAccountToken(user, accessToken);
                var authResponse = SignInResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }

}