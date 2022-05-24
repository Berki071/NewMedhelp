/*
package com.medhelp.medhelp.bg.socket;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.medhelp.medhelp.Constants;
import com.medhelp.medhelp.bg.oldClass.notification.ShowNotification;
import com.medhelp.medhelp.data.model.notification.NotificationAnalyzes;
import com.medhelp.medhelp.data.model.notification.NotificationReminderOfAdmission;
import com.medhelp.medhelp.data.model.notification.NotificationShares;
import com.medhelp.medhelp.data.pref.PreferencesManager;

public class ControlToSocket {

    private static final ControlToSocket instance=new ControlToSocket();
    private ControlToSocket(){}
    public static ControlToSocket getInstance()
    {
        return instance;
    }


    public static final String CONFIRMED="confirmed";
    public static final String RESEIVED="received";

    private SocketClient mSocketClient;
    private Handler handler;
    private ShowNotification showNotification;

    private int userID;
    private int centerID;


    public void initControlToSocket(Context bContext)
    {
        handler=new Handler();
        showNotification=new ShowNotification(bContext,(NotificationManager) bContext.getSystemService(Context.NOTIFICATION_SERVICE));

        PreferencesManager preferencesManager = new PreferencesManager(bContext);

        userID= preferencesManager.getCurrentUserId();
        centerID= preferencesManager.getCurrentCenterId();
    }

    public void startControlToSocket()
    {
        //Log.wtf("mLog","ServiceMy onStartCommand ");
        if ( (userID!=0  && centerID!=0)  &&  (mSocketClient == null || !mSocketClient.isConnected() || !mSocketClient.isRunning())) {

            //Log.wtf("mLog","ServiceMy onStartCommand "+userID+"&"+centerID);
            connectToServer();
        }
    }

    public void destroyControlToSocket()
    {
        //Log.wtf("mLog","service destroy");

        if (mSocketClient != null) {
            new Thread(() -> {
                mSocketClient.stopClient();
                mSocketClient = null;
            }).start();
        }
    }


    private void connectToServer() {
        new Thread(() -> {
            mSocketClient = new SocketClient(new SocketClient.OnMessageReceived() {
                @Override
                public void onConnected() {
                    //Log.wtf("mLog", "ServiceMy onConnected");
                    if (!latchPing)
                        sendPing();
                }

                @Override
                public void messageReceived(final String message) {
                    //Log.wtf("mLog", "ServiceMy messageReceived " + message);
                    rawMessage(message);
                }

                @Override
                public void errorRun1() {
                    Log.wtf("mLog", "errorRun1");
                    if (!latchPing)
                        sendPing();
                }
            }, centerID, userID);
            mSocketClient.run();
        }).start();
    }


    private boolean latchPing=false;
    private void sendPing() {
        Runnable runnable = new Runnable() {
            @Override public void run() {
                latchPing=true;

                //Log.wtf("mLog", Constants.PING);

                if (mSocketClient != null) {
                    if (!mSocketClient.isRunning()) {
                        mSocketClient.stopClient();
                        connectToServer();
                    } else {
                        mSocketClient.sendMessage(Constants.PING);
                    }
                }
                handler.postDelayed(this, 2000);
            }
        };
        handler.post(runnable);
    }

    private void rawMessage(String msg)
    {
        while(true)
        {
            String keyNoti=msg.substring(0,1);

            switch (keyNoti) {
                case "1":  //напоминание о приеме
                    showNotification.showReminderOfAdmission(disperseReminderOfAdmission(msg.substring(0,msg.indexOf("#"))));
                    msg=msg.substring(msg.indexOf("#")+1,msg.length());
                    if(msg.length()<=0)
                        return;
                    break;

                case "2":  //анализы готовы
                    showNotification.showAnalyzesAreReady(disperseAnalyzes(msg.substring(0,msg.indexOf("#"))));
                    msg=msg.substring(msg.indexOf("#")+1,msg.length());
                    if(msg.length()<=0)
                        return;
                    break;

                case "3": //акции
                    showNotification.showShare(disperseShare(msg.substring(0,msg.indexOf("#"))));
                    msg=msg.substring(msg.indexOf("#")+1,msg.length());
                    if(msg.length()<=0)
                        return;
                    break;
            }


        }
    }

    private NotificationReminderOfAdmission disperseReminderOfAdmission(String msg)
    {
        //todo разпарсить idBranch,IDuSER

        String tmp=msg;
        NotificationReminderOfAdmission not=new NotificationReminderOfAdmission();

        not.setIdNotification(tmp.substring(0,tmp.indexOf("&")));
        tmp=tmp.substring(tmp.indexOf("&")+1,tmp.length());

        not.setIdInner(tmp.substring(0,tmp.indexOf("&")));
        tmp=tmp.substring(tmp.indexOf("&")+1,tmp.length());

        not.setIdRecord(tmp.substring(0,tmp.indexOf("&")));
        tmp=tmp.substring(tmp.indexOf("&")+1,tmp.length());

        not.setTitle(tmp);

        mSocketClient.sendMessage(not.getIdNotification()+"."+not.getIdInner()+"."+RESEIVED);

        return not;
    }


    private NotificationAnalyzes disperseAnalyzes(String msg)
    {
        String tmp=msg;
        NotificationAnalyzes not=new NotificationAnalyzes();

        not.setIdNotification(tmp.substring(0,tmp.indexOf("&")));
        tmp=tmp.substring(tmp.indexOf("&")+1,tmp.length());

        not.setIdAnalizes(tmp.substring(0,tmp.indexOf("&")));
        tmp=tmp.substring(tmp.indexOf("&")+1,tmp.length());

        not.setTitle(tmp);

        mSocketClient.sendMessage(not.getIdNotification()+"."+not.getIdAnalizes()+"."+RESEIVED);

        return not;
    }

    private NotificationShares disperseShare(String msg)
    {
        String tmp=msg;
        NotificationShares not=new NotificationShares();

        not.setIdNotification(tmp.substring(0,1));  //id notification
        tmp=tmp.substring(2,tmp.length());

        not.setIdShare(tmp.substring(0,tmp.indexOf("&")));    //id share
        tmp=tmp.substring(tmp.indexOf("&")+1,tmp.length());

        not.setTitle(tmp.substring(0,tmp.indexOf("&")));    //title
        tmp=tmp.substring(tmp.indexOf("&")+1,tmp.length());

        not.setImgURL(tmp.substring(0,tmp.indexOf("&")));    //img
        tmp=tmp.substring(tmp.indexOf("&")+1,tmp.length());

        not.setDescription(tmp);    //description

        mSocketClient.sendMessage(not.getIdNotification()+"."+not.getIdShare()+"."+RESEIVED);

        return not;
    }


    public void confirmVisit(String idNotification, String idRecord)
    {
        mSocketClient.sendMessage(idNotification+"."+idRecord+"."+CONFIRMED);
    }
}
*/
