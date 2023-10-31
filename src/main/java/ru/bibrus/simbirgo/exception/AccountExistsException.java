package ru.bibrus.simbirgo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "User with this name is already registered")
public class AccountExistsException extends AuthenticationException {

    public AccountExistsException() {
        super("User with this name is already registered");
    }

}