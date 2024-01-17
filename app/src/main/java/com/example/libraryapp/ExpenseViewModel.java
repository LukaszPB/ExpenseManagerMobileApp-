package com.example.libraryapp;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ExpenseViewModel extends AndroidViewModel {
    private final ExpenseRepository expenseRepository;
    private final LiveData<List<Expense>> books;
    public ExpenseViewModel(@NonNull Application application) {
        super(application);
        expenseRepository = new ExpenseRepository(application);
        books = expenseRepository.findAllBooks();
    }
    public LiveData<List<Expense>> findAll() { return books; }
    public void insert(Expense expense) { expenseRepository.insert(expense); }
    public void update(Expense expense) { expenseRepository.update(expense); }
    public void delete(Expense expense) { expenseRepository.delete(expense); }
}
