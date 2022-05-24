package com.medhelp.medhelp.ui.video_consultation.video_chat;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.medhelp.medhelp.Constants;
import com.medhelp.medhelp.R;
import com.medhelp.medhelp.data.model.VisitResponse;
import com.medhelp.medhelp.ui._main_page.MainActivity;
import com.medhelp.medhelp.ui.video_consultation.video_chat.utils.CallReceiver;
import com.medhelp.medhelp.ui.video_consultation.video_chat.utils.WebInterface;
import com.medhelp.medhelp.utils.main.MainUtils;

import java.io.File;




import static com.medhelp.medhelp.ui._main_page.MainActivity.POINTER_TO_PAGE;

public class VideoChatActivity extends AppCompatActivity {
    Context context;
    VisitResponse data;

    WebView webView;
    View mDecorView;

    CallReceiver callReceiver=new CallReceiver();
    WebViewEasyRtc webViewEasyRtc;
    VideoChatListener listener;

    private AlertDialog alert;

    VideoChatPresenter presenter;

    public static final int SELECT_FILE_RESULT_CODE=233;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_chat);

        context=this;
        webView=findViewById(R.id.webView);
        init();
    }

    private void init() {
        data = getIntent().getExtras().getParcelable(VisitResponse.class.getCanonicalName());

        mDecorView = getWindow().getDecorView();

        IntentFilter filter=new IntentFilter();
        filter.addAction("android.intent.action.PHONE_STATE");

        this.registerReceiver(callReceiver, filter);
        callReceiver.setListener(new CallReceiver.CallReceiverListener() {
            @Override
            public void beginCallPhone() {
                if(webViewEasyRtc!=null)
                {
                    webViewEasyRtc.enableMicrophone(false);
                    webViewEasyRtc.enableCamera(false);
                }
            }

            @Override
            public void endCallPhone() {
                if(webViewEasyRtc!=null)
                {
                    if(webViewEasyRtc.getWebInterface().isResume())
                    {
                        webViewEasyRtc.enableMicrophone(true);
                        webViewEasyRtc.enableCamera(true);
                    }
                }
            }
        });

        presenter=new VideoChatPresenter(this);

        initEasyListener();

        WebView webView=findViewById(R.id.webView);

        webViewEasyRtc=WebViewEasyRtc.getInstance();
        webViewEasyRtc.setWebView(context,webView);
        webViewEasyRtc.setListener(listener);
        webViewEasyRtc.setDataForVideoChat(getIntent().getParcelableExtra(VisitResponse.class.getCanonicalName()), getIntent().getExtras().getString("companionid"));
        webViewEasyRtc.setWebInterface(WebInterface.getInstance());

        webViewEasyRtc.initWeb();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {   // режим погружения
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            mDecorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);}
    }

    boolean isClickBack=false;
    @Override
    public void onBackPressed() {
//        if (webView.canGoBack() == true) {
//            webView.goBack();
//        } else {
//            super.onBackPressed();
//        }

        isClickBack=true;

        if(webViewEasyRtc.currentDivShow.equals(WebViewEasyRtc.VIDEO))
        {
            Log.wtf("timber","onBackPressed");
        }

        if(webViewEasyRtc.currentDivShow.equals(WebViewEasyRtc.CALL_INCOMING))
        {
            webViewEasyRtc.incomingPhoneDown();
        }
        else
        {
            try {
                webViewEasyRtc.stopVideoForBack();
            }catch (Exception e)
            {
                Log.wtf("mLog","catch "+e.getMessage());
            }
        }
        onBack();
    }

    public void onBack()
    {
        presenter.stopAudioVibrationTimer();

        Handler mainHandler = new Handler(Looper.getMainLooper());
        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                isClickBack=true;
                Intent intent=new Intent(context, MainActivity.class);
                intent.putExtra(POINTER_TO_PAGE, Constants.MENU_ONLINE_CONSULTATION);
                startActivity(intent);
                finish();
            }
        };
        mainHandler.post(myRunnable);
    }

    @Override
    protected void onPause() {
        presenter.stopAudioVibrationTimer();

        if(!isClickBack) {

            if(webViewEasyRtc.currentDivShow.equals(WebViewEasyRtc.VIDEO))
            {
                webViewEasyRtc.enableCamera(false);
            }

            if(webViewEasyRtc.currentDivShow.equals(webViewEasyRtc.VIDEO))
            {
            }else {
                webViewEasyRtc.stopOnPauseStateActivityForPatient();
            }
        }

        if(webViewEasyRtc.getWebInterface()!=null)
            webViewEasyRtc.getWebInterface().setStateActivity(false);

        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.stopVibrations();

        if(webViewEasyRtc.currentDivShow.equals(WebViewEasyRtc.WAIT))
        {
            presenter.playWaitMusic();
        }

        webViewEasyRtc.enableMicrophone(true);
        webViewEasyRtc.enableCamera(true);

        if(webViewEasyRtc.getWebInterface()!=null)
            webViewEasyRtc.getWebInterface().setStateActivity(true);

    }

    @Override
    protected void onDestroy() {
        webViewEasyRtc.onDestroy();
        finish();
        this.unregisterReceiver(callReceiver);
        super.onDestroy();
    }


    public void showAlert()
    {
        String str="Выйти?";

        LayoutInflater inflater= getLayoutInflater();
        View view=inflater.inflate(R.layout.dialog_2textview_btn,null);

        TextView title=view.findViewById(R.id.title);
        TextView text=view.findViewById(R.id.text);
        Button btnYes =view.findViewById(R.id.btnYes);
        Button btnNo =view.findViewById(R.id.btnNo);

        title.setText(Html.fromHtml("<u>Подтвердите действие</u>"));
        text.setText(str);


        btnYes.setOnClickListener(v -> {
            if(context!=null)
                ((VideoChatActivity)context).isClickBack=true;

            if(webViewEasyRtc.currentDivShow.equals(webViewEasyRtc.VIDEO))
            {
                webViewEasyRtc.stopVideoForBack();
            }
            alert.dismiss();

            if(context!=null)
                ((VideoChatActivity)context).onBack();
        });

        btnNo.setVisibility(View.VISIBLE);
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert.dismiss();
            }
        });

        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setView(view);
        alert=builder.create();
        alert.setCanceledOnTouchOutside(false);
        alert.show();
    }


    private void initEasyListener()
    {
        listener=new VideoChatListener() {

            @Override
            public void closeNotification() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        presenter.closeNotificationAboutCall();
                    }
                });
            }

            @Override
            public void showNotifications(String id, String name, String duration, String timeStart,String companionId,String callerName) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        presenter.showNotification(id, name, duration, timeStart,companionId,callerName);
                    }
                });
            }

            @Override
            public void showToastMsg(String msg) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void callOnBack() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        onBack();
                    }
                });
            }

            @Override
            public void startVibration() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        presenter.startVibrations();
                    }
                });
            }

            @Override
            public void stopVibration() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        presenter.stopVibrations();
                    }
                });
            }

            @Override
            public void playWaitMusics() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        presenter.playWaitMusic();
                    }
                });
            }

            @Override
            public void stopMusics() {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        presenter.stopMusic();
                    }
                });
            }


            @Override
            public void showAlerts() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showAlert();
                    }
                });
            }

            @Override
            public void setIsClickBack(Boolean boo) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        isClickBack=boo;
                    }
                });
            }

            @Override
            public void activityOnBack() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        onBack();
                    }
                });
            }

            @Override
            public void endOfResponseTimes() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        presenter.endOfResponseTime();
                    }
                });
            }

            @Override
            public void clickAttachFile() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        presenter.selectFileToSend();
                    }
                });
            }
        };
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==SELECT_FILE_RESULT_CODE &&  resultCode == RESULT_OK){

            Uri contentURI = data.getData();
            if (contentURI != null) {
                File file = new File(MainUtils.getRealPathFromURI(contentURI, this));

                if (file.exists()) {
                    //webViewEasyRtc.sendFile("file://"+file.getPath());
                    webViewEasyRtc.sendFile(contentURI.toString());
                }
            }
        }
    }




    public interface VideoChatListener{
        void closeNotification();
        void showNotifications(String id, String name, String duration, String timeStart,String companionId,String callerName);
        void showToastMsg(String msg);
        void callOnBack();
        void startVibration();
        void stopVibration();
        void playWaitMusics();
        void stopMusics();
        void showAlerts();
        void setIsClickBack(Boolean boo);
        void activityOnBack();
        void endOfResponseTimes();
        void clickAttachFile();
    }
}
