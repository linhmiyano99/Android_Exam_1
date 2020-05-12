package com.example.fossilandroidexam.data.model.stackoverflowservice;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.fossilandroidexam.data.model.stackoverflowservice.StackOverflowAPI.BASE_URL;

public class StackOverflowDao {
    private static Retrofit INSTANCE;

    private static Retrofit getInstance() {
        if (INSTANCE == null) {
            Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                    .create();
            INSTANCE = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return INSTANCE;
    }
    public static StackOverflowAPI getStackOverflowAPI() {
        return getInstance().create(StackOverflowAPI.class);
    }




}
