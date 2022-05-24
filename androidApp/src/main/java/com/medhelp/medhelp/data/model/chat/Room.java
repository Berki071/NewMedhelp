//package com.medhelp.medhelp.data.model.chat;
//
//import android.os.Parcel;
//import android.os.Parcelable;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
//
//public class Room implements Parcelable, RealmObject {
//    @PrimaryKey
//    private long IdRoom;
//    private InfoAboutDoc infoAboutDoc;
//    private RealmList<Message> listMsg;
//
//    public long getRoom() {
//        return IdRoom;
//    }
//
//    public void setRoom(long room) {
//        this.IdRoom = room;
//    }
//
//    public InfoAboutDoc getInfoAboutDoc() {
//        return infoAboutDoc;
//    }
//
//    public void setInfoAboutDoc(InfoAboutDoc infoAboutDoc) {
//        this.infoAboutDoc = infoAboutDoc;
//    }
//
//    public RealmList<Message> getListMsg() {
//        return listMsg;
//    }
//
//    public void setListMsg(RealmList<Message> listMsg) {
//        this.listMsg = listMsg;
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
//        dest.writeLong(this.IdRoom);
//        dest.writeParcelable(this.infoAboutDoc, flags);
//
//        Message[] masMes=(Message[])listMsg.toArray();
//        List<Message> arrM=new ArrayList<>(Arrays.asList(masMes));
//
//        dest.writeTypedList(arrM);
//    }
//
//    public Room() {
//    }
//
//    protected Room(Parcel in) {
//        this.IdRoom = in.readLong();
//        this.infoAboutDoc = in.readParcelable(InfoAboutDoc.class.getClassLoader());
//
//        List<Message> arrM= in.createTypedArrayList(Message.CREATOR);
//        Message[] masMes=(Message[])arrM.toArray();
//        RealmList<Message> realmList=new RealmList<>();
//
//        realmList.addAll(Arrays.asList(masMes).subList(1, masMes.length));
//
//        this.listMsg = realmList;
//    }
//
//    public static final Parcelable.Creator<Room> CREATOR = new Parcelable.Creator<Room>() {
//        @Override
//        public Room createFromParcel(Parcel source) {
//            return new Room(source);
//        }
//
//        @Override
//        public Room[] newArray(int size) {
//            return new Room[size];
//        }
//    };
//}
