package com.medhelp.medhelp.ui.doctor.alertDoc

import android.app.AlertDialog
import android.content.Context
import com.medhelp.medhelp.ui.doctor.service_activity.ServiceActivity.Companion.getStartIntent
import com.medhelp.newmedhelp.model.AllDoctorsResponse
import android.widget.TextView
import com.medhelp.medhelp.data.pref.PreferencesManager
import android.view.LayoutInflater
import com.medhelp.medhelp.R
import android.text.method.ScrollingMovementMethod
import com.medhelp.medhelp.utils.workToFile.show_file.ShowFile2.BuilderImage
import com.medhelp.medhelp.utils.workToFile.show_file.ShowFile2
import com.medhelp.medhelp.utils.workToFile.show_file.ShowFile2.ShowListener
import android.content.Intent
import android.view.View
import android.widget.Button
import android.widget.ImageView
import com.medhelp.medhelp.Constants
import com.medhelp.medhelp.ui.doctor.service_activity.ServiceActivity
import com.medhelp.medhelp.ui.schedule.ScheduleFragment
import java.io.File

class AlertCardDoctor(val context: Context, val doc: AllDoctorsResponse, val idService: Int, val token: String) {

    var doc_info_image: ImageView? = null
    var doc_info_name: TextView? = null
    var doc_info_exp: TextView? = null
    var doc_info_spec: TextView? = null
    var doc_info_info: TextView? = null
    var doc_info_btn_close: Button? = null
    var doc_info_btn_record: Button? = null
    private var alertDialog: AlertDialog? = null
    private val preferences: PreferencesManager

    init {
        preferences = PreferencesManager(context)
        creteDialog()
    }

    private fun creteDialog() {
        val layoutInflater = LayoutInflater.from(context)
        val view = layoutInflater.inflate(R.layout.fragment_doctor_details, null)
        val builder = AlertDialog.Builder(context)
        builder.setView(view)
        alertDialog = builder.create()
        alertDialog!!.show()
        initValue(view)
        doc_info_info!!.movementMethod = ScrollingMovementMethod()
        doc_info_name!!.text = doc.fio_doctor
        doc_info_exp!!.text = doc.experience
        doc_info_spec!!.text = doc.name_specialties
        doc_info_info!!.text = doc.dop_info
        if (doc_info_image != null) {
            BuilderImage(context)
                .setType(ShowFile2.TYPE_ICO)
                .load(doc.image_url)
                .token(token)
                .imgError(R.drawable.sh_doc)
                .into(doc_info_image)
                .setListener(object : ShowListener {
                    override fun complete(file: File?) {}
                    override fun error(error: String?) {}
                })
                .build()
        }
    }

    private fun initValue(view: View) {
        doc_info_image = view.findViewById(R.id.doc_info_image)
        doc_info_name = view.findViewById(R.id.doc_info_name)
        doc_info_exp = view.findViewById(R.id.doc_info_exp)
        doc_info_spec = view.findViewById(R.id.doc_info_spec)
        doc_info_info = view.findViewById(R.id.doc_info_info)
        doc_info_btn_close = view.findViewById(R.id.doc_info_btn_close)
        doc_info_btn_record = view.findViewById(R.id.doc_info_btn_record)
        if (preferences.centerInfo!!.button_zapis == 0) doc_info_btn_record?.setVisibility(View.GONE)
        doc_info_btn_close?.setOnClickListener(View.OnClickListener { c: View? -> alertDialog!!.cancel() })
        doc_info_btn_record?.setOnClickListener(View.OnClickListener { c: View? ->
            showServiceActivity(doc.id!!)
            alertDialog!!.cancel()
        })
    }

    fun showServiceActivity(idDoctor: Int) {
        if (idDoctor != 0) {
            val intent = getStartIntent(context)
            intent.putExtra(ServiceActivity.EXTRA_DATA_ID_DOCTOR, idDoctor)
            intent.putExtra(ServiceActivity.EXTRA_DATA_SERVICE, idService)
            intent.putExtra(ScheduleFragment.EXTRA_BACK_PAGE, Constants.MENU_STAFF)
            context.startActivity(intent)
        }
    }
}