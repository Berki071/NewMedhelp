package com.medhelp.medhelp.data.model;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class VisitsCancelResponse {

    @SerializedName("imgError")
    private boolean error;

    @SerializedName("message")
    private String message;

    @SerializedName("response")
    private String[] response;

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

    public String[] getResponse() {
        return response;
    }

    public void setResponse(String[] response) {
        this.response = response;
    }

}
