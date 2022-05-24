package com.medhelp.medhelp.data.model;

import com.google.gson.annotations.SerializedName;

public class LoadDataZaklAmbItem {
    @SerializedName("data_priem")
    private String dataPriem;

    @SerializedName("diagnoz")
    private String diagnoz;

    @SerializedName("rekomend")
    private String rekomend;

    @SerializedName("sotr")
    private String sotr;

    @SerializedName("sotr_spec")
    private String sotrSpec;

    @SerializedName("cons")
    private String cons;

    @SerializedName("shapka")
    private String shapka;

    @SerializedName("nom_amb")
    private String nom_amb;

    @SerializedName("OOO")
    private String ooo;

    public String getDataPriem() {
        return dataPriem;
    }

    public void setDataPriem(String dataPriem) {
        this.dataPriem = dataPriem;
    }

    public String getDiagnoz() {
        return diagnoz;
    }

    public void setDiagnoz(String diagnoz) {
        this.diagnoz = diagnoz;
    }

    public String getRekomend() {
        return rekomend;
    }

    public void setRekomend(String rekomend) {
        this.rekomend = rekomend;
    }

    public String getSotr() {
        return sotr;
    }

    public void setSotr(String sotr) {
        this.sotr = sotr;
    }

    public String getSotrSpec() {
        return sotrSpec;
    }

    public void setSotrSpec(String sotrSpec) {
        this.sotrSpec = sotrSpec;
    }

    public String getCons() {
        return cons;
    }

    public void setCons(String cons) {
        this.cons = cons;
    }

    public String getShapka() {
        return shapka;
    }

    public void setShapka(String shapka) {
        this.shapka = shapka;
    }

    public String getNom_amb() {
        return nom_amb;
    }

    public void setNom_amb(String nom_amb) {
        this.nom_amb = nom_amb;
    }

    public String getOoo() {
        return ooo;
    }

    public void setOoo(String ooo) {
        this.ooo = ooo;
    }
}
