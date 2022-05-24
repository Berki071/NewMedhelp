package com.medhelp.medhelp.data.model.notification;

public class NotificationAnalyzes {
    private  String idNotification;
    private String idAnalizes;
    private String title;

    public NotificationAnalyzes() {}

    public String getIdNotification() {
        return idNotification;
    }

    public void setIdNotification(String idNotification) {
        this.idNotification = idNotification;
    }

    public String getIdAnalizes() {
        return idAnalizes;
    }

    public void setIdAnalizes(String idAnalizes) {
        this.idAnalizes = idAnalizes;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
