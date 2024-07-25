/*
 * Copyright (c) 2024.
 * Created this for the project called "TheJackFolio"
 * All right reserved by Jack
 */

package com.thejackfolio.microservices.clientapi.models;

import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class Joiner {

    private String name;
    private String email;
    private String description;
    private String message;

    public Joiner() {
    }

    public Joiner(String name, String email, String description) {
        this.name = name;
        this.email = email;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
        Joiner joiner = (Joiner) o;
        return Objects.equals(name, joiner.name) && Objects.equals(email, joiner.email) && Objects.equals(description, joiner.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, email, description);
    }

    @Override
    public String toString() {
        return "Joiner{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
