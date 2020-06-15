package com.herms.taskme.exception;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;


@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(TransactionSystemException.class)
    private ResponseEntity<Object> handleConstraintViolation(Exception ex) {
        Throwable cause = ((TransactionSystemException) ex).getRootCause();
        if (cause instanceof ConstraintViolationException) {
            ConstraintViolationException exception = ((ConstraintViolationException) cause);

            ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST);
            apiError.setMessage("Validation errors \n");
            apiError.addValidationErrors(exception.getConstraintViolations());
            return buildResponseEntity(apiError);
        }

        return buildResponseEntity(null);
    }

    @ExceptionHandler(UsernameAlreadyExistException.class)
    private ResponseEntity<Object> handleUsernameAlreadyExistException(UsernameAlreadyExistException ex) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST);
        apiError.setMessage(ex.getMessage());
        apiError.addSubError(ex.getField(), ex.getMessage());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(BadCredentialsException.class)
    private ResponseEntity<Object> handleBadCredentialsException(BadCredentialsException ex) {
        ApiError apiError = new ApiError(HttpStatus.UNAUTHORIZED);
        apiError.setMessage(ex.getMessage());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(Exception.class)
    private ResponseEntity<Object> handleException(Exception ex) {
    	ex.printStackTrace();
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST);
        apiError.setMessage(ex.getMessage());
        return buildResponseEntity(apiError);
    }

    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

}