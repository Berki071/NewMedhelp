package com.medhelp.medhelp.data.model.settings;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class AllTabsList {

    @SerializedName("imgError")
    private boolean error;

    @SerializedName("response")
    private List<AllTabsResponse> services = new ArrayList<>();

    @SerializedName("Message")
    private String message;


    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public List<AllTabsResponse> getServices() {
        return services;
    }

    public void setServices(List<AllTabsResponse> services) {
        this.services = services;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
