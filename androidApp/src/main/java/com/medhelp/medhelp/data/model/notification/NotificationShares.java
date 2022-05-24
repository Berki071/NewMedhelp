package com.medhelp.medhelp.data.model.notification;

public class NotificationShares {

    private String idNotification;
    private String idShare;
    private String title;
    private String description;
    private String imgURL;

    public NotificationShares(String idNotification,String idShare,String title,String description,String imgURL)
    {
        this.idNotification=idNotification;
        this.idShare=idShare;
        this.title=title;
        this.description=description;
        this.imgURL=imgURL;
    }
    public NotificationShares()
    {

    }

    public String getIdNotification() {
        return idNotification;
    }

    public void setIdNotification(String idNotification) {
        this.idNotification = idNotification;
    }

    public String getIdShare() {
        return idShare;
    }

    public void setIdShare(String idShare) {
        this.idShare = idShare;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }
}
