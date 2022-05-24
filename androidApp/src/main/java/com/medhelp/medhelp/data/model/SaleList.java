package com.medhelp.medhelp.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class SaleList {

    @SerializedName("imgError")
    private boolean error;

    @SerializedName("response")
    private List<SaleResponse> response = new ArrayList<>();

    @SerializedName("message")
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

    public List<SaleResponse> getResponse() {
        return response;
    }

    public void setResponse(List<SaleResponse> response) {
        this.response = response;
    }
}
