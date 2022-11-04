package com.example.quotepad.model;

public class UserModel {
    String name, username, email, id;

    public UserModel() {
    }

    public UserModel(String email, String username, String id) {
        this.email = email;
        this.username = username;
        this.id = id;
    }

    public UserModel(String name, String username, String email, String id) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }


    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}