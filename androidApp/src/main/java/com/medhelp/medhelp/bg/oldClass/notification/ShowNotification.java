//package com.medhelp.medhelp.bg.oldClass.notification;
//
//import android.app.NotificationManager;
//import android.content.Context;
//
//import com.medhelp.medhelp.R;
//import com.medhelp.medhelp.bg.oldClass.notification.notiItem.AnalysisNoti;
//import com.medhelp.medhelp.bg.oldClass.notification.notiItem.ChatNoti;
//import com.medhelp.medhelp.bg.oldClass.notification.notiItem.ReminderAdmNoti;
//import com.medhelp.medhelp.bg.oldClass.notification.notiItem.ShareNoti;
//import com.medhelp.medhelp.data.db.RealmHelper;
//import com.medhelp.medhelp.data.db.RealmManager;
//import com.medhelp.medhelp.data.model.chat.InfoAboutDoc;
//import com.medhelp.medhelp.data.model.chat.MessageFromServer;
//import com.medhelp.medhelp.data.model.notification.NotificationMsg;
//import com.medhelp.medhelp.data.pref.PreferencesHelper;
//import com.medhelp.medhelp.data.pref.PreferencesManager;
//import com.medhelp.medhelp.utils.rx.AppSchedulerProvider;
//import com.medhelp.medhelp.utils.rx.SchedulerProvider;
//import com.medhelp.medhelp.utils.timber_log.LoggingTree;
//
//import java.util.List;
//
//import io.reactivex.disposables.CompositeDisposable;
//import timber.log.Timber;
//
//public class ShowNotification {
//
//
//    private NotificationManager notificationManager;
//    private Context context;
//    private RealmHelper realmHelper;
//    private PreferencesHelper preferencesHelper;
//
//    public static final String CHANNEL_ID = "STREAM_DEFAULT";
//
//    private SchedulerProvider schedulerProvider;
//    public static final int ICON = R.drawable.kl_icon;
//
//    public static final int ID_CHAT_GROUP = -111;
//    public static final int ID_SHARE_GROUP = -222;
//    public static final int ID_REMINDER_GROUP = -333;
//    public static final int ID_ANALIZ_GROUP = -444;
//
//    String center;
//
//
//    public ShowNotification(Context context, NotificationManager notificationManager) {
//        this.context = context;
//        this.notificationManager = notificationManager;
//
//        schedulerProvider = new AppSchedulerProvider();
//        realmHelper = new RealmManager(this.context);
//        preferencesHelper = new PreferencesManager(context);
//        center = preferencesHelper.getCenterInfo().getTitle();
//    }
//
//    private void showMsgChat(InfoAboutDoc info, MessageFromServer msg) {
//        new ChatNoti(context, center, notificationManager, info, msg, getNotificationId());
//    }
//
//    public void showReminderOfAdmission(NotificationMsg notificationReminderOfAdmission) {
//        new ReminderAdmNoti(context, center, notificationManager, notificationReminderOfAdmission, getNotificationId());
//    }
//
//    public void showShare(NotificationMsg notificationShares) {
//        new ShareNoti(context, center, notificationManager, notificationShares, getNotificationId(), preferencesHelper.getCurrentUserInfo().getApiKey());
//    }
//
//    public void showAnalyzesAreReady(NotificationMsg notificationAnalyzes) {
//        new AnalysisNoti(context, center, notificationManager, notificationAnalyzes, getNotificationId());
//    }
//
//
//    private int notificationId = 1;
//
//    private int getNotificationId() {
//        notificationId++;
//
//        if (notificationId > 1000)
//            notificationId = 1;
//
//        return notificationId;
//    }
//
//
//    public void showNotificationMessage(List<MessageFromServer> list, long currentRoom) {
//        for (MessageFromServer msg : list) {
//
//            if (msg.getIdRoom() == currentRoom)
//                continue;
//
//            CompositeDisposable cd = new CompositeDisposable();
//            cd.add(realmHelper
//                    .getInfoAboutOneDoc(msg.getIdRoom())
//                    .subscribeOn(schedulerProvider.io())
//                    .observeOn(schedulerProvider.ui())
//                    .subscribe(responce -> {
//                                showMsgChat(responce, msg);
//                                cd.dispose();
//                            }, throwable -> {
//                                Timber.tag("my").e(LoggingTree.getMessageForError(throwable, "ShowNotification$showNotificationMessage "));
//                                cd.dispose();
//                            }
//                    )
//            );
//        }
//    }
//}
