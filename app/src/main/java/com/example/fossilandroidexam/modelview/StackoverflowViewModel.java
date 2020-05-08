package com.example.fossilandroidexam.modelview;


import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;


import com.example.fossilandroidexam.data.repository.RemoteRepository;
import com.example.fossilandroidexam.data.repository.BookmarkRepository;
import com.example.fossilandroidexam.data.model.stackoverflowservice.Reputation;
import com.example.fossilandroidexam.data.model.stackoverflowservice.User;

import java.util.List;

public class StackoverflowViewModel extends AndroidViewModel {
    private RemoteRepository repository;
    private BookmarkRepository bookmarkRepository;
    private LiveData<List<User>> listUsers;
    private LiveData<List<User>> listBookmarkUsers;
    private LiveData<List<Reputation>> listReputation;
    private LiveData<List<String>> listbookmark;

    public StackoverflowViewModel(Application application) {
        super(application);
        repository = RemoteRepository.getMainRepository();
        bookmarkRepository = BookmarkRepository.getBookmarkRepository(application);
        listUsers = repository.getListUsers();
        listBookmarkUsers = repository.getListBookmarkUsers();
        listReputation = repository.getListReputation();
        listbookmark = bookmarkRepository.getListBookmark();
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

    public LiveData<List<String>> getListbookmark() {
        return listbookmark;
    }

    public void loadDetailsOfUserOfPage(String userId, int intPage) {
        repository.loadDetailsOfUserOfPage(userId, intPage);
    }

    public void loadAllUsersOfPage(int intPage) {
        repository.loadAllUsersOfPage(intPage);
    }

    public void loadBookmarkUser(List<String> listBookmark) {

        if (listBookmark != null) {
            StringBuilder groupStringId = new StringBuilder();
            if (listBookmark.size() > 0) {
                groupStringId.append(listBookmark.get(0));
                for (int i = 1; i < listBookmark.size(); i++) {
                    groupStringId.append(";").append(listBookmark.get(i));
                }
                repository.loadBookmarkUser(groupStringId);
            }
        }
    }

    public void updateAUserOfBookmarkData(String key, Boolean value) {
        bookmarkRepository.updateAUserOfBookmarkData(key, value);
    }
}