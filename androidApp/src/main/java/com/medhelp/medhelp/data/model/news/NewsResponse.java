package com.medhelp.medhelp.data.model.news;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class NewsResponse implements Parcelable {
    @SerializedName("id")
    private int id;

    @SerializedName("data")
    private String date;

    @SerializedName("title")
    private String title;

    @SerializedName("text")
    private String text;

    @SerializedName("image")
    private String image;

    public NewsResponse(int id, String date,String title, String text) {
        this.id=id;
        this.date=date;
        this.title = title;
        this.text = text;
        this.image = image;
    }

    protected NewsResponse(Parcel in) {
        id = in.readInt();
        date = in.readString();
        title = in.readString();
        text = in.readString();
        image = in.readString();
    }

    public static final Creator<NewsResponse> CREATOR = new Creator<NewsResponse>() {
        @Override
        public NewsResponse createFromParcel(Parcel in) {
            return new NewsResponse(in);
        }

        @Override
        public NewsResponse[] newArray(int size) {
            return new NewsResponse[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(date);
        dest.writeString(title);
        dest.writeString(text);
        dest.writeString(image);
    }
}
