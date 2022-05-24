package com.medhelp.medhelp.data.model;

import com.google.gson.annotations.SerializedName;

public class TechUsersFcmIdItem {
    @SerializedName("fcm_key") String fcm_key;

    public String getFcm_key() {
        return fcm_key;
    }

    public void setFcm_key(String fcm_key) {
        this.fcm_key = fcm_key;
    }
}
