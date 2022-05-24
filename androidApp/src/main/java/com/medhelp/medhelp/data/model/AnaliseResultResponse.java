package com.medhelp.medhelp.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class AnaliseResultResponse {
    @SerializedName("imgError")
    private boolean error;

    @SerializedName("response")
    private List<AnaliseResponse> response = new ArrayList<>();

    @SerializedName("Message")
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public List<AnaliseResponse> getResponse() {
        return response;
    }

    public void setResponse(List<AnaliseResponse> response) {
        this.response = response;
    }
}
