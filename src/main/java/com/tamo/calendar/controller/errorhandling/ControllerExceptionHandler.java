package com.tamo.calendar.controller.errorhandling;

import com.tamo.calendar.exceptions.CandidateNotFoundException;
import com.tamo.calendar.exceptions.ClientNotFoundException;
import com.tamo.calendar.exceptions.DateNotValidException;
import com.tamo.calendar.exceptions.InterviewerNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

@RestControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {

        List<String> errors = new LinkedList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }

        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }
        ApiError apiError =
                new ApiError(HttpStatus.BAD_REQUEST, errors);

        return handleExceptionInternal(
                ex, apiError, headers, apiError.getStatus(), request);
    }

    @ExceptionHandler({
            CandidateNotFoundException.class,
            InterviewerNotFoundException.class,
            ClientNotFoundException.class
    })
    public ResponseEntity<ApiError> handleContentNotFound(Exception ex) {
        ApiError apiError = new ApiError(
                HttpStatus.NOT_FOUND, ex.getMessage());

        return new ResponseEntity<>(
                apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler({ DateNotValidException.class})
    public ResponseEntity<ApiError> handleDateException(Exception ex) {
        ApiError apiError = new ApiError(
                HttpStatus.BAD_REQUEST, ex.getMessage());

        return new ResponseEntity<>(
                apiError, new HttpHeaders(), apiError.getStatus());
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ApiError apiError = new ApiError(
                HttpStatus.BAD_REQUEST, "Problems deserializing JSON");

        return new ResponseEntity<>(
                apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler({ Exception.class })
    public ResponseEntity<ApiError> handleAll() {
        ApiError apiError = new ApiError(
                HttpStatus.INTERNAL_SERVER_ERROR, "error occurred");

        return new ResponseEntity<>(
                apiError, new HttpHeaders(), apiError.getStatus());
    }
}
