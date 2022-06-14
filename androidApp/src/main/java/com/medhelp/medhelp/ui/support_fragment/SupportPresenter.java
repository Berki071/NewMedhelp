package com.medhelp.medhelp.ui.support_fragment;

import android.content.Context;
import com.medhelp.medhelp.Constants;
import com.medhelp.medhelp.data.model.FCMResponse;
import com.medhelp.medhelp.data.model.TechUsersFcmIdItem;
import com.medhelp.medhelp.data.model.chat.SimpleResBoolean;
import com.medhelp.medhelp.data.network.NetworkManager;
import com.medhelp.medhelp.data.pref.PreferencesManager;
import com.medhelp.medhelp.utils.main.MainUtils;
import com.medhelp.medhelp.utils.timber_log.LoggingTree;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.List;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;



public class SupportPresenter {
    SupportDf mainView;
    Context context;

    PreferencesManager preferencesManager;
    NetworkManager networkManager;

    public SupportPresenter(SupportDf mainView) {
        this.mainView = mainView;
        context=mainView.getContext();

        preferencesManager=new PreferencesManager(mainView.getContext());
        networkManager=new NetworkManager(preferencesManager);
    }

    public String getPhone(String text)
    {
        String ph=text;
        ph=ph.substring(3);
        ph=ph.replaceAll("\\)","");
        ph=ph.replaceAll("-","");
        ph=ph.replaceAll("_","");
        return ph;
    }

    public void sendMsg(String loginTmp, String emailTmp, String msgTmp) {
        MainUtils.showLoading(mainView.getContext());

        networkManager
                .sendMsgToSupport(loginTmp, emailTmp, msgTmp)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SimpleResBoolean>() {
                               @Override
                               public void onSubscribe(Disposable d) {
                               }

                               @Override
                               public void onNext(SimpleResBoolean simpleResBoolean) {
                               }

                               @Override
                               public void onError(Throwable e) {
                                   Timber.tag("my").e(LoggingTree.getMessageForError(e, "SupportPresenter/sendMsg"));
                                   mainView.showAlertError();
                                   MainUtils.hideLoading();
                               }

                               @Override
                               public void onComplete() {
                                   MainUtils.hideLoading();
                                   Timber.tag("my").v("Отправлено сообщение в техподдержку: " + loginTmp + ", " + emailTmp + ", " + msgTmp);
                                   sendMsgNotification(loginTmp, emailTmp, msgTmp);
                                   mainView.showAlertSendComplete();

                               }
                           }

                );
    }


    public void sendMsgNotification(String loginTmp, String emailTmp, String msgTmp) {
        networkManager.getTechUsersFcm()
                .flatMap(techUsersFcmIdResponse ->{
                    if(techUsersFcmIdResponse.getResponse().size()!=1 || techUsersFcmIdResponse.getResponse().get(0).getFcm_key()!=null) {
                        JSONObject notiObj = creteJSONObjectNotification(techUsersFcmIdResponse.getResponse(),loginTmp,emailTmp,msgTmp);
                        if(notiObj ==null)
                            throw new Exception("Не удалось создать объект для отправки нотификации");

                        String servK= techUsersFcmIdResponse.getResponse().get(techUsersFcmIdResponse.getResponse().size()-1).getFcm_key();
                        return networkManager.sendMsgFCM(notiObj, servK/*Constants.SERVER_KEY_FCM*/,Constants.SENDER_ID_FCM);
                    }

                    throw new Exception("Нет fcmId поддержки");
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<FCMResponse>() {
                               @Override
                               public void onSubscribe(Disposable d) {}

                               @Override
                               public void onNext(FCMResponse simpleResBoolean) {}

                               @Override
                               public void onError(Throwable e) {
                                   Timber.tag("my").e(LoggingTree.getMessageForError(e, "SupportPresenter/sendMsgNotification"));
                               }

                               @Override
                               public void onComplete() {
                                   Timber.tag("my").v("Отправлено Notification в техподдержку: "+loginTmp+", "+emailTmp+", "+msgTmp);
                               }
                           }

                );

    }

    private JSONObject creteJSONObjectNotification(List<TechUsersFcmIdItem> list, String loginTmp, String emailTmp, String msgTmp) {
        try {
            JSONObject noti = new JSONObject();
            noti.put("title", "MedhelpB обращение в техподдержку");
            noti.put("body", msgTmp);
            noti.put("sound", "Enabled");

            JSONObject dopData = new JSONObject();
            dopData.put("app", "MedhelpB");
            dopData.put("login", loginTmp);
            dopData.put("email", emailTmp);

            JSONObject obj = new JSONObject();

            if(list.size()==1)
                obj.put("to", list.get(0).getFcm_key());
            else{
                JSONArray idsUsers=new JSONArray();

                for(int i=0;i<list.size()-1;i++){ // -1 т.к. последний ключ сервер а не id
                    idsUsers.put(list.get(i).getFcm_key());
                }

//                for(TechUsersFcmIdItem tmp : list){
//                    idsUsers.put(tmp.getFcm_key());
//                }
                obj.put("registration_ids", idsUsers);
            }

            obj.put("notification", noti);
            obj.put("data", dopData);

            return obj;
        } catch (JSONException e) {
            Timber.tag("my").e(LoggingTree.getMessageForError(e, "SupportPresenter$creteJSONObjectNotification"));
        }

        return null;
    }

}
