package com.medhelp.medhelp.data.model.news;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class NewsList {

    @SerializedName("imgError")
    private boolean error;

    @SerializedName("Message")
    private String message;

    @SerializedName("response")
    private List<NewsResponse> response = new ArrayList<>();

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

    public List<NewsResponse> getResponse() {
        return response;
    }

    public void setResponse(List<NewsResponse> response) {
        this.response = response;
    }
}
