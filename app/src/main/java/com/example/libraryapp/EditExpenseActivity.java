package com.example.libraryapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

public class EditExpenseActivity extends AppCompatActivity {
    public static final String EXTRA_EDIT_BOOK_TITLE = "com.modzel.EDIT_BOOK_TITLE";
    public static final String EXTRA_EDIT_BOOK_AUTHOR = "com.modzel.EDIT_BOOK_AUTHOR";
    public static final String EXTRA_EDIT_DAY = "com.modzel.EDIT_DAY";
    public static final String EXTRA_EDIT_MONTH = "com.modzel.EDIT_MONTH";
    public static final String EXTRA_EDIT_YEAR = "com.modzel.EDIT_YEAR";
    private EditText editTitleEditText;
    private EditText editAuthorEditText;
    private DatePicker datePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_expense);

        editTitleEditText = findViewById(R.id.edit_book_title);
        editAuthorEditText = findViewById(R.id.edit_book_author);
        datePicker = findViewById(R.id.edit_date);

        Intent starter = getIntent();
        if (starter.hasExtra(EXTRA_EDIT_BOOK_TITLE))
            editTitleEditText.setText(starter.getStringExtra(EXTRA_EDIT_BOOK_TITLE));
        if (starter.hasExtra(EXTRA_EDIT_BOOK_AUTHOR))
            editAuthorEditText.setText(starter.getStringExtra(EXTRA_EDIT_BOOK_AUTHOR));

        final Button button = findViewById(R.id.button_save);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent replyIntent = new Intent();
                if (TextUtils.isEmpty(editTitleEditText.getText())
                        || TextUtils.isEmpty(editAuthorEditText.getText()))
                    setResult(RESULT_CANCELED, replyIntent);
                else {
                    String title = editTitleEditText.getText().toString();
                    replyIntent.putExtra(EXTRA_EDIT_BOOK_TITLE, title);
                    String author = editAuthorEditText.getText().toString();
                    replyIntent.putExtra(EXTRA_EDIT_BOOK_AUTHOR, author);
                    int day = datePicker.getDayOfMonth();
                    replyIntent.putExtra(EXTRA_EDIT_DAY, day);
                    int month = datePicker.getMonth();
                    replyIntent.putExtra(EXTRA_EDIT_MONTH, month);
                    int year = datePicker.getYear();
                    replyIntent.putExtra(EXTRA_EDIT_YEAR, year);
                    setResult(RESULT_OK, replyIntent);
                }
                finish();
            }
        });
    }
}