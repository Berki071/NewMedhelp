package com.medhelp.medhelp.ui.doctor;


import android.content.Context;
import com.medhelp.medhelp.data.model.AllDoctorsResponse;
import com.medhelp.medhelp.data.model.SpecialtyList;
import com.medhelp.medhelp.data.network.NetworkManager;
import com.medhelp.medhelp.data.pref.PreferencesManager;
import com.medhelp.medhelp.utils.timber_log.LoggingTree;
import com.medhelp.shared.model.UserResponse;

import java.util.ArrayList;
import java.util.List;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class DoctorsPresenter implements DoctorsHelper.Presenter
{
    private List<AllDoctorsResponse> allDoc;
    Context context;
    DoctorsHelper.View view;

    PreferencesManager prefManager;
    NetworkManager networkManager;

    public DoctorsPresenter(Context context, DoctorsHelper.View view)
    {
        this.context=context;
        this.view=view;

        prefManager = new PreferencesManager(context);
        networkManager = new NetworkManager(prefManager);

        //int tt = prefManager.getCurrentUserInfo().getIdCenter();
    }

    @Override
    public void removePassword()
    {
        prefManager.setCurrentPassword("");
        prefManager.setUsersLogin(null);
    }

    @Override
    public void getDoctorList(int idSpec)
    {

        if(idSpec==-1  && allDoc!=null)
        {
            view.updateView(allDoc);
            view.hideLoading();
            return;
        }

        if(allDoc!=null)
        {
            view.updateView(sortAllDoc(idSpec));
            view.hideLoading();
            return;
        }

        view.showLoading();

        CompositeDisposable cd = new CompositeDisposable();
        cd.add(networkManager
                .getAllDoctors()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response ->
                        {
                            if (response.getResponses().size() <= 0) {
                                view.updateView(null);
                            } else {
                                allDoc = response.getResponses();

                                if(view!=null)
                                    view.updateView(response.getResponses());
                            }
                            view.hideLoading();
                            cd.dispose();
                        },
                        throwable ->
                        {
                            Timber.tag("my").e(LoggingTree.getMessageForError(throwable,"DoctorsPresenter$getDoctorList "));

                            view.hideLoading();
                            view.showErrorScreen();
                            cd.dispose();
                        })
        );


    }

    private List<AllDoctorsResponse> sortAllDoc(int idSpec)
    {
        List<AllDoctorsResponse> sortList =new ArrayList<>();

        for(int i=0;i<allDoc.size();i++)
        {
            AllDoctorsResponse doc=allDoc.get(i);

            if(doc.getId_specialties_int_list()==null)
                continue;

            if(doc.getId_specialties_int_list().size()==1  && doc.getId_specialties_int_list().get(0)==idSpec)
            {
                sortList.add(doc);
                continue;
            }

            for (int j=0;j<doc.getId_specialties_int_list().size();j++)
            {
                if(doc.getId_specialties_int_list().get(j)==idSpec)
                {
                    sortList.add(doc);
                }
            }
        }

        return sortList;
    }

    @Override
    public void getSpecialtyByCenter()
    {
        view.showLoading();

        CompositeDisposable cd = new CompositeDisposable();
        cd.add(networkManager
                .getCategoryApiCall()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(SpecialtyList::getSpec)
                .subscribe(response ->
                {
                    try
                    {
                        view.updateSpecialty(response);
                    } catch (Exception e)
                    {
                        Timber.tag("my").e(LoggingTree.getMessageForError(e,"DoctorsPresenter$getSpecialtyByCenter$1 "));
                    }

                    cd.dispose();
                }, throwable ->
                {
                    view.hideLoading();
                    Timber.tag("my").e(LoggingTree.getMessageForError(throwable,"DoctorsPresenter$getSpecialtyByCenter$2 "));
                    view.showErrorScreen();
                    cd.dispose();
                }));
    }

    @Override
    public void unSubscribe()
    {
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
