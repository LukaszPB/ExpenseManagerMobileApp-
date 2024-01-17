package com.example.libraryapp;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class ExpenseRepository {
    private final ExpenseDao expenseDao;
    private final LiveData<List<Expense>> books;
    ExpenseRepository(Application application) {
        ExpenseDatabase database = ExpenseDatabase.getDatabaseInstance(application);
        expenseDao = database.bookDao();
        books = expenseDao.findAll();
    }
    LiveData<List<Expense>> findAllBooks() { return books; }
    void insert(Expense expense) {
        ExpenseDatabase.databaseWriteExecutor.execute(()-> expenseDao.insert(expense));
    }
    void update(Expense expense) {
        ExpenseDatabase.databaseWriteExecutor.execute(()-> expenseDao.update(expense));
    }
    void delete(Expense expense) {
        ExpenseDatabase.databaseWriteExecutor.execute(()-> expenseDao.delete(expense));
    }
}
