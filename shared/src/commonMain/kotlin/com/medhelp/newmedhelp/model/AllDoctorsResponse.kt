package com.medhelp.newmedhelp.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
open class AllDoctorsResponse {
    @SerialName("id_doctor") var id = 0
    @SerialName("full_name") var fio_doctor: String? = null
    @SerialName("id_spec") var id_specialties_string: String? = null
    @SerialName("stag") var experience: String? = null
    @SerialName("specialty") var name_specialties: String? = null
    @SerialName("dop_info") var dop_info: String? = null
    @SerialName("image_url") var image_url: String? = null

    constructor(fio_doctor: String, titleSpec : String) {
        this.fio_doctor = fio_doctor
        this.name_specialties = titleSpec
    }

    constructor(
        id: Int,
        fio_doctor: String?,
        id_specialties_string: String?,
        experience: String?,
        name_specialties: String?,
        dop_info: String?,
        image_url: String?,
    ) {                     //for ios
        this.id = id
        this.fio_doctor = fio_doctor
        this.id_specialties_string = id_specialties_string
        this.experience = experience
        this.name_specialties = name_specialties
        this.dop_info = dop_info
        this.image_url = image_url
    }

    fun  getIdSpecialtiesIntList() : MutableList<Int>?{
        return setIdSpecialtiesString(id_specialties_string)
    }
//    fun  getIdSpecialtiesStringList() : MutableList<String>?{    //for ios
//        val tmp = setIdSpecialtiesString(id_specialties_string) ?: return null
//
//        var tmpNew = mutableListOf<String>()
//        for(i in tmp){
//            tmpNew.add(i.toString() )
//        }
//        return tmpNew
//    }

    fun setIdSpecialtiesString(id_specialties_string : String?) : MutableList<Int>?
    {
        this.id_specialties_string = id_specialties_string;

        if (this.id_specialties_string == null || this.id_specialties_string!!.isEmpty())
            return null

        var list : MutableList<Int> = mutableListOf()
        val numberCommas = getNumberOfCommas (this.id_specialties_string!!) + 1;

        if (numberCommas == 1) {
            list.add(this.id_specialties_string!!.toInt());
            return list
        }

        var str =this.id_specialties_string!!
        for (i in 0 until numberCommas)
        {
            val num = str.indexOf (",");

            if (num != -1) {
                val spec = str.substring (0, num);
                list.add(spec.toInt());
                str = str.substring(num + 1, str.length);
            } else {
                try {
                    list.add(str.toInt());
                } catch (e : Exception) { }
                break;
            }
        }

        return list
    }


    fun getNumberOfCommas(str : String) : Int
    {
        var commas = 0;
        for (ch in str.toCharArray())
        {
            if (ch == ',')
                commas++
        }
        return commas
    }
}