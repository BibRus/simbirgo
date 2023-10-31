package ru.bibrus.simbirgo.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

import ru.bibrus.simbirgo.exception.AccountExistsException;

@ControllerAdvice
public class ClientExceptionHandler {

    @ExceptionHandler(value = AccountExistsException.class)
    public ResponseEntity<ExceptionResponse> handleAccountExistsException(AccountExistsException exception) {
        return new ResponseEntity<>(ExceptionResponse
                .builder()
                .message(exception.getMessage())
                .build(),
                HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = UsernameNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleUsernameNotFoundException(UsernameNotFoundException exception) {
        return new ResponseEntity<>(ExceptionResponse
                .builder()
                .message(exception.getMessage())
                .build(),
                HttpStatus.CONFLICT);
    }

}