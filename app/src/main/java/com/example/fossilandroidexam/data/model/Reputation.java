package com.example.fossilandroidexam.data.model;

import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;

public class Reputation {
    @SerializedName("reputation_history_type")
    private String strReputationHistoryType;
    @SerializedName("reputation_change")
    private String strReputationChange;
    @SerializedName("creation_date")
    private String strCreateDate;
    @SerializedName("post_id")
    private String strPostId;
    @SerializedName("user_id")
    private String strUserId;
    @NotNull
    @Override
    public String toString() {
        return  "Reputation Type: " +strReputationHistoryType
                + "\nChange: " + strReputationChange
                + "\nCreated At: " + strCreateDate
                + "\nPost ID: " + strPostId;
    }

    public String getStrUserId() {
        return strUserId;
    }

}
