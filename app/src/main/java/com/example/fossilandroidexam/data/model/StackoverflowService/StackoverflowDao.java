package com.example.fossilandroidexam.data.model.StackoverflowService;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.fossilandroidexam.data.model.StackoverflowService.StackoverflowAPI.BASE_URL;

public class StackoverflowDao {
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
    public static StackoverflowAPI getStackoverflowAPI() {
        return getInstance().create(StackoverflowAPI.class);
    }




}
