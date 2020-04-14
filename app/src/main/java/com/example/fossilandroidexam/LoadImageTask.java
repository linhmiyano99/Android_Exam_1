package com.example.fossilandroidexam;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class LoadImageTask extends AsyncTask<String, Void, Bitmap> {
    private RecyclerViewAdapter adapter;
    private String key;

    public LoadImageTask(RecyclerViewAdapter adapter) {
        this.adapter = adapter;
    }


    @Override
    protected Bitmap doInBackground(String... params) {

        OkHttpClient client = new OkHttpClient();
        key = params[0];
        Call call = client.newCall(
                new Request.Builder()
                        .url(key)
                        .get()
                        .build());
        InputStream inputStream = null;

        try {
            Response response = call.execute();
            if (response.code() == 200 || response.code() == 201) {

                try {
                    inputStream = Objects.requireNonNull(response.body()).byteStream();

                } finally {

                    if (inputStream != null) {
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        inputStream.close();
                        return bitmap;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    // When the task is completed, this method will be called
    // Download complete. Lets update UI
    @Override
    protected void onPostExecute(Bitmap result) {
        if (result != null) {
            adapter.addMapImage(key, result);
            adapter.notifyLoadImageDone();
        } else {
            Log.e("MyMessage", "Failed to fetch data image!");
        }
    }
}
