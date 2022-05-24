//package com.medhelp.medhelp.bg.broadcast_reсeiver;
//
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//
//import com.medhelp.medhelp.bg.service.ServiceMy;
//
//import java.util.concurrent.TimeUnit;
//
//import androidx.work.Configuration;
//import androidx.work.Constraints;
//import androidx.work.NetworkType;
//import androidx.work.PeriodicWorkRequest;
//import androidx.work.WorkManager;
//
//public class BroadcastOnBootCompile extends BroadcastReceiver {
//
//    @Override
//    public void onReceive(Context context, Intent intent) {
//        {
//            Constraints constraints = new Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build();
//
//            PeriodicWorkRequest locationWork = new PeriodicWorkRequest.Builder(ServiceMy.class, 1, TimeUnit.MINUTES).addTag("mTaggg")
//                    .setConstraints(constraints).build();
//
//            WorkManager wm;
//            try {
//                wm = WorkManager.getInstance();
//            } catch (Exception e) {
//                WorkManager.initialize(context, new Configuration.Builder().build());
//                wm = WorkManager.getInstance(context);
//            }
//            wm.enqueue(locationWork);
//        }
//    }
//
//}
//
