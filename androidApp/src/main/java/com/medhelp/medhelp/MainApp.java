package com.medhelp.medhelp;

import android.app.Application;
import android.content.IntentFilter;
import android.net.ConnectivityManager;

import androidx.appcompat.app.AppCompatDelegate;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.interceptors.HttpLoggingInterceptor;
import com.medhelp.medhelp.utils.timber_log.LoggingTree;
import timber.log.Timber;


@SuppressWarnings("unused")
public class MainApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        //костыль от ночной темы
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        AndroidNetworking.initialize(getApplicationContext());

        Timber.plant(new LoggingTree(getApplicationContext()));

//        if (BuildConfig.DEBUG)
//           AndroidNetworking.enableLogging(HttpLoggingInterceptor.Level.BODY);


        //получает сообщения о состоянии сети пока запущено приложение, так же при инициализации посылает сообщение в широковещательный приемник и запускает сервис
//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
//        registerReceiver(new BroadcastOnBootCompile(), intentFilter);
    }
}
