package com.example.fossilandroidexam.data.repository;

import androidx.lifecycle.LiveData;

import com.example.fossilandroidexam.data.model.stackoverflowservice.RemoteDatabase;
import com.example.fossilandroidexam.data.model.stackoverflowservice.Reputation;
import com.example.fossilandroidexam.data.model.stackoverflowservice.User;

import java.util.List;

public class RemoteRepository {
    private static RemoteRepository INSTANCE;
    private RemoteDatabase remoteDatabase;
    private LiveData<List<User>> listUsers;
    private LiveData<List<User>> listBookmarkUsers;
    private LiveData<List<Reputation>> listReputation;

    public static RemoteRepository getRemoteRepository() {
        if(INSTANCE == null){
            INSTANCE = new RemoteRepository();
        }
        return INSTANCE;
    }
    private RemoteRepository(){
        remoteDatabase = RemoteDatabase.getRemoteRepository();
        listUsers = remoteDatabase.getListUsers();
        listBookmarkUsers = remoteDatabase.getListBookmarkUsers();
        listReputation = remoteDatabase.getListReputation();
    }

    public void loadDetailsOfUserOfPage(String userId, int intPage) {
        remoteDatabase.loadDetailsOfUserOfPage(userId, intPage);
    }

    public void loadAllUsersOfPage(int intPage) {
        remoteDatabase.loadAllUsersOfPage(intPage);
    }

    public void loadBookmarkUser(List<String> listBookmark) {
        remoteDatabase.loadBookmarkUser(listBookmark);
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
