package com.medhelp.medhelp.ui.analysis_price_list;

import android.content.Context;
import com.medhelp.medhelp.data.model.analise_price.AnalisePriceResponse;
import com.medhelp.medhelp.data.network.NetworkManager;
import com.medhelp.medhelp.data.pref.PreferencesManager;
import com.medhelp.medhelp.utils.rx.AppSchedulerProvider;
import com.medhelp.medhelp.utils.rx.SchedulerProvider;
import com.medhelp.medhelp.utils.timber_log.LoggingTree;
import com.medhelp.shared.model.UserResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

public class AnalysisPriceListPresenter implements AnalisePriceListHelper.Presenter {
    private SchedulerProvider schedulerProvider;

    private AnalisePriceListHelper.View activityHelper;
    private Context context;

    PreferencesManager prefManager;
    NetworkManager networkManager;

    public AnalysisPriceListPresenter(Context context, AnalisePriceListHelper.View activityHelper)
    {
        this.context=context;

        prefManager=new PreferencesManager(context);
        networkManager=new NetworkManager(prefManager);

        schedulerProvider=new AppSchedulerProvider();

        this.activityHelper=activityHelper;
    }

    @Override
    public void getAnalisePrice() {
        activityHelper.showLoading();
        CompositeDisposable cd=new CompositeDisposable();
        cd.add(networkManager.getAnalisePrice()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(response ->{
                    List<String> group=separateGroup(response.getResponse());
                    activityHelper.updateView(group , response.getResponse());
                    activityHelper.hideLoading();
                },throwable -> {
                    activityHelper.hideLoading();
                    Timber.tag("my").e(LoggingTree.getMessageForError(throwable,"AnalysisPriceListPresenter$getAnalisePrice "));
                    activityHelper.showErrorScreen();
                })
        );
    }

    @Override
    public void removePassword() {
        prefManager.setCurrentPassword("");
        prefManager.setUsersLogin(null);
        prefManager.setCurrentUserInfo(null);
    }

    @Override
    public String getUserToken() {
        return prefManager.getCurrentUserInfo().getApiKey();
    }


    private List<String> separateGroup(List<AnalisePriceResponse> list)
    {
        List<String> groupList=new ArrayList<>();

        groupList.add(list.get(0).getGroup());

        for (AnalisePriceResponse tmp : list)
        {
            Boolean overlap=false;

            for (String str : groupList)
            {
                if(str.equals(tmp.getGroup()))
                {
                    overlap=true;
                    break;
                }
            }

            if(!overlap)
            {
                groupList.add(tmp.getGroup());
            }

        }

        Collections.sort(groupList);
        return groupList;
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
