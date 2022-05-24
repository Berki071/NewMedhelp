//package com.medhelp.medhelp.bg.oldClass;
//
//import com.medhelp.medhelp.data.DataHelper;
//import com.medhelp.medhelp.data.model.CenterResponse;
//import com.medhelp.medhelp.data.model.chat.MessageFromServer;
//import com.medhelp.medhelp.utils.workToFile.convert_Base64.ConvertBase64;
//import com.medhelp.medhelp.utils.timber_log.LoggingTree;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.File;
//
//import io.reactivex.android.schedulers.AndroidSchedulers;
//import io.reactivex.disposables.CompositeDisposable;
//import io.reactivex.schedulers.Schedulers;
//import timber.log.Timber;
//
//public class SendImageByService {
//
//    private MessageFromServer msg;
//    private CenterResponse centerResponse;
//    private ConvertBase64 convertBase64;
//    private DataHelper dataHelper;
//    private SendMessageInterface listener;
//
//    public SendImageByService(DataHelper dataHelper, MessageFromServer msg, SendMessageInterface listener) {
//        this.dataHelper = dataHelper;
//        this.msg = msg;
//        this.listener = listener;
//
//        convertBase64 = new ConvertBase64();
//        centerResponse = dataHelper.getCenterInfo();
//
//        convertImgToBase64();
//    }
//
//    private void convertImgToBase64() {
//        CompositeDisposable cd = new CompositeDisposable();
//        cd.add(convertBase64.fileToBase64(new File(msg.getMsg()))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(response -> {
//                            send(createGson(response));
//                            cd.dispose();
//                        }, throwable -> {
//                            Timber.tag("my").e(LoggingTree.getMessageForError(throwable, "SendImageByService$convertImgToBase64"));
//                            listener.messageProcessingError(throwable, msg);
//                            cd.dispose();
//                        }
//                )
//        );
//    }
//
//    private JSONObject createGson(String base64) {
//        JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject.accumulate("FileName", clipNameWithoutExtension());
//            jsonObject.accumulate("FileFormat", clipToExtension());
//            jsonObject.accumulate("Base64Data", base64);
//
//            return jsonObject;
//        } catch (JSONException e) {
//            Timber.tag("my").e(LoggingTree.getMessageForError(e, "SendImageByService$createGson "));
//            listener.messageProcessingError(e, msg);
//            return null;
//        }
//    }
//
//    private String clipNameWithoutExtension() {
//        String path = msg.getMsg();
//        path = path.substring(path.lastIndexOf("/") + 1, path.lastIndexOf("."));
//        return path;
//    }
//
//    private String clipToName() {
//        String path = msg.getMsg();
//        path = path.substring(path.lastIndexOf("/") + 1, path.length());
//        return path;
//    }
//
//    private String clipToExtension() {
//        String path = msg.getMsg();
//        path = path.substring(path.lastIndexOf(".") + 1, path.length());
//        return path;
//    }
//
//
//    private void send(JSONObject json) {
//        if (json == null)
//            return;
//
//        String ip = centerResponse.getLogo();
//        ip = ip.substring(0, ip.indexOf("/load?") + 1) + "upload";
//
//        CompositeDisposable cd = new CompositeDisposable();
//        cd.add(dataHelper.sendImageToService(ip, json)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(response -> {
//                            if (response) {
//                                sendToServerOurMsg();
//                            } else {
//                                listener.messageProcessingError(new Throwable("вовремя передачи изображения произошла ошибка "), msg);
//                            }
//                            cd.dispose();
//                        }
//                        , throwable -> {
//                            Timber.tag("my").e(LoggingTree.getMessageForError(throwable, "SendImageByService$send "));
//                            listener.messageProcessingError(throwable, msg);
//                            cd.dispose();
//                        }
//                )
//        );
//    }
//
//    private void sendToServerOurMsg() {
//        CompositeDisposable cd = new CompositeDisposable();
//        cd.add(dataHelper
//                .sendOurMsgToServer(msg.getIdRoom(), clipToName(), msg.getType())
//                .concatMapCompletable(resp ->
//                {
//                    if (resp.getResponse()) {
//                        return dataHelper.updateOurMsgIsRead(msg);
//                    } else
//                        return null;
//                })
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(() -> {
//                    if (listener != null)
//                        listener.messageProcessingSuccessfulUpdate(msg);
//
//                    cd.dispose();
//                }, throwable -> {
//                    Timber.tag("my").e(LoggingTree.getMessageForError(throwable, "SendImageByService$sendToServerOurMsg"));
//                    if (listener != null)
//                        listener.messageProcessingError(throwable, msg);
//                    cd.dispose();
//                })
//        );
//    }
//
//}
