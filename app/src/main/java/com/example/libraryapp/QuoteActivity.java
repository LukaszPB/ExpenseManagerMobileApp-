package com.example.libraryapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuoteActivity extends AppCompatActivity {
    private TextView quoteTextView;
    private Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_quote);
        quoteTextView = findViewById(R.id.quote);
        Button button = findViewById(R.id.back);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        fetchQuoteData();
    }

    private void fetchQuoteData() {
        QuotesService quotesService = RetrofitInstance.getInstance().create(QuotesService.class);
        Call<List<Quote>> quotesContainerCall = quotesService.fetchQuote();
        quotesContainerCall.enqueue(new Callback<List<Quote>>() {
            @Override
            public void onResponse(Call<List<Quote>> call, Response<List<Quote>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Quote> quotesList = response.body();
                    if (quotesList != null && !quotesList.isEmpty()) {
                        Quote firstQuote = quotesList.get(random.nextInt(quotesList.size()-1));
                        String content="'";
                        content += firstQuote.getText()+"'";
                        quoteTextView.setText(content);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Quote>> call, Throwable t) {
            }
        });
    }


}
