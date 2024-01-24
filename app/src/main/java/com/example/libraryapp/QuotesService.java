package com.example.libraryapp;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface QuotesService {
    @GET("quotes")
    Call<List<Quote>> fetchQuote();
}
