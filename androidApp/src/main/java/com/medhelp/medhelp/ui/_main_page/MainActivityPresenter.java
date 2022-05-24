package com.medhelp.medhelp.ui._main_page;

import android.content.Context;
import com.medhelp.medhelp.data.model.BonusesItem;
import com.medhelp.medhelp.data.network.NetworkManager;
import com.medhelp.medhelp.data.pref.PreferencesManager;
import com.medhelp.medhelp.utils.main.MainUtils;
import com.medhelp.shared.model.UserResponse;

import java.util.Collections;
import java.util.List;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivityPresenter implements MainActivityHelper.Presenter {
    Context context;
    NetworkManager networkManager;
    PreferencesManager prefManager;

    MainActivityHelper.View view;

    List<BonusesItem> bonusesItemList;

    public MainActivityPresenter(Context context) {
        this.context = context;

        prefManager = new PreferencesManager(context);
        networkManager = new NetworkManager(prefManager);

        view = (MainActivity) context;
    }


    @Override
    public UserResponse getCurrentUser() {
        return prefManager.getCurrentUserInfo();
    }

    @Override
    public void setCurrentUser(UserResponse user) {
        prefManager.setCurrentUserInfo(user);
    }

    @Override
    public void removePassword() {
        prefManager.setCurrentPassword("");
        prefManager.setUsersLogin(null);
        prefManager.setCurrentUserInfo(null);
    }

    public String getLogin() {
        return prefManager.getCurrentLogin();
    }


    public void getAllBonuses() {
        CompositeDisposable cd = new CompositeDisposable();
        cd.add(networkManager
                .getAllBonuses()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responses -> {
                            if (responses.getResponse().size() == 1 && responses.getResponse().get(0).getDate() == null) {
                                bonusesItemList = null;
                                view.initBonuses(null);
                            } else {
                                bonusesItemList = responses.getResponse();
                                Collections.sort(bonusesItemList);
                                view.initBonuses(MainUtils.getSumBonuses(bonusesItemList));
                            }
                            cd.dispose();
                        }, throwable -> {
                            bonusesItemList = null;
                            view.initBonuses(null);
                            cd.dispose();
                        }
                )
        );
    }



    public List<BonusesItem> getBonusesItemList() {
        return bonusesItemList;
    }
}
