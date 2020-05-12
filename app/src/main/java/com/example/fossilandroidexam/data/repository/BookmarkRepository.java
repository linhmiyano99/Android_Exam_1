package com.example.fossilandroidexam.data.repository;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.fossilandroidexam.data.model.bookmark.ListBookmarkResponse;
import com.example.fossilandroidexam.data.model.bookmark.LoadBookmarkTask;
import com.example.fossilandroidexam.data.model.bookmark.UpdateBookmark;

import java.util.List;

public class BookmarkRepository implements ListBookmarkResponse {

    private static BookmarkRepository INSTANCE;
    private MutableLiveData<List<String>> listBookmark; // use MutableLiveData to update data
    private Context context;

    private BookmarkRepository(Application application) {
        this.context = application.getApplicationContext();
        listBookmark = new MutableLiveData<>();
        loadBookmarkData();
    }

    public static BookmarkRepository getBookmarkRepository(Application application) {
        if (INSTANCE == null) {
            INSTANCE = new BookmarkRepository(application);
        }
        return INSTANCE;
    }

    public LiveData<List<String>> getListBookmark() {
        return listBookmark;
    }

    @Override
    public void processListBookmarkFinish(List<String> output) {
        listBookmark.setValue(output);
    }

    private void loadBookmarkData() {
        LoadBookmarkTask task = new LoadBookmarkTask();
        //this to set delegate / listener back to this class
        task.setDelegate(this);
        task.execute(context);
    }

    public void updateAUserOfBookmarkData(String key, Boolean value) {
        UpdateBookmark task = new UpdateBookmark(key, value);
        task.execute(context);
    }
}
