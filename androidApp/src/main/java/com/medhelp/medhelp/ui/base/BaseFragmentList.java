package com.medhelp.medhelp.ui.base;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.ListFragment;
import android.view.View;

import com.medhelp.medhelp.utils.main.MainUtils;



@SuppressWarnings("unused")
public abstract class BaseFragmentList extends ListFragment implements MvpView {

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
            dialog.cancel();
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
        super.onDestroy();
    }

    public interface Callback {
        void onFragmentAttached();

        void onFragmentDetached(String tag);
    }
}
