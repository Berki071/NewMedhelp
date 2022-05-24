package com.medhelp.medhelp.data.model;

import com.google.gson.annotations.SerializedName;
import com.medhelp.medhelp.ui.electronic_conclusions_fragment.adapters.recy.DataClassForElectronicRecy;

public class ResultZakl2Item extends DataClassForElectronicRecy {
        @SerializedName("data_priem")
        private String dataPriema;

        @SerializedName("name_spec")
        private String nameSpec;

        @SerializedName("id_kl")
        private String idKl;

        @SerializedName("id_filial")
        private String idFilial;


        public String getDataPriema() {
            return dataPriema;
        }

        public void setDataPriema(String dataPriema) {
            this.dataPriema = dataPriema;
        }

        public String getNameSpec() {
            return nameSpec;
        }

        public void setNameSpec(String nameSpec) {
            this.nameSpec = nameSpec;
        }

        public String getIdKl() {
            return idKl;
        }

        public void setIdKl(String idKl) {
            this.idKl = idKl;
        }

        public String getIdFilial() {
            return idFilial;
        }

        public void setIdFilial(String idFilial) {
            this.idFilial = idFilial;
        }

        public String getNameFile(String name){
            return name+"_"+getDataPriema().replace(".","_")+"_"+getNameSpec()+".pdf";
        }

        public String getTitle(){
            return dataPriema+" "+nameSpec;
        }
}
