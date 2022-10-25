package com.example.quotepad.model;

public class QuotesModel {
    private String quotes, time, type;

    QuotesModel(){

    }

    public QuotesModel(String quotes, String type, String time) {
        this.quotes = quotes;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
