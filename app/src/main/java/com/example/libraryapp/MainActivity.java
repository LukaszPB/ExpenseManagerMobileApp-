package com.example.libraryapp;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ExpenseViewModel expenseViewModel;
    public static final int NEW_BOOK_ACTIVITY_REQUEST_CODE = 1;
    public static final int EDIT_BOOK_ACTIVITY_REQUEST_CODE = 2;
    private Expense editedExpense = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final BookAdapter adapter = new BookAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        expenseViewModel = ViewModelProviders.of(this).get(ExpenseViewModel.class);
        expenseViewModel.findAll().observe(this, new Observer<List<Expense>>() {
            @Override
            public void onChanged(@Nullable final List<Expense> expenses) {
                adapter.setBooks(expenses);
            }
        });

        FloatingActionButton addBookButton = findViewById(R.id.add_button);
        addBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditBookActivity.class);
                startActivityForResult(intent, NEW_BOOK_ACTIVITY_REQUEST_CODE);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == NEW_BOOK_ACTIVITY_REQUEST_CODE) {
                Expense expense = new Expense(data.getStringExtra(EditBookActivity.EXTRA_EDIT_BOOK_TITLE),
                        data.getStringExtra(EditBookActivity.EXTRA_EDIT_BOOK_AUTHOR));
                expenseViewModel.insert(expense);
                Snackbar.make(findViewById(R.id.coordinator_layout),
                                getString(R.string.expense_added),
                                Snackbar.LENGTH_LONG)
                        .show();
            }
            else if (requestCode == EDIT_BOOK_ACTIVITY_REQUEST_CODE) {
                editedExpense.setTitle(data.getStringExtra(EditBookActivity.EXTRA_EDIT_BOOK_TITLE));
                editedExpense.setAuthor(data.getStringExtra(EditBookActivity.EXTRA_EDIT_BOOK_AUTHOR));
                expenseViewModel.update(editedExpense);
                editedExpense = null;
                Snackbar.make(findViewById(R.id.coordinator_layout),
                                getString(R.string.expense_edited),
                                Snackbar.LENGTH_LONG)
                        .show();
            }
        }
        else
            Snackbar.make(findViewById(R.id.coordinator_layout),
                            getString(R.string.empty_not_saved),
                            Snackbar.LENGTH_LONG)
                    .show();
    }

    private class BookHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        private TextView bookTitleTextView;
        private TextView bookAuthorTextView;
        private Expense expense;

        public BookHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.expense_list_item, parent, false));
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

            bookTitleTextView = itemView.findViewById(R.id.book_title);
            bookAuthorTextView = itemView.findViewById(R.id.book_author);
        }

        public void bind(Expense expense) {
            this.expense = expense;
            bookTitleTextView.setText(expense.getName());
            bookAuthorTextView.setText(expense.getPrice());
        }

        @Override
        public void onClick(View v) {
            MainActivity.this.editedExpense = this.expense;
            Intent intent = new Intent(MainActivity.this, EditBookActivity.class);
            intent.putExtra(EditBookActivity.EXTRA_EDIT_BOOK_TITLE, expense.getName());
            intent.putExtra(EditBookActivity.EXTRA_EDIT_BOOK_AUTHOR, expense.getPrice());
            startActivityForResult(intent, EDIT_BOOK_ACTIVITY_REQUEST_CODE);
        }

        @Override
        public boolean onLongClick(View v) {
            MainActivity.this.expenseViewModel.delete(this.expense);
            return true;
        }
    }

    private class BookAdapter extends RecyclerView.Adapter<BookHolder> {
        private List<Expense> expenses;

        @NonNull
        @Override
        public BookHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new BookHolder(getLayoutInflater(), parent);
        }

        @Override
        public void onBindViewHolder(@NonNull BookHolder holder, int position) {
            if (expenses != null) {
                Expense expense = expenses.get(position);
                holder.bind(expense);
            }
            else
                Log.d("MainActivity", "No books");
        }

        @Override
        public int getItemCount() {
            if (expenses != null)
                return expenses.size();
            return 0;
        }

        void setBooks(List<Expense> expenses) {
            this.expenses = expenses;
            notifyDataSetChanged();
        }
    }
}