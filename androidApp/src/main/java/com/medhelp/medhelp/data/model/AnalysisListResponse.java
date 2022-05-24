package com.medhelp.medhelp.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class AnalysisListResponse {
    @SerializedName("imgError")
    private boolean error;

    @SerializedName("response")
    private List<AnaliseListData> response = new ArrayList<>();

    @SerializedName("Message")
    private String message;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public List<AnaliseListData> getResponse() {
        return response;
    }

    public void setResponse(List<AnaliseListData> response) {
        this.response = response;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}
