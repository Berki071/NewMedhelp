package com.medhelp.medhelp.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class ScheduleList {

    @SerializedName("imgError")
    private boolean error;

    @SerializedName("Message")
    private String message;

    @SerializedName("response")
    private List<ScheduleResponse> response = new ArrayList<>();

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

    public List<ScheduleResponse> getResponse() {
        return response;
    }

    public void setResponse(List<ScheduleResponse> response) {
        this.response = response;
    }
}
