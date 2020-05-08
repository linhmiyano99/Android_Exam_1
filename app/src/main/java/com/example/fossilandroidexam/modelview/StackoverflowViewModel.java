package com.example.fossilandroidexam.modelview;


import android.app.Application;
import android.graphics.Bitmap;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;


import com.example.fossilandroidexam.data.Repository.RemoteRepository;
import com.example.fossilandroidexam.data.model.BookmarkDatabase.BookmarkDatabase;
import com.example.fossilandroidexam.data.model.ImageDatabase.ImageFromUrl;
import com.example.fossilandroidexam.data.model.StackoverflowService.Reputation;
import com.example.fossilandroidexam.data.model.StackoverflowService.User;

import java.util.List;
import java.util.Map;

public class StackoverflowViewModel extends AndroidViewModel {
    private RemoteRepository repository;
    private BookmarkDatabase bookmarkDatabase;
    private ImageFromUrl imageDatabase;// view
    private LiveData<List<User>> listUsers;
    private LiveData<List<User>> listBookmarkUsers;
    private LiveData<List<Reputation>> listReputation;
    private LiveData<List<String>> listbookmark;

    public StackoverflowViewModel(Application application) {
        super(application);
        repository = RemoteRepository.getMainRepository();
        bookmarkDatabase = BookmarkDatabase.getBookmarkDatabase(application);
        imageDatabase = ImageFromUrl.getImageDatabase(application);
        listUsers = repository.getListUsers();
        listBookmarkUsers = repository.getListBookmarkUsers();
        listReputation = repository.getListReputation();
        listbookmark = bookmarkDatabase.getListBookmark();
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
        bookmarkDatabase.updateAUserOfBookmarkData(key, value);
    }
    public void loadImage(String url) {
        imageDatabase.loadImage(url);
    }
    public LiveData<Map.Entry<String, Bitmap>> getEntryImage() {
        return imageDatabase.getEntryImage();
    }
}