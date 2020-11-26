package com.example.exam.data.bookmark;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import java.util.List;

public class UpdateBookmark extends AsyncTask<Context, Void, List<String>> {

    private String key;
    private Boolean value;

     UpdateBookmark(String key, Boolean value) {
        this.key = key;
        this.value = value;
    }

    @Override
    protected List<String> doInBackground(Context... contexts) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(contexts[0]);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if(value) {
            editor.putBoolean(key, true);
        }
        else {
            editor.remove(key);
        }
        editor.apply();

        return null;
    }
}

