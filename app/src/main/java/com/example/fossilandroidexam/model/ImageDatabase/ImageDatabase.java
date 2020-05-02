package com.example.fossilandroidexam.model.ImageDatabase;

import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.AbstractMap;
import java.util.Map;

public class ImageDatabase implements ImageResponse {
    private MutableLiveData<Map.Entry<String, Bitmap>> entryImage;
    private ImageDatabase() {
        entryImage = new MutableLiveData<>();
    }
    private static ImageDatabase INSTANCE;
    public static ImageDatabase getImageDatabase()
    {
        if(INSTANCE == null)
        {
            INSTANCE = new ImageDatabase();
        }
        return  INSTANCE;
    }

    @Override
    public void processImageFinish(final String key, final Bitmap image) {
        entryImage.setValue(new AbstractMap.SimpleEntry<>(key, image));
    }
    public void loadImage(String url){
        // need filter overlapping
        LoadImageTask task = new LoadImageTask();
        task.setDelegate(this);
        task.execute(url);
    }

    public LiveData<Map.Entry<String, Bitmap>> getEntryImage() {
        return entryImage;
    }
}
