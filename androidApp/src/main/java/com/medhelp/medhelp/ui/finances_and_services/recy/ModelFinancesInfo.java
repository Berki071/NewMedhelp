package com.medhelp.medhelp.ui.finances_and_services.recy;

public class ModelFinancesInfo {
    private String date;
    private String services;
    private String price;
    private String status;

    public ModelFinancesInfo(String date, String services, String price, String status) {
        this.date = date;
        this.services = services;
        this.price = price;
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getServices() {
        return services;
    }

    public void setServices(String services) {
        this.services = services;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
