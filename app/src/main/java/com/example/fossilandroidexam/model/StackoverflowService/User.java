package com.example.fossilandroidexam.model.StackoverflowService;

import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;

public class User {
    @SerializedName("display_name")
    public String strDisplayName;
    @SerializedName("profile_image")
    public String srtProfileImageUrl;
    @SerializedName("reputation")
    public Integer intReputation;
    @SerializedName("location")
    public String strLocation;
    public int age;
    @SerializedName("user_id")
    public String strUserId;

    @NotNull
    @Override
    public String toString() {
        return  strDisplayName
                + "\n " + intReputation
                + ", " + strLocation
                + ", " + age;
    }
}
