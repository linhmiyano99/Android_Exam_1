package com.example.fossilandroidexam.data.model;

import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;

public class User {
    @SerializedName("display_name")
    public String strDisplayName; // Should we public here?
    @SerializedName("profile_image")
    public String srtProfileImageUrl;
    @SerializedName("reputation")
    public Integer intReputation;
    @SerializedName("location")
    public String strLocation;
    @SerializedName("user_id")
    public String strUserId;

    @NotNull
    @Override
    public String toString() {
        return  strDisplayName
                + "\n " + intReputation
                + ", " + strLocation ;
    }

    public String getSrtProfileImageUrl() {
        return srtProfileImageUrl;
    }

    public String getStrUserId() {
        return strUserId;
    }
}
