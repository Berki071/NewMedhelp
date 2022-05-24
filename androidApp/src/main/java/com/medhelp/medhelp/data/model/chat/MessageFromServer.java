package com.medhelp.medhelp.data.model.chat;

import com.google.gson.annotations.SerializedName;

public class MessageFromServer implements Cloneable {
    @SerializedName("idroom") long idRoom;
    @SerializedName("tip") String type;
    @SerializedName("text") String msg;

    private long id;

    public MessageFromServer(){}

    public MessageFromServer(long idRoom, String type, String msg, long id )
    {
        this.idRoom=idRoom;
        this.type=type;
        this.msg=msg;
        this.id=id;
    }

    public long getIdRoom() {
        return idRoom;
    }

    public void setIdRoom(long idRoom) {
        this.idRoom = idRoom;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
