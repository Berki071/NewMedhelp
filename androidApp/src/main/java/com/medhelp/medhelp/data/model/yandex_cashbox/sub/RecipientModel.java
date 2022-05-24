package com.medhelp.medhelp.data.model.yandex_cashbox.sub;

import com.google.gson.annotations.SerializedName;

public class RecipientModel {
    @SerializedName("account_id")
    private String account_id;

    @SerializedName("gateway_id")
    private String gateway_id;

    public String getAccount_id() {
        return account_id;
    }

    public void setAccount_id(String account_id) {
        this.account_id = account_id;
    }

    public String getGateway_id() {
        return gateway_id;
    }

    public void setGateway_id(String gateway_id) {
        this.gateway_id = gateway_id;
    }
}
