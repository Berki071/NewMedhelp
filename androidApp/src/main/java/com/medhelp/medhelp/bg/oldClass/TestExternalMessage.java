//package com.medhelp.medhelp.bg.temporaryInsteadSocket.external_message;
//
//import android.content.Context;
//
//import com.androidnetworking.error.ANError;
//import com.medhelp.medhelp.data.DataHelper;
//import com.medhelp.medhelp.data.model.CenterResponse;
//import com.medhelp.medhelp.data.model.chat.MessageFromServer;
//import com.medhelp.medhelp.utils.workToFile.show_file.LoadFile;
//import com.medhelp.medhelp.utils.workToFile.show_file.ShowFile2;
//import com.medhelp.medhelp.utils.main.MainUtils;
//import com.medhelp.medhelp.utils.timber_log.LoggingTree;
//
//import java.io.File;
//import java.util.ArrayList;
//import java.util.List;
//
//import io.reactivex.android.schedulers.AndroidSchedulers;
//import io.reactivex.disposables.CompositeDisposable;
//import io.reactivex.schedulers.Schedulers;
//import timber.log.Timber;
//
//public class TestExternalMessage {
//    private Context context;
//    private DataHelper dataHelper;
//    private ExternalMessageListener listener;
//
//    private String ip=null;
//
//    public TestExternalMessage(Context context, DataHelper dataHelper, ExternalMessageListener listener)
//    {
//        this.context=context;
//        this.dataHelper=dataHelper;
//        this.listener = listener;
//
//        if(context==null  || dataHelper==null  || listener==null)
//        {
//            Timber.tag("my").e(LoggingTree.getMessageForError(null,"TestExternalMessage context==null  || dataHelper==null  || listener==null"));
//            return;
//        }
//
//        CenterResponse centerResponse=dataHelper.getCenterInfo();
//        String ipImage= centerResponse.getLogo();
//
//        if(ipImage==null  || ipImage.equals(""))
//        {
//            Timber.tag("my").e(LoggingTree.getMessageForError(null, "TestExternalMessage ipImage==null  || ipImage.equals(\"\")"));
//            return;
//        }
//
//        ip = ipImage.substring(0, ipImage.indexOf("path=")+5);
//
//        if(ip==null  || ip.equals(""))
//        {
//            Timber.tag("my").e(LoggingTree.getMessageForError(null, "TestExternalMessage ip==null  || ip.equals(\"\")  ipImage= "+ipImage));
//            return;
//        }
//    }
//
//    public void test()
//    {
//        allItem=0;
//        verifiedItem=0;
//
//        if(dataHelper.getCurrentUserInfo()==null || dataHelper.getCurrentUserInfo().getApiKey()==null || dataHelper.getCurrentUserInfo().getApiKey().isEmpty()
//                ||  dataHelper.getCenterInfo()==null|| dataHelper.getCenterInfo().getDb_name()==null || dataHelper.getCenterInfo().getDb_name().isEmpty())
//            return;
//
//        int idUser = dataHelper.getCurrentUserInfo().getIdUser();
//        int idBranch = dataHelper.getCurrentUserInfo().getIdBranch();
//        List<MessageFromServer> tmpList = new ArrayList<>();
//
//        if(idUser==0)
//            return;
//
//        CompositeDisposable cd = new CompositeDisposable();
//        cd.add(dataHelper
//                .getAllExternalMsg(idUser, idBranch)
//                .concatMapSingle(res -> {
//                    if (res.getRespons().size() <= 0  ||  res.getRespons().get(0).getMsg()==null) {
//                        listener.latchExternalMsgTrue();
//                        cd.dispose();
//                        return null;
//                    }
//
//                    for (MessageFromServer mfs : res.getRespons()) {
//                        tmpList.add(mfs);
//                    }
//
//                    return dataHelper.testExistAllRoom(res.getRespons());
//                })
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(response -> {
//
//                    if (response) {
//                        overwriteMsgIfTypeImage(tmpList);
//                    } else {
//                        getAllRoom(tmpList);
//                    }
//
//                    cd.dispose();
//                }, throwable -> {
//
//                    String msg=throwable.getMessage();
//                    String str = null;
//                    if (throwable instanceof ANError) {
//                        str=((ANError) throwable).getErrorDetail();
//                    }
//                   //if(msg==null  || (!msg.contains("Failed to connect to") && !msg.contains("connect timed out") && !msg.contains("connection abort") && !msg.contains("failed to connect to")  && !msg.contains("timeout") ))
//                    if (str==null || !str.contains("connectionError")  || msg == null || (!msg.contains("Failed to connect to") && !msg.contains("connect timed out") && !msg.contains("connection abort")
//                            && !msg.contains("failed to connect to") && !msg.contains("timeout") && !msg.contains("Connection reset by peer")))
//                       Timber.tag("my").e(LoggingTree.getMessageForError(throwable,"TestExternalMessage$test "));
//
//                    listener.latchExternalMsgTrue();
//                    listener.saveExternalMsgError(throwable.getMessage());
//
//                    cd.dispose();
//                })
//        );
//    }
//
//
//    private void getAllRoom( List<MessageFromServer> list)
//    {
//        int idUser = dataHelper.getCurrentUserInfo().getIdUser();
//        int idBranch = dataHelper.getCurrentUserInfo().getIdBranch();
//
//        CompositeDisposable cd = new CompositeDisposable();
//        cd.add(dataHelper
//                .getAllRoom(idUser,idBranch)
//                .concatMapCompletable(res->
//                        dataHelper.saveInfoAboutDoc(res.getRespons()))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(() -> {
//                    creteRoom(list);
//                    cd.dispose();
//                }, throwable ->
//                {
//                    Timber.tag("my").e(LoggingTree.getMessageForError(throwable,"TestExternalMessage$getAllRoom "));
//
//                    listener.saveExternalMsgError(throwable.getMessage());
//                    listener.latchExternalMsgTrue();
//
//                    cd.dispose();
//                })
//        );
//    }
//
//    private void creteRoom(List<MessageFromServer> list) {
//        CompositeDisposable cd = new CompositeDisposable();
//        cd.add(dataHelper
//                .testExistAllRoom(list)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(response -> {
//
//                            if (response) {
//                                overwriteMsgIfTypeImage(list);
//                            } else {
//
//                                listener.saveExternalMsgError("комната не создана, нет информации о докторе");
//                                listener.latchExternalMsgTrue();
//                            }
//
//                            cd.dispose();
//                        }, throwable -> {
//
//                            Timber.tag("my").e(LoggingTree.getMessageForError(throwable,"TestExternalMessage$creteRoom"));
//
//                            listener.saveExternalMsgError(throwable.getMessage());
//                            listener.latchExternalMsgTrue();
//
//                            cd.dispose();
//                        }
//                )
//        );
//    }
//
//    private int allItem=0;
//    private int verifiedItem=0;
//    private void overwriteMsgIfTypeImage(List<MessageFromServer> list)
//    {
//        allItem=list.size();
//        for (MessageFromServer tmp : list)
//        {
//            if(tmp.getType().equals(MainUtils.IMAGE))
//            {
//                if(ip==null)
//                    return;
//
//                new LoadFile(context, ShowFile2.TYPE_IMAGE, tmp.getMsg(), ip + tmp.getMsg(), dataHelper.getCurrentUserInfo().getApiKey(), null, new LoadFile.LoadFileListener() {
//                    @Override
//                    public void success(List<File> img) {
//                        tmp.setMsg(img.get(0).getAbsolutePath());
//
//                        testForContinuation(list);
//                    }
//
//                    @Override
//                    public void error(String err) {
//                        list.remove(tmp);
//                        allItem--;
//
//                        testForContinuation(list);
//                    }
//                });
//            }
//            else
//            {
//                testForContinuation(list);
//            }
//        }
//    }
//
//    private void testForContinuation(List<MessageFromServer> list)
//    {
//        verifiedItem++;
//
//        if(allItem==verifiedItem)
//        {
//            saveExternalMsgToDB(list);
//        }
//    }
//
//    private void saveExternalMsgToDB( List<MessageFromServer> list)
//    {
//        //overwriteMsgIfTypeImage(list);
//
//        CompositeDisposable cd=new CompositeDisposable();
//        cd.add(dataHelper
//                .saveExternalMsg(list)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(()->{
//                    listener.saveExternalMsgRefresh();
//                    listener.showNotificationMessage(list);
//                    listener.latchExternalMsgTrue();
//
//                    cd.dispose();
//                },throwable -> {
//
//                    Timber.tag("my").e(LoggingTree.getMessageForError(null,"TestExternalMessage$saveExternalMsgToDB "+throwable.getMessage()));
//
//                    listener.saveExternalMsgError(throwable.getMessage());
//                    listener.latchExternalMsgTrue();
//
//                    cd.dispose();
//                })
//        );
//    }
//
//
//
//
//    public interface ExternalMessageListener{
//        void latchExternalMsgTrue();
//        void showNotificationMessage(List<MessageFromServer> list);
//        void saveExternalMsgError(String msg);
//        void saveExternalMsgRefresh();
//    }
//
//}
