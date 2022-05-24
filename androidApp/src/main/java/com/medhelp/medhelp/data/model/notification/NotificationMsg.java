package com.medhelp.medhelp.data.model.notification;

import com.google.gson.annotations.SerializedName;

public class NotificationMsg {
    @SerializedName("id_filial") private int idBranch;

    @SerializedName("id_kl") private int idUser;

    @SerializedName("data") private String date;

    @SerializedName("id_zapisi") private int idRecords;

    @SerializedName("text") private String message;

    @SerializedName("tip") private String type;


    public int getIdBranch() {
        return idBranch;
    }

    public void setIdBranch(int idBranch) {
        this.idBranch = idBranch;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getIdRecords() {
        return idRecords;
    }

    public void setIdRecords(int idRecords) {
        this.idRecords = idRecords;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
