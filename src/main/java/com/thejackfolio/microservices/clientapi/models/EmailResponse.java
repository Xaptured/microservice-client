package com.thejackfolio.microservices.clientapi.models;

import org.springframework.stereotype.Component;

@Component
public class EmailResponse {

    private String message;

    public EmailResponse() {
    }

    public EmailResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
