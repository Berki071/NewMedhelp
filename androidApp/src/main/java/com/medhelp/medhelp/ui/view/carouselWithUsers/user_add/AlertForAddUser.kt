package com.medhelp.medhelp.ui.view.carouselWithUsers.user_add

import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import br.com.sapereaude.maskedEditText.MaskedEditText
import com.medhelp.medhelp.R
import com.medhelp.shared.model.UserResponse
import kotlinx.coroutines.cancel

class AlertForAddUser {
    private var alertDialog: AlertDialog? = null
    var alertDialogIncorrectData: AlertDialog? = null
    private var phone: MaskedEditText? = null
    private var password: MaskedEditText? = null
    private var ok: Button? = null
    private var cancel: Button? = null
    private var pasTitle: TextView? = null
    private var loadMain: ConstraintLayout? = null
    var presenter: AlertForAddUserPresenter? = null
    var phoneRoot: String? = null
    var user: UserResponse? = null
    var listener: AlertForInfoUserListener
    var context: Context

    constructor(context: Context, phoneRoot: String?, listener: AlertForInfoUserListener) {
        this.context = context
        this.phoneRoot = phoneRoot
        this.listener = listener
        initVariable(context)
    }

    constructor(context: Context, user: UserResponse?, listener: AlertForInfoUserListener) {
        this.context = context
        this.user = user
        this.listener = listener
        initVariable(context)
    }

    private fun initVariable(context: Context) {
        val layoutInflater = LayoutInflater.from(context)
        val view = layoutInflater.inflate(R.layout.dialog_for_info_user, null)
        val builder = AlertDialog.Builder(context)
        builder.setView(view)
        alertDialog = builder.create()
        alertDialog?.setCanceledOnTouchOutside(false)
        alertDialog?.setOnCancelListener(object : DialogInterface.OnCancelListener {
            override fun onCancel(p0: DialogInterface?) {
                presenter?.mainScope?.cancel()
            }
        })
        alertDialog!!.show()
        phone = view.findViewById(R.id.phone)
        password = view.findViewById(R.id.password)
        ok = view.findViewById(R.id.ok)
        cancel = view.findViewById(R.id.cancel)
        pasTitle = view.findViewById(R.id.pasTitle)
        loadMain = view.findViewById(R.id.loadMain)
        showPhoneInput()
        presenter = AlertForAddUserPresenter(context, this)
        ok?.setOnClickListener(View.OnClickListener {
            val login = getPhone()
            if (ok!!.getText() == "Запросить код") {
                if (login != "" && login.length == 10 && login.substring(
                        0,
                        1
                    ) == "9"
                ) presenter!!.sendPhoneForSMS(login) else {
                    showAlertPrompt("Проверьте правильность ввода данных")
                }
            } else {
                val pas = getPassword()
                if (pas != "" && pas.length == 6) {
                    presenter!!.sendLoginAndPassword(login, pas)
                } else {
                    showAlertPrompt("Проверьте правильность ввода данных")
                }
            }
        })
        cancel?.setOnClickListener(View.OnClickListener { alertDialog!!.cancel() })

        alertDialog
    }

    private fun getPhone(): String {
        var ph = phone!!.text.toString()
        ph = ph.substring(3)
        ph = ph.replace("\\)".toRegex(), "")
        ph = ph.replace("-".toRegex(), "")
        ph = ph.replace("_".toRegex(), "")
        return ph
    }

    private fun getPassword(): String {
        return password!!.text.toString().replace(" ", "").replace("_", "")
    }

    private fun showPhoneInput() {
        hideLoading()
        pasTitle!!.visibility = View.GONE
        password!!.visibility = View.GONE
        ok!!.text = "Запросить код"
    }

    private fun showPasswordInput() {
        //showAlertPrompt("Введите код из СМС, сообщение направлено на указанный номер");
        phone!!.isEnabled = false
        pasTitle!!.visibility = View.VISIBLE
        password!!.visibility = View.VISIBLE
        password!!.requestFocus()
        ok!!.text = "Добавить"
    }

    private fun showAlertPrompt(msg: String) {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.dialog_2textview_btn, null)
        val title = view.findViewById<TextView>(R.id.title)
        val text = view.findViewById<TextView>(R.id.text)
        val btnYes = view.findViewById<Button>(R.id.btnYes)
        val btnNo = view.findViewById<Button>(R.id.btnNo)
        title.visibility = View.GONE
        btnNo.visibility = View.GONE
        text.text = msg
        btnYes.setOnClickListener { v: View? -> alertDialogIncorrectData!!.cancel() }
        val builder = AlertDialog.Builder(context)
        builder.setView(view)
        alertDialogIncorrectData = builder.create()
        alertDialogIncorrectData!!.setCanceledOnTouchOutside(false)
        alertDialogIncorrectData!!.show()
    }

    fun responsePhoneSent(isCorrect: Boolean) {
        if (isCorrect) {
            showPasswordInput()
        } else {
            showAlertPrompt("В настоящий момент пациента с таким номером телефона не зарегистрировано в медицинском центре")
        }
    }

    fun responsePasswordSent(isCorrect: Boolean) {
        if (isCorrect) {
            listener.cratedNewUser(
                presenter!!.prefManager.usersLogin,
                presenter!!.prefManager.currentUserInfo
            )
            alertDialog!!.cancel()
        } else {
            showAlertPrompt("Неверный код")
        }
    }

    fun showLoading() {
        loadMain!!.visibility = View.VISIBLE
        ok!!.isEnabled = false
        cancel!!.isEnabled = false
    }

    fun hideLoading() {
        loadMain!!.visibility = View.GONE
        ok!!.isEnabled = true
        cancel!!.isEnabled = true
    }

    fun errorRefreshUsers() {
        showAlertPrompt("Не удалось обновить список пользователей")
    }

    interface AlertForInfoUserListener {
        fun cratedNewUser(
            dataList: List<UserResponse?>?,
            currentUser: UserResponse?
        ) //void renewedUser(UserResponse user);  //отличается тем что уже есть рут и собственный айди
    }
}