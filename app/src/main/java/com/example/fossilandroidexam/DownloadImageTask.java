package com.example.fossilandroidexam;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap>{

    private ImageView imageView;

    public DownloadImageTask(ImageView imageView)  {
        this.imageView= imageView;
    }


    @Override
    protected Bitmap doInBackground(String... params) {

        OkHttpClient client = new OkHttpClient();
        String url = params[0];
        Call call = client.newCall(
                new Request.Builder()
                        .url(url)
                        .get()
                        .build());
        Log.d("xxxxx", String.valueOf(call));
        InputStream inputStream = null;

        try {
            Response response = call.execute();
            if (response.code() == 200 || response.code() == 201) {

                try {
                    inputStream = response.body().byteStream();
                    Log.d("xxxxx", String.valueOf(inputStream));

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
        if(result  != null){
            this.imageView.setImageBitmap(result);
        } else{
            Log.e("MyMessage", "Failed to fetch data image!");
        }
    }
}
