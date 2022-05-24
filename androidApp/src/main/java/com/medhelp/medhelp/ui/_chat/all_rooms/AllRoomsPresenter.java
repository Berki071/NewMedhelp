//package com.medhelp.medhelp.ui._chat.all_rooms;
//
//import android.content.Context;
//import com.medhelp.medhelp.data.db.RealmHelper;
//import com.medhelp.medhelp.data.db.RealmManager;
//import com.medhelp.medhelp.data.model.chat.InfoAboutDoc;
//import com.medhelp.medhelp.data.network.NetworkManager;
//import com.medhelp.medhelp.data.pref.PreferencesManager;
//import com.medhelp.medhelp.utils.rx.AppSchedulerProvider;
//import com.medhelp.medhelp.utils.rx.SchedulerProvider;
//import com.medhelp.medhelp.utils.timber_log.LoggingTree;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import io.reactivex.disposables.CompositeDisposable;
//import timber.log.Timber;
//
//public class AllRoomsPresenter implements AllRoomsHelper.Presenter {
//    private AllRoomsHelper.View activityHelper;
//
//    private SchedulerProvider schedulerProvider;
//    private Context context;
//
//    PreferencesManager prefManager;
//    NetworkManager networkManager;
//    RealmHelper realmManager;
//
//    public AllRoomsPresenter(Context context)
//    {
//        this.context=context;
//
//        prefManager = new PreferencesManager(context);
//        networkManager = new NetworkManager(prefManager);
//        realmManager = new RealmManager(context);
//
//        schedulerProvider=new AppSchedulerProvider();
//
//        activityHelper =(AllRoomsHelper.View)context;
//    }
//
//
//    @Override
//    public void getAllRooms() {
//        activityHelper.showLoading(true);
//
//        int idUser=prefManager.getCurrentUserInfo().getIdUser();
//        int idBranch=prefManager.getCurrentUserInfo().getIdBranch();
//
//        final List<InfoAboutDoc> li =new ArrayList<>();
//
//        CompositeDisposable cd = new CompositeDisposable();
//        cd.add(networkManager
//                .getAllRoom(idUser,idBranch)
//                .concatMapCompletable(res->
//                        {
//                            for(InfoAboutDoc ia  :  res.getRespons())
//                            {
//                                li.add(ia);
//                            }
//                            return realmManager.saveInfoAboutDoc(res.getRespons());
//                        })
//
//                .subscribeOn(schedulerProvider.io())
//                .observeOn(schedulerProvider.ui())
//                .subscribe(() -> {
//                    activityHelper.updateRecy(li);
//                    activityHelper.showLoading(false);
//                    cd.dispose();
//                }, throwable ->
//                {
//                    Timber.tag("my").e(LoggingTree.getMessageForError(throwable,"AllRoomsPresenter$getAllRooms "));
//                    activityHelper.showLoading(false);
//                    activityHelper.showErrorScreen();
//                    cd.dispose();
//                })
//        );
//    }
//
//    @Override
//    public String getUserToken() {
//        return prefManager.getCurrentUserInfo().getApiKey();
//    }
//
//
//}
