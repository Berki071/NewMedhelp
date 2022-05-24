package com.medhelp.medhelp.data.model.yandex_cashbox.sub;

import com.google.gson.annotations.SerializedName;

public class PaymentMethodInfModel {
    @SerializedName("type")
    private String type;

    @SerializedName("id")
    private String id;

    @SerializedName("saved")
    private String saved;

    @SerializedName("card")
    private CardModel card;

    @SerializedName("title")
    private String title;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSaved() {
        return saved;
    }

    public void setSaved(String saved) {
        this.saved = saved;
    }

    public CardModel getCard() {
        return card;
    }

    public void setCard(CardModel card) {
        this.card = card;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
