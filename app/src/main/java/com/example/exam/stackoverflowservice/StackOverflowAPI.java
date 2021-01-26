package com.example.exam.stackoverflowservice;

import com.example.exam.data.model.Reputation;
import com.example.exam.data.model.User;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface StackOverflowAPI {
    String BASE_URL = "https://api.stackexchange.com";

    @GET("/2.2/users?pageSize=30&site=stackOverflow")
    Call<ListWrapper<User>> getAllUsers(@Query("page") int numberOfPage);

    @GET("/2.2/users/{ids}?order=desc&sort=reputation&site=stackOverflow")
    Call<ListWrapper<User>> getUserFromId(@Path("ids") StringBuilder userId);

    @GET("2.2/users/{userId}/reputation-history?pageSize=30&site=stackOverflow")
    Call<ListWrapper<Reputation>> getReputationForUser(@Path("userId") String userId, @Query("page") int numberOfPage);

}
