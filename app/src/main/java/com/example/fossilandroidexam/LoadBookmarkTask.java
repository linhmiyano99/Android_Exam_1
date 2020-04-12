package com.example.fossilandroidexam;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.Map;


public class LoadBookmarkTask extends AsyncTask<Context, Void, Map<String, Boolean>> {

    private RecyclerViewAdapter adapter;
    public LoadBookmarkTask(RecyclerViewAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    protected Map<String, Boolean> doInBackground(Context... contexts) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(contexts[0]);
        return (Map<String, Boolean>) sharedPreferences.getAll();
    }
    @Override
    protected void onPostExecute(Map<String,Boolean> result) {
        if(result  != null){
            adapter.setMapBookmark(result);
        } else{
            Log.e("MyMessage", "Failed to fetch data bookmark");
        }
    }
}
