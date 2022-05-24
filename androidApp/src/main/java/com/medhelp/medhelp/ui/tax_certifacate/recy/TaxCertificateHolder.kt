package com.medhelp.medhelp.ui.tax_certifacate.recy


import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.chauthai.swipereveallayout.SwipeRevealLayout
import com.medhelp.medhelp.R
import com.medhelp.medhelp.data.pref.PreferencesManager
import java.io.File

class TaxCertificateHolder(val view : View, val listener : TaxCertificateHolderListener) : RecyclerView.ViewHolder(view) {
    val title : TextView = view.findViewById(R.id.title)
    val date : TextView = view.findViewById(R.id.date)
    val swipeRevealLayout : SwipeRevealLayout = view.findViewById(R.id.swipeRevealLayout)
    val bdnDelete : FrameLayout = view.findViewById(R.id.btnDelete)
    val btnTitle : FrameLayout = view.findViewById(R.id.btnTitle)

    var data : File? =null
    val preferencesManager =PreferencesManager(view.context)
    val showTooltipTax =ShowTooltipTax()

    init{
        btnTitle.setOnClickListener{
            listener.onClick(data!!)
        }

        bdnDelete.setOnClickListener{
            data?.let { it1 -> listener.delete(title.text.toString() ,it1) }
        }
    }

    fun onBind(file : File, position : Int){
        data=file

        val tmp =  Integer.parseInt((file.length()/1024).toString());

        var nameE=file.name
        nameE=nameE.substring(0, nameE.lastIndexOf("."))
        nameE=nameE.replace("Tax_","")

        val ind=nameE.lastIndexOf("_")
        nameE=nameE.substring(0,ind)+" - "+ nameE.substring(ind+1)
        nameE=nameE.replace("_"," ")

        title.text=nameE.substring(0,nameE.length-23)
        date.text=nameE.substring(nameE.length-23)

        if(position==0){
            if(preferencesManager.showTooltipTax){
                  showTooltip("Сдвиньте влево для удаления")
            }
        }
    }

    fun showTooltip(msg : String){
        preferencesManager.setShowTooltipTax()
        showTooltipTax.showTooltip(btnTitle, msg)
    }

    interface TaxCertificateHolderListener{
        fun onClick(file : File);
        fun delete(titleRecy : String,file : File);
    }
}