package com.example.fossilandroidexam.model.StackoverflowService;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.fossilandroidexam.model.ListWrapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StackoverflowService {
    private StackoverflowAPI stackoverflowAPI;
    private MutableLiveData listUsers;
    private MutableLiveData listBookmarkUsers;
    private MutableLiveData listReputation;
    private static StackoverflowService INSTANCE;
    public static StackoverflowService getStackoverflowService()
    {
        if(INSTANCE == null)
        {
            INSTANCE = new StackoverflowService();
        }
        return  INSTANCE;
    }
    private StackoverflowService() {
        listUsers = new MutableLiveData();
        listBookmarkUsers = new MutableLiveData();
        listReputation = new MutableLiveData();
        createStackoverflowAPI();
    }
    private void createStackoverflowAPI() {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(StackoverflowAPI.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        stackoverflowAPI = retrofit.create(StackoverflowAPI.class);
    }


    private Callback<ListWrapper<Reputation>> reputationCallBack = new Callback<ListWrapper<Reputation>> () {
        @Override
        public void onResponse(@NotNull Call<ListWrapper<Reputation>> call, Response<ListWrapper<Reputation>> response) {
            if (response.isSuccessful()) {
                assert response.body() != null;
                List<Reputation> list = new ArrayList<>(response.body().items);
                if(response.body().items.size() == 0)
                    return;
                listReputation.setValue(list);
            } else {
                Log.d("reputationCallBack", "Code: " + response.code() + " Message: " + response.message());
            }
        }

        @Override
        public void onFailure(@NotNull Call<ListWrapper<Reputation>> call, Throwable t) {
            t.printStackTrace();
            Log.d("xxxxxx","onFailure");
        }

    };
    private Callback<ListWrapper<User>> idUserCallBack = new Callback<ListWrapper<User>>() {

        @Override
        public void onResponse(@NotNull Call<ListWrapper<User>> call, Response<ListWrapper<User>> response) {
            if (response.isSuccessful()) {
                assert response.body() != null;
                List<User> list = new ArrayList<>(response.body().items);
                if(response.body().items.size() == 0)
                    return;
                listBookmarkUsers.setValue(list);
            }
        }

        @Override
        public void onFailure(@NotNull Call<ListWrapper<User>> call, Throwable t) {
            t.printStackTrace();
            Log.d("xxxxxx","onFailure");
        }
    };

    private Callback<ListWrapper<User>> usersCallback = new Callback<ListWrapper<User>> () {
        @Override
        public void onResponse(@NotNull Call<ListWrapper<User>> call, Response<ListWrapper<User>> response) {
            if (response.isSuccessful()) {
                assert response.body() != null;
                List<User> list = new ArrayList<>(response.body().items);
                if(response.body().items.size() == 0)
                    return;
                Log.d("listUsers", String.valueOf(list.size()));
                listUsers.setValue(list);


            } else {
                Log.d("usersCallback", "Code: " + response.code() + " Message: " + response.message());
            }
        }

        @Override
        public void onFailure(@NotNull Call<ListWrapper<User>> call, Throwable t) {
            t.printStackTrace();
            Log.d("xxxxxx","onFailure");
        }
    };
    public void loadDetailsOfUserOfPage(String userId, int intPage){
        stackoverflowAPI.getReputationForUser(userId, intPage).enqueue(reputationCallBack);
    }
    public void loadAllUsersOfPage(int intPage) {
        stackoverflowAPI.getAllUsers(intPage).enqueue(usersCallback);
    }
    public void loadBookmarkUser(StringBuilder groupStringId ) {
        stackoverflowAPI.getUserFromId(groupStringId).enqueue(idUserCallBack);
    }

    public LiveData<List<User>> getListUsers() {
        return listUsers;
    }

    public LiveData<List<User>> getListBookmarkUsers() {
        return listBookmarkUsers;
    }

    public LiveData<List<Reputation>> getListReputation() {
        return listReputation;
    }

}
