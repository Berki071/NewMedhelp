package com.medhelp.medhelp.data.model;

import com.google.gson.annotations.SerializedName;
import com.medhelp.medhelp.utils.main.TimesUtils;

public class MessageSupList implements Comparable  {
    @SerializedName("id_message") private String idMessage;
    @SerializedName("data") String date;
    @SerializedName("time") String time;
    @SerializedName("text") String message;
    @SerializedName("otpravitel") String sender;
    @SerializedName("view_user") boolean viewTech;
    @SerializedName("view_tech") boolean viewUser;
    @SerializedName("tip") String type;

    Long timeL;
    Long dateL;
    int timezoneMillSec =0;

    public String getIdMessage() {
        return idMessage;
    }
    public void setIdMessage(String idMessage) {
        this.idMessage = idMessage;
    }

    public Long getTimeL() {
        if(timeL==null)
            timeL= TimesUtils.stringToLong(time, TimesUtils.DATE_FORMAT_HHmm);

        return timeL;
    }

    public Long getDateL() {
        if(dateL==null)
            dateL= TimesUtils.stringToLong(date, TimesUtils.DATE_FORMAT_ddMMyyyy);

        return dateL;
    }

    public void correctByTimeZone(int timezone)
    {
        this.timezoneMillSec =timezone;
        Long tmpDataMillSec=TimesUtils.stringToLong(time+" "+date,TimesUtils.DATE_FORMAT_HHmm+" "+TimesUtils.DATE_FORMAT_ddMMyyyy);
        tmpDataMillSec+=timezone;
        timeL=null;
        dateL=null;
        setTime(TimesUtils.longToString(tmpDataMillSec,TimesUtils.DATE_FORMAT_HHmm));
        setDate(TimesUtils.longToString(tmpDataMillSec,TimesUtils.DATE_FORMAT_ddMMyyyy));
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public boolean isViewTech() {
        return viewTech;
    }

    public void setViewTech(boolean viewTech) {
        this.viewTech = viewTech;
    }

    public boolean isViewUser() {
        return viewUser;
    }

    public void setViewUser(boolean viewUser) {
        this.viewUser = viewUser;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getTimezoneMillSec() {
        return timezoneMillSec;
    }

    @Override
    public int compareTo(Object o) {
        MessageSupList tmp=(MessageSupList)o;

        int tmpDifference=(int)(getDateL()-tmp.getDateL());
        if(tmpDifference!=0)
            return tmpDifference;
        else
        {
            return (int)(getTimeL()-tmp.getTimeL());
        }
    }
}
