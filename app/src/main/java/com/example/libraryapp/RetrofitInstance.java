package com.example.libraryapp;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {
    private static Retrofit instance;

    public static final String QUOTES_API_URL = "https://type.fit/api/";

    public static Retrofit getInstance() {
        if(instance == null) {
            OkHttpClient client = new OkHttpClient();
            instance = new Retrofit.Builder()
                    .baseUrl(QUOTES_API_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }

        return instance;
    }
}
