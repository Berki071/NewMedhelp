package com.medhelp.medhelp.ui.analise_list;

import android.content.Context;
import com.medhelp.medhelp.data.model.AnaliseListData;
import com.medhelp.medhelp.data.network.NetworkManager;
import com.medhelp.medhelp.data.pref.PreferencesManager;
import com.medhelp.medhelp.ui.analise_list.recy_folder.DataForAnaliseList;
import com.medhelp.medhelp.utils.timber_log.LoggingTree;
import com.medhelp.shared.model.UserResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class AnaliseListPresenter implements AnaliseListHelper.Presenter {
    Context context;
    AnaliseListHelper.View view;

    PreferencesManager prefManager;
    NetworkManager networkManager;

    public AnaliseListPresenter(Context context, AnaliseListHelper.View view)
    {
        prefManager=new PreferencesManager(context);
        networkManager=new NetworkManager(prefManager);

        this.view=view;
    }

    @Override
    public void updateAnaliseList() {
        view.showLoading();

        CompositeDisposable cd = new CompositeDisposable();
        cd.add(networkManager
                .getResultAnalyzesList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(dateList -> {
                    if (view==null) {
                        return;
                    }

                    ArrayList<DataForAnaliseList> newList= processData(dateList.getResponse());
                    if(newList!=null)
                        Collections.sort(newList);
                    view.updateAnaliseData(newList);
                    view.hideLoading();
                    cd.dispose();
                }, throwable -> {
                    Timber.tag("my").e(LoggingTree.getMessageForError(throwable,"AnaliseListPresenter$updateAnaliseList "));

                    if (view==null) {
                        return;
                    }
                    view.hideLoading();
                    view.showErrorScreen();
                    cd.dispose();
                }));
    }


    private ArrayList<DataForAnaliseList> processData(List<AnaliseListData> list)
    {
        if(list==null)
            return null;

        if(list.size()<=0)
            return null;

        if(list.size()==1 && list.get(0).getStatus()==null)
            return null;

        ArrayList<DataForAnaliseList> newList=new ArrayList<>();

        for(int i=0;i<list.size();i++)
        {
            String date=list.get(i).getDateOfPass();
            int num=checkInclusion(date,newList);

            if(num==-1)
            {
                ArrayList<AnaliseListData> tmp=new ArrayList<>();
                tmp.add(list.get(i));
                DataForAnaliseList dataForAnaliseList=new DataForAnaliseList(list.get(i).getDateOfPass(),tmp);
                newList.add(dataForAnaliseList);
            }
            else
            {
                newList.get(num).getItems().add(list.get(i));
            }
        }

        return newList;
    }

    private int checkInclusion(String date,ArrayList<DataForAnaliseList> newList)
    {
        if(newList==null)
            return -1;

        if(newList.size()<=0)
            return -1;

        for(int i=0;i<newList.size();i++)
        {
            if(newList.get(i).getTitle().equals(date))
                return i;
        }

        return -1;
    }

    @Override
    public void removePassword() {
        prefManager.setCurrentPassword("");
        prefManager.setUsersLogin(null);
        prefManager.setCurrentUserInfo(null);
    }

    @Override
    public void unSubscribe() {
        view.hideLoading();
    }

    @Override
    public String getUserToken() {
        return prefManager.getCurrentUserInfo().getApiKey();
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
