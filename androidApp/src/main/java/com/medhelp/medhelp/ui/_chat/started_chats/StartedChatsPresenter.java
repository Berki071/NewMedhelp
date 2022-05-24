//package com.medhelp.medhelp.ui._chat.started_chats;
//
//import android.content.Context;
//import com.medhelp.medhelp.data.db.RealmHelper;
//import com.medhelp.medhelp.data.db.RealmManager;
//import com.medhelp.medhelp.data.network.NetworkManager;
//import com.medhelp.medhelp.data.pref.PreferencesManager;
//import com.medhelp.medhelp.utils.rx.AppSchedulerProvider;
//import com.medhelp.medhelp.utils.rx.SchedulerProvider;
//import com.medhelp.medhelp.utils.timber_log.LoggingTree;
//import com.medhelp.shared.model.UserResponse;
//
//import io.reactivex.disposables.CompositeDisposable;
//import timber.log.Timber;
//
//public class StartedChatsPresenter implements StartedChatsHelper.Presenter {
//
//    private SchedulerProvider schedulerProvider;
//
//    private StartedChatsHelper.View activityHelper;
//
//    private CompositeDisposable cd;
//
//    PreferencesManager prefManager;
//    NetworkManager networkManager;
//    RealmHelper realmManager;
//
//    public StartedChatsPresenter(Context context, StartedChatsHelper.View activityHelper)
//    {
//        prefManager=new PreferencesManager(context);
//        networkManager=new NetworkManager(prefManager);
//        realmManager=new RealmManager(context);
//
//        schedulerProvider=new AppSchedulerProvider();
//
//        this.activityHelper =activityHelper;
//        cd = new CompositeDisposable();
//    }
//
//    private boolean loadInfo=false;
//    private boolean loadRoom=false;
//
//    private final String keyInfo="keyInfo";
//    private final String keyRoom="keyRoom";
//
//
//    private void mainShowLoading(String method, boolean show)
//    {
//        switch(method)
//        {
//            case keyInfo:
//                loadInfo=show;
//                break;
//
//            case keyRoom:
//                loadRoom=show;
//                break;
//        }
//
//
//        if(loadInfo  ||  loadRoom)
//        {
//            if(!activityHelper.isLoading())
//            {
//                activityHelper.showLoading();
//            }
//        }
//        else
//        {
//            if(activityHelper.isLoading())
//            {
//                activityHelper.hideLoading();
//            }
//        }
//    }
//
//    @Override
//    public void getListRoom() {
//
//        mainShowLoading(keyRoom,true);
//        cd.add(realmManager
//                .getAllActiveRooms()
//                .subscribeOn(schedulerProvider.io())
//                .observeOn(schedulerProvider.ui())
//                .subscribe(response->
//                        {
//                            activityHelper.updateRecy(response);
//                            mainShowLoading(keyRoom,false);
//                        }, throwable ->
//                        {
//                            Timber.tag("my").e(LoggingTree.getMessageForError(throwable,"StartedChatsPresenter$getListRoom"));
//                            mainShowLoading(keyRoom,false);
//                        }
//                )
//        );
//    }
//
//    @Override
//    public void onDestroy() {
//        cd.dispose();
//    }
//
//    @Override
//    public String getUserToken() {
//        return prefManager.getCurrentUserInfo().getApiKey();
//    }
//
//
//    @Override
//    public void removePassword() {
//        prefManager.setCurrentPassword("");
//        prefManager.setUsersLogin(null);
//        prefManager.setCurrentUserInfo(null);
//    }
//
//    @Override
//    public UserResponse getCurrentUser()
//    {
//        return prefManager.getCurrentUserInfo();
//    }
//
//    @Override
//    public void setCurrentUser(UserResponse user)
//    {
//        prefManager.setCurrentUserInfo(user);
//    }
//}
