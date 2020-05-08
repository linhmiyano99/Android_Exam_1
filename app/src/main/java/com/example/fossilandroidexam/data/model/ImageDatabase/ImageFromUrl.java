package com.example.fossilandroidexam.data.model.ImageDatabase;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.AbstractMap;
import java.util.Map;

public class ImageFromUrl{
    private MutableLiveData<Map.Entry<String, Bitmap>> entryImage;
    private Context context;
    private ImageFromUrl(Application application) {
        entryImage = new MutableLiveData<>();
        this.context = application.getApplicationContext();
    }
    private static ImageFromUrl INSTANCE;
    public static ImageFromUrl getImageDatabase(Application application)
    {
        if(INSTANCE == null)
        {
            INSTANCE = new ImageFromUrl(application);
        }
        return  INSTANCE;
    }

    public void loadImage(final String url){
        Glide.with(context)
                .asBitmap()
                .load(url)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        entryImage.setValue(new AbstractMap.SimpleEntry<>(url, resource));
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                });
    }

    public LiveData<Map.Entry<String, Bitmap>> getEntryImage() {
        return entryImage;
    }
}
