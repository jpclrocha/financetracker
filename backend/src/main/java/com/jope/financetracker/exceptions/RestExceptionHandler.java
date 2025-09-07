package com.jope.financetracker.exceptions;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.jope.financetracker.dto.api_error.ApiErrorDTO;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

        @ExceptionHandler(ResourceNotFoundException.class)
        protected ResponseEntity<ApiErrorDTO> handleResourceNotFoundException(ResourceNotFoundException e,
                        WebRequest request) {
                HttpStatus status = HttpStatus.NOT_FOUND;
                String path = ((ServletWebRequest) request).getRequest().getRequestURI();
                ApiErrorDTO error = new ApiErrorDTO(
                                status,
                                e.getMessage(),
                                path,
                                Instant.now());
                return new ResponseEntity<>(error, status);
        }

        @ExceptionHandler(DatabaseException.class)
        protected ResponseEntity<ApiErrorDTO> handleDatabaseException(DatabaseException e, WebRequest request) {
            HttpStatus status = HttpStatus.BAD_REQUEST;
            String path = ((ServletWebRequest) request).getRequest().getRequestURI();
            ApiErrorDTO error = new ApiErrorDTO(
                    status,
                    e.getMessage(),
                    path,
                    Instant.now());
            return new ResponseEntity<>(error, status);
        }

        @ExceptionHandler(InvalidTokenException.class)
        protected ResponseEntity<ApiErrorDTO> handleInvalidTokenException(InvalidTokenException e, WebRequest request) {
            HttpStatus status = HttpStatus.BAD_REQUEST;
            String path = ((ServletWebRequest) request).getRequest().getRequestURI();
            ApiErrorDTO error = new ApiErrorDTO(
                    status,
                    e.getMessage(),
                    path,
                    Instant.now());
            return new ResponseEntity<>(error, status);
        }

        @ExceptionHandler(AccessDeniedException.class)
        protected ResponseEntity<ApiErrorDTO> handleAccessDeniedException(AccessDeniedException e,
                        WebRequest request) {
                HttpStatus status = HttpStatus.NOT_FOUND;
                String path = ((ServletWebRequest) request).getRequest().getRequestURI();
                ApiErrorDTO error = new ApiErrorDTO(
                                status,
                                e.getMessage(),
                                path,
                                Instant.now());
                return new ResponseEntity<>(error, status);
        }

        @ExceptionHandler(ResponseStatusException.class)
        protected ResponseEntity<ApiErrorDTO> handleResponseStatusException(ResponseStatusException e,
                        WebRequest request) {
                HttpStatus status = HttpStatus.NOT_FOUND;
                String path = ((ServletWebRequest) request).getRequest().getRequestURI();
                ApiErrorDTO error = new ApiErrorDTO(
                                status,
                                e.getMessage(),
                                path,
                                Instant.now());
                return new ResponseEntity<>(error, status);
        }

        @ExceptionHandler(IllegalArgumentException.class)
        protected ResponseEntity<ApiErrorDTO> handleIllegalArgumentException(IllegalArgumentException e,
                        WebRequest request) {
                HttpStatus status = HttpStatus.NOT_FOUND;
                String path = ((ServletWebRequest) request).getRequest().getRequestURI();
                ApiErrorDTO error = new ApiErrorDTO(
                                status,
                                e.getMessage(),
                                path,
                                Instant.now());
                return new ResponseEntity<>(error, status);
        }
}
