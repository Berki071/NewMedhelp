package com.medhelp.medhelp.ui.video_consultation.video_chat.noti;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import com.medhelp.medhelp.R;
import com.medhelp.medhelp.data.model.VisitResponse;

//public class MyNotificationVideoChat {
//
//    private Context context;
//    private String companionid;
//    private NotificationManager notificationManager;
//    private String callerName;
//
//
//    public static final String CHANNEL_ID="STREAM_DEFAULT";
//    public static final int ID_NOTI_FOR_VIDEO_CALL=1;
//    private String GROUP_KEY_Reminder= "com.medhelp.medhelp.videochaaa2";
//
//    public MyNotificationVideoChat(Context context, String companionid, String callerName)
//    {
//        this.context=context;
//        this.companionid=companionid;
//        this.callerName=callerName;
//        notificationManager= (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        showNotificationWait();
//    }
//
//    public MyNotificationVideoChat(Context context, VisitResponse data)
//    {
//        this.context=context;
//        notificationManager= (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        showNotificationInvite(data);
//    }
//
//    private void showNotificationWait() {
//
//        Intent intBtnOpen = new Intent(context, NotificationVideoChatButtonListener.class);
//        intBtnOpen.putExtra("companionid",companionid);
//        intBtnOpen.setAction(NotificationVideoChatButtonListener.ACTION_GO_TO_VIDEO);
//        PendingIntent pendingMyOpen = PendingIntent.getBroadcast(context, 1, intBtnOpen, PendingIntent.FLAG_IMMUTABLE);
//
//        Intent intBtnClose=new Intent(context, NotificationVideoChatButtonListener.class);
//        intBtnClose.setAction(NotificationVideoChatButtonListener.ACTION_CLOSE);
//        intBtnClose.putExtra("companionid",companionid);
//        PendingIntent pendingMyClose = PendingIntent.getBroadcast(context, 1, intBtnClose,PendingIntent.FLAG_IMMUTABLE);
//
//        //для поддержки версии 8 и выше
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            CharSequence name = "channel_name";
//            String description = "channel_description";
//            int importance = NotificationManager.IMPORTANCE_HIGH;
//            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
//            channel.setDescription(description);
//
//            assert notificationManager != null;
//            notificationManager.createNotificationChannel(channel);
//        }
//
//        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context,CHANNEL_ID)
//                .setSmallIcon(R.drawable.kl_icon)
//                .setContentTitle("Входящий звонок")
//                .setContentText(callerName)
//                .setAutoCancel(true)
//                .setPriority(NotificationCompat.PRIORITY_MAX)
//                //.setStyle(new NotificationCompat.BigTextStyle().bigText("Необходимо перейти на страницу видеозвонка и ожидать подключение врача"))
//                .addAction(R.drawable.ic_done_green_500_36dp,"Ответить",pendingMyOpen)
//                .addAction(R.drawable.ic_close_red_600_18dp,"Отклонить",pendingMyClose)
//                .setOngoing(true)   //нельзя смахнуть
//                .setGroup(GROUP_KEY_Reminder);
//
//        assert notificationManager != null;
//
//        notificationManager.notify(ID_NOTI_FOR_VIDEO_CALL, mBuilder.build());
//    }
//
//    private void showNotificationInvite(VisitResponse data)
//    {
//        Intent intBtnOpen = new Intent(context, NotificationVideoChatButtonListener.class);
//        intBtnOpen.putExtra(VisitResponse.class.getCanonicalName(),data);
//        intBtnOpen.setAction(NotificationVideoChatButtonListener.ACTION_GO_TO_VIDEO_WAIT);
//        PendingIntent pendingMyOpen = PendingIntent.getBroadcast(context, 1, intBtnOpen, PendingIntent.FLAG_IMMUTABLE);
//
//        Intent intBtnClose=new Intent(context, NotificationVideoChatButtonListener.class);
//        intBtnClose.setAction(NotificationVideoChatButtonListener.ACTION_CLOSE_NOTI_ONLY);
//        PendingIntent pendingMyClose = PendingIntent.getBroadcast(context, 1, intBtnClose, PendingIntent.FLAG_IMMUTABLE);
//
//
//        //для поддержки версии 8 и выше
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            CharSequence name = "channel_name";
//            String description = "channel_description";
//            int importance = NotificationManager.IMPORTANCE_HIGH;
//            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
//            channel.setDescription(description);
//
//            assert notificationManager != null;
//            notificationManager.createNotificationChannel(channel);
//        }
//
//        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context,CHANNEL_ID)
//                .setSmallIcon(R.drawable.kl_icon)
//                .setContentTitle("Входящий звонок")
//                .setAutoCancel(true)
//                .setPriority(NotificationCompat.PRIORITY_MAX)
//                .setStyle(new NotificationCompat.BigTextStyle().bigText("Необходимо перейти на страницу видеозвонка и ожидать подключение врача"))
//                .addAction(R.drawable.ic_done_green_500_36dp,"Перейти",pendingMyOpen)
//                .addAction(R.drawable.ic_close_red_600_18dp,"Отмена",pendingMyClose)
//                .setOngoing(true)   //нельзя смахнуть
//                .setGroup(GROUP_KEY_Reminder);
//
//        assert notificationManager != null;
//
//        notificationManager.notify(ID_NOTI_FOR_VIDEO_CALL, mBuilder.build());
//    }
//
//
//}
