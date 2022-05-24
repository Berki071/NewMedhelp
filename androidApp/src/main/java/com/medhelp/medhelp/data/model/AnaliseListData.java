package com.medhelp.medhelp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class AnaliseListData implements Parcelable {
    @SerializedName("data_start")
    private String dateOfPass;

    @SerializedName("data_end")
    private String dateResult;

    @SerializedName("analiz")
    private String title;

    @SerializedName("statys")
    private String status;

    private Boolean showTooltip=false;

    protected AnaliseListData(Parcel in) {
        dateOfPass = in.readString();
        dateResult = in.readString();
        title = in.readString();
        status = in.readString();
        showTooltip=false;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(dateOfPass);
        dest.writeString(dateResult);
        dest.writeString(title);
        dest.writeString(status);
    }

    public static final Creator<AnaliseListData> CREATOR = new Creator<AnaliseListData>() {
        @Override
        public AnaliseListData createFromParcel(Parcel in) {
            return new AnaliseListData(in);
        }

        @Override
        public AnaliseListData[] newArray(int size) {
            return new AnaliseListData[size];
        }
    };

    public String getDateOfPass() {
        return dateOfPass;
    }

    public void setDateOfPass(String dateOfPass) {
        this.dateOfPass = dateOfPass;
    }

    public String getDateResult() {
        return dateResult;
    }

    public void setDateResult(String dateResult) {
        this.dateResult = dateResult;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public Boolean getShowTooltip() {
        return showTooltip;
    }

    public void setShowTooltip(Boolean showTooltip) {
        this.showTooltip = showTooltip;
    }
}

