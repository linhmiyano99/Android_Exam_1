package com.example.fossilandroidexam.model.BookmarkDatabase;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class LoadBookmarkTask extends AsyncTask<Context, Void, List<String>> {
    private ListBookmarkResponse delegate = null;

    void setDelegate(ListBookmarkResponse delegate) {
        this.delegate = delegate;
    }

    @Override
    protected List<String> doInBackground(Context... contexts) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(contexts[0]);
        List<String> listBookmark = new ArrayList<>();
        Map<String, ?> map = sharedPreferences.getAll();
        for (Map.Entry<String, ?> entry:map.entrySet()) {
            listBookmark.add(entry.getKey());
        }
        return listBookmark;
    }
    @Override
    protected void onPostExecute(List<String> result) {
        if(result  != null){
            delegate.processListBookmarkFinish(result);
        } else{
            Log.e("MyMessage", "Failed to fetch data!");
        }
    }
}
