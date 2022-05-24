package com.medhelp.medhelp.data.model.yandex_cashbox;

import com.google.gson.annotations.SerializedName;
import com.medhelp.medhelp.data.model.AllDoctorsResponse;

import java.util.ArrayList;
import java.util.List;

public class YandexKeyList {
    @SerializedName("imgError")
    private boolean error;

    @SerializedName("message")
    private String message;

    @SerializedName("response")
    private List<YandexKey> mResponses = new ArrayList<>();

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

    public List<YandexKey> get_mResponses() {
        return mResponses;
    }

    public void set_mResponses(List<YandexKey> mResponses) {
        this.mResponses = mResponses;
    }
}
