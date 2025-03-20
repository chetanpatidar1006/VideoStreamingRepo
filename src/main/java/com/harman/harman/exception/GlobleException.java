package com.harman.harman.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static com.harman.harman.constant.Constants.DATA_NOT_FOUND;
import static com.harman.harman.constant.Constants.INTERNAL_SERVER_ERROR;

@ControllerAdvice
public class GlobleException {

    private static final Logger logger = LoggerFactory.getLogger(GlobleException.class);

    @ExceptionHandler(ResourceNotFound.class)
    public ResponseEntity<ErrorResponse> handleVideoNotFoundException(ResourceNotFound ex) {
        logger.debug("Resource not found exception: {}", ex.getStackTrace());
        ErrorResponse errorResponse = new ErrorResponse(DATA_NOT_FOUND, ex.getMessage());
        logger.info("Returning NOT_FOUND response: {}", errorResponse.toString());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex) {
        logger.error("Internal server error: {}", ex.getStackTrace());
        ErrorResponse errorResponse = new ErrorResponse(INTERNAL_SERVER_ERROR, "Something went wrong.");
        logger.info("Returning INTERNAL_SERVER_ERROR response: {}", errorResponse.toString());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
