package com.example.quotepad.model;

public class UserModel {
    String name, username, email,password, id;

    public UserModel() {
    }

    public UserModel(String email, String password, String username) {
        this.email = email;
        this.password = password;
        this.username = username;
    }

    public UserModel(String name, String username, String email, String password,String id) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}