package com.medhelp.medhelp.data.model;

import com.google.gson.annotations.SerializedName;

public class SendTaxCertificateResponse {
    @SerializedName("nom_doc")
    private String nom_doc;
    @SerializedName("fio_nalogoplat")
    private String fio_nalogoplat;
    @SerializedName("inn")
    private String inn;
    @SerializedName("fio_pac")
    private String fio_pac;
    @SerializedName("nom_amb")
    private String nom_amb;
    @SerializedName("itogo")
    private String itogo;
    @SerializedName("itogo_propis")
    private String itogo_propis;
    @SerializedName("dati_oplat")
    private String dati_oplat;
    @SerializedName("OOO")
    private String OOO;
    @SerializedName("rekviziti")
    private String rekviziti;
    @SerializedName("licenziya")
    private String licenziya;
    @SerializedName("min_data")
    private String min_data;
    @SerializedName("kto_vidal")
    private String kto_vidal;
    @SerializedName("telefon")
    private String telefon;
    @SerializedName("dbname")
    private String dbname;

    public String getNom_doc() {
        return nom_doc;
    }
    public void setNom_doc(String nom_doc) {
        this.nom_doc = nom_doc;
    }

    public String getFio_nalogoplat() {
        return fio_nalogoplat;
    }
    public void setFio_nalogoplat(String fio_nalogoplat) {
        this.fio_nalogoplat = fio_nalogoplat;
    }

    public String getInn() {
        return inn;
    }
    public void setInn(String inn) {
        this.inn = inn;
    }

    public String getFio_pac() {
        return fio_pac;
    }
    public void setFio_pac(String fio_pac) {
        this.fio_pac = fio_pac;
    }

    public String getNom_amb() {
        return nom_amb;
    }
    public void setNom_amb(String nom_amb) {
        this.nom_amb = nom_amb;
    }

    public String getItogo() {
        return itogo;
    }
    public void setItogo(String itogo) {
        this.itogo = itogo;
    }

    public String getItogo_propis() {
        return itogo_propis;
    }
    public void setItogo_propis(String itogo_propis) {
        this.itogo_propis = itogo_propis;
    }

    public String getDati_oplat() {
        return dati_oplat;
    }
    public void setDati_oplat(String dati_oplat) {
        this.dati_oplat = dati_oplat;
    }

    public String getOOO() {
        return OOO;
    }
    public void setOOO(String OOO) {
        this.OOO = OOO;
    }

    public String getRekviziti() {
        return rekviziti;
    }
    public void setRekviziti(String rekviziti) {
        this.rekviziti = rekviziti;
    }

    public String getLicenziya() {
        return licenziya;
    }
    public void setLicenziya(String licenziya) {
        this.licenziya = licenziya;
    }

    public String getMin_data() {
        return min_data;
    }
    public void setMin_data(String min_data) {
        this.min_data = min_data;
    }

    public String getKto_vidal() {
        return kto_vidal;
    }
    public void setKto_vidal(String kto_vidal) {
        this.kto_vidal = kto_vidal;
    }

    public String getTelefon() {
        return telefon;
    }
    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public String getDbname() {
        return dbname;
    }
    public void setDbname(String dbname) {
        this.dbname = dbname;
    }
}
