//package com.medhelp.medhelp.bg.oldClass.notification.notiItem;
//
//import android.app.Notification;
//import android.app.NotificationChannel;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.content.Context;
//import android.content.Intent;
//import android.media.RingtoneManager;
//import android.net.Uri;
//import android.os.Build;
//import androidx.core.app.NotificationCompat;
//
//import com.medhelp.medhelp.Constants;
//import com.medhelp.medhelp.bg.oldClass.notification.ShowNotification;
//import com.medhelp.medhelp.data.model.notification.NotificationMsg;
//import com.medhelp.medhelp.ui._main_page.MainActivity;
//import com.medhelp.medhelp.utils.main.TimesUtils;
//
//import static com.medhelp.medhelp.ui._main_page.MainActivity.POINTER_TO_PAGE;
//
//public class AnalysisNoti {
//    private int idNoti;
//    NotificationMsg notificationAnalyzes;
//    private NotificationManager notificationManager;
//    private String center;
//    private Context context;
//
//    private Uri sound;
//    PendingIntent pendingIntent;
//    String type;
//
//    private String GROUP_KEY_Analysis = "com.medhelp.medhelp.Medelp.Msg.Analiz";
//
//
//    public AnalysisNoti(Context context, String center, NotificationManager notificationManager, NotificationMsg notificationAnalyzes, int idNoti)
//    {
//        this.notificationAnalyzes=notificationAnalyzes;
//        this.notificationManager=notificationManager;
//        this.center=center;
//        this.context=context;
//        this.idNoti=idNoti;
//
//        sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//
//        Intent intent = new Intent(context, MainActivity.class);
//        intent.putExtra(POINTER_TO_PAGE, Constants.MENU_RESULT_ANALISES);
//        pendingIntent = PendingIntent.getActivity(context, idNoti, intent, PendingIntent.FLAG_IMMUTABLE);
//
//        long dateNoti= TimesUtils.stringToLong(notificationAnalyzes.getDate(),TimesUtils.DATE_FORMAT_ddMMyyyy);
//        long dateCurrent_pp =TimesUtils.longToNewFormatLong(System.currentTimeMillis()-(1000*60*60*24*2),TimesUtils.DATE_FORMAT_ddMMyyyy);
//        if(dateCurrent_pp >dateNoti)
//            return;
//
//        showNoti();
//    }
//
//    private void showNoti()
//    {
//        //для поддержки версии 8 и выше
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            // Create the NotificationChannel, but only on API 26+ because
//            // the NotificationChannel class is new and not in the support library
//            CharSequence name = "channel_name";
//            String description = "channel_description";
//            int importance = NotificationManager.IMPORTANCE_DEFAULT;
//            NotificationChannel channel = new NotificationChannel(ShowNotification.CHANNEL_ID+3, name, importance);
//            channel.setDescription(description);
//
//            // Register the channel with the system
//            assert notificationManager != null;
//            notificationManager.createNotificationChannel(channel);
//        }
//
//
//        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, ShowNotification.CHANNEL_ID+3)
//                .setSmallIcon(ShowNotification.ICON)
//                .setContentTitle(center)
//                .setContentText(notificationAnalyzes.getMessage())
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                .setStyle(new NotificationCompat.BigTextStyle().bigText(notificationAnalyzes.getMessage()))
//                .setContentIntent(pendingIntent)
//                .setAutoCancel(true) //удаляет нотификацию после нажатия на нее
//                .setGroup(GROUP_KEY_Analysis)
//                .setSound(sound)
//                ;
//
//        //показ
//        assert notificationManager != null;
//
//        Notification notiGroup =
//                new NotificationCompat.Builder(context, ShowNotification.CHANNEL_ID)
//                        .setSmallIcon(ShowNotification.ICON)
//                        .setAutoCancel(true)
//                        .setGroup(GROUP_KEY_Analysis)
//                        .setGroupSummary(true)
//                        .build();
//
//        notificationManager.notify(idNoti, mBuilder.build());
//        notificationManager.notify(ShowNotification.ID_ANALIZ_GROUP, notiGroup);
//    }
//}
