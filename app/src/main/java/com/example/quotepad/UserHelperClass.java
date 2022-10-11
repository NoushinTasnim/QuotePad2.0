package com.example.quotepad;

public class UserHelperClass {
    String name, phone, username, email,password;

    public UserHelperClass() {
    }
    public UserHelperClass(String name, String username, String email,String password) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
    }
    public UserHelperClass(String phone) {
        this.phone = phone;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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