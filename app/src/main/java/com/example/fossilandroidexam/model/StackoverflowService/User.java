package com.example.fossilandroidexam.model.StackoverflowService;

import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;

public class User {
    @SerializedName("display_name")
    private String strDisplayName; // Should we public here?
    @SerializedName("profile_image")
    private String srtProfileImageUrl;
    @SerializedName("reputation")
    private Integer intReputation;
    @SerializedName("location")
    private String strLocation;
    @SerializedName("user_id")
    private String strUserId;

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
