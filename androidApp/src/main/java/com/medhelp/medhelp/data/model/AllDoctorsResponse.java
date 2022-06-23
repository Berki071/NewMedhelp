//package com.medhelp.medhelp.data.model;
//
//import com.google.gson.annotations.SerializedName;
//import com.medhelp.medhelp.utils.timber_log.LoggingTree;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import timber.log.Timber;
//
//public class AllDoctorsResponse {
//    @SerializedName("id_doctor")
//    private int id_doctor;
//
//    @SerializedName("full_name")
//    private String fio_doctor;
//
//    @SerializedName("id_spec")
//    private String id_specialties_string;
//
//    @SerializedName("stag")
//    private String experience;
//
//    @SerializedName("specialty")
//    private String name_specialties;
//
//    @SerializedName("dop_info")
//    private String dop_info;
//
//    @SerializedName("image_url")
//    private String image_url;
//
//    private List<Integer> id_specialties_int_list;
//
//    public List<Integer> getId_specialties_int_list() {
//        if(id_specialties_int_list==null)
//        {
//            setId_specialties_string(id_specialties_string);
//        }
//
//        return id_specialties_int_list;
//    }
//
//    public void setId_specialties_int_list(List<Integer> id_specialties_int_list) {
//        this.id_specialties_int_list = id_specialties_int_list;
//    }
//
//
//    public int getId_doctor() {
//        return id_doctor;
//    }
//
//    public void setId_doctor(int id_doctor) {
//        this.id_doctor = id_doctor;
//    }
//
//    public String getFio_doctor() {
//        return fio_doctor;
//    }
//
//    public void setFio_doctor(String fio_doctor) {
//        this.fio_doctor = fio_doctor;
//    }
//
//    public String getId_specialties_string() {
//        return id_specialties_string;
//    }
//
//    public void setId_specialties_string(String id_specialties_string) {
//        this.id_specialties_string = id_specialties_string;
//
//        if(this.id_specialties_string.isEmpty())
//            return;
//
//        id_specialties_int_list=new ArrayList<>();
//        int numberCommas= getNumberOfCommas(this.id_specialties_string)+1;
//
//        if(numberCommas==1)
//        {
//            id_specialties_int_list.add(Integer.parseInt(this.id_specialties_string));
//            return;
//        }
//
//        String str=this.id_specialties_string;
//        for(int i=0;i<numberCommas;i++)
//        {
//            int num=str.indexOf(",");
//
//            if(num!=-1) {
//                String spec = str.substring(0,num);
//                id_specialties_int_list.add(Integer.parseInt(spec));
//                str=str.substring(num+1,str.length());
//            }
//            else
//            {
//                try {
//                    id_specialties_int_list.add(Integer.parseInt(str));
//                }catch (Exception e){
//                    Timber.tag("my").e(LoggingTree.getMessageForError(e, "AllDoctorsResponse/setId_specialties_string"));
//                }
//                break;
//            }
//        }
//    }
//
//    private int getNumberOfCommas(String str)
//    {
//        int commas=0;
//        for(char ch:str.toCharArray())
//        {
//            if(ch==',')
//                commas++;
//        }
//        return commas;
//    }
//
//
//    public String getExperience() {
//        return experience;
//    }
//
//    public void setExperience(String experience) {
//        this.experience = experience;
//    }
//
//    public String getName_specialties() {
//        return name_specialties;
//    }
//
//    public void setName_specialties(String name_specialties) {
//        this.name_specialties = name_specialties;
//    }
//
//    public String getDop_info() {
//        return dop_info;
//    }
//
//    public void setDop_info(String dop_info) {
//        this.dop_info = dop_info;
//    }
//
//    public String getImage_url() {
//        return image_url;
//    }
//
//    public void setImage_url(String image_url) {
//        this.image_url = image_url;
//    }
//
//}
