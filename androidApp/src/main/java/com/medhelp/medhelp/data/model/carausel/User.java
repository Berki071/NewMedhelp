package com.medhelp.medhelp.data.model.carausel;

public class User {
    private int idMain;
    private int idSecond;

    private String surname;
    private String name;
    private String patronymic;
    private String phone;
    private String birthday;
    private String email;

    public User(int idMain, int idSecond, String surname, String name, String patronymic, String phone, String birthday, String email) {
        this.idMain = idMain;
        this.idSecond = idSecond;
        this.surname = surname;
        this.name = name;
        this.patronymic = patronymic;
        this.phone = phone;
        this.birthday = birthday;
        this.email = email;
    }

    public User(){
        surname="";
        name="";
        patronymic ="";
        phone="";
        birthday="";
        email="";
    }

    public int getIdMain() {
        return idMain;
    }

    public void setIdMain(int idMain) {
        this.idMain = idMain;
    }

    public int getIdSecond() {
        return idSecond;
    }

    public void setIdSecond(int idSecond) {
        this.idSecond = idSecond;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
