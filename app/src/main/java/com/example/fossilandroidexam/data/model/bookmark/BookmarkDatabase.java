package com.example.fossilandroidexam.data.model.bookmark;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class BookmarkDatabase implements ListBookmarkResponse {

    private static BookmarkDatabase INSTANCE;
    private MutableLiveData<List<String>> listBookmark; // use MutableLiveData to update data
    private Context context;

    private BookmarkDatabase(Application application) {
        this.context = application.getApplicationContext();
        listBookmark = new MutableLiveData<>();
        loadBookmarkData();
    }

    public static BookmarkDatabase getBookmarkRepository(Application application) {
        if (INSTANCE == null) {
            INSTANCE = new BookmarkDatabase(application);
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
