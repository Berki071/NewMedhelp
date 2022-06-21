package com.medhelp.medhelp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.medhelp.medhelp.ui.electronic_conclusions_fragment.adapters.recy.DataClassForElectronicRecy;

public class AnaliseResponse extends DataClassForElectronicRecy implements Parcelable {
    @SerializedName("data")
    private String date;

    @SerializedName("file")
    private String linkToPDF;


    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public String getLinkToPDF() {
        return linkToPDF;
    }
    public void setLinkToPDF(String linkToPDF) {
        this.linkToPDF = linkToPDF;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.date);
        dest.writeString(this.linkToPDF);
    }

    public AnaliseResponse() {
    }

    protected AnaliseResponse(Parcel in) {
        this.date = in.readString();
        this.linkToPDF = in.readString();
    }

    public AnaliseResponse(String date, String idAnalise, String linkToPDF)
    {
        this.date=date;
        this.linkToPDF=linkToPDF;
    }

    public static final Creator<AnaliseResponse> CREATOR = new Creator<AnaliseResponse>() {
        @Override
        public AnaliseResponse createFromParcel(Parcel source) {
            return new AnaliseResponse(source);
        }

        @Override
        public AnaliseResponse[] newArray(int size) {
            return new AnaliseResponse[size];
        }
    };

    //для электронных заключений
    public String getDateForZakl(){
       return getDate().substring(getDate().length()-10);
    }
    public String getTitle() {
        return getDate().substring(getDate().length()-10)+" "+getDate().substring(0,getDate().length()-14);
    }
}
