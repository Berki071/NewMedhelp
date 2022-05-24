package com.medhelp.medhelp.ui.schedule.recycler;

import java.util.List;

public class DateState {
    private int idDoctor;
    private int idSpec;
    private String doctor;
    private List<String> category;
    private String foto;

    public DateState(int idDoctor, int idSpec, String name, List<String> category,String foto) {
        this.idDoctor = idDoctor;
        this.idSpec = idSpec;
        this.doctor = name;
        this.category = category;
        this.foto=foto;
    }

    public String getDoctor() {
        return doctor;
    }

    public void setDoctor(String name) {
        this.doctor = name;
    }

    public List<String> getCategory() {
        return category;
    }

    public int getIdDoctor() {
        return idDoctor;
    }

    public void setIdDoctor(int idDoctor) {
        this.idDoctor = idDoctor;
    }

    public int getIdSpec() {
        return idSpec;
    }

    public void setIdSpec(int idSpec) {
        this.idSpec = idSpec;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}
