package com.example.quotepad.model;

public class QuotesModel {
    private String quote, time, type, author, id;

    QuotesModel(){

    }

    public QuotesModel(String quote, String author) {
        this.quote = quote;
        this.author = author;
    }

    public QuotesModel(String quote, String time, String type, String author, String id) {
        this.quote = quote;
        this.time = time;
        this.type = type;
        this.author = author;
        this.id = id;
    }

    public QuotesModel(String quote, String type, String time) {
        this.quote = quote;
        this.time = time;
        this.type = type;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getQuote() {
        return quote;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
