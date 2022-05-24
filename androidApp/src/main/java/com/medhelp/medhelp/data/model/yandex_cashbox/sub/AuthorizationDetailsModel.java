package com.medhelp.medhelp.data.model.yandex_cashbox.sub;

import com.google.gson.annotations.SerializedName;

public class AuthorizationDetailsModel {
    @SerializedName("rrn")
    private String rrn;

    @SerializedName("auth_code")
    private String auth_code;

    public String getRrn() {
        return rrn;
    }

    public void setRrn(String rrn) {
        this.rrn = rrn;
    }

    public String getAuth_code() {
        return auth_code;
    }

    public void setAuth_code(String auth_code) {
        this.auth_code = auth_code;
    }
}
