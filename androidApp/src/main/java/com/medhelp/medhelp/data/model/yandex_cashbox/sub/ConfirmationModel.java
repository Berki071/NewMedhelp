package com.medhelp.medhelp.data.model.yandex_cashbox.sub;

import com.google.gson.annotations.SerializedName;

public class ConfirmationModel {
    @SerializedName("type")
    private String type;

    @SerializedName("confirmation_url")
    private String confirmation_url;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getConfirmation_url() {
        return confirmation_url;
    }

    public void setConfirmation_url(String confirmation_url) {
        this.confirmation_url = confirmation_url;
    }
}
