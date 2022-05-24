//package com.medhelp.medhelp.bg.temporaryInsteadSocket.sendMsg;
//
//import com.medhelp.medhelp.data.DataHelper;
//import com.medhelp.medhelp.data.model.CenterResponse;
//import com.medhelp.medhelp.data.model.chat.MessageFromServer;
//import com.medhelp.medhelp.utils.timber_log.LoggingTree;
//
//import io.reactivex.android.schedulers.AndroidSchedulers;
//import io.reactivex.disposables.CompositeDisposable;
//import io.reactivex.schedulers.Schedulers;
//import timber.log.Timber;
//
//public class SendText {
//    private DataHelper dataHelper;
//    private MessageFromServer msg;
//    private SendMessageInterface listener;
//
//    private CenterResponse centerResponse;
//
//    public SendText(DataHelper dataHelper,MessageFromServer msg, SendMessageInterface listener) {
//        this.dataHelper=dataHelper;
//        this.msg=msg;
//        this.listener=listener;
//
//        centerResponse=dataHelper.getCenterInfo();
//
//        sendToServerOurMsg();
//    }
//
//    private void sendToServerOurMsg()
//    {
//        CompositeDisposable cd=new CompositeDisposable();
//        cd.add(dataHelper
//                .sendOurMsgToServer(msg.getIdRoom(),msg.getMsg(), msg.getType())
//                .concatMapCompletable(resp->
//                {
//                    if(resp.getResponse())
//                    {
//                        return dataHelper.updateOurMsgIsRead(msg);
//                    }
//                    else
//                        return null;
//                })
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(()->{
//                    if(listener!=null)
//                        listener.messageProcessingSuccessfulUpdate(msg);
//
//                    cd.dispose();
//                },throwable->{
//                    Timber.tag("my").e(LoggingTree.getMessageForError(throwable,"SendText$sendToServerOurMsg"));
//                    if(listener!=null)
//                        listener.messageProcessingError(throwable , msg);
//                    cd.dispose();
//                })
//        );
//    }
//
//}
