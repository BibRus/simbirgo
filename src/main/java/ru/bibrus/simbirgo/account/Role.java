package ru.bibrus.simbirgo.account;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

public enum Role {
    USER,
    ADMIN;

    public static Set<SimpleGrantedAuthority> getAuthorities() {
        return Set.of(Role.values())
                .stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                .collect(Collectors.toSet());

    }
}