package com.rsskey.server.Controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

public class APIError {

    public HttpStatus status;
    public LocalDateTime timestamp;
    public String message;
    public String debugMessage;

    public static ResponseEntity unauthorizedResponse() {
        return new ResponseEntity( new APIError(HttpStatus.UNAUTHORIZED, "Request Unauthorized"), HttpStatus.UNAUTHORIZED);
    }

    private APIError() {
        timestamp = LocalDateTime.now();
    }

    public APIError(HttpStatus status) {
        this();
        this.status = status;
    }

    public APIError(HttpStatus status, Throwable ex) {
        this();
        this.status = status;
        this.message = "Unexpected error";
        this.debugMessage = ex.getLocalizedMessage();
    }

    public APIError(HttpStatus status, String message) {
        this();
        this.status = status;
        this.message = message;
    }

    public APIError(HttpStatus status, String message, Throwable ex) {
        this();
        this.status = status;
        this.message = message;
        this.debugMessage = ex.getLocalizedMessage();
    }
}
