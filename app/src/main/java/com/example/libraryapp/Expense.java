package com.example.libraryapp;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "expense")
public class Expense {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String price;

    public Expense(String name, String price) {
        this.name = name;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public void setTitle(String name) {
        this.name = name;
    }

    public void setAuthor(String price) {
        this.price = price;
    }

    public void setId(int id) {
        this.id = id;
    }
}
