package com.medhelp.medhelp.utils.timber_log;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.androidnetworking.error.ANError;
import com.medhelp.medhelp.data.network.NetworkManager;
import com.medhelp.medhelp.data.pref.PreferencesManager;
import com.medhelp.medhelp.utils.main.TimesUtils;
import com.medhelp.medhelp.utils.rx.AppSchedulerProvider;
import com.medhelp.medhelp.utils.rx.SchedulerProvider;

import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

public class LoggingTree extends Timber.DebugTree {

    //Timber.e - for error (priority 6)
    //Timber.i - for onCreate Activity (priority 4)
    //Timber.v - verbose information about actions (priority 2)
    //Timber.d - ALERT_TYPE (priority 3)

    public static final String VERBOSE_INFORMATION="VERBOSE_INFORMATION";
    public static final String CREATE_ACTIVITY="CREATE_ACTIVITY";
    public static final String ERROR="ERROR";
    public static final String ERROR_PAYMENT="ERROR_PAYMENT";
    public static final String DEFAULT_ERROR_TYPE="DEFAULT_ERROR_TYPE";
    public static final String ALERT_TYPE="ALERT_TYPE";


    private NetworkManager networkManager;
    private SchedulerProvider schedulerProvider;

    private int versionCode;

    PreferencesManager pm;

    public LoggingTree(Context context) {
        pm=new PreferencesManager(context);
        networkManager=new NetworkManager(pm);
        schedulerProvider=new AppSchedulerProvider();

        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            int verCode = pInfo.versionCode;
            versionCode=verCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void log(int priority, String tag, String message, Throwable t) {

        if(tag==null || !tag.equals("my"))
            return;

        switch (priority) {
            case 2:
                sendLogToServer(VERBOSE_INFORMATION ,message);
                break;
            case 3:
                sendLogToServer(ALERT_TYPE ,message);
                break;
            case 4:
                sendLogToServer(CREATE_ACTIVITY ,message);
                break;
            case 6:
                sendLogToServer(ERROR ,message);
                break;
            default:
                sendLogToServer(DEFAULT_ERROR_TYPE , message);
        }
    }


    public static String getMessageForError(Throwable t,String message)
    {
        String err="";

        if(t!=null)
        {
            if (t instanceof ANError)
            {
                ANError anError = (ANError) t;

                err+="ANError ErrorDetail: ";
                err += anError.getErrorDetail();

                err+="\n";

                err+="ANError ErrorBody: ";
                err += anError.getErrorBody();

                err+="\n";
            }

            err+="Throwable message: ";
            err += t.getMessage();
        }

        return message+"\n"+err;
    }




    private void sendLogToServer(String type, String message)
    {

        if (type.equals(ERROR) && message.contains("ShoppingBasket")){
            type=ERROR_PAYMENT;
        }

//        if(pm.getCurrentUserInfo()==null  || pm.getCurrentUserInfo().getApiKey()==null ||  pm.getCurrentUserInfo().getApiKey().equals("") || pm.getCurrentUserInfo().getIdUser()==0)
//            return;

        message=message.trim();


        CompositeDisposable cd = new CompositeDisposable();
        cd.add(networkManager
                .sendLogToServer(type, message, versionCode)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(response -> {
                            //Log.wtf("mLog", response.getMessage());
                            cd.dispose();
                        }, throwable -> {
                            //Log.wtf("mLog", throwable.getMessage());
                            cd.dispose();
                        }
                )
        );
    }

    private String getCurrentTime()
    {
        return TimesUtils.longToString(System.currentTimeMillis(),TimesUtils.DATE_FORMAT_HHmmss_ddMMyyyy);
    }
}
