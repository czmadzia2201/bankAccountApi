package com.accounts.model;

import org.springframework.http.HttpStatus;

public class RequestFailed {
    HttpStatus status;
    String message;

    public RequestFailed(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
