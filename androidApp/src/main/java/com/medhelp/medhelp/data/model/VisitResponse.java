package com.medhelp.medhelp.data.model;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;
import com.medhelp.medhelp.utils.main.TimesUtils;

@SuppressWarnings("unused")
public class VisitResponse implements Parcelable, Comparable {
    @SerializedName("id_zapisi") private int idRecord;
    @SerializedName("idysl") private int idServices;
    @SerializedName("naim_ysl") private String nameServices;
    @SerializedName("id_specialty") private int id_specialty;
    @SerializedName("adm_date") private String dateOfReceipt;
    @SerializedName("adm_time") private String timeOfReceipt;
    @SerializedName("status") private String status;
    @SerializedName("call") private String call;
    @SerializedName("idsotr") private int idSotr;
    @SerializedName("image_url") private String photoSotr;
    @SerializedName("full_name") private String nameSotr;
    @SerializedName("rabotaet") private String works;
    @SerializedName("id_kl") private int idUser;
    @SerializedName("id_filial") private int idBranch;
    @SerializedName("naim_filial") private String nameBranch;
    @SerializedName("kabinet") private String cabinet;
    @SerializedName("komment") private String comment;
    @SerializedName("dlit") private int durationService;
    @SerializedName("price") private int price;
    @SerializedName("dop") private String dop;

    private boolean isAddInBasket = false;
    long timeMils = 0;
    String userName;
    String executeTheScenario;
    int durationSec;


    public VisitResponse() {
        durationSec=-1;
    }

    protected VisitResponse(Parcel in) {
        idRecord = in.readInt();
        idServices = in.readInt();
        nameServices = in.readString();
        id_specialty = in.readInt();
        dateOfReceipt = in.readString();
        timeOfReceipt = in.readString();
        status = in.readString();
        call = in.readString();
        idSotr = in.readInt();
        photoSotr = in.readString();
        nameSotr = in.readString();
        works = in.readString();
        idUser = in.readInt();
        idBranch = in.readInt();
        nameBranch = in.readString();
        cabinet = in.readString();
        comment = in.readString();
        durationService = in.readInt();
        price = in.readInt();
        dop = in.readString();
        isAddInBasket = in.readByte() != 0;
        timeMils = in.readLong();
        userName = in.readString();
        executeTheScenario = in.readString();
        durationSec = in.readInt();
    }

    public static final Creator<VisitResponse> CREATOR = new Creator<VisitResponse>() {
        @Override
        public VisitResponse createFromParcel(Parcel in) {
            return new VisitResponse(in);
        }

        @Override
        public VisitResponse[] newArray(int size) {
            return new VisitResponse[size];
        }
    };

    public String getCabinet() {
        return cabinet;
    }

    public void setCabinet(String cabinet) {
        this.cabinet = cabinet;
    }

    public boolean isAddInBasket() {
        return isAddInBasket;
    }

    public void setAddInBasket(boolean addInBasket) {
        isAddInBasket = addInBasket;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getIdRecord() {
        return idRecord;
    }

    public void setIdRecord(int idRecord) {
        this.idRecord = idRecord;
    }

    public String getNameSotr() {
        return nameSotr;
    }

    public void setNameSotr(String nameSotr) {
        this.nameSotr = nameSotr;
    }

    public String getNameServices() {
        return nameServices;
    }

    public void setNameServices(String nameServices) {
        this.nameServices = nameServices;
    }

    public int getId_specialty() {
        return id_specialty;
    }

    public void setId_specialty(int id_specialty) {
        this.id_specialty = id_specialty;
    }

    public String getDateOfReceipt() {
        return dateOfReceipt;
    }

    public void setDateOfReceipt(String dateOfReceipt) {
        this.dateOfReceipt = dateOfReceipt;
    }

    public String getTimeOfReceipt() {
        return timeOfReceipt;
    }

    public void setTimeOfReceipt(String timeOfReceipt) {
        this.timeOfReceipt = timeOfReceipt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCall() {
        return call;
    }

    public void setCall(String call) {
        this.call = call;
    }

    public String getPhotoSotr() {
        return photoSotr;
    }

    public void setPhotoSotr(String photoSotr) {
        this.photoSotr = photoSotr;
    }

    public String getWorks() {
        return works;
    }

    public void setWorks(String works) {
        this.works = works;
    }

    public int getIdServices() {
        return idServices;
    }

    public void setIdServices(int idServices) {
        this.idServices = idServices;
    }

    public int getIdSotr() {
        return idSotr;
    }

    public void setIdSotr(int idSotr) {
        this.idSotr = idSotr;
    }

    public int getIdBranch() {
        return idBranch;
    }

    public void setIdBranch(int idBranch) {
        this.idBranch = idBranch;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getNameBranch() {
        return nameBranch;
    }

    public void setNameBranch(String nameBranch) {
        this.nameBranch = nameBranch;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDop() {
        return dop;
    }

    public void setDop(String dop) {
        this.dop = dop;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getExecuteTheScenario() {
        return executeTheScenario;
    }

    public void setExecuteTheScenario(String executeTheScenario) {
        this.executeTheScenario = executeTheScenario;
    }

    public int getDurationSec() {
        if(durationSec==-1)
            durationSec=durationService*60;
        return durationSec;
    }

    public int getDurationService() {
        return durationService;
    }

    public void setDurationSec(int durationSec) {
        this.durationSec = durationSec;
    }

    @Override
    public int compareTo(@NonNull Object o) {

        long dateCurrent = TimesUtils.stringToLong(dateOfReceipt, TimesUtils.DATE_FORMAT_ddMMyyyy);
        long dateObj = TimesUtils.stringToLong(((VisitResponse) o).getDateOfReceipt(), TimesUtils.DATE_FORMAT_ddMMyyyy);

        if (dateCurrent > dateObj)
            return 1;
        else if (dateCurrent < dateObj)
            return -1;
        else {
            long timeCurrent = TimesUtils.stringToLong(timeOfReceipt, TimesUtils.DATE_FORMAT_HHmm);
            long timeObj = TimesUtils.stringToLong(((VisitResponse) o).getTimeOfReceipt(), TimesUtils.DATE_FORMAT_HHmm);

            return Long.compare(timeCurrent, timeObj);
        }
    }

    public long getTimeMills() {
        if (timeMils == 0)
            return TimesUtils.stringToLong((getTimeOfReceipt() + " " + getDateOfReceipt()), TimesUtils.DATE_FORMAT_HHmm_ddMMyyyy);
        else
            return timeMils;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(idRecord);
        dest.writeInt(idServices);
        dest.writeString(nameServices);
        dest.writeInt(id_specialty);
        dest.writeString(dateOfReceipt);
        dest.writeString(timeOfReceipt);
        dest.writeString(status);
        dest.writeString(call);
        dest.writeInt(idSotr);
        dest.writeString(photoSotr);
        dest.writeString(nameSotr);
        dest.writeString(works);
        dest.writeInt(idUser);
        dest.writeInt(idBranch);
        dest.writeString(nameBranch);
        dest.writeString(cabinet);
        dest.writeString(comment);
        dest.writeInt(durationService);
        dest.writeInt(price);
        dest.writeString(dop);
        dest.writeByte((byte) (isAddInBasket ? 1 : 0));
        dest.writeLong(timeMils);
        dest.writeString(userName);
        dest.writeString(executeTheScenario);
        dest.writeInt(durationSec);
    }
}