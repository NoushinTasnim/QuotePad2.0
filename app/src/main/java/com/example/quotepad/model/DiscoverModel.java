package com.example.quotepad.model;

public class

DiscoverModel {
    private String quote, date, type, author, id;

    DiscoverModel(){

    }

    public DiscoverModel(String quote, String date, String type, String author, String id) {
        this.quote = quote;
        this.date = date;
        this.type = type;
        this.author = author;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
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
}
