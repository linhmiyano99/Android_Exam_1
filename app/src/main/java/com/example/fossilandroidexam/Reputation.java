package com.example.fossilandroidexam;

import com.google.gson.annotations.SerializedName;

public class Reputation {
    @SerializedName("reputation_history_type")
    public String strReputationHistoryType;
    @SerializedName("reputation_change")
    public String strReputationChange;
    @SerializedName("creation_date")
    public String strCreateDate;
    @SerializedName("post_id")
    public String strPostId;
    @SerializedName("user_id")
    public String strUserId;
    @Override
    public String toString() {
        return  "Reputation Type: " +strReputationHistoryType
                + "\nChange: " + strReputationChange
                + "\nCreated At: " + strCreateDate
                + "\nPost ID: " + strPostId;
    }
}
