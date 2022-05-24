package com.medhelp.medhelp.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class ScheduleResponse {

    @SerializedName("id_doctor")
    private int idDoctor;

    @SerializedName("full_name")
    private String fullName;

    @SerializedName("is_work")
    private boolean isWork;

    @SerializedName("adm_day")
    private String admDay;

    @SerializedName("adm_time")
    private List<String> admTime = new ArrayList<>();

    @SerializedName("id_spec")
    private int id_spec;

    @SerializedName("foto")
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

    public boolean isWork() {
        return isWork;
    }

    public void setWork(boolean work) {
        isWork = work;
    }

    public String getAdmDay() {
        return admDay;
    }

    public void setAdmDay(String admDay) {
        this.admDay = admDay;
    }

    public List<String> getAdmTime() {
        return admTime;
    }

    public void setAdmTime(List<String> admTime) {
        this.admTime = admTime;
    }

    public int getId_spec() {
        return id_spec;
    }

    public void setId_spec(int id_spec) {
        this.id_spec = id_spec;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
