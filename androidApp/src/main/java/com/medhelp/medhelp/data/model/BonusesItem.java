package com.medhelp.medhelp.data.model;

import com.google.gson.annotations.SerializedName;
import com.medhelp.medhelp.utils.TimesUtils;

//public class BonusesItem implements Comparable<BonusesItem> {
//    @SerializedName("data_bonus")
//    String date;
//    @SerializedName("sym_bonus")
//    int value;
//    @SerializedName("statys_bonus")
//    String status;
//
//    public String getDate() {
//        return date;
//    }
//
//    public void setDate(String date) {
//        this.date = date;
//    }
//
//    public int getValue() {
//        return value;
//    }
//
//    public void setValue(int value) {
//        this.value = value;
//    }
//
//    public String getStatus() {
//        return status;
//    }
//
//    public void setStatus(String status) {
//        this.status = status;
//    }
//
//    @Override
//    public int compareTo(BonusesItem o) {
//        Long l1= TimesUtils.stringToLong(getDate(),TimesUtils.DATE_FORMAT_ddMMyyyy);
//        Long l2= TimesUtils.stringToLong(o.getDate(),TimesUtils.DATE_FORMAT_ddMMyyyy);
//
//        return l1.compareTo(l2);
//    }
//}
