package com.example.libraryapp;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ExpenseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Expense expense);
    @Update
    void update(Expense expense);
    @Delete
    void delete(Expense expense);
    @Query("DELETE FROM Expense")
    void deleteAll();
    @Query("SELECT * FROM Expense ORDER BY name")
    LiveData<List<Expense>> findAll();
    @Query("SELECT * FROM Expense WHERE name LIke :name")
    List<Expense> findBookWithTitle(String name);
}
