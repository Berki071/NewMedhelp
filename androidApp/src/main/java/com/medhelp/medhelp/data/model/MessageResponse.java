//package com.medhelp.medhelp.data.model;
//
//import android.os.Parcel;
//import android.os.Parcelable;
//
//import com.google.gson.annotations.SerializedName;
//
//@SuppressWarnings("unused")
//public class MessageResponse extends RealmObject implements Parcelable {
//
//    @PrimaryKey
//    @SerializedName("id")
//    private int idMessage;
//
//    @SerializedName("id_room")
//    private int idRoom;
//
//    @SerializedName("message")
//    private String message;
//
//    @SerializedName("id_user")
//    private int idUser;
//
//    @SerializedName("id_doctor")
//    private int idDoctor;
//
//    @SerializedName("is_read")
//    private String isRead;
//
//    @SerializedName("time_stamp")
//    private String timeStamp;
//
//    @SerializedName("message_key")
//    private String messageKey;
//
//    public int getIdMessage() {
//        return idMessage;
//    }
//
//    public void setIdMessage(int idMessage) {
//        this.idMessage = idMessage;
//    }
//
//    public int getIdRoom() {
//        return idRoom;
//    }
//
//    public void setIdRoom(int idRoom) {
//        this.idRoom = idRoom;
//    }
//
//    public String getMessage() {
//        return message;
//    }
//
//    public void setMessage(String message) {
//        this.message = message;
//    }
//
//    public int getIdUser() {
//        return idUser;
//    }
//
//    public void setIdUser(int idUser) {
//        this.idUser = idUser;
//    }
//
//    public int getIdDoctor() {
//        return idDoctor;
//    }
//
//    public void setIdDoctor(int idDoctor) {
//        this.idDoctor = idDoctor;
//    }
//
//    public String getIsRead() {
//        return isRead;
//    }
//
//    public void setIsRead(String isRead) {
//        this.isRead = isRead;
//    }
//
//    public String getTimeStamp() {
//        return timeStamp;
//    }
//
//    public void setTimeStamp(String timeStamp) {
//        this.timeStamp = timeStamp;
//    }
//
//    public String getMessageKey() {
//        return messageKey;
//    }
//
//    public void setMessageKey(String messageKey) {
//        this.messageKey = messageKey;
//    }
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeInt(this.idMessage);
//        dest.writeInt(this.idRoom);
//        dest.writeString(this.message);
//        dest.writeInt(this.idUser);
//        dest.writeInt(this.idDoctor);
//        dest.writeString(this.isRead);
//        dest.writeString(this.timeStamp);
//        dest.writeString(this.messageKey);
//    }
//
//    public MessageResponse() {
//    }
//
//    public MessageResponse(int idMessage) {
//        this.idMessage = idMessage;
//    }
//
//    protected MessageResponse(Parcel in) {
//        this.idMessage = in.readInt();
//        this.idRoom = in.readInt();
//        this.message = in.readString();
//        this.idUser = in.readInt();
//        this.idDoctor = in.readInt();
//        this.isRead = in.readString();
//        this.timeStamp = in.readString();
//        this.messageKey = in.readString();
//    }
//
//    public static final Creator<MessageResponse> CREATOR = new Creator<MessageResponse>() {
//        @Override
//        public MessageResponse createFromParcel(Parcel source) {
//            return new MessageResponse(source);
//        }
//
//        @Override
//        public MessageResponse[] newArray(int size) {
//            return new MessageResponse[size];
//        }
//    };
//}
