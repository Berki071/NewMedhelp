package com.medhelp.medhelp.data.model.yandex_cashbox.sub;

import com.google.gson.annotations.SerializedName;

public class CardModel {
    @SerializedName("first6")
    private String first6;

    @SerializedName("last4")
    private String last4;

    @SerializedName("expiry_month")
    private String expiry_month;

    @SerializedName("expiry_year")
    private String expiry_year;

    @SerializedName("card_type")
    private String card_type;

    public String getFirst6() {
        return first6;
    }

    public void setFirst6(String first6) {
        this.first6 = first6;
    }

    public String getLast4() {
        return last4;
    }

    public void setLast4(String last4) {
        this.last4 = last4;
    }

    public String getExpiry_month() {
        return expiry_month;
    }

    public void setExpiry_month(String expiry_month) {
        this.expiry_month = expiry_month;
    }

    public String getExpiry_year() {
        return expiry_year;
    }

    public void setExpiry_year(String expiry_year) {
        this.expiry_year = expiry_year;
    }

    public String getCard_type() {
        return card_type;
    }

    public void setCard_type(String card_type) {
        this.card_type = card_type;
    }
}
