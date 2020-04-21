package com.example.fossilandroidexam;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class UpdateBookmark extends AsyncTask<Context, Void, List<String>> {


    private String key;
    private Boolean value;
    private SharedPreferences sharedPreferences;
    public UpdateBookmark( String key, Boolean value) {
        this.key = key;
        this.value = value;
    }


    @Override
    protected List<String> doInBackground(Context... contexts) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(contexts[0]);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if(value) {
            editor.putBoolean(key, value);
        }
        else {
            editor.remove(key);
        }
        editor.apply(); // Very important

        return null;
    }
}

