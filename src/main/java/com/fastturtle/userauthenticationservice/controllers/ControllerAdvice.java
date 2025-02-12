package com.fastturtle.userauthenticationservice.controllers;

import com.fastturtle.userauthenticationservice.exceptions.UserAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler({UserAlreadyExistsException.class, BadCredentialsException.class})
    private ResponseEntity<String> handleException(Exception ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
