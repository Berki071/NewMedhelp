package com.medhelp.medhelp.ui.doctor.service_activity;


import android.content.Context;
import com.medhelp.medhelp.data.model.CategoryResponse;
import com.medhelp.medhelp.data.model.ServiceResponse;
import com.medhelp.medhelp.data.network.NetworkManager;
import com.medhelp.medhelp.data.pref.PreferencesManager;
import com.medhelp.medhelp.utils.timber_log.LoggingTree;
import java.util.List;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;


public class ServicePresenter implements ServiceHelper.Presenter {
    Context context;
    ServiceHelper.View view;

    PreferencesManager prefManager;
    NetworkManager networkManager;

    public ServicePresenter(Context context, ServiceHelper.View view)
    {
        this.context=context;
        this.view=view;

        prefManager=new PreferencesManager(context);
        networkManager=new NetworkManager(prefManager);
    }


    @Override
    public void getData(int idDoctor, int idBranch, int idUser)
    {
        view.showLoading();

        CompositeDisposable cd=new CompositeDisposable();
        cd.add(networkManager
                .getCategoryApiCall(idDoctor)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response ->
                {
                    if (response != null)
                    {
                        getPrice(response.getSpec(), idDoctor,idBranch,idUser);
                    }
                    else {
                        view.hideLoading();
                    }

                    cd.dispose();
                }, throwable ->
                {
                    Timber.tag("my").e(LoggingTree.getMessageForError(throwable,"ServicePresenter$getFilesForRecy "));

                    if (view==null)
                    {
                        return;
                    }
                    view.hideLoading();
                    view.showErrorScreen();
                    cd.dispose();
                }));
    }


    private void getPrice(List<CategoryResponse> categoryResponse, int idDoctor, int idBranch, int idUser)
    {

        CompositeDisposable cd=new CompositeDisposable();

        cd.add(networkManager
                .getPriceApiCall(idDoctor,idBranch,idUser)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response ->
                {
                    view.updateView(categoryResponse, response.getServices());
                    view.hideLoading();
                    cd.dispose();
                }, throwable ->
                {
                    Timber.tag("my").e(LoggingTree.getMessageForError(throwable,"ServicePresenter$getPrice "));

                    if (view==null)
                    {
                        return;
                    }
                    view.hideLoading();
                    view.showErrorScreen();
                    cd.dispose();
                }));
    }

    @Override
    public void changeFabFavorites(ServiceResponse item) {
        if(item.getFavorites().equals("1"))
        {
            CompositeDisposable cd = new CompositeDisposable();
            cd.add(networkManager
                    .insertFavoritesService(item.getId())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(response -> {
                                Timber.tag("my").v("Добавление в закладки: id услуги "+item.getId()+", название услуги "+item.getTitle());
                                cd.dispose();
                            },
                            throwable -> {
                                Timber.tag("my").e(LoggingTree.getMessageForError(throwable,"ServicePresenter$changeFabFavorites$1 "));

                                item.setFavorites("0");
                                view.refreshRecy();
                                view.showError("Не удалось сохранить в избранное");
                               // Log.wtf("mLog", throwable.getMessage());
                                cd.dispose();
                            })
            );
        }
        else
        {
            CompositeDisposable cd=new CompositeDisposable();
            cd.add(networkManager
                    .deleteFavoritesService(item.getId())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(response->{
                                cd.dispose();
                            },
                            throwable ->{
                                Timber.tag("my").e(LoggingTree.getMessageForError(throwable,"ServicePresenter$changeFabFavorites$2 "));

                                item.setFavorites("1");
                                view.refreshRecy();
                                view.showError("Не удалось удалить из избранного");
                                //Log.wtf("mLog", throwable.getMessage());
                                cd.dispose();
                            } )
            );
        }
    }

    @Override
    public void unSubscribe()
    {
        view.hideLoading();
    }

    @Override
    public void testToSpam(ServiceAdapter.ViewHolder view2, int service, int limitService) {
        view.showLoading();

        CompositeDisposable cd=new CompositeDisposable();
        cd.add(networkManager
                .checkSpamRecord(service)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response->{
                            int limitCenter=prefManager.getCenterInfo().getMax_records();

                          /*  int co1=response.getmResponses().get(0).getCount1();
                            int co2=response.getmResponses().get(0).getCount2();
*/
                            if(limitService < response.get_mResponses().get(0).getCount1()  ||  limitCenter < response.get_mResponses().get(0).getCount2())
                            {
                                view.showError("Превышен лимит записей, для получения более подробной информации обратитесь в медицинский центр");
                            }
                            else
                            {
                                view2.jumpToNextPage();
                            }
                            view.hideLoading();
                            cd.dispose();
                        },
                        throwable ->{
                            Timber.tag("my").e(LoggingTree.getMessageForError(throwable,"ServicePresenter$testToSpam "));
                            view.hideLoading();
                            view.showError("Не удалось проверить на спам");
                            cd.dispose();
                        } )
        );
    }

}
