package ru.bibrus.simbirgo.configuration;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import ru.bibrus.simbirgo.token.TokenRepository;

import org.springframework.lang.NonNull;

import lombok.RequiredArgsConstructor;

import java.util.Objects;
import java.io.IOException;

import static ru.bibrus.simbirgo.configuration.JwtService.AUTHORIZATION_HEADER;
import static ru.bibrus.simbirgo.configuration.JwtService.TOKEN_INDEX_IN_HEADER;
import static ru.bibrus.simbirgo.configuration.JwtService.TOKEN_TYPE;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final TokenRepository tokenRepository;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        final String authenticationHeader = request.getHeader(AUTHORIZATION_HEADER);
        if (Objects.isNull(authenticationHeader) || !authenticationHeader.startsWith(TOKEN_TYPE)) {
            filterChain.doFilter(request, response);
            return;
        }

        final String jwt = authenticationHeader.substring(TOKEN_INDEX_IN_HEADER);
        final String username = jwtService.extractUsername(jwt);
        if (Objects.nonNull(username) && Objects.isNull(SecurityContextHolder.getContext().getAuthentication())) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            if (isTokenValid(jwt, userDetails)) {
                setAuthenticationContext(userDetails, request);
            }
        }
        filterChain.doFilter(request, response);
    }

    private boolean isTokenValid(String jwt, UserDetails userDetails) {
        return jwtService.isTokenValid(jwt, userDetails) && tokenRepository.findByToken(jwt)
                .map(token -> !token.isExpired() && !token.isRevoked())
                .orElse(false);
    }
    
    private void setAuthenticationContext(UserDetails userDetails, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }
    
}