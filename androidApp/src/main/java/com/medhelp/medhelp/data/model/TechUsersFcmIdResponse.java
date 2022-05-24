package com.medhelp.medhelp.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TechUsersFcmIdResponse {
    @SerializedName("error")
    private boolean error;

    @SerializedName("message")
    private String message;

    @SerializedName("response")
    private List<TechUsersFcmIdItem> response;

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

    public List<TechUsersFcmIdItem> getResponse() {
        return response;
    }

    public void setResponse(List<TechUsersFcmIdItem> response) {
        this.response = response;
    }
}
