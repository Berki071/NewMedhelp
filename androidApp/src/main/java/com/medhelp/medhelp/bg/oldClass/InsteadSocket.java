//package com.medhelp.medhelp.bg.temporaryInsteadSocket;
//
//import android.app.NotificationManager;
//import android.content.Context;
//import android.os.Handler;
//import android.os.Looper;
//
//import com.androidnetworking.error.ANError;
//import com.medhelp.medhelp.bg.oldClass.notification.ShowNotification;
//import com.medhelp.medhelp.bg.temporaryInsteadSocket.external_message.TestExternalMessage;
//import com.medhelp.medhelp.bg.oldClass.SendImageByService;
//import com.medhelp.medhelp.bg.temporaryInsteadSocket.sendMsg.SendMessageInterface;
//import com.medhelp.medhelp.bg.temporaryInsteadSocket.sendMsg.SendText;
//import com.medhelp.medhelp.data.DataHelper;
//import com.medhelp.medhelp.data.DataManager;
//import com.medhelp.medhelp.data.db.RealmHelper;
//import com.medhelp.medhelp.data.db.RealmManager;
//import com.medhelp.medhelp.data.model.chat.MessageFromServer;
//import com.medhelp.medhelp.data.model.notification.NotificationMsg;
//import com.medhelp.medhelp.data.network.NetworkHelper;
//import com.medhelp.medhelp.data.network.NetworkManager;
//import com.medhelp.medhelp.data.pref.PreferencesManager;
//import com.medhelp.medhelp.utils.workToFile.convert_Base64.ConvertBase64;
//import com.medhelp.medhelp.utils.main.MainUtils;
//import com.medhelp.medhelp.utils.rx.AppSchedulerProvider;
//import com.medhelp.medhelp.utils.rx.SchedulerProvider;
//import com.medhelp.medhelp.utils.timber_log.LoggingTree;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import io.reactivex.disposables.CompositeDisposable;
//import timber.log.Timber;
//
//public class InsteadSocket implements SendMessageInterface, TestExternalMessage.ExternalMessageListener {
//    //region singleton init
//    private static final /*volatile*/ InsteadSocket instance = new InsteadSocket();
//
//    private InsteadSocket() {
//    }
//
//    public synchronized static InsteadSocket getInstance() {
//        return instance;
//    }
//
//    private long currentTime;
//
//    public synchronized void init(Context context) {
//        this.context = context;
//
//        if (!latchIsStartTimer) {
//            latchIsStartTimer = true;
//            PreferencesManager prefManager = new PreferencesManager(context);
//            NetworkHelper networkManager = new NetworkManager(prefManager);
//            RealmHelper realmManager = new RealmManager(context);
//            dataHelper = new DataManager(context, prefManager, networkManager, realmManager);
//
//            schedulerProvider = new AppSchedulerProvider();
//
//            externalMessage = new TestExternalMessage(context, dataHelper, this);
//
//            showNotification = new ShowNotification(context, (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE));
//
//            //handler=new Handler();
//            handler = new Handler(Looper.getMainLooper());
//
//            convertBase64 = new ConvertBase64();
//
//
//            //Log.wtf("mLogTest","latchNotification = true init");
//            latchNotification = latchExternalMsg = latchNoRead = true;
//
//            startTimer();
//        } else {
//            long nTime = System.currentTimeMillis();
//            if (nTime - currentTime > timeRefreshNoti) {
//                //Log.wtf("mLogTest","latchNotification = true init2");
//                latchNotification = latchExternalMsg = latchNoRead = true;
//                startTimer();
//            }
//        }
//    }
//    //endregion
//
//
//    private boolean latchIsStartTimer = false;
//    private Handler handler;
//    private DataHelper dataHelper;
//    private Context context;
//    private SchedulerProvider schedulerProvider;
//    private InsteadSocketListener insteadSocketListener;
//
//    private TestExternalMessage externalMessage;
//
//    private boolean latchNotification = true;
//    private boolean latchExternalMsg = true;
//    private boolean latchNoRead = true;
//
//    private ShowNotification showNotification;
//
//    private ConvertBase64 convertBase64;
//
//    private final int timeRefreshMsg = 5 * 1000;
//    private final int timeRefreshNoti = 60 * 1000;
//
//    private void startTimer() {
//        //Log.wtf("mLogTimer", );
//
//        if (!latchIsStartTimer) {
//            return;
//        }
//
//        currentTime = System.currentTimeMillis();
//
//        //чат
//        if (!latchExternalMsg || !latchNoRead)
//            return;
//        else {
//            latchExternalMsg = latchNoRead = false;
//            Runnable runnable = () -> {
//                testNoReadMessage();
//                externalMessage.test();
//            };
//            handler.postDelayed(runnable, timeRefreshMsg);
//        }
//
//        if (!latchNotification)
//            return;
//        else {
//            latchNotification = false;
//
//            Runnable runnable = () -> {
//                getNotificationMessage();
//            };
//            handler.postDelayed(runnable, timeRefreshNoti);
//        }
//    }
//
//    //region notification
//    private void getNotificationMessage() {
//        if (dataHelper.getCurrentUserInfo() == null || dataHelper.getCurrentUserInfo().getApiKey() == null || dataHelper.getCurrentUserInfo().getApiKey().isEmpty()
//                || dataHelper.getCenterInfo() == null || dataHelper.getCenterInfo().getDb_name() == null || dataHelper.getCenterInfo().getDb_name().isEmpty())
//            return;
//
//        int idUser;
//        int idBranch;
//        try {
//            idUser = dataHelper.getCurrentUserInfo().getIdUser();
//            idBranch = dataHelper.getCurrentUserInfo().getIdBranch();
//        } catch (Exception e) {
//            return;
//        }
//
//        if (idUser == 0 || idBranch == 0)
//            return;
//
//        CompositeDisposable cd = new CompositeDisposable();
//        cd.add(dataHelper.
//                        getAllNotification(idUser, idBranch)
//                        .subscribeOn(schedulerProvider.io())
//                        .observeOn(schedulerProvider.ui())
//                        .subscribe(response -> {
//
//                                    if (response.getmResponses().size() == 1 && response.getmResponses().get(0).getDate() == null) {
//                                        //Log.wtf("mLogTest","latchNotification1 = true");
//                                        latchNotification = true;
//                                        startTimer();
//                                        cd.dispose();
//                                        return;
//                                    }
//
//                                    for (NotificationMsg msg : response.getmResponses()) {
//                                        processingNotificationMsg(msg);
//                                    }
//
//                                    //Log.wtf("mLogTest","latchNotification2 = true");
//                                    latchNotification = true;
//                                    startTimer();
//                                    cd.dispose();
//
//                                }, throwable -> {
//
//                                    String msg = throwable.getMessage();
//                                    String str = null;
//                                    if (throwable instanceof ANError) {
//                                        str = ((ANError) throwable).getErrorDetail();
//                                    }
//
////                            if (msg == null || (!msg.contains("Failed to connect to") && !msg.contains("connect timed out") && !msg.contains("connection abort")
////                                    && !msg.contains("failed to connect to") && !msg.contains("timeout")))
//                                    if (str == null || !str.contains("connectionError") || msg == null || (!msg.contains("Failed to connect to") && !msg.contains("connect timed out") && !msg.contains("connection abort")
//                                            && !msg.contains("failed to connect to") && !msg.contains("timeout")))
//                                        Timber.tag("my").e(LoggingTree.getMessageForError(throwable, "InsteadSocket$getNotificationMessage "));
//
//
//                                    if (insteadSocketListener != null)
//                                        insteadSocketListener.NotificationError(throwable.getMessage());
//
//                                    latchNotification = true;
//                                    startTimer();
//                                    cd.dispose();
//                                }
//                        )
//        );
//    }
//
//    private void processingNotificationMsg(NotificationMsg msg) {
//        switch (msg.getType()) {
//            case "zapis":
//                showNotification.showReminderOfAdmission(msg);
//                break;
//
//            case "sale":
//                showNotification.showShare(msg);
//
//                break;
//
//            case "analiz":
//                showNotification.showAnalyzesAreReady(msg);
//                break;
//        }
//    }
////endregion
//
//
//    //region test and send no reade
//    private List<MessageFromServer> sentMsgList = new ArrayList<>();
//
//    private void testNoReadMessage() {
//        CompositeDisposable cd = new CompositeDisposable();
//        cd.add(dataHelper
//                .getAllNoReadMsg()
//                .subscribeOn(schedulerProvider.io())
//                .observeOn(schedulerProvider.ui())
//                .subscribe(response -> {
//                    if (response.size() != 0) {
//                        addToSentList(response);
//
//                        for (MessageFromServer msg : response) {
//                            switch (msg.getType()) {
//                                case MainUtils.IMAGE:
//                                    new SendImageByService(dataHelper, msg, this);
//                                    break;
//
//                                case MainUtils.TEXT:
//                                    new SendText(dataHelper, msg, this);
//                                    break;
//
//                                case MainUtils.FILE:
//                                    break;
//                                default:
//                                    Timber.tag("my").e(LoggingTree.getMessageForError(null, "InsteadSocket$testNoReadMessage  testNoReadMessage некорректный тип сообщения чата: %s " + msg.getType()));
//                            }
//                        }
//                    } else {
//                        latchNoRead = true;
//                        startTimer();
//                    }
//                    cd.dispose();
//                }, throwable ->
//                {
//                    Timber.tag("my").e(LoggingTree.getMessageForError(throwable, "InsteadSocket$testNoReadMessage "));
//
//                    if (insteadSocketListener != null)
//                        insteadSocketListener.testNoReadMessageError(throwable.getMessage());
//
//                    latchNoRead = true;
//                    startTimer();
//
//                    cd.dispose();
//                }));
//    }
//
//
//    private void addToSentList(List<MessageFromServer> list) {
//        sentMsgList.addAll(list);
//    }
//
//    private void deleteFromSentList(MessageFromServer msg) {
//        sentMsgList.remove(msg);
//
//        if (sentMsgList.size() <= 0) {
//            latchNoRead = true;
//            startTimer();
//        }
//    }
//
//
//    //region info from send message
//    @Override
//    public void messageProcessingSuccessfulUpdate(MessageFromServer msg) {
//        deleteFromSentList(msg);
//
//        if (insteadSocketListener != null)
//            insteadSocketListener.updateOurMsgIsReadToRealmDone();
//    }
//
//
//    @Override
//    public void messageProcessingError(Throwable throwable, MessageFromServer msg) {
//        deleteFromSentList(msg);
//
//        if (insteadSocketListener != null)
//            insteadSocketListener.updateOurMsgIsReadToRealmError(throwable.getMessage());
//    }
//    //endregion
//    //endregion
//
//    //region listener external message
//    @Override
//    public void latchExternalMsgTrue() {
//        latchExternalMsg = true;
//        startTimer();
//    }
//
//    @Override
//    public void showNotificationMessage(List<MessageFromServer> list) {
//        showNotification.showNotificationMessage(list, currentRoom);
//    }
//
//    @Override
//    public void saveExternalMsgError(String msg) {
//        if (insteadSocketListener != null)
//            insteadSocketListener.saveExternalMsgError(msg);
//    }
//
//    @Override
//    public void saveExternalMsgRefresh() {
//        if (insteadSocketListener != null)
//            insteadSocketListener.saveExternalMsgRefresh();
//    }
////endregion
//
//
//    private long currentRoom = -1;
//
//    public void setListenerInsteadSocket(InsteadSocketListener listener, long room) {
//        insteadSocketListener = listener;
//        currentRoom = room;
//    }
//
//    public void onDestroy() {
//        latchIsStartTimer = false;
//    }
//
//
//    public interface InsteadSocketListener {
//        void saveExternalMsgRefresh();
//
//        void saveExternalMsgError(String error);
//
//        void updateOurMsgIsReadToRealmDone();
//
//        void updateOurMsgIsReadToRealmError(String error);
//
//        void sendToServerOurMsgError(String error);
//
//        void testNoReadMessageError(String error);
//
//        void NotificationError(String error);
//    }
//
//}
