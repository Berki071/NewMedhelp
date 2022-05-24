package com.medhelp.medhelp.ui.video_consultation.video_chat;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.medhelp.medhelp.Constants;
import com.medhelp.medhelp.R;
import com.medhelp.medhelp.data.model.VisitResponse;
import com.medhelp.medhelp.ui.video_consultation.video_chat.utils.MyWebChromeClient;
import com.medhelp.medhelp.ui.video_consultation.video_chat.utils.MyWebViewClient;
import com.medhelp.medhelp.ui.video_consultation.video_chat.utils.WebInterface;
import com.medhelp.medhelp.utils.main.TimesUtils;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import timber.log.Timber;

public class WebViewEasyRtc {
    private static final WebViewEasyRtc instance=new WebViewEasyRtc();
    private WebViewEasyRtc(){}

    public static WebViewEasyRtc getInstance()
    {
        return instance;
    }

    private WebView webView;

    private VisitResponse dataForVideoChat;
    private String companionId;

    private WebInterface webInterface;
    private MyWebViewClient.WrbClientListener webViewListener;
    private WebInterface.WebInterfaceListener webInterfaceListener;

    public static final String WAIT="wait";
    public static final String CALL_OUTGOING ="CALL_OUTGOING";
    public static final String CALL_INCOMING="call_incoming";
    public static final String VIDEO="video";

    public static final int  TIME_WAIT_UP_PHONE=1000*20;
    public String currentDivShow="";

    private Timer timer;
    private MyTimeTask myTimeTask;

    public VideoChatActivity.VideoChatListener listener;

    private Boolean initForPhoneUp=false;
    private boolean initWebView=false;

    private long videoPlay=0;

    Context context;

    MyWebChromeClient myWebChromeClient;

    public void onDestroy()
    {
        currentDivShow="";
        stopTimer();
        listener=null;
        webView=null;
    }


    public WebInterface getWebInterface() {
        return webInterface;
    }

    public void setWebInterface(WebInterface webInterface) {
        this.webInterface = webInterface;
    }

    public void setWebView(Context context, WebView webView) {
        this.context=context;
        this.webView = webView;
        initWebView=false;
    }

    public void setListener(VideoChatActivity.VideoChatListener listener)
    {
        this.listener=listener;
    }

    public void setDataForVideoChat(VisitResponse dataForVideoChat, String companionId)
    {
        this.companionId =companionId;
        if(dataForVideoChat!=null)
            this.dataForVideoChat=dataForVideoChat;
    }

    public VisitResponse getDataForVideoChat()
    {
        return dataForVideoChat;
    }

    public void initWeb()
    {
        initListeners();

        myWebChromeClient=new MyWebChromeClient(context);
        webView.setWebViewClient(new MyWebViewClient(webViewListener));
        webView.setWebChromeClient(myWebChromeClient);
        WebSettings webSettings = webView.getSettings();
        webSettings.setAllowFileAccess(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setDomStorageEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setAllowFileAccessFromFileURLs(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            //для звука
            webSettings .setMediaPlaybackRequiresUserGesture(false);
        }

        webInterface.setListener( webInterfaceListener,context);
        webView.addJavascriptInterface(webInterface, "AndroidListener");
        webView.loadUrl("https://5.130.3.124:8443/");
    }


    private void initListeners()
    {
        webViewListener =new MyWebViewClient.WrbClientListener() {
            @Override
            public void ready() {

                webView.evaluateJavascript("setIdAndroid('"+dataForVideoChat.getIdUser()+"')",null);
                webView.evaluateJavascript("setTimeStart('"+ dataForVideoChat.getTimeOfReceipt()+" "+dataForVideoChat.getDateOfReceipt()+"')",null);
                webView.evaluateJavascript("setClearDuration('"+dataForVideoChat.getDurationSec()+"')",null);  //время в секундах

                webView.evaluateJavascript("setNameUser('"+dataForVideoChat.getUserName()+"')",null);
                webView.evaluateJavascript("setAppName('ChatikMedHelp"+dataForVideoChat.getIdRecord()+"')",null);

                webView.evaluateJavascript("setTimeOfReceipt('"+  dataForVideoChat.getTimeOfReceipt()+"')",null);
                webView.evaluateJavascript("setTypeClient('patient')",null);

                webView.evaluateJavascript("connect()",null);
                initForPhoneUp=true;
            }

            @Override
            public void error(String msg) {
                Timber.tag("my").e("WebViewEasyRtc initListeners: "+msg);
                listener.showToastMsg(context.getResources().getString(R.string.api_default_error));
                listener.callOnBack();
            }
        };




        webInterfaceListener=new WebInterface.WebInterfaceListener() {
            @Override
            public void callCancel() {
                if(WebInterface.getInstance().isResume())
                    listener.callOnBack();

                Timber.tag("my").v("Звонок, окончен id_record: "+dataForVideoChat.getIdRecord());
            }


            @Override
            public void showDiv(final String divName, boolean isResume) {
                if(divName.equals(VIDEO))
                {
                    Handler mainHandler = new Handler(Looper.getMainLooper());
                    Runnable myRunnable = new Runnable() {
                        @Override
                        public void run() {
                            listener.stopVibration();
                            String tmp= String.valueOf(getTimerTime());

                            webView.evaluateJavascript("setSessionDuration('" + tmp + "')", null);

                            Timber.tag("my").v("Звонок, начало видео id_record: "+dataForVideoChat.getIdRecord());

                            if (tmp.equals("0")) {
                                webView.evaluateJavascript("stopCall()", null);
                                listener.callOnBack();
                            }
                        }
                    };
                    mainHandler.post(myRunnable);
                }


                if(divName.equals(WAIT))
                {
                    if(isResume)
                        listener.playWaitMusics();

                    Timber.tag("my").v("Окно ожидания доктора id_record: "+dataForVideoChat.getIdRecord());
                }

                if(currentDivShow.equals(WAIT) && !divName.equals(WAIT))
                {
                    listener.stopMusics();
                }


                if(currentDivShow.equals(CALL_OUTGOING) && !divName.equals(CALL_OUTGOING))
                {
                    listener.stopMusics();
                    stopTimer();
                }


                if(divName.equals(CALL_INCOMING))
                {
                    if(WebInterface.getInstance().isResume())
                        listener.startVibration();

                }

                if(currentDivShow.equals(CALL_INCOMING)  &&  !divName.equals(CALL_INCOMING))
                {
                    listener.stopVibration();
                }

                currentDivShow=divName;
            }

            @Override
            public void timeLeft(String time) {
               // listener.showToastMsg("Time left = "+time);
            }

            @Override
            public void crossBtnClick( ) {
                listener.showAlerts();
            }

            @Override
            public void clickBtnBack() {
                listener.setIsClickBack(true);

                if(currentDivShow.equals(VIDEO))
                {
                    webView.evaluateJavascript("stopVideoForBack()",null);
                }

                listener.callOnBack();

            }

            @Override
            public void showToast(String message) {
                if(message.contains("Requested video size"))
                    return;

                Log.wtf("send_load_file ", "showToast:"+message);
                //listener.showToastMsg(message);
            }

            @Override
            public void showError(String message) {
                Timber.tag("my").e("WebViewEasyRtc webInterfaceListener: "+message);
            }

            @Override
            public void showNotification(String id, String name, String duration, String timeStart, String companionId, String callerName) {
                listener.showNotifications(id,name,duration,timeStart,companionId ,callerName);

            }

            @Override
            public void logSuccess() {

                Handler mainHandler = new Handler(Looper.getMainLooper());
                Runnable myRunnable = new Runnable() {
                    @Override
                    public void run() {
                        if(initForPhoneUp)
                        {
                            initWebView=true;
                        }

                        if(performCloseRtcPhoneDown)
                        {
                            performCloseRtcPhoneDown =false;

                            webView.evaluateJavascript("setCompanionId('" + companionId + "')", null);
                            webView.evaluateJavascript("notifiIncomingPhoneDown()", null);
                            listener.activityOnBack();
                            return;
                        }

                        if(performReplyRtcPhoneUp)
                        {
                            initForPhoneUp = false;
                            performReplyRtcPhoneUp =false;
                            webView.evaluateJavascript("setCompanionId('" + companionId + "')", null);
                            webView.evaluateJavascript("incomingPhoneUp()", null);
                            return;
                        }

                        if (dataForVideoChat.getExecuteTheScenario().equals(Constants.SCENARIO_VIDEO) && initForPhoneUp) {
                           // listener.closeNotification();

                            if (companionId != null && !companionId.equals("") && webView!=null) {
                                initForPhoneUp = false;
                                webView.evaluateJavascript("setCompanionId('" + companionId + "')", null);
                                webView.evaluateJavascript("incomingPhoneUp()", null);
                            }
                        }
                        else if(dataForVideoChat.getExecuteTheScenario().equals(Constants.SCENARIO_INCOMING_WAIT)  && initForPhoneUp)
                        {
                            if (companionId != null && !companionId.equals("")  && webView!=null) {
                                initForPhoneUp = false;
                                webView.evaluateJavascript("setCompanionId('" + companionId + "')", null);
                                webView.evaluateJavascript("showDiv(\"call_incoming\")", null);
                            }
                        }
                    }
                };
                mainHandler.post(myRunnable);
            }

            @Override
            public void clearNotification() {
                listener.closeNotification();
            }

            @Override
            public void clickAttachFile() {
                listener.clickAttachFile();
            }

            @Override
            public void callerVideoCanplay() {
                videoPlay=System.currentTimeMillis();
                String date=TimesUtils.longToString(videoPlay,TimesUtils.DATE_FORMAT_HHmmss_ddMMyyyy);
                //Log.wtf("timber","video play "+date);
            }

            @Override
            public void callerVideoSuspend() {
                long currentTime =System.currentTimeMillis();
                long difference= currentTime - videoPlay;
                difference/=1000;
                long min=difference/60;
                long sec=difference%60;
                videoPlay=0;

               // listener.showToastMsg("Звонок продлился: "+min+" мин. "+sec+" c.");
            }

            @Override
            public void clickBtnPhoneDown() {
                Log.wtf("timber","трубку положил patient");
            }

            @Override
            public void sendTimeTimer(String time) {
                //в зеленой для сигнал за 5 и 1 минуту
            }


            @Override
            public void sendReceiveStatusCB(String msg) {
               Log.wtf("send_load_file","sendReceiveStatusCB: "+msg);
            }
            @Override
            public void sendBlobAcceptor(String msg) {
                Log.wtf("send_load_file","sendBlobAcceptor: "+msg);
            }
            @Override
            public void sendAcceptRejectCB(String msg) {
                Log.wtf("send_load_file","sendAcceptRejectCB: "+msg);
            }

        };
    }



    private int getTimerTime()
    {
        long currentTimeLong=System.currentTimeMillis();
        long timeOfReceiptStart=dataForVideoChat.getTimeMills();
        long timeOfReceiptEnd=timeOfReceiptStart+(dataForVideoChat.getDurationSec()*1000);

//        String d1 = ListActivity.longToString(currentTimeLong);
//        String d2 = ListActivity.longToString(timeOfReceiptStart);
//        String d3 = ListActivity.longToString(timeOfReceiptEnd);

        if(((timeOfReceiptStart-Constants.MIN_TIME_BEFORE_VIDEO_CALL*60*1000)<currentTimeLong)  && currentTimeLong<timeOfReceiptEnd)
        {
            if (timeOfReceiptStart > currentTimeLong) {
                return dataForVideoChat.getDurationSec();
            } else {
                long k1 =  timeOfReceiptEnd-currentTimeLong;
                long k2 = (k1 / 1000);
                return (int) k2;
            }
        }
        else
        {
            return 0;
        }
    }



    public void startTimer(){
        stopTimer();

        timer=new Timer();
        myTimeTask=new MyTimeTask();

        timer.schedule(myTimeTask,TIME_WAIT_UP_PHONE);
    }

    public void stopTimer(){
        if(timer!=null)
        {
            timer.cancel();
            timer=null;
        }
    }

    class MyTimeTask extends TimerTask {   //таймер ожидания поднятия трубки
        @Override
        public void run() {
            listener.endOfResponseTimes();
        }

    }


    public void incomingPhoneDown()
    {
        webView.evaluateJavascript("incomingPhoneDown()",null);
    }

    public void stopVideoForBack()
    {
        webView.evaluateJavascript("stopVideoForBack()",null);
    }

    public void stopOnPauseStateActivityForDoc()
    {
        webView.evaluateJavascript("stopOnPauseStateActivityForDoc()", null);
    }

    public void stopCall()
    {
        webView.evaluateJavascript("stopCall()", null);
    }

    public void stopOnPauseStateActivityForPatient()
    {
        webView.evaluateJavascript("stopOnPauseStateActivityForPatient()", null);
    }


    boolean performCloseRtcPhoneDown =false;
    public void closeRtcPhoneDown(String companionId)
    {
        Handler mainHandler = new Handler(Looper.getMainLooper());
        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                if (listener!=null)
                    listener.closeNotification();

                if(initWebView) {
                    webView.evaluateJavascript("setCompanionId('" + companionId + "')", null);
                    webView.evaluateJavascript("notifiIncomingPhoneDown()", null);
                    listener.activityOnBack();
                }
                else
                {
                    WebViewEasyRtc.this.companionId = companionId;
                    performCloseRtcPhoneDown =true;
                }
            }
        };
        mainHandler.post(myRunnable);
    }


    boolean performReplyRtcPhoneUp = false;
    public void replyRtcPhoneUp(String companionId) {
        Handler mainHandler = new Handler(Looper.getMainLooper());
        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {

                if (initWebView) {
                    initForPhoneUp = false;
                    webView.evaluateJavascript("setCompanionId('" + WebViewEasyRtc.this.companionId + "')", null);
                    webView.evaluateJavascript("incomingPhoneUp()", null);
                } else {
                    WebViewEasyRtc.this.companionId = companionId;
                    performReplyRtcPhoneUp =true;
                }
            }
        };
        mainHandler.post(myRunnable);
    }

    public void enableMicrophone(boolean boo)
    {
        if(boo)
        {
            webView.evaluateJavascript("microphoneOn()", null);
        }
        else
        {
            webView.evaluateJavascript("microphoneOff()", null);
        }
    }

    public void enableCamera(boolean boo)
    {
        if(boo)
        {
            webView.evaluateJavascript("cameraOn()", null);
        }
        else
        {
            webView.evaluateJavascript("cameraOff()", null);
        }
    }

    public void sendFile(String path){
        webView.evaluateJavascript("sendFile('"+path+"')", null);
    }

    public void sendFile(File files){
//        MultipartEntityBuilder multiPartEntity = MultipartEntityBuilder.create ();
//        multiPartEntity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
//        FileBody fileBody = new FileBody(files); //image should be a String
//        multiPartEntity.addPart("file", fileBody);


        //webView.evaluateJavascript("sendFile('"+multiPartEntity.toString()+"')", null);
    }

    public void setUriInMyWebChromeClient(Uri uri)
    {
        myWebChromeClient.setUriToMUploadMessage(uri);
    }

}
