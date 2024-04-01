package com.vladislav.spring.jpa.postgresql.exception;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class ExceptionManager {

        private static final Logger logger = LoggerFactory.getLogger(ExceptionManager.class);

        @ExceptionHandler(ObjectNotFoundException.class)
        public ResponseEntity<ErrorMessage> objectNotFoundException(
                        final ObjectNotFoundException ex, final WebRequest request) {
                ErrorMessage message = new ErrorMessage(
                                HttpStatus.NOT_FOUND.value(),
                                new Date(),
                                ex.getMessage(),
                                request.getDescription(false));

                logger.error("ObjectNotFoundException occurred: {}", message);
                return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }

        @ExceptionHandler(ObjectExistedException.class)
        public ResponseEntity<ErrorMessage> objectExistedException(
                        final ObjectExistedException ex, final WebRequest request) {
                ErrorMessage message = new ErrorMessage(
                                HttpStatus.CONFLICT.value(),
                                new Date(),
                                ex.getMessage(),
                                request.getDescription(false));

                logger.error("ObjectExistedException occurred: {}", message);
                return new ResponseEntity<>(message, HttpStatus.CONFLICT);
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<ErrorMessage> globalExceptionHandler(
                        final Exception ex, final WebRequest request) {
                ErrorMessage message = new ErrorMessage(
                                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                new Date(),
                                ex.getMessage(),
                                request.getDescription(false));

                logger.error("Exception occurred: {}", message);
                return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
        public ResponseEntity<ErrorMessage> handleMethodNotSupportedException(
                        final HttpRequestMethodNotSupportedException ex, final WebRequest request) {
                ErrorMessage message = new ErrorMessage(
                                HttpStatus.METHOD_NOT_ALLOWED.value(),
                                new Date(),
                                ex.getMessage(),
                                request.getDescription(false));

                logger.error("HttpRequestMethodNotSupportedException occurred: {}", message);
                return new ResponseEntity<>(message, HttpStatus.METHOD_NOT_ALLOWED);
        }
}
