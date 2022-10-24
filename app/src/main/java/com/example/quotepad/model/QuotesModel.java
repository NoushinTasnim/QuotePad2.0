package com.example.quotepad.model;

public class QuotesModel {
    private String quotes, user, time, type;

    QuotesModel(){

    }

    public QuotesModel(String quotes, String user, String time, String type) {
        this.quotes = quotes;
        this.user = user;
        this.time = time;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getQuotes() {
        return quotes;
    }

    public void setQuotes(String quotes) {
        this.quotes = quotes;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
