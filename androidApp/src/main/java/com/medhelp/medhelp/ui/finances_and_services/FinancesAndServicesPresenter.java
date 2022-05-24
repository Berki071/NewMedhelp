package com.medhelp.medhelp.ui.finances_and_services;

import android.content.Context;

import com.androidnetworking.error.ANError;
import com.google.gson.Gson;
import com.medhelp.medhelp.data.model.DateList;
import com.medhelp.medhelp.data.network.NetworkManager;
import com.medhelp.medhelp.data.pref.PreferencesManager;
import com.medhelp.medhelp.ui.view.shopping_basket.sub.DataPaymentForRealm;
import com.medhelp.medhelp.utils.timber_log.LoggingTree;
import com.medhelp.shared.model.UserResponse;

import java.util.ArrayList;
import java.util.List;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class FinancesAndServicesPresenter implements FinancesAndServicesHelper.Presenter {
    private Context context;
    private FinancesAndServicesHelper.View helper;

    PreferencesManager prefManager;
    NetworkManager networkManager;

    public FinancesAndServicesPresenter(Context context, FinancesAndServicesHelper.View helper) {
        this.context=context;
        this.helper=helper;

        prefManager=new PreferencesManager(context);
        networkManager=new NetworkManager(prefManager);

        getRealmPaymentData();
    }

    @Override
    public void removePassword() {
        prefManager.setCurrentPassword("");
        prefManager.setUsersLogin(null);
        prefManager.setCurrentUserInfo(null);
    }

    public void getVisits() {
        final String[] today = new String[1];
        final String[] time = new String[1];

        helper.showLoading();
        CompositeDisposable cd = new CompositeDisposable();
        cd.add(networkManager
                .getCurrentDateApiCall()
                .subscribeOn(Schedulers.io())
                .map(DateList::getResponse)
                .concatMap(resp->{
                    today[0] =resp.getToday();
                    time[0] =resp.getTime();
                    return networkManager.getAllReceptionApiCall();
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response ->
                        {
                            if(!response.isError())
                                helper.updateData(response.getResponse(), today[0], time[0]);
                            else
                                helper.showErrorScreen();

                            helper.hideLoading();
                            cd.dispose();
                        },
                        throwable -> {
                            Timber.tag("my").e(LoggingTree.getMessageForError(throwable,"ProfilePresenter$getVisits "));

                            helper.hideLoading();
                            helper.showErrorScreen();
                            cd.dispose();
                        }));
    }

    private void getRealmPaymentData() {
        List<DataPaymentForRealm> list = prefManager.getAllPaymentData();
        if (list.size() != 0) {
            List<DataPaymentForRealm> listToYandex = new ArrayList<>();
            List<DataPaymentForRealm> listToServer = new ArrayList<>();

            for (DataPaymentForRealm tmp : list) {
                if (tmp.getYandexInformation()) {
                    listToYandex.add(tmp);
                } else {
                    listToServer.add(tmp);
                }
            }

            if (listToYandex.size() > 0) {
                for (DataPaymentForRealm tmp : listToYandex) {
                    testPaidToYandex(tmp);
                }
            }

            if (listToServer.size() > 0) {
                for (DataPaymentForRealm tmp : listToServer) {
                    sendToServer(tmp);
                }
            }
        }
    }

    private void testPaidToYandex(DataPaymentForRealm data)
    {
        CompositeDisposable cd = new CompositeDisposable();

        cd.add(networkManager
                .getPaymentInformation(data.getIdPayment(), data.getYKeyObt().getIdShop(), data.getYKeyObt().getKeyShop())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            switch (response.getStatus()) {
                                case "waiting_for_capture":    //ожидает подтверждения или отмены
                                    sendToServer(data);
                                    break;

                                case "succeeded":    //ожидает подтверждения или отмены
                                    sendToServer(data);
                                    break;
                                default:
                                    Timber.tag("my").e(LoggingTree.getMessageForError(null,"FinancesAndServicesPresenter/makingPayment default  response.getStatus()"+response.getStatus()+"; response.getId()(idPayment) "
                                            +response.getId()+"; response.getPaid() "+response.getPaid()));

                            }

                            prefManager.deletePaymentData(data);

                            cd.dispose();

                        }, throwable -> {
                            if (throwable instanceof ANError) {
                                ANError anError = (ANError) throwable;
                                String msg = anError.getErrorBody();
                                if (msg != null && msg.contains("not found")) {
                                    prefManager.deletePaymentData(data);
                                    cd.dispose();
                                    return;
                                }
                            }

                            Gson gson=new Gson();
                            Timber.tag("my").e(LoggingTree.getMessageForError(throwable, "FinancesAndServicesPresenter/getPaymentInformation "+gson.toJson(data)));
                            cd.dispose();
                        }
                )
        );
    }

    private int getCountItems(String element)
    {
        return element.length() - element.replace("&", "").length();
    }

    private void sendToServer(DataPaymentForRealm data)
    {
        Timber.tag("my").v("услуги оплачены, Sum: "+data.getPrice()+"; IdZapisi: "+data.getIdZapisi()+"; IdPayment: "+data.getIdPayment()+"; IdYsl: "+data.getIdYsl());

        CompositeDisposable cd = new CompositeDisposable();

        cd.add(networkManager
                .sendToServerPaymentData(data.getIdUser(), data.getIdBranch(), data.getIdZapisi(), data.getIdYsl(), data.getPrice(),getCountItems(data.getIdUser()), data.getIdPayment())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            if (response.getResponse()) {

                            }
                            else
                            {
                                Gson gson=new Gson();
                                Timber.tag("my").e(LoggingTree.getMessageForError(null,"FinancesAndServicesPresenter/sendPaymentToServer, совпал pay_id "+gson.toJson(data)));
                            }

                            prefManager.deletePaymentData(data);

                            cd.dispose();

                        }, throwable -> {
                            Gson gson=new Gson();
                            Timber.tag("my").e(LoggingTree.getMessageForError(throwable, "FinancesAndServicesPresenter/sendPaymentToServer : "+gson.toJson(data)));
                            cd.dispose();
                        }
                )
        );
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
