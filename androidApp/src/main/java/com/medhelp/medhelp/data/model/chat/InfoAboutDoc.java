//package com.medhelp.medhelp.data.model.chat;
//
//import android.os.Parcel;
//import android.os.Parcelable;
//
//import com.google.gson.annotations.SerializedName;
//
//public class InfoAboutDoc implements Parcelable, RealmObject {
//
//    @SerializedName("idroom")
//    private long room;
//
//    @SerializedName("idsotr")
//    private long idDoc;
//
//    @SerializedName("fio_sotr")
//    private String name;
//
//    @SerializedName("image_url")
//    private String imgLink;
//
//    @SerializedName("stag")
//    private String experience;
//
//    @SerializedName("spec")
//    private String speciality;
//
//    @SerializedName("dop")
//    private String additionally;
//
//    private long forIdUser;
//
//
//    public long getIdDoc() {
//        return idDoc;
//    }
//
//    public void setIdDoc(long idDoc) {
//        this.idDoc = idDoc;
//    }
//
//    public long getRoom() {
//        return room;
//    }
//
//    public void setRoom(long room) {
//        this.room = room;
//    }
//
//    public String getImgLink() {
//        return imgLink;
//    }
//
//    public void setImgLink(String imgLink) {
//        this.imgLink = imgLink;
//    }
//
//    public String getExperience() {
//        return experience;
//    }
//
//    public void setExperience(String experience) {
//        this.experience = experience;
//    }
//
//    public String getSpeciality() {
//        return speciality;
//    }
//
//    public void setSpeciality(String speciality) {
//        this.speciality = speciality;
//    }
//
//    public String getAdditionally() {
//        return additionally;
//    }
//
//    public void setAdditionally(String additionally) {
//        this.additionally = additionally;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public long getForIdUser() {
//        return forIdUser;
//    }
//
//    public void setForIdUser(long forIdUser) {
//        this.forIdUser = forIdUser;
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
//        dest.writeLong(this.room);
//        dest.writeLong(this.idDoc);
//        dest.writeString(this.name);
//        dest.writeString(this.imgLink);
//        dest.writeString(this.experience);
//        dest.writeString(this.speciality);
//        dest.writeString(this.additionally);
//        dest.writeLong(this.forIdUser);
//    }
//
//    public InfoAboutDoc() {
//    }
//
//    protected InfoAboutDoc(Parcel in) {
//        this.room = in.readLong();
//        this.idDoc = in.readLong();
//        this.name = in.readString();
//        this.imgLink = in.readString();
//        this.experience = in.readString();
//        this.speciality = in.readString();
//        this.additionally = in.readString();
//        this.forIdUser = in.readLong();
//    }
//
//    public static final Creator<InfoAboutDoc> CREATOR = new Creator<InfoAboutDoc>() {
//        @Override
//        public InfoAboutDoc createFromParcel(Parcel source) {
//            return new InfoAboutDoc(source);
//        }
//
//        @Override
//        public InfoAboutDoc[] newArray(int size) {
//            return new InfoAboutDoc[size];
//        }
//    };
//}
