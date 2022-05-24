package com.medhelp.medhelp.bg.notifiaction;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.medhelp.medhelp.R;
import com.medhelp.medhelp.ui.splash.SplashActivity;

public class SimpleNotificationForFCM {
    public static final String CHANNEL_ID="STREAM_DEFAULT";

    int idNoti=111;
    private NotificationManager notificationManager;
    private Context context;


    private Uri sound;
    PendingIntent pendingIntent;
    private String GROUP_KEY_CHAT= "com.medhelp.medhelp.Medhelp.Msg.CHAT";


    public SimpleNotificationForFCM(Context context, NotificationManager notificationManager)
    {
        this.notificationManager=notificationManager;
        this.context=context;
    }

    public void showData(String title, String msd, String type_message, String id_kl, String id_filial) {
        sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Intent intent = new Intent(context, SplashActivity.class);
        intent.putExtra("type_message",type_message);
        intent.putExtra("id_kl",id_kl);
        intent.putExtra("id_filial",id_filial);
        pendingIntent = PendingIntent.getActivity(context, idNoti, intent, PendingIntent.FLAG_IMMUTABLE);

        showNoti(title,msd);
    }


    private void showNoti(String title, String msg)
    {
        //для поддержки версии 8 и выше
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel, but only on API 26+ because
            // the NotificationChannel class is new and not in the support library
            CharSequence name = "channel_name";
            String description = "channel_description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            // Register the channel with the system
            assert notificationManager != null;
            notificationManager.createNotificationChannel(channel);
        }

        Notification mBuilder = new NotificationCompat.Builder(context,CHANNEL_ID)
                .setSmallIcon(R.drawable.kl_icon)
                .setContentTitle(title)
                .setContentText(msg)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // .setStyle(new NotificationCompat.BigTextStyle().bigText(Html.fromHtml("<b>"+info.getName()+"</b>: "+ type)))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true) //удаляет нотификацию после нажатия на нее
                .setGroup(GROUP_KEY_CHAT)
                .setSound(sound)
                .build();

        //показ
        assert notificationManager != null;

        notificationManager.notify(idNoti, mBuilder);
    }
}

