package com.example.fossilandroidexam.data.stackoverflowservice;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.fossilandroidexam.data.model.Reputation;
import com.example.fossilandroidexam.data.model.User;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RemoteDatabase {

    private static RemoteDatabase INSTANCE;
    private StackOverflowAPI stackoverflowAPI;
    private MutableLiveData<List<User>> listUsers;
    private MutableLiveData<List<User>> listBookmarkUsers;
    private MutableLiveData<List<Reputation>> listReputation;

    private Callback<ListWrapper<Reputation>> reputationCallBack = new Callback<ListWrapper<Reputation>>() {
        @Override
        public void onResponse(@NotNull Call<ListWrapper<Reputation>> call, Response<ListWrapper<Reputation>> response) {
            if (response.isSuccessful()) {
                assert response.body() != null;
                List<Reputation> list = new ArrayList<>(response.body().items);
                if (response.body().items.size() == 0)
                    return;
                listReputation.setValue(list);
            } else {
                Log.d("reputationCallBack", "Code: " + response.code() + " Message: " + response.message());
            }
        }

        @Override
        public void onFailure(@NotNull Call<ListWrapper<Reputation>> call, Throwable t) {
            t.printStackTrace();
            Log.d("onFailure", "onFailure");
        }

    };

    private Callback<ListWrapper<User>> idUserCallBack = new Callback<ListWrapper<User>>() {

        @Override
        public void onResponse(@NotNull Call<ListWrapper<User>> call, Response<ListWrapper<User>> response) {
            if (response.isSuccessful()) {
                assert response.body() != null;
                List<User> list = new ArrayList<>(response.body().items);
                if (response.body().items.size() == 0)
                    return;
                listBookmarkUsers.setValue(list);
            }
        }

        @Override
        public void onFailure(@NotNull Call<ListWrapper<User>> call, Throwable t) {
            t.printStackTrace();
            Log.d("onFailure", "onFailure");
        }
    };
    private Callback<ListWrapper<User>> usersCallback = new Callback<ListWrapper<User>>() {
        @Override
        public void onResponse(@NotNull Call<ListWrapper<User>> call, Response<ListWrapper<User>> response) {
            if (response.isSuccessful()) {
                assert response.body() != null;
                List<User> list = new ArrayList<>(response.body().items);
                if (response.body().items.size() == 0)
                    return;
                listUsers.setValue(list);


            } else {
                Log.d("usersCallback", "Code: " + response.code() + " Message: " + response.message());
            }
        }

        @Override
        public void onFailure(@NotNull Call<ListWrapper<User>> call, Throwable t) {
            t.printStackTrace();
            Log.d("onFailure", "onFailure");
        }
    };

    private RemoteDatabase() {
        stackoverflowAPI = StackOverflowDao.getStackOverflowAPI();
        listUsers = new MutableLiveData<>();
        listBookmarkUsers = new MutableLiveData<>();
        listReputation = new MutableLiveData<>();
    }

    public static RemoteDatabase getRemoteRepository() {
        if (INSTANCE == null) {
            synchronized (RemoteDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = new RemoteDatabase();
                }
            }
        }
        return INSTANCE;
    }

    public void loadDetailsOfUserOfPage(String userId, int intPage) {
        stackoverflowAPI.getReputationForUser(userId, intPage).enqueue(reputationCallBack);
    }

    public void loadAllUsersOfPage(int intPage) {
        stackoverflowAPI.getAllUsers(intPage).enqueue(usersCallback);
    }

    public void loadBookmarkUser(List<String> listBookmark) {
        if (listBookmark != null) {
            if (listBookmark.size() > 0) {
                StringBuilder groupStringId = new StringBuilder();
                groupStringId.append(listBookmark.get(0));
                for (int i = 1; i < listBookmark.size(); i++) {
                    groupStringId.append(";").append(listBookmark.get(i));
                }
                stackoverflowAPI.getUserFromId(groupStringId).enqueue(idUserCallBack);
            }
        }
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
