//package com.medhelp.medhelp.data.model;
//
//import com.google.gson.annotations.SerializedName;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@SuppressWarnings("unused")
//public class ServiceList {
//
//    @SerializedName("imgError")
//    private boolean error;
//
//    @SerializedName("Message")
//    private String message;
//
//    @SerializedName("response")
//    private List<ServiceResponse> services = new ArrayList<>();
//
//    public String getMessage() {
//        return message;
//    }
//
//    public void setMessage(String message) {
//        this.message = message;
//    }
//
//    public boolean isError() {
//        return error;
//    }
//
//    public void setError(boolean error) {
//        this.error = error;
//    }
//
//    public List<ServiceResponse> getServices() {
//        return services;
//    }
//
//    public void setServices(List<ServiceResponse> services) {
//        this.services = services;
//    }
//}