//package com.medhelp.medhelp.ui.video_consultation;
//
//import android.content.Context;
//
//import com.medhelp.medhelp.Constants;
//import com.medhelp.medhelp.data.db.RealmHelper;
//import com.medhelp.medhelp.data.db.RealmManager;
//import com.medhelp.medhelp.data.model.VisitResponse;
//import com.medhelp.medhelp.data.network.NetworkManager;
//import com.medhelp.medhelp.data.pref.PreferencesManager;
//import com.medhelp.medhelp.utils.timber_log.LoggingTree;
//import com.medhelp.shared.model.UserResponse;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import io.reactivex.android.schedulers.AndroidSchedulers;
//import io.reactivex.disposables.CompositeDisposable;
//import io.reactivex.schedulers.Schedulers;
//import timber.log.Timber;
//
//public class OnlineConsultationPresenter {
//    private OnlineConsultationFragment view;
//
//    PreferencesManager prefManager;
//    NetworkManager networkManager;
//    RealmHelper realmManager;
//
//    public OnlineConsultationPresenter(Context context, OnlineConsultationFragment view)
//    {
//        prefManager=new PreferencesManager(context);
//        networkManager=new NetworkManager(prefManager);
//        realmManager=new RealmManager(context);
//
//        this.view =view;
//    }
//
//
//    public void removePassword() {
//        prefManager.setCurrentPassword("");
//        prefManager.setUsersLogin(null);
//        prefManager.setCurrentUserInfo(null);
//    }
//
//    public String getUserToken() {
//        return prefManager.getCurrentUserInfo().getApiKey();
//    }
//
//
//    public void getConsultationList() {
//
//
//        view.showLoading();
//        CompositeDisposable cd = new CompositeDisposable();
//        cd.add(networkManager
//                .getAllReceptionApiCall()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(response ->
//                        {
//                            if (view!=null) {
//                                view.updateRecy(filtrationOnOldAndVideoCall(response.getResponse()));
//                                view.hideLoading();
//                            }
//                            cd.dispose();
//                        },
//                        throwable -> {
//                            Timber.tag("my").e(LoggingTree.getMessageForError(throwable,"ProfilePresenter$getVisits "));
//
//                            if (view != null)  {
//                                view.hideLoading();
//                                view.showErrorScreen();
//                                cd.dispose();
//                            }
//                        }));
//
//
//        //OnlineConsultationData t1=new OnlineConsultationData();
////        t1.setDurationSec(10 *60);
////        t1.setExecuteTheScenario(OnlineConsultationData.SCENARIO_NON);
////        t1.setDocId("2");
////        t1.setDocName("Захарьин Григорий Анатольевич");
////        t1.setPatientId("1");
////        t1.setPatientName("Иванов Иван Иванович");
////        t1.setService("Бегать по воде");
////        t1.setTimeDateStart("11:00 20.04.2020");
//
//    }
//
//    private List<VisitResponse> filtrationOnOldAndVideoCall(List<VisitResponse> list)
//    {
//        if(list==null || list.size()==0 || (list.size()==1 && list.get(0).getDateOfReceipt()==null))
//            return list;
//
//        List<VisitResponse> newList=new ArrayList<>();
//        long currentTime=System.currentTimeMillis();
//
//        for (VisitResponse visit : list) {
//            if ((visit.getTimeMills()+(visit.getDurationSec()*1000)> currentTime) && visit.getDop().equals(Constants.TYPE_DOP_VIDEO_CALL)) {
//                visit.setUserName(prefManager.getCurrentUserInfo().getSurname()+" "+prefManager.getCurrentUserInfo().getName()+" "+prefManager.getCurrentUserInfo().getPatronymic());
//                visit.setExecuteTheScenario(Constants.SCENARIO_NON);
//                newList.add(visit);
//            }
//        }
//
//        return newList;
//    }
//
//    public UserResponse getCurrentUser()
//    {
//        return prefManager.getCurrentUserInfo();
//    }
//
//    public void setCurrentUser(UserResponse user)
//    {
//        prefManager.setCurrentUserInfo(user);
//    }
//
//
//}
