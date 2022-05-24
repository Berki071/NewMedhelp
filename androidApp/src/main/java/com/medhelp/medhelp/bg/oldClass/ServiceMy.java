//package com.medhelp.medhelp.bg.service;
//
//import android.content.Context;
//
//import androidx.annotation.NonNull;
//
//import android.util.Log;
//
//import com.medhelp.medhelp.bg.temporaryInsteadSocket.InsteadSocket;
//import com.medhelp.medhelp.data.pref.PreferencesManager;
//
//import androidx.work.Worker;
//import androidx.work.WorkerParameters;
//
//public class ServiceMy extends Worker {
//    private InsteadSocket insteadSocket;
//    private PreferencesManager preferencesManager;
//
//    public ServiceMy(@NonNull Context context, @NonNull WorkerParameters workerParams) {
//        super(context, workerParams);
//    }
//
//    @NonNull
//    @Override
//    public Result doWork() {
//
//        preferencesManager=new PreferencesManager(getApplicationContext());
//        String pass=preferencesManager.getCurrentPassword();
//
//        if(pass==null  || pass.equals(""))
//            return Result.failure();
//
//        insteadSocket=InsteadSocket.getInstance();
//        insteadSocket.init(getApplicationContext());
//
//       // return Worker.Result.SUCCESS;
//        return Result.success();
//    }
//}

