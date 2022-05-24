package com.medhelp.medhelp.data.model.notification;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class NotificationMsgList {
    @SerializedName("imgError")
    private boolean error;

    @SerializedName("message")
    private String message;

    @SerializedName("response")
    private List<NotificationMsg> mResponses = new ArrayList<>();

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<NotificationMsg> getmResponses() {
        return mResponses;
    }

    public void setmResponses(List<NotificationMsg> mResponses) {
        this.mResponses = mResponses;
    }
}
