package com.example.libraryapp;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import java.util.Date;

@Entity(tableName = "expense")
@TypeConverters(Expense.DateConverter.class)
public class Expense {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private int price;
    private Date date;

    public Expense(String name, int price, Date date) {
        this.name = name;
        this.price = price;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }
    public Date getDate() { return date; }

    public void setTitle(String name) {
        this.name = name;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setId(int id) {
        this.id = id;
    }
    public void setDate(Date date) { this.date = date; }
    public static class DateConverter {
        @TypeConverter
        public static Date fromTimestamp(Long value) {
            return value == null ? null : new Date(value);
        }

        @TypeConverter
        public static Long dateToTimestamp(Date date) {
            return date == null ? null : date.getTime();
        }
    }
}
