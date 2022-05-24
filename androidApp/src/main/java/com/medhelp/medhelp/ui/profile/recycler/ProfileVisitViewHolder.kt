package com.medhelp.medhelp.ui.profile.recycler

import android.os.Build
import android.text.Html
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.medhelp.medhelp.Constants
import com.medhelp.medhelp.R
import com.medhelp.medhelp.data.model.VisitResponse
import com.medhelp.medhelp.data.pref.PreferencesManager
import com.medhelp.medhelp.ui._main_page.MainActivity
import com.medhelp.medhelp.utils.main.TimesUtils
import com.medhelp.medhelp.utils.workToFile.show_file.ShowFile2
import com.medhelp.medhelp.utils.workToFile.show_file.ShowFile2.BuilderImage
import com.medhelp.medhelp.utils.workToFile.show_file.ShowFile2.ShowListener
import com.medhelp.shared.model.CenterResponse
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder
import java.io.File



const val toPay = "Оплатить"
const val paid = "Оплачено"
const val inBasket = "В корзине"

class ProfileVisitViewHolder(val itemView: View, val today: String, val time: String, val itemClickListener: ItemClickListener, val token: String)  : ChildViewHolder(itemView) {
    //в обоих вью
    var image_video: ImageView? = null
    var receptionLogo: ImageView? = null
    var receptionTitle: TextView? = null
    var receptionDoctor: TextView? = null
    var receptionDate: TextView? = null
    var receptionTime: TextView? = null
    var receptionPrice: TextView? = null
    var receptionCancel: Button? = null
    var receptionConfirm: Button? = null
    var recommendation: TextView? = null
    var btn_postpone: Button? = null
    var btn_payment: Button? = null
    var receptionLogoNo: ImageView? = null
    var receptionTitleNo: TextView? = null
    var receptionDoctorNo: TextView? = null
    var receptionDateNo: TextView? = null
    var receptionTimeNo: TextView? = null
    var receptionEnrollAgain: Button? = null
    var txtBranch: TextView? = null

    private val YES = "да"
    private val NO = "нет"
    private var visitRes: VisitResponse? = null
    private var centerResponse: CenterResponse
    private var context = itemView.context
    private var prefManager= PreferencesManager(itemView.context)

    init {
        centerResponse = prefManager.centerInfo!!
        initValue(itemView)
    }

    private fun initValue(v: View) {
        image_video = v.findViewById(R.id.image_video)
        receptionLogo = v.findViewById(R.id.image_doc_item)
        receptionTitle = v.findViewById(R.id.tv_price_name_item)
        receptionDoctor = v.findViewById(R.id.tv_doc_name_item)
        receptionDate = v.findViewById(R.id.tv_date_item)
        receptionTime = v.findViewById(R.id.tv_time_item)
        receptionPrice = v.findViewById(R.id.tv_price_item)
        receptionCancel = v.findViewById(R.id.btn_cancel_profile_item)
        receptionConfirm = v.findViewById(R.id.btn_confirm_profile_item)
        recommendation = v.findViewById(R.id.recommendations)
        btn_postpone = v.findViewById(R.id.btn_postpone)
        btn_payment = v.findViewById(R.id.btn_payment)
        receptionLogoNo = v.findViewById(R.id.image_doc_item_no)
        receptionTitleNo = v.findViewById(R.id.tv_price_name_item_no)
        receptionDoctorNo = v.findViewById(R.id.tv_doc_name_item_no)
        receptionDateNo = v.findViewById(R.id.tv_date_item_no)
        receptionTimeNo = v.findViewById(R.id.tv_time_item_no)
        receptionEnrollAgain = v.findViewById(R.id.btn_enroll_again)
        txtBranch = v.findViewById(R.id.tv_branch_item)

        receptionCancel?.setOnClickListener(View.OnClickListener { c: View? ->
            if (receptionCancel!!.getText().toString() == "Отмена") {
                if (!statusIsPaid(visitRes!!.status)) {
                    itemClickListener.cancelBtnClick(
                        visitRes!!.idUser,
                        visitRes!!.idRecord,
                        visitRes!!.idBranch
                    )
                } else {
                    showAlertActionProhibited()
                }
            } else {
                itemClickListener.confirmComing(visitRes)
            }
        })
        receptionConfirm?.setOnClickListener(View.OnClickListener { c: View? ->
            itemClickListener.confirmBtnClick(
                visitRes!!.idUser, visitRes!!.idRecord, visitRes!!.idBranch, visitRes!!.comment
            )
        })
        receptionEnrollAgain?.setOnClickListener(View.OnClickListener { c: View? ->
            itemClickListener.enrollAgainBtnClick(visitRes)
        })
        btn_postpone?.setOnClickListener(View.OnClickListener { c: View? ->
            if (!statusIsPaid(
                    visitRes!!.status
                )
            ) {
                itemClickListener.postponeBtnClick(visitRes)
            } else {
                showAlertActionProhibited()
            }
        })
        btn_payment?.setOnClickListener(View.OnClickListener { c: View? ->
            val tmp = btn_payment!!.getText().toString()
            if (tmp == toPay) {
                visitRes!!.isAddInBasket = true
                btn_payment!!.setText(inBasket)
                itemClickListener.payBtnClick(visitRes, true)
            } else if (tmp == inBasket) {
                visitRes!!.isAddInBasket = false
                btn_payment!!.setText(toPay)
                itemClickListener.payBtnClick(visitRes, false)
            }
        })
    }

    fun onBindButton(response: VisitResponse?, blockBasket: Boolean, yandexStoreIsWork: Boolean) {
        visitRes = response
        if (response != null) {
            if (response.nameServices != null && receptionTitle != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) receptionTitle!!.text =
                    Html.fromHtml(
                        "<u>" + response.nameServices + "</u>",
                        Html.FROM_HTML_MODE_LEGACY
                    ) else receptionTitle!!.text =
                    Html.fromHtml("<u>" + response.nameServices + "</u>")
            } else {
                receptionTitle!!.text = Html.fromHtml("<u>" + response.nameServices + "</u>")
            }
        }
        if (response!!.dop != null) {
            if (response.dop == Constants.TYPE_DOP_VIDEO_CALL) image_video!!.visibility =
                View.VISIBLE else image_video!!.visibility = View.GONE
        } else image_video!!.visibility = View.GONE
        if (response.nameSotr != null && receptionDoctor != null) {
            receptionDoctor!!.text = response.nameSotr
        }
        if (response.dateOfReceipt != null && receptionDate != null) {
            receptionDate!!.text = response.dateOfReceipt
        }
        if (response.timeOfReceipt != null && receptionTime != null) {
            receptionTime!!.text = response.timeOfReceipt
        }
        if (response.call == NO && isTheTimeConfirm(
                response.dateOfReceipt,
                response.timeOfReceipt
            )
        ) {
            receptionConfirm!!.visibility = View.VISIBLE
        } else {
            receptionConfirm!!.visibility = View.GONE
        }
        if (isTheTimeCancel(response.dateOfReceipt, response.timeOfReceipt)) {
            //receptionCancel.setVisibility(View.VISIBLE);
            btn_postpone!!.visibility = View.VISIBLE
            receptionCancel!!.text = "Отмена"
        } else {
            //receptionCancel.setVisibility(View.GONE);
            btn_postpone!!.visibility = View.GONE
            if (response.status == "wk" || response.status == "wkp") {
                receptionCancel!!.text = "Я пришел"
            } else {
                receptionCancel!!.visibility = View.GONE
            }
        }
        if (!(response.status == "p" || response.status == "wkp" || response.status == "kpp") && yandexStoreIsWork) {
            btn_payment!!.visibility = View.VISIBLE
        } else {
            btn_payment!!.visibility = View.GONE
        }
        if (response.isAddInBasket) {
            btn_payment!!.text = inBasket
        } else {
            btn_payment!!.text = toPay
        }
        if (blockBasket && !response.isAddInBasket) {
            btn_payment!!.isEnabled = false
            btn_payment!!.setBackgroundResource(R.drawable.rounded_bg_inactive_24dp)
        } else {
            btn_payment!!.isEnabled = true
            btn_payment!!.setBackgroundResource(R.drawable.rounded_bg_primary_24dp)
        }
        txtBranch!!.text = response.nameBranch
        receptionPrice!!.text = response.price.toString() + "р"
        BuilderImage(receptionLogo!!.context)
            .setType(ShowFile2.TYPE_ICO)
            .load(response.photoSotr)
            .token(token)
            .imgError(R.drawable.sh_doc)
            .into(receptionLogo)
            .setListener(object : ShowListener {
                override fun complete(file: File) {}
                override fun error(error: String?) {}
            })
            .build()
        if (response.comment != "") initClickRecommendation(response.comment) else recommendation!!.visibility =
            View.GONE
    }

    private var alert: AlertDialog? = null
    private fun initClickRecommendation(msg: String) {
        recommendation!!.setOnClickListener { click: View? ->
            val inflater = (context as MainActivity).layoutInflater
            val view = inflater.inflate(R.layout.dialog_2textview_btn, null)
            val title = view.findViewById<TextView>(R.id.title)
            val text = view.findViewById<TextView>(R.id.text)
            val btnYes = view.findViewById<Button>(R.id.btnYes)
            val btnNo = view.findViewById<Button>(R.id.btnNo)
            title.text = "Рекомендации перед приемом"
            text.text = msg
            btnYes.setOnClickListener { v: View? -> alert!!.dismiss() }
            btnNo.visibility = View.GONE
            val builder = AlertDialog.Builder(context)
            builder.setView(view)
            alert = builder.create()
            alert!!.show()
        }
    }

    var dialog: AlertDialog? = null
    private fun showAlertActionProhibited() {
        val str =
            "Для отмены или переноса оплаченного приема необходимо обратиться к администраторам медицинского центра"
        val inflater = (context as MainActivity).layoutInflater
        val view = inflater.inflate(R.layout.dialog_2textview_btn, null)
        val title = view.findViewById<TextView>(R.id.title)
        val text = view.findViewById<TextView>(R.id.text)
        val btnYes = view.findViewById<Button>(R.id.btnYes)
        val btnNo = view.findViewById<Button>(R.id.btnNo)
        title.visibility = View.GONE
        btnNo.visibility = View.GONE
        text.text = str
        btnYes.setOnClickListener { v: View? ->
            dialog!!.cancel()
            dialog!!.cancel()
        }
        val builder = AlertDialog.Builder(context)
        builder.setView(view)
        dialog = builder.create()
        dialog!!.setCanceledOnTouchOutside(false)
        dialog!!.show()
    }

    fun onBindNoButton(response: VisitResponse?, blockBasket: Boolean, yandexStoreIsWork: Boolean) {
        if (response != null) {
            if (response.dop != null) {
                if (response.dop == Constants.TYPE_DOP_VIDEO_CALL) image_video!!.visibility =
                    View.VISIBLE else image_video!!.visibility = View.GONE
            } else image_video!!.visibility = View.GONE
            if (response.nameServices != null && receptionTitleNo != null) {
                receptionTitleNo!!.text = Html.fromHtml("<u>" + response.nameServices + "</u>")
            }
            if (response.nameSotr != null && receptionDoctorNo != null) {
                receptionDoctorNo!!.text = response.nameSotr
            }
            if (response.dateOfReceipt != null && receptionDateNo != null) {
                receptionDateNo!!.text = response.dateOfReceipt
            }
            if (response.timeOfReceipt != null && receptionTimeNo != null) {
                receptionTimeNo!!.text = response.timeOfReceipt
            }
            visitRes = response
            txtBranch!!.text = response.nameBranch
            if (response.works == "уволен" || prefManager.centerInfo?.button_zapis==0) {
                receptionEnrollAgain!!.visibility = View.GONE
            }
            if (!(response.status == "p" || response.status == "wkp" || response.status == "kpp") && yandexStoreIsWork && testDate()) {
                btn_payment!!.visibility = View.VISIBLE
            } else {
                btn_payment!!.visibility = View.GONE
            }
            if (response.isAddInBasket) {
                btn_payment!!.text = inBasket
            } else {
                btn_payment!!.text = toPay
            }
            if (blockBasket && !response.isAddInBasket) {
                btn_payment!!.isEnabled = false
                btn_payment!!.setBackgroundResource(R.drawable.rounded_bg_inactive_24dp)
            } else {
                btn_payment!!.isEnabled = true
                btn_payment!!.setBackgroundResource(R.drawable.rounded_bg_primary_24dp)
            }
            BuilderImage(receptionLogoNo!!.context)
                .setType(ShowFile2.TYPE_ICO)
                .load(response.photoSotr)
                .token(token)
                .imgError(R.drawable.sh_doc)
                .into(receptionLogoNo)
                .setListener(object : ShowListener {
                    override fun complete(file: File) {}
                    override fun error(error: String?) {}
                })
                .build()
        }
    }

    private fun testDate(): Boolean {
        val analysis = TimesUtils.stringToLong( /*item.getTimeOfReceipt()+" "+*/
            visitRes!!.dateOfReceipt, TimesUtils.DATE_FORMAT_ddMMyyyy
        )
        val tDay = TimesUtils.stringToLong( /*time+" "+*/today, TimesUtils.DATE_FORMAT_ddMMyyyy)
        //int timeTo=1000*60*centerResponse.getTimeForDenial();
        return analysis >= tDay
    }

    private fun isTheTimeConfirm(date: String, time2: String): Boolean {
        val analysis = TimesUtils.stringToLong("$time2 $date", TimesUtils.DATE_FORMAT_HHmm_ddMMyyyy)
        // analysis=TimesUtils.localLongToUtcLong(analysis);
        val tDay = TimesUtils.stringToLong("$time $today", TimesUtils.DATE_FORMAT_HHmm_ddMMyyyy)
        val timeTo = 1000 * 60 * centerResponse.timeForConfirm
        val d1 = TimesUtils.longToString(analysis, TimesUtils.DATE_FORMAT_HHmmss_ddMMyyyy)
        val d11 = TimesUtils.longToString(analysis, TimesUtils.DATE_FORMAT_HHmmss_ddMMyyyy)
        val d2 = TimesUtils.longToString(tDay, TimesUtils.DATE_FORMAT_HHmmss_ddMMyyyy)
        val d3 = TimesUtils.longToString(tDay + timeTo, TimesUtils.DATE_FORMAT_HHmmss_ddMMyyyy)
        val boo = analysis >= tDay && analysis <= tDay + timeTo
        return analysis >= tDay && analysis <= tDay + timeTo
    }

    private fun isTheTimeCancel(date: String, time2: String): Boolean {
        val analysis = TimesUtils.stringToLong("$time2 $date", TimesUtils.DATE_FORMAT_HHmm_ddMMyyyy)
        val tDay = TimesUtils.stringToLong("$time $today", TimesUtils.DATE_FORMAT_HHmm_ddMMyyyy)
        val timeTo = 1000 * 60 * centerResponse.timeForDenial
        return analysis >= tDay && analysis >= tDay + timeTo
    }

    companion object {
        @JvmStatic
        fun statusIsPaid(status: String): Boolean {
            return status == "p" || status == "wkp" || status == "kpp"
        }
    }


}