package com.example.fossilandroidexam.data.Repository;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.fossilandroidexam.data.model.BookmarkDatabase.ListBookmarkResponse;
import com.example.fossilandroidexam.data.model.BookmarkDatabase.LoadBookmarkTask;
import com.example.fossilandroidexam.data.model.BookmarkDatabase.UpdateBookmark;

import java.util.List;

public class BookmarkRepository implements ListBookmarkResponse {
    private MutableLiveData<List<String>> listBookmark; // use MutableLiveData to update data
    private Context context;

    private static BookmarkRepository INSTANCE;
    public static BookmarkRepository getBookmarkRepository(Application application)
    {
        if(INSTANCE == null)
        {
            INSTANCE = new BookmarkRepository(application);
        }
        return  INSTANCE;
    }

    private BookmarkRepository(Application application) {
        this.context = application.getApplicationContext();
        listBookmark = new MutableLiveData<>();
        loadBookmarkData();
    }

    public LiveData<List<String>> getListBookmark() {
        return listBookmark;
    }

    @Override
    public void processListBookmarkFinish(List<String> output) {
            listBookmark.setValue(output);
        Log.d("Value", String.valueOf(listBookmark));

    }
    private void loadBookmarkData() {
        LoadBookmarkTask task = new LoadBookmarkTask();
        //this to set delegate / listener back to this class
        task.setDelegate(this);
        task.execute(context);
    }
    public void updateAUserOfBookmarkData(String key, Boolean value){
        UpdateBookmark task = new UpdateBookmark(key, value);
        task.execute(context);
    }
}
