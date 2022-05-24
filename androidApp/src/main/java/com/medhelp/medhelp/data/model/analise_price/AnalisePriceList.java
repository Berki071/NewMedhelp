package com.medhelp.medhelp.data.model.analise_price;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class AnalisePriceList {
    @SerializedName("imgError")
    private boolean error;

    @SerializedName("Message")
    private String message;

    @SerializedName("response")
    private List<AnalisePriceResponse> response = new ArrayList<>();

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

    public List<AnalisePriceResponse> getResponse() {
        return response;
    }

    public void setResponse(List<AnalisePriceResponse> response) {
        this.response = response;
    }
}
