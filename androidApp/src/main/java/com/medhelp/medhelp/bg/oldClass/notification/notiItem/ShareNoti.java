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
//import com.medhelp.medhelp.utils.TimesUtils;
//
//import static com.medhelp.medhelp.ui._main_page.MainActivity.POINTER_TO_PAGE;
//
//public class ShareNoti {
//
//    private int idNoti;
//    private NotificationMsg notificationShares;
//    private NotificationManager notificationManager;
//    private String center;
//    private Context context;
//
//    private Uri sound;
//    private PendingIntent pendingIntent;
//    private String GROUP_KEY_SHARE = "com.medhelp.medhelp.Medelp.Msg.Share";
//    String token;
//
//    public ShareNoti(Context context, String center, NotificationManager notificationManager, NotificationMsg notificationShares, int idNoti, String token) {
//        this.notificationShares = notificationShares;
//        this.notificationManager = notificationManager;
//        this.center = center;
//        this.context = context;
//        this.idNoti = idNoti;
//        this.token = token;
////
////        sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
////
////        Intent intent = new Intent(context, MainActivity.class);
////        intent.putExtra(POINTER_TO_PAGE, Constants.MENU_SALE);
////        pendingIntent = PendingIntent.getActivity(context, idNoti, intent, PendingIntent.FLAG_IMMUTABLE);
////
////        long dateNoti= TimesUtils.stringToLong(notificationShares.getDate(),TimesUtils.DATE_FORMAT_ddMMyyyy);
////        long dateCurrent=TimesUtils.longToNewFormatLong(System.currentTimeMillis()-(1000*60*60*24*2),TimesUtils.DATE_FORMAT_ddMMyyyy);
////        //String s1=TimesUtils.longToString(dateNoti);
////        //String s3=TimesUtils.longToString(dateCurrent);
////        if(dateCurrent>dateNoti)
////            return;
////
////        showNoti();
//    }
//
////    private void showNoti() {
////        //для поддержки версии 8 и выше
////        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
////            // Create the NotificationChannel, but only on API 26+ because
////            // the NotificationChannel class is new and not in the support library
////            CharSequence name = "channel_name";
////            String description = "channel_description";
////            int importance = NotificationManager.IMPORTANCE_DEFAULT;
////            NotificationChannel channel = new NotificationChannel(ShowNotification.CHANNEL_ID + 2, name, importance);
////            channel.setDescription(description);
////
////            // Register the channel with the system
////            assert notificationManager != null;
////            notificationManager.createNotificationChannel(channel);
////        }
////
////        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, ShowNotification.CHANNEL_ID + 2)
////                .setSmallIcon(ShowNotification.ICON)
////                .setContentTitle(center)
////                .setContentText(notificationShares.getMessage())
////                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
////                // .setStyle(new NotificationCompat.BigTextStyle().bigText(Html.fromHtml("<b>"+notificationShares.getTitle()+"</b><br>"+notificationShares.getDescription())))
////                .setStyle(new NotificationCompat.BigTextStyle().bigText(notificationShares.getMessage()))
////                .setContentIntent(pendingIntent)
////                .setAutoCancel(true)
////                .setGroup(GROUP_KEY_SHARE)
////                .setSound(sound);
////
//// /*
////        если надо будет картинку
////        Bitmap bm= getBitmap(notificationShares.getImgURL());
////        if(bm!=null)
////        {
////            mBuilder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bm).bigLargeIcon(null));
////        }*/
////
////        Notification notiGroup =
////                new NotificationCompat.Builder(context, ShowNotification.CHANNEL_ID)
////                        .setSmallIcon(ShowNotification.ICON)
////                        .setAutoCancel(true)
////                        .setGroup(GROUP_KEY_SHARE)
////                        .setGroupSummary(true)
////                        .build();
////
////
////        //показ
////        assert notificationManager != null;
////        notificationManager.notify(idNoti, mBuilder.build());
////        notificationManager.notify(ShowNotification.ID_SHARE_GROUP, notiGroup);
////    }
//
///*    private Bitmap getBitmap(String path) {
//        Bitmap bmp = null;
//        try {
//            URL url = new URL(path + "&token=" + token);
//            bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
//        } catch (MalformedURLException e) {
//            Timber.tag("my").e(e);
//        } catch (IOException e) {
//            Timber.tag("my").e(e);
//        }
//
//        return bmp;
//    }*/
//}
