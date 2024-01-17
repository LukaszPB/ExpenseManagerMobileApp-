package com.example.libraryapp;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Expense.class},version = 2, exportSchema = false)
public abstract class ExpenseDatabase extends RoomDatabase {
    private static ExpenseDatabase databaseInstance;
    static final ExecutorService databaseWriteExecutor = Executors.newSingleThreadExecutor();
    public abstract ExpenseDao bookDao();
    static ExpenseDatabase getDatabaseInstance(final Context context) {
        if(databaseInstance == null) {
            databaseInstance = Room.databaseBuilder(context.getApplicationContext(),
                    ExpenseDatabase.class,"book_database")
                    .addCallback(roomDatabaseCallback)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return databaseInstance;
    }
    private static final RoomDatabase.Callback roomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            databaseWriteExecutor.execute(()->{
                ExpenseDao dao = databaseInstance.bookDao();
                Expense expense = new Expense("Clean Code","Robert C. Martin");
                dao.insert(expense);
            });
        }
    };
}
