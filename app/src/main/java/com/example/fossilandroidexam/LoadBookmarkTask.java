package com.example.fossilandroidexam;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class LoadBookmarkTask extends AsyncTask<Context, Void, List<String>> {
    public ListBookmarkResponse delegate = null;
    //private RecyclerViewAdapter adapter;
   /* public LoadBookmarkTask(RecyclerViewAdapter adapter) {
        this.adapter = adapter;
    }*/
    public LoadBookmarkTask() {
    }

    @Override
    protected List<String> doInBackground(Context... contexts) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(contexts[0]);
        List<String> listBookmark = new ArrayList<>();
        Map<String, Boolean> map = (Map<String, Boolean>) sharedPreferences.getAll();
        for (Map.Entry<String, Boolean> entry:map.entrySet()
             ) {
            listBookmark.add(entry.getKey());
        }
        return listBookmark;
    }
    @Override
    protected void onPostExecute(List<String> result) {
        if(result  != null){
            //adapter.setListBookmark(result);
            //adapter.notifyLoadImageDone();
            delegate.processListBookmarkFinish(result);
        } else{
            Log.e("MyMessage", "Failed to fetch data!");
        }
    }
}
