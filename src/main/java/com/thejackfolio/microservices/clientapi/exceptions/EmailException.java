package com.thejackfolio.microservices.clientapi.exceptions;

public class EmailException extends  Exception{

    public EmailException(String message, Throwable cause){
        super(message, cause);
    }
    public EmailException(String message){
        super(message);
    }
}
