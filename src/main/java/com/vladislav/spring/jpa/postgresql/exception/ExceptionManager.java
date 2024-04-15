package com.vladislav.spring.jpa.postgresql.exception;

import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class ExceptionManager {

        private static final Logger logger = LoggerFactory.getLogger(ExceptionManager.class);

        @ExceptionHandler(ObjectNotFoundException.class)
        public ResponseEntity<ErrorMessage> handleObjectNotFoundException(
                        ObjectNotFoundException ex, WebRequest request) {
                String defaultMessage = "Object not found";
                String message = ex.getMessage() != null ? ex.getMessage() : defaultMessage;
                ErrorMessage errorMessage = new ErrorMessage(
                                HttpStatus.NOT_FOUND.value(),
                                new Date(),
                                message,
                                request.getDescription(false));
                logger.error("ObjectNotFoundException occurred: {}", errorMessage);
                return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
        }

        @ExceptionHandler(ObjectExistedException.class)
        public ResponseEntity<ErrorMessage> handleObjectExistedException(
                        ObjectExistedException ex, WebRequest request) {
                String defaultMessage = "Object already exists";
                String message = ex.getMessage() != null ? ex.getMessage() : defaultMessage;
                ErrorMessage errorMessage = new ErrorMessage(
                                HttpStatus.CONFLICT.value(),
                                new Date(),
                                message,
                                request.getDescription(false));
                logger.error("ObjectExistedException occurred: {}", errorMessage);
                return new ResponseEntity<>(errorMessage, HttpStatus.CONFLICT);
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<ErrorMessage> handleGlobalException(
                        Exception ex, WebRequest request) {
                String defaultMessage = "Internal Server Error";
                String message = ex.getMessage() != null ? ex.getMessage() : defaultMessage;
                ErrorMessage errorMessage = new ErrorMessage(
                                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                new Date(),
                                message,
                                request.getDescription(false));
                logger.error("Exception occurred: {}", errorMessage);
                return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
        public ResponseEntity<ErrorMessage> handleMethodNotSupportedException(
                        HttpRequestMethodNotSupportedException ex, WebRequest request) {
                ErrorMessage errorMessage = new ErrorMessage(
                                HttpStatus.METHOD_NOT_ALLOWED.value(),
                                new Date(),
                                ex.getMessage(),
                                request.getDescription(false));
                logger.error("HttpRequestMethodNotSupportedException occurred: {}", errorMessage);
                return new ResponseEntity<>(errorMessage, HttpStatus.METHOD_NOT_ALLOWED);
        }

        @ExceptionHandler(MissingServletRequestParameterException.class)
        public ResponseEntity<ErrorMessage> handleMissingServletRequestParameterException(
                        MissingServletRequestParameterException ex, WebRequest request) {
                ErrorMessage errorMessage = new ErrorMessage(
                                HttpStatus.BAD_REQUEST.value(),
                                new Date(),
                                ex.getMessage(),
                                "Required parameter is missing");
                logger.error("MissingServletRequestParameterException occurred: {}", errorMessage);
                return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
        }

        @ExceptionHandler(BadRequestException.class)
        public ResponseEntity<ErrorMessage> handleBadRequestException(
                        BadRequestException ex, WebRequest request) {
                String defaultMessage = "Bad Request";
                String message = ex.getMessage() != null ? ex.getMessage() : defaultMessage;
                ErrorMessage errorMessage = new ErrorMessage(
                                HttpStatus.BAD_REQUEST.value(),
                                new Date(),
                                message,
                                request.getDescription(false));
                logger.error("BadRequestException occurred: {}", errorMessage);
                return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
        }
}
