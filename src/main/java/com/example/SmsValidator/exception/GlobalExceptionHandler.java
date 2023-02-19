package com.example.SmsValidator.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<?> handleException2(Exception exception) {
        return ResponseEntity.badRequest().body(new ErrorResponse(exception));
    }

    @ExceptionHandler(ServletRequestBindingException.class)
    public ResponseEntity<?> handleException3(Exception exception) {
        return ResponseEntity.badRequest().body(new ErrorResponse(exception.getLocalizedMessage()));

    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception exception) {
        return ResponseEntity.badRequest().body(new ErrorResponse());
    }

}