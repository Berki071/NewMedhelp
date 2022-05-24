//package com.medhelp.medhelp.bg.oldClass.notification;
//
//import android.app.NotificationManager;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Build;
//import android.os.Handler;
//import android.service.notification.StatusBarNotification;
//
//import com.medhelp.medhelp.bg.oldClass.notification.notiItem.ReminderAdmNoti;
//import com.medhelp.medhelp.data.network.NetworkHelper;
//import com.medhelp.medhelp.data.network.NetworkManager;
//import com.medhelp.medhelp.data.pref.PreferencesManager;
//import com.medhelp.medhelp.ui._main_page.MainActivity;
//import com.medhelp.medhelp.utils.rx.AppSchedulerProvider;
//import com.medhelp.medhelp.utils.rx.SchedulerProvider;
//import com.medhelp.medhelp.utils.timber_log.LoggingTree;
//
//import java.util.Objects;
//
//import io.reactivex.disposables.CompositeDisposable;
//import timber.log.Timber;
//
//import static com.medhelp.medhelp.bg.oldClass.notification.ShowNotification.ID_REMINDER_GROUP;
//
//public class BroadcastClickBtnFromNotification extends BroadcastReceiver {
//   // private ControlToSocket controlToSocket;
//    private Handler handler;
//
//
//    @Override
//    public void onReceive(Context context, Intent intent) {
//        try {
//            String ss = Objects.requireNonNull(intent.getExtras()).getString(ReminderAdmNoti.KEY_CLICK_BTN);
//
//            int keyId=intent.getExtras().getInt(ReminderAdmNoti.KEY_NOTIFICATION);
//            deleteNotification(context,keyId);
//
//
//
//            if(ss.equals(ReminderAdmNoti.BTN_CONFIRM))
//            {
//                PreferencesManager prefManager=new PreferencesManager(context);
//                NetworkHelper networkManager=new NetworkManager(prefManager);
//
//                int idUser=intent.getExtras().getInt(ReminderAdmNoti.ID_USER);
//                int idBranch=intent.getExtras().getInt(ReminderAdmNoti.ID_BRANCH);
//                int id_record =intent.getExtras().getInt(ReminderAdmNoti.KEY_RECORD);
//                SchedulerProvider schedulerProvider=new AppSchedulerProvider();
//
//                new CompositeDisposable().add(networkManager
//                        .sendConfirmationOfVisit(idUser, id_record, idBranch)
//                        .subscribeOn(schedulerProvider.io())
//                        .observeOn(schedulerProvider.ui())
//                        .subscribe(response ->
//                                {
//                                    Timber.tag("my").v("Подтвердение приема, id записи "+ id_record);
//                                }
//                                ,throwable -> {
//                                    Timber.tag("my").e(LoggingTree.getMessageForError(throwable,"BroadcastClickBtnFromNotification$onReceive$1 "));
//
//                                    Runnable runnable= () -> onReceive(context,intent);
//                                    handler.postDelayed(runnable,10*1000);
//                                })
//                );
//            }
//
//            if(ss.equals(ReminderAdmNoti.BTN_REFUSE))
//            {
//                int idUser=intent.getExtras().getInt(ReminderAdmNoti.ID_USER);
//                int idBranch=intent.getExtras().getInt(ReminderAdmNoti.ID_BRANCH);
//                int idRecord= intent.getExtras().getInt(ReminderAdmNoti.ID_RECORD);
//
//                Intent mIntent=new Intent(context, MainActivity.class);
//                mIntent.putExtra(ReminderAdmNoti.ID_USER,idUser);
//                mIntent.putExtra(ReminderAdmNoti.ID_BRANCH,idBranch);
//                mIntent.putExtra(ReminderAdmNoti.ID_RECORD,idRecord);
//                context.startActivity(mIntent);
//            }
//
//        }catch (Exception e)
//        {
//            Timber.tag("my").e(LoggingTree.getMessageForError(e,"BroadcastClickBtnFromNotification$onReceive$2"));
//        }
//    }
//
//    private void deleteNotification(Context context, int id)
//    {
//        NotificationManager nm = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
//        nm.cancel(id);
//        StatusBarNotification[]mas=null;
//
//        // когда есть несколько уведомлений с кнопками в группе, то при нажатии на кнопки провереть надо ли убать заголовок группы надо вручную
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            mas= nm.getActiveNotifications();
//            Boolean isDeleteGrope=true;
//            for(StatusBarNotification item : mas)
//            {
//                if(item.getId()!=ID_REMINDER_GROUP  &&  item.getId()!=id)
//                {
//                    isDeleteGrope=false;
//                    break;
//                }
//            }
//
//            if(isDeleteGrope)
//            {
//                nm.cancel(ID_REMINDER_GROUP);
//            }
//        }
//    }
//}
