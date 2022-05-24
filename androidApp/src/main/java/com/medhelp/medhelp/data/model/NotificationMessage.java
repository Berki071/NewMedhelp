package com.medhelp.medhelp.data.model;

@SuppressWarnings("unused")
public class NotificationMessage {

    private String message;
    private int idChatRoom;
    private int idUser;

    public NotificationMessage(String message, int idChatRoom, int idUser) {
        this.message = message;
        this.idChatRoom = idChatRoom;
        this.idUser = idUser;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getIdChatRoom() {
        return idChatRoom;
    }

    public void setIdChatRoom(int idChatRoom) {
        this.idChatRoom = idChatRoom;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }
}