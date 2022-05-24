package com.medhelp.medhelp.data.model.notification;

public class NotificationReminderOfAdmission {
    private String idNotification;
    private String idInner;
    private String idRecord;
    private String title;
    private String idBranch;
    private String idUser;


    public NotificationReminderOfAdmission(){}

    public String getIdNotification() {
        return idNotification;
    }

    public void setIdNotification(String idNotification) {
        this.idNotification = idNotification;
    }

    public String getIdInner() {
        return idInner;
    }

    public void setIdInner(String idInner) {
        this.idInner = idInner;
    }

    public String getIdRecord() {
        return idRecord;
    }

    public void setIdRecord(String idRecord) {
        this.idRecord = idRecord;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIdBranch() {
        return idBranch;
    }

    public void setIdBranch(String idBranch) {
        this.idBranch = idBranch;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }
}
