//package com.medhelp.medhelp.data.model.settings;
//
//import com.google.gson.annotations.SerializedName;
//
//public class SettingsAllBranchHospitalResponse implements Comparable {
//
//    @SerializedName("id_filial") private int idBranch;
//    @SerializedName("naim_filial") private String nameBranch;
//    @SerializedName("shopid") private String idShop;
//    @SerializedName("secretkey") private String keyShope;
//    @SerializedName("clientkey") private String keyAppYandex;
//
//
//    private Boolean isFavorite = false;
//
//    public SettingsAllBranchHospitalResponse() {
//    }
//
//    public SettingsAllBranchHospitalResponse(int idBranch, String nameBranch, Boolean isFavorite) {
//        this.idBranch = idBranch;
//        this.nameBranch = nameBranch;
//        this.isFavorite = isFavorite;
//    }
//
//    public String getIdShop() {
//        return idShop;
//    }
//
//    public void setIdShop(String idShop) {
//        this.idShop = idShop;
//    }
//
//    public String getKeyShope() {
//        return keyShope;
//    }
//
//    public void setKeyShope(String keyShope) {
//        this.keyShope = keyShope;
//    }
//
//    public String getKeyAppYandex() {
//        return keyAppYandex;
//    }
//
//    public void setKeyAppYandex(String keyAppYandex) {
//        this.keyAppYandex = keyAppYandex;
//    }
//
//    public Boolean getFavorite() {
//        return isFavorite;
//    }
//
//    public void setFavorite(Boolean favorite) {
//        isFavorite = favorite;
//    }
//
//    public int getIdBranch() {
//        return idBranch;
//    }
//
//    public void setIdBranch(int idBranch) {
//        this.idBranch = idBranch;
//    }
//
//    public String getNameBranch() {
//        return nameBranch;
//    }
//
//    public void setNameBranch(String nameBranch) {
//        this.nameBranch = nameBranch;
//    }
//
//    @Override
//    public int compareTo(Object o) {
//
//        int res1 = isFavorite.compareTo(((SettingsAllBranchHospitalResponse) o).getFavorite());
//        res1 *= -1;
//
//        if (res1 != 0)
//            return res1;
//        else
//            return nameBranch.compareTo(((SettingsAllBranchHospitalResponse) o).getNameBranch());
//    }
//}
