package com.medhelp.medhelp.ui.base;

import androidx.annotation.StringRes;

public interface MvpView {

    void showLoading();

    void hideLoading();

    boolean isLoading();

    void openActivityLogin();

    void showError(@StringRes int resId);

    void showError(String message);

    void showMessage(String message);

    void showMessage(@StringRes int resId);

    void hideKeyboard();

    void userRefresh();
}
