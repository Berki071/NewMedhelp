package com.medhelp.medhelp.ui.doctor.adapters

import com.medhelp.newmedhelp.model.AllDoctorsResponse
import androidx.recyclerview.widget.RecyclerView
import com.medhelp.medhelp.ui.base.BaseViewHolder
import android.view.ViewGroup
import com.medhelp.medhelp.ui.doctor.adapters.DoctorsAdapter
import android.view.LayoutInflater
import com.medhelp.medhelp.R
import android.widget.TextView
import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import android.widget.ImageView
import com.medhelp.medhelp.utils.workToFile.show_file.ShowFile2.BuilderImage
import com.medhelp.medhelp.utils.workToFile.show_file.ShowFile2
import com.medhelp.medhelp.utils.workToFile.show_file.ShowFile2.ShowListener
import com.medhelp.medhelp.data.pref.PreferencesManager
import java.io.File
import java.util.ArrayList

class DoctorsAdapter(var response: MutableList<AllDoctorsResponse>) :
    RecyclerView.Adapter<BaseViewHolder>() {
    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.onBind(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return if (viewType == VIEW_TYPE_NORMAL) {
            ViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_doctor, parent, false)
            )
        } else EmptyViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_error_download, parent, false)
        )
    }

    override fun getItemViewType(position: Int): Int {
        return if (response != null && response!!.size > 0) {
            VIEW_TYPE_NORMAL
        } else 0
    }

    override fun getItemCount(): Int {
        return if (response != null && response!!.size > 0) {
            response!!.size
        } else {
            0
        }
    }

    fun addItems(repoList: List<AllDoctorsResponse>?) {
        response!!.clear()
        response!!.addAll(repoList!!)
        notifyDataSetChanged()
    }

    fun getItem(num: Int): AllDoctorsResponse {
        return response!![num]
    }

    internal inner class ViewHolder(itemView: View) : BaseViewHolder(itemView) {
        var docName: TextView
        var docExp: TextView
        var docImage: ImageView
        var token: String?
        override fun clear() {
            docImage.setImageDrawable(null)
            docName.text = ""
            docExp.text = ""
        }

        @SuppressLint("StringFormatMatches")
        override fun onBind(position: Int) {
            super.onBind(position)
            val repo = response!![position]
            if (repo != null) {
                docName.text = repo.fio_doctor
                docExp.text = repo.name_specialties

                BuilderImage(docImage.context)
                    .setType(ShowFile2.TYPE_ICO)
                    .load(repo.image_url)
                    .token(token)
                    .imgError(R.drawable.sh_doc)
                    .into(docImage)
                    .setListener(object : ShowListener {
                        override fun complete(file: File?) {
                            Log.wtf("", "")
                        }

                        override fun error(error: String?) {
                            Log.wtf("", "")
                        }
                    })
                    .build()

            }
        }

        init {
            docName = itemView.findViewById(R.id.doc_name_tv)
            docExp = itemView.findViewById(R.id.doc_spec_tv)
            docImage = itemView.findViewById(R.id.doc_img)
            val context = itemView.context
            val ph = PreferencesManager(context)
            token = ph.currentUserInfo!!.apiKey
        }
    }

    internal inner class EmptyViewHolder(itemView: View?) : BaseViewHolder(itemView) {
        override fun clear() {}
    }

    fun setFilter(filterService: List<AllDoctorsResponse>?) {
        response = ArrayList()
        response!!.addAll(filterService!!)
        notifyDataSetChanged()
    }

    companion object {
        private const val VIEW_TYPE_NORMAL = 11
    }
}