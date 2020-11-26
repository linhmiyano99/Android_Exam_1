package com.example.fossilandroidexam.modelview;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.fossilandroidexam.data.model.Reputation;
import com.example.fossilandroidexam.data.model.User;
import com.example.fossilandroidexam.data.repository.LocalRepository;
import com.example.fossilandroidexam.data.repository.RemoteRepository;

import java.util.List;

public class StackOverflowViewModel extends AndroidViewModel {

    private RemoteRepository repository;
    private LocalRepository localRepository;
    private LiveData<List<User>> listUsers;
    private LiveData<List<User>> listBookmarkUsers;
    private LiveData<List<Reputation>> listReputation;
    private LiveData<List<String>> listBookmark;

    public StackOverflowViewModel(Application application) {
        super(application);
        repository = RemoteRepository.getRemoteRepository();
        localRepository = LocalRepository.getLocalRepository(application);
        listUsers = repository.getListUsers();
        listBookmarkUsers = repository.getListBookmarkUsers();
        listReputation = repository.getListReputation();
        listBookmark = localRepository.getListBookmark();
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

    public LiveData<List<String>> getListBookmark() {
        return listBookmark;
    }

    public void loadDetailsOfUserOfPage(String userId, int intPage) {
        repository.loadDetailsOfUserOfPage(userId, intPage);
    }

    public void loadAllUsersOfPage(int intPage) {
        repository.loadAllUsersOfPage(intPage);
    }

    public void loadBookmarkUser(List<String> listBookmark) {
        repository.loadBookmarkUser(listBookmark);
    }

    public void updateAUserOfBookmarkData(String key, Boolean value) {
        localRepository.updateAUserOfBookmarkData(key, value);
    }
}