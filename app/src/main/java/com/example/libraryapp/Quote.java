package com.example.libraryapp;

import com.google.gson.annotations.SerializedName;

public class Quote {
    @SerializedName("text")
    private String text;

    @SerializedName("author")
    private String author;

    public String getText() {
        return text;
    }

    public String getAuthor() {
        return author;
    }

    public void setText(String quote) {
        this.text = quote;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
