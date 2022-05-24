package com.medhelp.medhelp.ui.profile;


import android.content.Context;
import com.google.gson.Gson;
import com.medhelp.medhelp.data.model.VisitResponse;
import com.medhelp.medhelp.data.network.NetworkManager;
import com.medhelp.medhelp.data.pref.PreferencesManager;
import com.medhelp.medhelp.ui.view.shopping_basket.sub.DataPaymentForRealm;
import com.medhelp.medhelp.utils.timber_log.LoggingTree;
import com.medhelp.shared.model.UserResponse;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class ProfilePresenter implements ProfileHelper.Presenter {

    Context context;
    ProfileHelper.View viewHelper;

    PreferencesManager prefManager;
    NetworkManager networkManager;

    public ProfilePresenter(Context context, ProfileHelper.View viewHelper) {
        this.context = context;

        this.viewHelper = viewHelper;

        prefManager = new PreferencesManager(context);
        networkManager = new NetworkManager(prefManager);
    }


    @Override
    public void getVisits() {
        final String[] time = new String[1];
        final String[] today = new String[1];

        viewHelper.showLoading();
        CompositeDisposable cd = new CompositeDisposable();
        cd.add(networkManager
                .getCurrentDateApiCall()
                .concatMap(resp -> {
                    time[0] = resp.getResponse().getTime();
                    today[0] = resp.getResponse().getToday();
                    return networkManager.getAllReceptionApiCall();
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response ->
                        {
                            if (viewHelper == null)
                                return;

                            if (!response.isError())
                                viewHelper.updateData(response.getResponse(), today[0], time[0]);
                            else
                                viewHelper.showErrorScreen();

                            viewHelper.hideLoading();
                            viewHelper.swipeDismiss();
                            cd.dispose();
                        },
                        throwable -> {
                            Timber.tag("my").e(LoggingTree.getMessageForError(throwable, "ProfilePresenter$getVisits "));

                            if (viewHelper == null)
                                return;

                            viewHelper.hideLoading();
                            viewHelper.swipeDismiss();
                            viewHelper.showErrorScreen();
                            cd.dispose();
                        }));
    }

    @Override
    public void updateHeaderInfo() {
        viewHelper.updateHeader(prefManager.getCenterInfo());
    }

    @Override
    public void unSubscribe() {
        viewHelper.hideLoading();
    }

    @Override
    public void cancellationOfVisit(int idUser, int id_record, String cause, int idBranch) {
        viewHelper.showLoading();

        new CompositeDisposable().add(networkManager
                .sendCancellationOfVisit(idUser, id_record, cause, idBranch)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response ->
                        {
                            Timber.tag("my").v("Отмена приема id записи " + id_record + " причина " + cause);
                            getVisits();
                        }
                        , throwable ->
                        {
                            viewHelper.hideLoading();
                            Timber.tag("my").e(LoggingTree.getMessageForError(throwable, "ProfilePresenter$cancellationOfVisit "));
                            viewHelper.showError("Произошла ошибка при выполнении операции, попробуйте повторить");
                        }
                ));
    }

    @Override
    public void confirmationOfVisit(int idUser, int id_record, int idBranch) {
        viewHelper.showLoading();

        new CompositeDisposable().add(networkManager
                .sendConfirmationOfVisit(idUser, id_record, idBranch)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response ->
                        {
                            Timber.tag("my").v("Подтвердение приема id записи %s", id_record);
                            if (response.getResponse()) {
                                getVisits();
                            }
                        }
                        , throwable ->
                        {
                            Timber.tag("my").e(LoggingTree.getMessageForError(throwable, "ProfilePresenter$confirmationOfVisit "));
                            viewHelper.showError("Произошла ошибка при выполнении операции, попробуйте повторить");
                        }
                )
        );

        viewHelper.hideLoading();
    }

    @Override
    public String getCurrentHospitalBranch() {
        //String ss=getDataHelper().getCurrentNameBranch();
        return prefManager.getCurrentUserInfo().getNameBranch();
    }

    @Override
    public String getUserToken() {
        return prefManager.getCurrentUserInfo().getApiKey();
    }


    @Override
    public UserResponse getCurrentUser() {
        return prefManager.getCurrentUserInfo();
    }

    @Override
    public void setCurrentUser(UserResponse user) {
        prefManager.setCurrentUserInfo(user);
    }


    private int getCountItems(String element) {
        return element.length() - element.replace("&", "").length();
    }

    private void sendToServer(DataPaymentForRealm data) {
        Timber.tag("my").v("услуги оплачены, Sum: " + data.getPrice() + "; IdRecord: " + data.getIdZapisi() + "; IdPayment: " + data.getIdPayment() + "; IdYsl: " + data.getIdYsl());

        CompositeDisposable cd = new CompositeDisposable();

        cd.add(networkManager
                .sendToServerPaymentData(data.getIdUser(), data.getIdBranch(), data.getIdZapisi(), data.getIdYsl(), data.getPrice(), getCountItems(data.getIdUser()), data.getIdPayment())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            if (response.getResponse()) {

                            } else {
                                Gson gson = new Gson();
                                Timber.tag("my").e(LoggingTree.getMessageForError(null, "ProfilePresenter/sendPaymentToServer, совпал pay_id " + gson.toJson(data)));
                            }

                            prefManager.deletePaymentData(data);

                            cd.dispose();

                        }, throwable -> {
                            Gson gson = new Gson();
                            Timber.tag("my").e(LoggingTree.getMessageForError(throwable, "ProfilePresenter/sendPaymentToServer : " + gson.toJson(data)));
                            cd.dispose();
                        }
                )
        );
    }

    @Override
    public void sendConfirmComing(VisitResponse viz) {

        viewHelper.showLoading();
        CompositeDisposable cd = new CompositeDisposable();
        cd.add(networkManager
                .sendIAmHere(viz.getIdUser(), viz.getIdRecord(), viz.getIdBranch())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {

                            viewHelper.hideLoading();
                            viewHelper.responseConfirmComing(viz);
                            cd.dispose();
                        }, throwable -> {
                            viewHelper.hideLoading();
                            Timber.tag("my").e(LoggingTree.getMessageForError(throwable, "ProfilePresenter/sendConfirmComing "));
                            cd.dispose();
                        }
                )
        );
    }
}
