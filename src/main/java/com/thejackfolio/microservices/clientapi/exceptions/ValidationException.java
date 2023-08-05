package com.thejackfolio.microservices.clientapi.exceptions;

public class ValidationException extends Exception{

    public ValidationException(String message){
        super(message);
    }
}
