package com.medhelp.medhelp.data.model.osn;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class CheckSpamRecordList {
    @SerializedName("imgError")
    private boolean error;

    @SerializedName("message")
    private String message;

    @SerializedName("response")
    private List<CheckSpamRecordResponse> mResponses = new ArrayList<>();

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

    public List<CheckSpamRecordResponse> get_mResponses() {
        return mResponses;
    }

    public void set_mResponses(List<CheckSpamRecordResponse> mResponses) {
        this.mResponses = mResponses;
    }
}
