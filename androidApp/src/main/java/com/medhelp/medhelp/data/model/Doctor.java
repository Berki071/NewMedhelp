package com.medhelp.medhelp.data.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Doctor implements Parcelable {
    @SerializedName("id_doctor")
    private int idDoctor;

    @SerializedName("full_name")
    private String fullName;

    @SerializedName("id_spec")   //было инт
    private String idSpec;

    @SerializedName("stag")
    private String experience;

    @SerializedName("specialty")
    private String specialty;

    @SerializedName("dop_info")
    private String dop_info;

    @SerializedName("image_url")
    private String photo;


    public int getIdDoctor() {
        return idDoctor;
    }

    public void setIdDoctor(int idDoctor) {
        this.idDoctor = idDoctor;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getIdSpec() {
        return idSpec;
    }

    public void setIdSpec(String idSpec) {
        this.idSpec = idSpec;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getDop_info() {
        return dop_info;
    }

    public void setDop_info(String dop_info) {
        this.dop_info = dop_info;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.idDoctor);
        dest.writeString(this.fullName);
        dest.writeString(this.idSpec);
        dest.writeString(this.photo);
        dest.writeString(this.experience);
        dest.writeString(this.dop_info);
        dest.writeString(this.specialty);
    }

    public Doctor() {
    }

    protected Doctor(Parcel in) {
        this.idDoctor = in.readInt();
        this.fullName = in.readString();
        this.idSpec = in.readString();
        this.photo = in.readString();
        this.experience = in.readString();
        this.dop_info = in.readString();
        this.specialty = in.readString();
    }

    public static final Creator<Doctor> CREATOR = new Creator<Doctor>() {
        @Override
        public Doctor createFromParcel(Parcel source) {
            return new Doctor(source);
        }

        @Override
        public Doctor[] newArray(int size) {
            return new Doctor[size];
        }
    };
}
