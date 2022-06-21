package com.medhelp.medhelp.ui.tax_certifacate.to_orter_certificate_df

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.DialogFragment
import com.medhelp.medhelp.R
import com.medhelp.medhelp.data.pref.PreferencesManager
import com.medhelp.medhelp.ui.tax_certifacate.DataForTaxCertificate
import com.medhelp.medhelp.ui.view.ETextFieldWithTitleForNumber
import com.medhelp.medhelp.ui.view.EtFieldWithTitle
import com.medhelp.medhelp.ui.view.TextFieldWithTitle
import com.medhelp.medhelp.utils.Different
import com.medhelp.medhelp.utils.TimesUtils
import com.medhelp.shared.model.UserResponse
import java.util.*

class ToOrderCertificateDf : DialogFragment() {
    var listener : ToOrderCertificateListener? = null
    fun setData(listener : ToOrderCertificateListener){
       this.listener=listener
    }

    lateinit var dateStart : TextFieldWithTitle
    lateinit var dateStop : TextFieldWithTitle
    lateinit var et_inn : ETextFieldWithTitleForNumber
    lateinit var checkBox : CheckBox
    lateinit var et_fio : EtFieldWithTitle
    lateinit var btnClose : Button
    lateinit var btnToOrder : Button
  // lateinit var fioTil : TextInputLayout

    var preferencesManager : PreferencesManager? = null


    override fun onStart() {
        super.onStart()
        setHasOptionsMenu(true)

        //костыль, по умолчанию окно показывается не во весь размер
        val dialog = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            Objects.requireNonNull(dialog.window)?.setLayout(width, height)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.df_to_order_certificate, null)
        initValue(view)
        return view
    }

    fun initValue(v : View){
        dateStart=v.findViewById(R.id.dateStart)
        dateStop=v.findViewById(R.id.dateStop)
        et_inn=v.findViewById(R.id.et_inn)
        checkBox=v.findViewById(R.id.checkBox)
        et_fio=v.findViewById(R.id.et_fio)
        btnClose=v.findViewById(R.id.btnClose)
        btnToOrder=v.findViewById(R.id.btnToOrder)

        preferencesManager=PreferencesManager(requireContext())

        dateStart.text.text= TimesUtils.getCurrentDate(TimesUtils.DATE_FORMAT_ddMMyyyy)
        dateStop.text.text= TimesUtils.getCurrentDate(TimesUtils.DATE_FORMAT_ddMMyyyy)
        et_fio.visibility=View.GONE

        dateStart.setOnClickListener{
            selectDateFineTuning(1)
        }
        dateStop.setOnClickListener{
            selectDateFineTuning(2)
        }
        btnClose.setOnClickListener{
            dismiss()
        }
        btnToOrder.setOnClickListener{
            toOrder()
        }

        checkBox.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                if(isChecked){
                    et_fio.visibility=View.VISIBLE
                }else{
                    et_fio.visibility=View.GONE
                }
            }
        })
    }


    fun toOrder(){
        val dateS=dateStart.text.text.toString()
        val dateE=dateStop.text.text.toString()
        val inn=et_inn.text.text.toString()
        val user= if(checkBox.isChecked) et_fio.text.text.toString() else getCurrentUserName()

        if(inn.isEmpty()){
            Different.showAlertInfo(requireActivity(),"Ошибка","Поле ИНН не заполнено")
            return
        }

        if(inn.length!=12){
            Different.showAlertInfo(requireActivity(),"Ошибка","Проверьте ИНН")
            return
        }


        if(user.isEmpty()){
            Different.showAlertInfo(requireActivity(),"Ошибка","Поле ФИО не заполнено")
            return
        }



        val currentUser=getCurrentUser()
        if(currentUser==null){
            Different.showAlertInfo(requireActivity(),"Ошибка","Проблема с текущим пользователем")
            return
        }

       val dataFor=DataForTaxCertificate(currentUser.idUser.toString(), currentUser.idBranch.toString(), dateS, dateE,inn, getCurrentUserName(), user)

        listener?.sendOrderCertificate(dataFor)
        dismiss()
    }

    fun getCurrentUser() : UserResponse?{
        val currentLog=preferencesManager?.currentUserInfo
        //val list= preferencesManager?.usersLogin

        return currentLog
//        for(itm in  list!!){
//            if(itm.login==currentLog)
//                return itm
//        }

    }

    fun getCurrentUserName() : String {
        val currentLog=preferencesManager?.currentUserInfo

       return currentLog?.surname+" "+currentLog?.name+" "+currentLog?.patronymic

//        val list= preferencesManager?.usersLogin
//
//        for(itm in  list!!){
//            if(itm.login==currentLog)
//                return itm.surname+" "+itm.name+" "+itm.patronymic
//        }
        return  ""
    }

    private fun selectDateFineTuning(type: Int) {
        val tmp: OnDateSetListener
        val dateAndTime = Calendar.getInstance()
        if (type == 1) {
            tmp = r2d1

            dateAndTime.clear()
            dateAndTime.timeInMillis = TimesUtils.stringToLong(dateStart.text.getText().toString(), TimesUtils.DATE_FORMAT_ddMMyyyy) ?: 0
            object : DatePickerDialog(requireContext(), tmp, dateAndTime[Calendar.YEAR], dateAndTime[Calendar.MONTH], dateAndTime[Calendar.DAY_OF_MONTH]) {
                override fun onDateChanged(view: DatePicker, year: Int, month: Int, dayOfMonth: Int) {
                    var month = month
                    month++
                    var str = if (dayOfMonth > 9) "$dayOfMonth." else "0$dayOfMonth."
                    str += if (month > 9) "$month." else "0$month."
                    str += year.toString()

                    dateStart.text.setText(str)
                    dismiss()
                }
            }.show()
        } else if(type == 2) {
            tmp = r2d2

            dateAndTime.clear()
            dateAndTime.timeInMillis = TimesUtils.stringToLong(dateStop.text.getText().toString(), TimesUtils.DATE_FORMAT_ddMMyyyy) ?: 0
            object : DatePickerDialog(requireContext(), tmp, dateAndTime[Calendar.YEAR], dateAndTime[Calendar.MONTH], dateAndTime[Calendar.DAY_OF_MONTH]) {
                override fun onDateChanged(view: DatePicker, year: Int, month: Int, dayOfMonth: Int) {
                    var month = month
                    month++
                    var str = if (dayOfMonth > 9) "$dayOfMonth." else "0$dayOfMonth."
                    str += if (month > 9) "$month." else "0$month."
                    str += year.toString()

                    dateStop.text.setText(str)
                    dismiss()
                }
            }.show()
        }
    }

    // установка обработчика для выпадающего списка "конкретная дата"
    val r2d1 = OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            var monthOfYear = monthOfYear
            monthOfYear++
            var str = if (dayOfMonth > 9) "$dayOfMonth." else "0$dayOfMonth."
            str += if (monthOfYear > 9) "$monthOfYear." else "0$monthOfYear."
            str += year.toString() + ""
            dateStart.text.setText(Html.fromHtml( /*"<u>"+*/str /*+"</u>"*/))
        }
    val r2d2 = OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
        var monthOfYear = monthOfYear
        monthOfYear++
        var str = if (dayOfMonth > 9) "$dayOfMonth." else "0$dayOfMonth."
        str += if (monthOfYear > 9) "$monthOfYear." else "0$monthOfYear."
        str += year.toString() + ""
        dateStop.text.setText(Html.fromHtml( /*"<u>"+*/str /*+"</u>"*/))
    }

    interface ToOrderCertificateListener{
        fun sendOrderCertificate(data : DataForTaxCertificate)
    }

}