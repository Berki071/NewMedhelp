package com.medhelp.medhelp.ui.splash

import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import com.medhelp.medhelp.R
import com.medhelp.medhelp.fingerprint.FingerprintApi
import com.medhelp.medhelp.ui._main_page.MainActivity
import com.medhelp.medhelp.ui.base.BaseActivity
import com.medhelp.medhelp.ui.login.LoginActivity
import com.medhelp.medhelp.ui.splash.dialog.FingerprintDialog
import kotlinx.coroutines.cancel
import timber.log.Timber

const val FINGERPRINT_DIALOG = "fingerprint_dialog"

class SplashActivity : BaseActivity(), FingerprintDialog.Callback {
    var presenter: SplashPresenter? = null

    private var apiFingerprint: FingerprintApi? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        //Log.i("Loginctivityaaass", "Hello from shared module: " + (Greeting().greeting()))


        presenter = SplashPresenter(this)

        val curIntent = intent
        if (curIntent != null) {
            val id_kl = curIntent.getStringExtra("id_kl")
            val id_filial = curIntent.getStringExtra("id_filial")
            if (id_kl != null && id_filial != null) {
                presenter!!.testOpenNextActivity(id_kl, id_filial)
                return
            }
        }
        presenter!!.testOpenNextActivity(null, null)
    }
    override fun setUp() {}


    fun openLoginActivity() {
        val intent = LoginActivity.getStartIntent(this)
        startActivity(intent)
        finish()
    }

    fun openProfileActivity(showLock: Boolean) {
        if (!showLock) {
            val intent = Intent(this, MainActivity::class.java)
            checkIntentOnData(intent)
            startActivity(intent)
            finish()
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (isFingerprintSupported) {
                    checkFingerprintAvailability()
                } else {
                    openProfileActivity(false)
                }
            } else {
                openProfileActivity(false)
            }
        }
    }

    private fun checkIntentOnData(intent: Intent) {
        //обработка нотификации Fcm
        val curIntent = getIntent()
        if (curIntent != null) {
            val type_message = curIntent.getStringExtra("type_message")
            val id_kl = curIntent.getStringExtra("id_kl")
            val id_filial = curIntent.getStringExtra("id_filial")
            Log.wtf("logFCM", "splash type_message= $type_message, id_kl= $id_kl, id_filial= $id_filial")
            if (type_message != null && id_kl != null && id_filial != null) {
                intent.putExtra("type_message", type_message)
                intent.putExtra("id_kl", id_kl)
                intent.putExtra("id_filial", id_filial)
            }
        }
    }


    //region fingerprint
    @RequiresApi(api = Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == FingerprintApi.PERMISSION_FINGERPRINT) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Timber.tag("my").v("Пользователь дал разрешение использования отпечатков пальцев")
                checkFingerprintAvailability()
            } else {
                Timber.tag("my").v("Пользователь не дал разрешение использования отпечатков пальцев")
            }
        }
    }

    override fun onSuccess() {
        openProfileActivity(false)
    }

    override fun onClick(dialog: DialogInterface, which: Int) {
        apiFingerprint!!.cancel()
        dialog.dismiss()
        openLoginActivity()
    }

    // Check for availability and any additional requirements for the api.
    @get:RequiresApi(api = Build.VERSION_CODES.M)
    private val isFingerprintSupported: Boolean
        private get() =// Check for availability and any additional requirements for the api.
            FingerprintApi.create(this).also { apiFingerprint = it } != null && apiFingerprint!!.isFingerprintSupported

    @RequiresApi(api = Build.VERSION_CODES.M)
    private fun checkFingerprintAvailability() {
        try {
            val dialog = FingerprintDialog.getInstance(this@SplashActivity)
            dialog.show(supportFragmentManager, FINGERPRINT_DIALOG)
            apiFingerprint!!.start(dialog)
        } catch (e: Exception) {
            Timber.tag("my").e(e)
        }
    }

    //endregion
    override fun onDestroy() {
        if (apiFingerprint != null) apiFingerprint!!.cancel()
        super.onDestroy()

        presenter?.mainScope?.cancel()
    }

    //от BaseActivity
    override fun userRefresh() {}
}