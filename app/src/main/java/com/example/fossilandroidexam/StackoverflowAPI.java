package com.example.fossilandroidexam;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface StackoverflowAPI {
    String BASE_URL = "https://api.stackexchange.com";

    @GET("/2.2/users?pagesize=30&site=stackoverflow")
    Call<ListWrapper<User>> getUsers(@Query("page") int numberOfPage);

    @GET("users")
    Call<User> getUserById(@Query("id") Integer id);
    @GET("2.2/users/{userId}/reputation-history?page=1&pagesize=30&site=stackoverflow")
    Call<ListWrapper<Reputation>> getReputationForUser(@Path("userId") String userId);

}
