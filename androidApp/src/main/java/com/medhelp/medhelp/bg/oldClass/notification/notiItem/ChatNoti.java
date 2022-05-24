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
//import android.text.Html;
//
//import com.medhelp.medhelp.bg.oldClass.notification.ShowNotification;
//import com.medhelp.medhelp.data.model.chat.InfoAboutDoc;
//import com.medhelp.medhelp.data.model.chat.MessageFromServer;
//import com.medhelp.medhelp.ui._chat.room.RoomActivity;
//import com.medhelp.medhelp.utils.main.MainUtils;
//
//public class ChatNoti {
//    private int idNoti;
//    private MessageFromServer msg;
//    private InfoAboutDoc info;
//    private NotificationManager notificationManager;
//    private String center;
//    private Context context;
//
//    private Uri sound;
//    PendingIntent pendingIntent;
//    String type;
//
//    private String GROUP_KEY_CHAT= "com.medhelp.medhelp.Medelp.Msg.CHAT";
//
//
//
//    public ChatNoti(Context context, String center,NotificationManager notificationManager,InfoAboutDoc info,MessageFromServer msg, int idNoti)
//    {
//        this.msg=msg;
//        this.info=info;
//        this.notificationManager=notificationManager;
//        this.center=center;
//        this.context=context;
//        this.idNoti=idNoti;
//
//        sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//
//        Intent intent = new Intent(context, RoomActivity.class);
//        intent.putExtra(InfoAboutDoc.class.getCanonicalName(),info);
//        pendingIntent = PendingIntent.getActivity(context, idNoti, intent, PendingIntent.FLAG_IMMUTABLE);
//
//        type=getType();
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
//            NotificationChannel channel = new NotificationChannel(ShowNotification.CHANNEL_ID, name, importance);
//            channel.setDescription(description);
//
//            // Register the channel with the system
//            assert notificationManager != null;
//            notificationManager.createNotificationChannel(channel);
//        }
//
//        Notification mBuilder = new NotificationCompat.Builder(context, ShowNotification.CHANNEL_ID)
//                .setSmallIcon(ShowNotification.ICON)
//                .setContentTitle(center)
//                .setContentText(Html.fromHtml("<b>"+info.getName()+"</b>: "+ type))
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                .setStyle(new NotificationCompat.BigTextStyle().bigText(Html.fromHtml("<b>"+info.getName()+"</b>: "+ type)))
//                .setContentIntent(pendingIntent)
//                .setAutoCancel(true) //удаляет нотификацию после нажатия на нее
//                .setGroup(GROUP_KEY_CHAT)
//                .setSound(sound)
//                .build();
//
//        //показ
//        assert notificationManager != null;
//
//        Notification notiGroup =
//                new NotificationCompat.Builder(context, ShowNotification.CHANNEL_ID)
//                        .setSmallIcon(ShowNotification.ICON)
//                        .setAutoCancel(true)
//                        .setGroup(GROUP_KEY_CHAT)
//                        .setGroupSummary(true)
//                        .build();
//
//        notificationManager.notify(idNoti, mBuilder);
//        notificationManager.notify(ShowNotification.ID_CHAT_GROUP, notiGroup);
//    }
//
//    private String getType()
//    {
//        if(msg.getType().equals(MainUtils.TEXT))
//        {
//            return msg.getMsg();
//        }
//        else if(msg.getType().equals(MainUtils.IMAGE))
//        {
//            return "Изображение";
//        }
//        else
//        {
//            return "Файл";
//        }
//    }
//}
