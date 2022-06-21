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
//import com.medhelp.medhelp.R;
//import com.medhelp.medhelp.bg.oldClass.notification.BroadcastClickBtnFromNotification;
//import com.medhelp.medhelp.bg.oldClass.notification.ShowNotification;
//import com.medhelp.medhelp.data.model.notification.NotificationMsg;
//import com.medhelp.medhelp.ui.profile.ProfileFragment;
//import com.medhelp.medhelp.utils.TimesUtils;
//
//public class ReminderAdmNoti {
//
//    public static final String BTN_CONFIRM = "btnConfirm";
//    public static final String BTN_REFUSE = "btnRefuse";
//    public static final String KEY_CLICK_BTN = "KEY_CLICK_BTN";
//    public static final String KEY_NOTIFICATION = "KEY_NOTIFICATION";
//    public static final String KEY_RECORD = "KEY_RECORD";
//    public static final String ID_RECORD = "ID_RECORD";
//    public static final String ID_BRANCH = "ID_BRANCH";
//    public static final String ID_USER = "ID_USER";
//
//    private int idNoti;
//    NotificationMsg notificationReminderOfAdmission;
//    private NotificationManager notificationManager;
//    private String center;
//    private Context context;
//    PendingIntent pendingIntent1;
//    PendingIntent pendingIntent2;
//    PendingIntent pendingIntent3;
//
//    private Uri sound;
//
//    private String GROUP_KEY_Reminder= "com.medhelp.medhelp.Medelp.Msg.Reminder";
//
//    public ReminderAdmNoti(Context context, String center, NotificationManager notificationManager, NotificationMsg notificationReminderOfAdmission, int idNoti)
//    {
//        this.notificationReminderOfAdmission=notificationReminderOfAdmission;
//        this.notificationManager=notificationManager;
//        this.center=center;
//        this.context=context;
//        this.idNoti=idNoti;
//
//        sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//
//        Intent it=new Intent(context,BroadcastClickBtnFromNotification.class);
//        it.setAction(context.getResources().getString(R.string.key));
//        it.putExtra(KEY_CLICK_BTN,BTN_CONFIRM);
//        it.putExtra(KEY_NOTIFICATION, idNoti);
//        it.putExtra(ID_USER,notificationReminderOfAdmission.getIdUser());
//        it.putExtra(ID_BRANCH,notificationReminderOfAdmission.getIdBranch());
//        it.putExtra(KEY_RECORD,notificationReminderOfAdmission.getIdRecords());
//        pendingIntent1=PendingIntent.getBroadcast(context,idNoti,it,PendingIntent.FLAG_IMMUTABLE);
//
//        Intent it2=new Intent(context,BroadcastClickBtnFromNotification.class);
//        it2.setAction(context.getResources().getString(R.string.key));
//        it2.putExtra(KEY_CLICK_BTN,BTN_REFUSE);
//        it2.putExtra(KEY_NOTIFICATION, idNoti);
//        it2.putExtra(ID_RECORD,notificationReminderOfAdmission.getIdRecords());
//        it2.putExtra(ID_USER,notificationReminderOfAdmission.getIdUser());
//        it2.putExtra(ID_BRANCH,notificationReminderOfAdmission.getIdBranch());
//        pendingIntent2=PendingIntent.getBroadcast(context,idNoti+100,it2,PendingIntent.FLAG_IMMUTABLE);
//
//        Intent intent = new Intent(context, ProfileFragment.class);
//        pendingIntent3 = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);
//
//        long dateNoti= TimesUtils.stringToLong(notificationReminderOfAdmission.getDate(),TimesUtils.DATE_FORMAT_ddMMyyyy);
//        long dateCurrent=TimesUtils.longToNewFormatLong(System.currentTimeMillis(),TimesUtils.DATE_FORMAT_ddMMyyyy);
//        if(dateCurrent==dateNoti  || (dateCurrent+(1000*60*60*24))==dateNoti) {
//            showNoti();
//        }
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
//            NotificationChannel channel = new NotificationChannel(ShowNotification.CHANNEL_ID+1, name, importance);
//            channel.setDescription(description);
//
//            // Register the channel with the system
//            assert notificationManager != null;
//            notificationManager.createNotificationChannel(channel);
//        }
//
//        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, ShowNotification.CHANNEL_ID)
//                .setSmallIcon(ShowNotification.ICON)
//                .setContentTitle(center)
//                .setContentText(notificationReminderOfAdmission.getMessage())
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                .setStyle(new NotificationCompat.BigTextStyle().bigText(notificationReminderOfAdmission.getMessage()))
//                .setContentIntent(pendingIntent3)
//                .setAutoCancel(true) //удаляет нотификацию после нажатия на нее
//                .addAction(R.drawable.ic_close_red_600_18dp,context.getString(R.string.refuse),pendingIntent2)
//                .addAction(R.drawable.ic_done_green_500_36dp,context.getString(R.string.txt_confirm),pendingIntent1)
//                .setGroup(GROUP_KEY_Reminder)
//                .setSound(sound);
//
//        assert notificationManager != null;
//
//        Notification notiGroup =
//                new NotificationCompat.Builder(context, ShowNotification.CHANNEL_ID)
//                        .setSmallIcon(ShowNotification.ICON)
//                        .setAutoCancel(true)
//                        .setGroup(GROUP_KEY_Reminder)
//                        .setGroupSummary(true)
//                        .build();
//
//
//        notificationManager.notify(idNoti, mBuilder.build());
//        notificationManager.notify(ShowNotification.ID_REMINDER_GROUP, notiGroup);
//    }
//}
