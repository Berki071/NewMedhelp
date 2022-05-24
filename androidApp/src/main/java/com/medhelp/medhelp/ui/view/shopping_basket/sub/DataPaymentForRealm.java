package com.medhelp.medhelp.ui.view.shopping_basket.sub;

import com.google.gson.Gson;
import com.medhelp.medhelp.data.model.yandex_cashbox.YandexKey;

public class DataPaymentForRealm {
    long id;
    String idPayment;
    String yKey;

    String idUser;
    String idBranch;
    String idZapisi;
    String idYsl;
    String price;

    boolean isYandexInformation;

    public YandexKey getYKeyObt() {
        Gson gson=new Gson();
        return gson.fromJson(yKey,YandexKey.class);
    }

    public void setYKeyObt(YandexKey yKey) {
        Gson gson=new Gson();
        this.yKey = gson.toJson(yKey);
    }

    public String getyKey() {
        return yKey;
    }

    public void setyKey(String yKey) {
        this.yKey = yKey;
    }

    public boolean getYandexInformation() {
        return isYandexInformation;
    }

    public void setYandexInformation(boolean yandexInformation) {
        isYandexInformation = yandexInformation;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getIdPayment() {
        return idPayment;
    }

    public void setIdPayment(String idPayment) {
        this.idPayment = idPayment;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getIdBranch() {
        return idBranch;
    }

    public void setIdBranch(String idBranch) {
        this.idBranch = idBranch;
    }

    public String getIdZapisi() {
        return idZapisi;
    }

    public void setIdZapisi(String idZapisi) {
        this.idZapisi = idZapisi;
    }

    public String getIdYsl() {
        return idYsl;
    }

    public void setIdYsl(String idYsl) {
        this.idYsl = idYsl;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
