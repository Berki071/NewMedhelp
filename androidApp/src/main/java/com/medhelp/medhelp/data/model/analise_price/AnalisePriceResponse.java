package com.medhelp.medhelp.data.model.analise_price;

import com.google.gson.annotations.SerializedName;

public class AnalisePriceResponse {
    @SerializedName("gryppa")
    private String group;

    @SerializedName("analiz")
    private String title;

    @SerializedName("cena")
    private int price;

    @SerializedName("srok")
    private int time;

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
