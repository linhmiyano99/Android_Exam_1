package com.example.fossilandroidexam.data.model.stackoverflowservice;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface StackoverflowAPI {
    String BASE_URL = "https://api.stackexchange.com";

    @GET("/2.2/users?pagesize=30&site=stackoverflow")
    Call<ListWrapper<User>> getAllUsers(@Query("page") int numberOfPage);

    @GET("/2.2/users/{ids}?order=desc&sort=reputation&site=stackoverflow")
    Call<ListWrapper<User>> getUserFromId(@Path("ids") StringBuilder userId);

    @GET("2.2/users/{userId}/reputation-history?pagesize=30&site=stackoverflow")
    Call<ListWrapper<Reputation>> getReputationForUser(@Path("userId") String userId, @Query("page") int numberOfPage);

}
