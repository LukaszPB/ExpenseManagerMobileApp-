package com.example.libraryapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.provider.MediaStore;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private ExpenseViewModel expenseViewModel;
    public static final int NEW_BOOK_ACTIVITY_REQUEST_CODE = 1;
    public static final int EDIT_BOOK_ACTIVITY_REQUEST_CODE = 2;
    private Expense editedExpense = null;
    private TextView totalCostTextView;
    private Spinner spinner;
    private String selectedValue = "dniu";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final BookAdapter adapter = new BookAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        totalCostTextView = findViewById(R.id.total_cost);
        spinner = findViewById(R.id.spinner);

        String[] spinner_val = {"dniu","miesiącu","roku"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinner_val);

        // Specify the layout to use when the list of choices appears
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinner.setAdapter(spinnerAdapter);

        // Set a listener to handle item selection
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Handle the selected item
                ((TextView) parentView.getChildAt(0)).setTextSize(30);
                ((TextView) parentView.getChildAt(0)).setTypeface(null, Typeface.BOLD);
                selectedValue = spinner_val[position];
                Expense expense = new Expense("test",-1,new Date());
                expenseViewModel.insert(expense);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing here
            }
        });

        expenseViewModel = ViewModelProviders.of(this).get(ExpenseViewModel.class);

        expenseViewModel.findAll().observe(this, new Observer<List<Expense>>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onChanged(@Nullable final List<Expense> expenses) {
                List<Expense> filterExpenses = new ArrayList<>();
                LocalDate currentDate = LocalDate.now();

                for (Expense expense : expenses) {
                    if(expense.getPrice()<0) {
                        expenseViewModel.delete(expense);
                        return;
                    }

                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd", Locale.getDefault());
                    int day = Integer.parseInt(dateFormat.format(expense.getDate()));

                    if (selectedValue.equals("dniu")
                            && day == currentDate.getDayOfMonth()
                            && expense.getDate().getMonth()+1 == currentDate.getMonthValue()
                            && currentDate.getYear() == expense.getDate().getYear()) {
                        filterExpenses.add(expense);
                    } else if (selectedValue.equals("miesiącu")
                            && expense.getDate().getMonth()+1 == currentDate.getMonthValue()
                            && currentDate.getYear() == expense.getDate().getYear()) {
                        filterExpenses.add(expense);
                    }
                    else if (selectedValue.equals("roku") && currentDate.getYear() == expense.getDate().getYear()) {
                        filterExpenses.add(expense);
                    }
                }

                int totalCost = 0;
                for(Expense expense : filterExpenses) {
                    totalCost += expense.getPrice();
                }
                totalCostTextView.setText("suma wydatków: " + totalCost + "zł");
                adapter.setBooks(filterExpenses);
            }
        });

        FloatingActionButton addBookButton = findViewById(R.id.add_button);
        addBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditExpenseActivity.class);
                startActivityForResult(intent, NEW_BOOK_ACTIVITY_REQUEST_CODE);
            }
        });

        FloatingActionButton photoButton = findViewById(R.id.photo_button);
        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent zrobZdjecieIntent = new Intent();
                zrobZdjecieIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                try{
                    Intent intent = new Intent();
                    intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                    //startActivityForResult(zrobZdjecieIntent, REQUEST_IMAGE_CAPTURE);
                }
                catch(Exception e){
                    e.printStackTrace();
                }
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
            int year = data.getIntExtra(EditExpenseActivity.EXTRA_EDIT_YEAR, 0);
            int month = data.getIntExtra(EditExpenseActivity.EXTRA_EDIT_MONTH, 0);
            int day = data.getIntExtra(EditExpenseActivity.EXTRA_EDIT_DAY, 0);
            if (requestCode == NEW_BOOK_ACTIVITY_REQUEST_CODE) {
                Expense expense = new Expense(data.getStringExtra(EditExpenseActivity.EXTRA_EDIT_BOOK_TITLE),
                        Integer.parseInt(Objects.requireNonNull(data.getStringExtra(EditExpenseActivity.EXTRA_EDIT_BOOK_AUTHOR))),
                        new Date(year,month,day));
                expenseViewModel.insert(expense);
                Snackbar.make(findViewById(R.id.coordinator_layout),
                                getString(R.string.expense_added),
                                Snackbar.LENGTH_LONG)
                        .show();
            }
            else if (requestCode == EDIT_BOOK_ACTIVITY_REQUEST_CODE) {
                editedExpense.setTitle(data.getStringExtra(EditExpenseActivity.EXTRA_EDIT_BOOK_TITLE));
                editedExpense.setPrice(Integer.parseInt(Objects.requireNonNull(data.getStringExtra(EditExpenseActivity.EXTRA_EDIT_BOOK_AUTHOR))));
                editedExpense.setDate(new Date(year,month,day));
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
        private TextView dateTextView;
        private Expense expense;

        public BookHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.expense_list_item, parent, false));
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

            bookTitleTextView = itemView.findViewById(R.id.book_title);
            bookAuthorTextView = itemView.findViewById(R.id.book_author);
            dateTextView = itemView.findViewById(R.id.expense_date);
        }

        public void bind(Expense expense) {
            this.expense = expense;
            bookTitleTextView.setText(expense.getName());
            bookAuthorTextView.setText(String.valueOf(expense.getPrice()) + "zł");

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MM 20yy", Locale.getDefault());
            dateTextView.setText(dateFormat.format(expense.getDate()));
        }

        @Override
        public void onClick(View v) {
            MainActivity.this.editedExpense = this.expense;
            Intent intent = new Intent(MainActivity.this, EditExpenseActivity.class);
            intent.putExtra(EditExpenseActivity.EXTRA_EDIT_BOOK_TITLE, expense.getName());
            intent.putExtra(EditExpenseActivity.EXTRA_EDIT_BOOK_AUTHOR, Integer.toString(expense.getPrice()));
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