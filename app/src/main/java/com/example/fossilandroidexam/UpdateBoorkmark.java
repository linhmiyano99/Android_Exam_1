package com.example.fossilandroidexam;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;


public class UpdateBoorkmark extends AsyncTask<Context, Void, Void> {

    private RecyclerViewAdapter adapter;
    private String key;
    private Boolean value;
    public UpdateBoorkmark( String key, Boolean value) {
        this.key = key;
        this.value = value;
    }

    @Override
    protected Void doInBackground(Context... contexts) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(contexts[0]);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply(); // Very important
        return null;
    }

}
