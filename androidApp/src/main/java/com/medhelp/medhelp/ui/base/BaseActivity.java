package com.medhelp.medhelp.ui.base;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.medhelp.medhelp.R;

import com.medhelp.medhelp.ui.login.LoginActivity;
import com.medhelp.medhelp.utils.main.MainUtils;


import timber.log.Timber;

@SuppressWarnings("unused")
public abstract class BaseActivity extends AppCompatActivity
        implements MvpView, BaseFragment.Callback {

    private ProgressDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        hideLoading();
        super.onResume();

    }


    @Override
    public void showLoading() {
        hideLoading();
        if(this!=null)
            dialog = MainUtils.showLoadingDialog(this);
    }

    @Override
    public boolean isLoading() {
        return dialog != null && dialog.isShowing();
    }

    @Override
    public void hideLoading() {
        if (dialog != null && dialog.isShowing()) {
            dialog.cancel();
        }
    }

    private void showSnackBar(String message) {
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT);
        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setTextColor(ContextCompat.getColor(this, R.color.white));
        textView.setMaxLines(3);
        snackbar.show();
    }

    @Override
    public void showError(String message) {
        if (message != null) {
            showSnackBar(message);
        } else {
            showSnackBar(getString(R.string.some_error));
        }
    }

    @Override
    public void showError(@StringRes int resId) {
        showError(getString(resId));
    }

    @Override
    public void showMessage(String message) {
        if (message != null) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getString(R.string.some_error), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showMessage(@StringRes int resId) {
        showMessage(getString(resId));
    }


    @Override
    public void onFragmentAttached() {
    }

    @Override
    public void onFragmentDetached(String tag) {
    }

    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    @Override
    public void openActivityLogin() {
        startActivity(LoginActivity.getStartIntent(this));
        finish();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    protected abstract void setUp();


//region permission
    public final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE=132;
    protected boolean checkPermissions() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    protected void requestPermissions()
    {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Timber.tag("my").v("Пользователь дал разрешение на использование памяти телефона");

                if (listener != null) {
                    listener.permissionGranted();
                }

            } else {
                Timber.tag("my").v("Пользователь не дал разрешение на использование памяти телефона");

                if (listener != null) {
                    listener.permissionDenied();
                }
            }
        }
    }
    //endregion

//region listener
    ListenerBaseActivity listener;

    protected interface ListenerBaseActivity{
        void permissionGranted();
        void permissionDenied();
    }

    protected void setListener(ListenerBaseActivity listener)
    {
        this.listener=listener;
    }

    protected void clearListener()
    {
        listener=null;
    }
    //endregion

}
