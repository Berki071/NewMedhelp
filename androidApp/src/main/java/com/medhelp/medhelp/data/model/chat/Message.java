//package com.medhelp.medhelp.data.model.chat;
//
//import android.os.Parcel;
//import android.os.Parcelable;
//import androidx.annotation.NonNull;
//
//import io.realm.RealmObject;
//import io.realm.annotations.PrimaryKey;
//
//public class Message implements Parcelable, Comparable<Message>, RealmObject {
//    public static final int MSG=0;
//    public static final int DATE=1;
//    public static final int Img=2;
//    public static final int FILE=3;
//
//    @PrimaryKey
//    private long id;
//    private boolean isMyMsg;
//    private Boolean isRead;
//    private String msg;
//    private Long timeUtc;
//    public int type;
//
//    private long idUser;
//
//    public long getIdUser() {
//        return idUser;
//    }
//
//    public void setIdUser(long idUser) {
//        this.idUser = idUser;
//    }
//
//    public int getType() {
//        return type;
//    }
//
//    public void setType(int type) {
//        this.type = type;
//    }
//
//    public long getId() {
//        return id;
//    }
//
//    public void setId(long id) {
//        this.id = id;
//    }
//
//    public boolean isMyMsg() {
//        return isMyMsg;
//    }
//
//    public void setMyMsg(boolean myMsg) {
//        isMyMsg = myMsg;
//    }
//
//    public Boolean getRead() {
//        return isRead;
//    }
//
//    public void setRead(Boolean read) {
//        isRead = read;
//    }
//
//    public String getMsg() {
//        return msg;
//    }
//
//    public void setMsg(String msg) {
//        this.msg = msg;
//    }
//
//    public Long getTimeUtc() {
//        return timeUtc;
//    }
//
//    public void setTimeUtc(Long timeUtc) {
//        this.timeUtc = timeUtc;
//    }
//
//    @Override
//    public int compareTo(@NonNull Message o) {
//        long t1=this.timeUtc;
//        long t2=o.timeUtc;
//        return (int)(t1-t2);
//    }
//
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeLong(this.id);
//        dest.writeByte(this.isMyMsg ? (byte) 1 : (byte) 0);
//        dest.writeValue(this.isRead);
//        dest.writeString(this.msg);
//        dest.writeValue(this.timeUtc);
//        dest.writeInt(this.type);
//        dest.writeLong(this.idUser);
//    }
//
//    public Message() {
//    }
//
//    protected Message(Parcel in) {
//        this.id = in.readLong();
//        this.isMyMsg = in.readByte() != 0;
//        this.isRead = (Boolean) in.readValue(Boolean.class.getClassLoader());
//        this.msg = in.readString();
//        this.timeUtc = (Long) in.readValue(Long.class.getClassLoader());
//        this.type = in.readInt();
//        this.idUser = in.readLong();
//    }
//
//    public static final Creator<Message> CREATOR = new Creator<Message>() {
//        @Override
//        public Message createFromParcel(Parcel source) {
//            return new Message(source);
//        }
//
//        @Override
//        public Message[] newArray(int size) {
//            return new Message[size];
//        }
//    };
//}
