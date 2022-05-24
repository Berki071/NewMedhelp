package com.medhelp.medhelp.ui.tax_certifacate.recy

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chauthai.swipereveallayout.ViewBinderHelper
import com.medhelp.medhelp.R
import com.medhelp.medhelp.ui.electronic_conclusions_fragment.adapters.recy.DataClassForElectronicRecy
import com.medhelp.medhelp.ui.tax_certifacate.DataForTaxCertificate
import java.io.File


class TaxCertificateAdapter(val context : Context, var list : MutableList<File>, val listener : TaxCertificateHolder.TaxCertificateHolderListener)
    : RecyclerView.Adapter<TaxCertificateHolder>() {
    private val viewBinderHelper = ViewBinderHelper()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaxCertificateHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.item_tax_certificate2, parent, false)
        return TaxCertificateHolder(view,listener)
    }

    override fun onBindViewHolder(holder: TaxCertificateHolder, position: Int) {
        viewBinderHelper.bind(holder.swipeRevealLayout, list[position].toString());
        holder.onBind(list[position], position)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun deleteItem(file : File){
       for(i in list!!){
           if(i== file){
               list.remove(i)
               notifyDataSetChanged()
               return
           }
       }
    }

    fun setNewLisetData(list: MutableList<File>) {
        this.list=list
        notifyDataSetChanged()
    }

}