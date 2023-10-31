package ru.bibrus.simbirgo.configuration;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import ru.bibrus.simbirgo.token.TokenRepository;

import lombok.RequiredArgsConstructor;

import java.util.Objects;

import static ru.bibrus.simbirgo.configuration.JwtService.AUTHORIZATION_HEADER;
import static ru.bibrus.simbirgo.configuration.JwtService.TOKEN_INDEX_IN_HEADER;
import static ru.bibrus.simbirgo.configuration.JwtService.TOKEN_TYPE;

@Service
@RequiredArgsConstructor
public class SignOutService implements LogoutHandler {

    private final TokenRepository tokenRepository;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        final String authHeader = request.getHeader(AUTHORIZATION_HEADER);
        final String jwt;
        if (authHeader == null || !authHeader.startsWith(TOKEN_TYPE)) {
            return;
        }
        jwt = authHeader.substring(TOKEN_INDEX_IN_HEADER);
        var storedToken = tokenRepository.findByToken(jwt)
                .orElse(null);
        if (Objects.nonNull(storedToken)) {
            storedToken.setExpired(true);
            storedToken.setRevoked(true);
            tokenRepository.save(storedToken);
            SecurityContextHolder.clearContext();
        }
    }
}