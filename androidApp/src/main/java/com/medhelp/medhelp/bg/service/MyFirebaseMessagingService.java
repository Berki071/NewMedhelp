package com.medhelp.medhelp.bg.service;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.medhelp.medhelp.bg.notifiaction.SimpleNotificationForFCM;
import com.medhelp.medhelp.data.model.chat.SimpleResBoolean;
import com.medhelp.medhelp.data.network.NetworkManager;
import com.medhelp.medhelp.data.pref.PreferencesManager;
import com.medhelp.medhelp.utils.timber_log.LoggingTree;
import com.medhelp.shared.model.UserResponse;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (remoteMessage.getNotification() != null) {
                    //если открыта страница то она обновиться без уведомлений
                    if (remoteMessage.getData().size() > 0){
                        String type_message = remoteMessage.getData().get("type_message");
                        String id_kl = remoteMessage.getData().get("id_kl");
                        String id_filial = remoteMessage.getData().get("id_filial");

                        if(type_message!=null && id_kl!=null && id_filial!=null){
                            SimpleNotificationForFCM simpleNotificationForFCM=new SimpleNotificationForFCM(getApplicationContext(), (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE));
                            simpleNotificationForFCM.showData(remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody(),type_message,id_kl,id_filial);
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onNewToken(String token) {
        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(token);
    }

    private void sendRegistrationToServer(String token) {
        PreferencesManager prefManager = new PreferencesManager(getApplicationContext());
        NetworkManager networkManager = new NetworkManager(prefManager);

        List<UserResponse> list = prefManager.getUsersLogin();
        if(list==null || list.size()==0){
            return;
        }

        Observable.fromIterable(list)
                .flatMap(userResponse ->
                        networkManager.sendFcmId(String.valueOf(userResponse.getIdUser()),String.valueOf(userResponse.getIdBranch()),token))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SimpleResBoolean>() {
                               @Override
                               public void onSubscribe(Disposable d) {}

                               @Override
                               public void onNext(SimpleResBoolean userResponse) {}

                               @Override
                               public void onError(Throwable e) {
                                   Timber.tag("my").e(LoggingTree.getMessageForError(e, "MyFirebaseMessagingService$sendFcmToken"));
                               }

                               @Override
                               public void onComplete() {}
                           }
                );
    }

}
