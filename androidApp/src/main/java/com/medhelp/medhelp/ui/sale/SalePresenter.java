package com.medhelp.medhelp.ui.sale;

import android.content.Context;
import com.medhelp.medhelp.data.model.SaleList;
import com.medhelp.medhelp.data.network.NetworkManager;
import com.medhelp.medhelp.data.pref.PreferencesManager;
import com.medhelp.medhelp.utils.timber_log.LoggingTree;
import com.medhelp.shared.model.CenterResponse;
import com.medhelp.shared.model.UserResponse;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class SalePresenter implements SaleHelper.Presenter {
    Context context;
    SaleHelper.View viewHelper;

    PreferencesManager prefManager;
    NetworkManager networkManager;

    public SalePresenter (Context context, SaleHelper.View viewHelper)
    {
        this.context=context;

        this.viewHelper=viewHelper;

        prefManager=new PreferencesManager(context);
        networkManager=new NetworkManager(prefManager);
    }

    @Override
    public CenterResponse getCenterInfo() {
        return prefManager.getCenterInfo();
    }

    @Override
    public void updateSaleList() {
        viewHelper.showLoading();
        CompositeDisposable cd = new CompositeDisposable();
        cd.add(networkManager
                .getCurrentDateApiCall()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(dateList -> {
                    if (viewHelper==null) {
                        return;
                    }
                    getSalePastDate(dateList.getResponse().getToday());
                    cd.dispose();
                }, throwable -> {
                    Timber.tag("my").e(LoggingTree.getMessageForError(throwable,"SalePresenter$updateSaleList "));

                    if (viewHelper==null) {
                        return;
                    }
                    viewHelper.hideLoading();
                    viewHelper.showErrorScreen();
                    cd.dispose();
                }));
    }

    private void getSalePastDate(String today) {
        CompositeDisposable cd = new CompositeDisposable();
        cd.add(networkManager
                .getSaleApiCall(today)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(SaleList::getResponse)
                .subscribe(response -> {
                    if (viewHelper==null) {
                        return;
                    }

                    viewHelper.updateSaleData(response);
                    viewHelper.hideLoading();
                    cd.dispose();
                }, throwable -> {
                    Timber.tag("my").e(LoggingTree.getMessageForError(throwable,"SalePresenter$getSalePastDate"));

                    if (viewHelper==null) {
                        return;
                    }
                    viewHelper.hideLoading();
                    viewHelper.showErrorScreen();
                    cd.dispose();
                }));
    }

    @Override
    public void unSubscribe() {
        viewHelper.hideLoading();
    }

    @Override
    public String getUserToken() {
        return prefManager.getCurrentUserInfo().getApiKey();
    }


    @Override
    public void removePassword() {
        prefManager.setCurrentPassword("");
        prefManager.setUsersLogin(null);
        prefManager.setCurrentUserInfo(null);
    }

    @Override
    public UserResponse getCurrentUser()
    {
        return prefManager.getCurrentUserInfo();
    }

    @Override
    public void setCurrentUser(UserResponse user)
    {
        prefManager.setCurrentUserInfo(user);
    }

}
