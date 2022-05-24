package com.medhelp.medhelp.bg.service;

import android.app.NotificationManager;
import android.content.Context;
import android.util.Log;

public class FirebaseProcessingSingleton {

    private static final FirebaseProcessingSingleton instance = new FirebaseProcessingSingleton();
    private FirebaseProcessingSingleton() {
    }
    public synchronized static FirebaseProcessingSingleton getInstance() {
        return instance;
    }


    FirebaseProcessingSingletonListener listenerBusonChat;
    String idUser;

    FirebaseProcessingSingletonListener listenerMedhelpB;

//    public void sendNotificationBuson(Context context, NotificationManager notificationManager, String msg, String idFirmN, String idUserN) {
//        Log.wtf("fcmMyService", "22 " + (context == null) + (notificationManager == null) + (msg == null) + " " + (msg != null ? msg : "пусто"));
//
//        if (listenerBusonChat != null && (idUser==null || (idUser!=null && idUser.equals(idUserN))))
//            listenerBusonChat.newMsg();
//        else {
//            ShowNotificationBusonSupport showNotificationBusonSupport=new ShowNotificationBusonSupport(context, notificationManager);
//            showNotificationBusonSupport.showDataBuson("Buson", msg, idFirmN, idUserN);
//        }
//    }
//
//    public void sendNotificationMedhelpB(Context context, NotificationManager notificationManager, String msg, String login, String email){
//        if (listenerMedhelpB != null)
//            listenerMedhelpB.newMsg();
//        else {
//            ShowNotificationBusonSupport showNotificationBusonSupport=new ShowNotificationBusonSupport(context, notificationManager);
//            showNotificationBusonSupport.showDataMedhelpB("MedhelpB",msg,login,email );
//        }
//    }
//
//    public void setBusonChatListener(FirebaseProcessingSingletonListener insteadSocketListener, String idUser) {
//        this.listenerBusonChat = insteadSocketListener;
//        this.idUser=idUser;
//    }
//    public void clearBusonChatListener() {
//        this.listenerBusonChat = null;
//        idUser=null;
//    }
//
//    public void setMedhelpBListener(FirebaseProcessingSingletonListener insteadSocketListener) {
//        this.listenerMedhelpB = insteadSocketListener;
//
//    }
//    public void clearMedhelpBtListener() {
//        this.listenerMedhelpB = null;
//    }


    public interface FirebaseProcessingSingletonListener {
        void newMsg();
    }
}
