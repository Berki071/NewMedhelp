package com.medhelp.medhelp.data.model.chat;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class MessageFromServerList {

    @SerializedName("imgError")
    private boolean error;

    @SerializedName("message")
    private String message;

    @SerializedName("response")
    private List<MessageFromServer> respons = new ArrayList<>();

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

    public List<MessageFromServer> getRespons() {
        return respons;
    }

    public void setRespons(List<MessageFromServer> respons) {
        this.respons = respons;
    }
}
