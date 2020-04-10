package com.example.fossilandroidexam;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface StackoverflowAPI {
    String BASE_URL = "https://api.stackexchange.com";

    @GET("/2.2/users?page=1&pagesize=30&site=stackoverflow")
    Call<ListWrapper<User>> getUsers();

    @GET("users")
    Call<User> getUserById(@Query("id") Integer id);

}
