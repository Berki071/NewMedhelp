package com.medhelp.medhelp.ui.doctor.adapters

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.View
import com.medhelp.newmedhelp.model.CategoryResponse
import android.widget.BaseAdapter
import android.widget.SpinnerAdapter
import android.view.ViewGroup
import android.widget.TextView
import com.medhelp.medhelp.R
import java.util.ArrayList

class DocSpinnerAdapter(val context: Context, list: List<CategoryResponse>) : BaseAdapter(), SpinnerAdapter {
    var list: MutableList<CategoryResponse> = mutableListOf()
    val colorHint = Color.parseColor("#E1F5FE")
    val colorText = Color.parseColor("#FFFFFF")

    init {
        val cr = CategoryResponse(context.getString(R.string.specOneElement))
        this.list.add(cr)
        this.list.addAll(list!!)
    }

    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(i: Int): Any {
        return list[i]
    }

    override fun getItemId(i: Int): Long {
        return i.toLong()
    }



    override fun getDropDownView(position: Int, view: View?, parent: ViewGroup?): View {
        val txt = TextView(context)
        txt.setPadding(16, 16, 16, 16)
        txt.textSize = 18f
        txt.gravity = Gravity.CENTER_VERTICAL
        txt.text = list[position].title
        if (position == 0) {
            // Set the hint text color gray
            txt.setTextColor(colorHint)
        } else {
            txt.setTextColor(colorText)
        }
        return txt
    }

    override fun getView(position: Int, view: View?, viewgroup: ViewGroup?): View {
        val txt = TextView(context)
        txt.gravity = Gravity.START
        txt.setPadding(16, 16, 16, 16)
        txt.textSize = 16f
        txt.text = list[position!!].title
        if (position == 0) {
            // Set the hint text color gray
            txt.setTextColor(colorHint)
        } else {
            txt.setTextColor(colorText)
        }
        return txt
    }

    fun getIdSpec(numberInList: Int): Int {
        return list[numberInList].id!!
    }
}