package com.medhelp.medhelp.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.appcompat.app.AlertDialog
import br.com.sapereaude.maskedEditText.MaskedEditText
import com.medhelp.medhelp.R
import com.medhelp.medhelp.ui._main_page.MainActivity
import com.medhelp.medhelp.ui.base.BaseActivity
import com.medhelp.medhelp.ui.support_fragment.SupportDf
import com.medhelp.medhelp.utils.main.MainUtils
import com.medhelp.medhelp.utils.main.NetworkUtils
import com.medhelp.medhelp.utils.workToFile.show_file.ShowFile2
import kotlinx.coroutines.cancel

class LoginActivity : BaseActivity() {
    var presenter: LoginPresenter? = null

    lateinit var etUsername: MaskedEditText
    lateinit var etPassword: EditText
    lateinit var chbSave: CheckBox
    lateinit var forgotPas: TextView
    lateinit var howToRegister: TextView
    lateinit var writeToSupport: TextView
    lateinit var btn_login_enter : Button

    private var username: String? = null
    private var password: String? = null
    var alert: AlertDialog? = null

    public override fun onSaveInstanceState(outState: Bundle) {
        if (phone.trim() != "" || etPassword!!.text.toString().trim() != "")
            outState.putString(LOGIN_KEY, username)
        super.onSaveInstanceState(outState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initValue()

        updateUsernameHint(presenter?.prefManager?.currentLogin, presenter?.prefManager?.currentPassword)

        if (savedInstanceState != null) {
            username = savedInstanceState.getString(LOGIN_KEY)
            etUsername!!.setText(username)
            password = savedInstanceState.getString(PASSWORD_KEY)
            etPassword!!.setText(password)
        }

        val sh2 = ShowFile2()
        sh2.clearLoadList()
        setUp()
    }
    private fun initValue(){
        etUsername=findViewById(R.id.et_login_username)
        etPassword=findViewById(R.id.et_login_password)
        chbSave=findViewById(R.id.chb_login_save)
        forgotPas=findViewById(R.id.forgotPas)
        howToRegister=findViewById(R.id.howToRegister)
        writeToSupport=findViewById(R.id.writeToSupport)
        btn_login_enter=findViewById(R.id.btn_login_enter)

        presenter = LoginPresenter(this)

        btn_login_enter.setOnClickListener {
            userLogin()
        }
    }

    override fun setUp() {
        etPassword!!.setOnEditorActionListener { v: TextView?, actionId: Int, event: KeyEvent? ->
            if (event != null && event.keyCode == KeyEvent.KEYCODE_NAVIGATE_NEXT
                || actionId == EditorInfo.IME_ACTION_DONE
            ) {
                userLogin()
            }
            false
        }
        etUsername!!.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                etPassword!!.isFocusable = true
            }
            false
        }
        forgotPas!!.setOnClickListener { v: View? -> showAlert() }
        writeToSupport!!.setOnClickListener { v: View? ->
            val loginTmp = phone
            val supportDf = SupportDf()
            if (loginTmp.length == 10) supportDf.setData(loginTmp, null)
            supportDf.show(supportFragmentManager, SupportDf::class.java.canonicalName)
        }
        howToRegister!!.setOnClickListener { v: View? ->
            MainUtils.showAlertInfo(
                this,
                "Хотите зарегистрироваться?",
                "Для регистрации в мобильном приложении обратитесь в медицинский центр и Вам вышлют пароль"
            )
        }
    }

    override fun onResume() {
        super.onResume()
        if (!NetworkUtils.isNetworkConnected(this)) {
            showError(R.string.connection_error)
        }
    }

    var title: TextView? = null
    var text: TextView? = null
    private fun showAlert() {
        val inflater = this.layoutInflater
        val view = inflater.inflate(R.layout.dialog_2textview_btn, null)
        title = view.findViewById(R.id.title)
        text = view.findViewById(R.id.text)
        val btnYes = view.findViewById<Button>(R.id.btnYes)
        val btnNo = view.findViewById<Button>(R.id.btnNo)
        val img = view.findViewById<ImageView>(R.id.img)
        title!!.setText("Вы действительно хотите восстановить пароль?")
        text!!.setText("Старый пароль будет заменен на новый ")
        btnYes.setOnClickListener { v: View? ->
            if (btnNo != null && btnNo.visibility == View.VISIBLE) {
                btnNo.visibility = View.GONE
                text!!.setVisibility(View.VISIBLE)
                img.visibility = View.VISIBLE
                val strUName = phone
                if (strUName != "" && strUName.length == 10 && strUName.substring(
                        0,
                        1
                    ) == "9"
                ) presenter!!.restorePass(strUName) else responseOnPassRequest("Проверьте правильность ввода номера")
            } else {
                alert!!.dismiss()
            }
        }
        btnNo!!.setOnClickListener { v: View? -> alert!!.dismiss() }
        val builder = AlertDialog.Builder(this)
        builder.setView(view)
        alert = builder.create()
        alert!!.show()
    }

    fun responseOnPassRequest(str: String?) {
        if (title == null || text == null) return
        title!!.text = "Изменение пароля!"
        text!!.text = str
    }

    fun updateUsernameHint(username: String?, password: String?) {
        if (username != null && username.length > 0) {
            etUsername!!.setText(username)
        }
        if (password != null && password.length > 0) {
            etPassword!!.setText(password)
        }
    }

    val isNeedSave: Boolean
        get() = chbSave!!.isChecked

    fun openProfileActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    fun closeActivity() {
        finish()
    }

    private fun userLogin() {
        hideKeyboard()
        username = phone
        password = etPassword!!.text.toString()
        presenter!!.onLoginClick(username, password)
    }

    private val phone: String
        private get() {
            var ph = etUsername!!.text.toString()
            ph = ph.substring(3)
            ph = ph.replace("\\)".toRegex(), "")
            ph = ph.replace("-".toRegex(), "")
            ph = ph.replace("_".toRegex(), "")
            return ph
        }

    override fun userRefresh() {}
    override fun onBackPressed() {}

    override fun onDestroy() {
        super.onDestroy()

        presenter?.mainScope?.cancel()
    }

    companion object {
        private const val LOGIN_KEY = "LOGIN_KEY"
        private const val PASSWORD_KEY = "PASSWORD_KEY"
        @JvmStatic
        fun getStartIntent(context: Context?): Intent {
            return Intent(context, LoginActivity::class.java)
        }
    }
}