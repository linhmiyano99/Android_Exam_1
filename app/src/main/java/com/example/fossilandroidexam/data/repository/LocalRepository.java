package com.example.fossilandroidexam.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.fossilandroidexam.data.bookmark.BookmarkDatabase;

import java.util.List;

public class LocalRepository {
    private static LocalRepository INSTANCE;
    private BookmarkDatabase bookmarkDatabase;
    private LiveData<List<String>> listBookmark; // use MutableLiveData to update data

    public static LocalRepository getLocalRepository(Application application) {
        if(INSTANCE == null){
            synchronized(LocalRepository.class) {
                if(INSTANCE == null) {
                    INSTANCE = new LocalRepository(application);
                }
            }
        }
        return INSTANCE;
    }

    private LocalRepository(Application application){
        bookmarkDatabase = BookmarkDatabase.getBookmarkRepository(application);
        listBookmark = bookmarkDatabase.getListBookmark();
    }

    public void updateAUserOfBookmarkData(String key, Boolean value) {
        bookmarkDatabase.updateAUserOfBookmarkData(key, value);
    }

    public LiveData<List<String>> getListBookmark() {
        return listBookmark;
    }
}
