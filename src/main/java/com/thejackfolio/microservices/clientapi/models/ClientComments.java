/*
 * Copyright (c) 2023.
 * Created this for the project called "TheJackFolio"
 * All right reserved by Jack
 */

package com.thejackfolio.microservices.clientapi.models;

import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Objects;

@Component
public class ClientComments implements Serializable {

    private String email;
    private String comments;
    private boolean replied;
    private String message;

    public ClientComments() {
    }

    public ClientComments(String email, String comments, boolean replied, String message) {
        this.email = email;
        this.comments = comments;
        this.replied = replied;
        this.message = message;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public boolean isReplied() {
        return replied;
    }

    public void setReplied(boolean replied) {
        this.replied = replied;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClientComments that = (ClientComments) o;
        return replied == that.replied && Objects.equals(email, that.email) && Objects.equals(comments, that.comments) && Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, comments, replied, message);
    }

    @Override
    public String toString() {
        return "ClientComments{" +
                "email='" + email + '\'' +
                ", comments='" + comments + '\'' +
                ", replied=" + replied +
                ", message='" + message + '\'' +
                '}';
    }
}
