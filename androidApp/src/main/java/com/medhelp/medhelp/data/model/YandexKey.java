package com.medhelp.medhelp.data.model;

public class YandexKey {
    String uuid;

    int idBranch ;
    String idShop;
    String keyShop;
    String keyAppYandex;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getIdBranch() {
        return idBranch;
    }

    public void setIdBranch(int idBranch) {
        this.idBranch = idBranch;
    }

    public String getIdShop() {
        return idShop;
    }

    public void setIdShop(String idShop) {
        this.idShop = idShop;
    }

    public String getKeyShop() {
        return keyShop;
    }

    public void setKeyShop(String keyShop) {
        this.keyShop = keyShop;
    }

    public String getKeyAppYandex() {
        return keyAppYandex;
    }

    public void setKeyAppYandex(String keyAppYandex) {
        this.keyAppYandex = keyAppYandex;
    }
}
