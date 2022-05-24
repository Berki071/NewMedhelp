package com.medhelp.medhelp.ui.base;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;

import android.util.Log;
import android.view.View;

import com.medhelp.medhelp.utils.main.MainUtils;


import timber.log.Timber;

@SuppressWarnings("all")
public abstract class BaseFragment extends Fragment implements MvpView {

    private BaseActivity activity;
    private ProgressDialog dialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUp(view);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BaseActivity) {
            BaseActivity activity = (BaseActivity) context;
            this.activity = activity;
            activity.onFragmentAttached();
        }
    }

    @Override
    public void showLoading() {
        hideLoading();
        if(this.getContext()!=null)
            dialog = MainUtils.showLoadingDialog(this.getContext());
    }

    @Override
    public boolean isLoading() {
        return dialog != null && dialog.isShowing();
    }

    @Override
    public void hideLoading() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }


    @Override
    public void showError(String message) {
        if (activity != null) {
            activity.showError(message);
        }
    }

    @Override
    public void showError(@StringRes int resId) {
        if (activity != null) {
            activity.showError(resId);
        }
    }

    @Override
    public void showMessage(String message) {
        if (activity != null) {
            activity.showMessage(message);
        }
    }

    @Override
    public void showMessage(@StringRes int resId) {
        if (activity != null) {
            activity.showMessage(resId);
        }
    }

    @Override
    public void onDetach() {
        activity = null;
        super.onDetach();
    }

    @Override
    public void hideKeyboard() {
        if (activity != null) {
            activity.hideKeyboard();
        }
    }

    @Override
    public void openActivityLogin() {
        if (activity != null) {
            activity.openActivityLogin();
        }
    }


    public BaseActivity getBaseActivity() {
        return activity;
    }

    protected abstract void setUp(View view);

    @Override
    public void onDestroy() {
        hideLoading();

        super.onDestroy();
    }

    public interface Callback {
        void onFragmentAttached();

        void onFragmentDetached(String tag);
    }

    public final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE=132;
    protected boolean checkPermissions() {
        return ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;

    }


    protected void requestPermissions()
    {
        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Timber.tag("my").v("Пользователь дал разрешение на использование памяти телефона");

                    if(listener!=null)
                    {
                        listener.permissionGranted();
                    }

                } else {
                    Timber.tag("my").v("Пользователь не дал разрешение на использование памяти телефона");

                    if(listener!=null)
                    {
                        listener.permissionDenied();
                    }
                }
            }
        }
    }


    BaseFragment.ListenerBaseFragment listener;

    protected void setListener(ListenerBaseFragment listener)
    {
        this.listener=listener;
    }

    protected void clearListener()
    {
        listener=null;
    }

    protected interface ListenerBaseFragment{
        void permissionGranted();
        void permissionDenied();
    }
}
