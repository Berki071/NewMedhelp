package com.medhelp.medhelp.data.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class SaleResponse implements Parcelable {
    @SerializedName("id_sale")
    private int idSale;

    @SerializedName("sale_image")
    private String saleImage;

    @SerializedName("sale_description")
    private String saleDescription;

    public int getIdSale() {
        return idSale;
    }

    public void setIdSale(int idSale) {
        this.idSale = idSale;
    }

    public String getSaleImage() {
        return saleImage;
    }

    public void setSaleImage(String saleImage) {
        this.saleImage = saleImage;
    }

    public String getSaleDescription() {
        return saleDescription;
    }

    public void setSaleDescription(String saleDescription) {
        this.saleDescription = saleDescription;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.idSale);
        dest.writeString(this.saleImage);
        dest.writeString(this.saleDescription);
    }

    public SaleResponse() {
    }

    protected SaleResponse(Parcel in) {
        this.idSale = in.readInt();
        this.saleImage = in.readString();
        this.saleDescription = in.readString();
    }

    public static final Creator<SaleResponse> CREATOR = new Creator<SaleResponse>() {
        @Override
        public SaleResponse createFromParcel(Parcel source) {
            return new SaleResponse(source);
        }

        @Override
        public SaleResponse[] newArray(int size) {
            return new SaleResponse[size];
        }
    };
}
