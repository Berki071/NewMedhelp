package com.medhelp.medhelp.ui.video_consultation.video_chat.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.PermissionRequest;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.medhelp.medhelp.ui.video_consultation.video_chat.VideoChatActivity;

public class MyWebChromeClient extends WebChromeClient {
    private ValueCallback<Uri> mUploadMessage;
    public final static int FILECHOOSER_RESULTCODE = 1;

    Context context;

    public MyWebChromeClient(Context context) {
        this.context = context;
    }

    public void setUriToMUploadMessage (Uri uri)
    {
        if(mUploadMessage!=null)
        {
            mUploadMessage.onReceiveValue(uri);
            mUploadMessage = null;
        }
    }

    @Override
    public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
        toLog(url,message);
        return true;
    }

    @Override
    public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
        toLog(url,message);
        return true;
    }

    @Override
    public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, final JsPromptResult result) {
        toLog(url,message);
        return true;
    }

    @Override
    public void onPermissionRequest(final PermissionRequest request) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            request.grant(request.getResources());
        }
    }

    private void toLog(String url, String msg)
    {
        Log.wtf("mLog","MyWebChromeClient url= "+url+"; msg= "+msg);
    }

    public void onProgressChanged(WebView view, int progress) { }

    //The undocumented magic method override
    //Eclipse will swear at you if you try to put @Override here
    // For Android 3.0+
    public void openFileChooser(ValueCallback<Uri> uploadMsg) {
        mUploadMessage = uploadMsg;
//        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
//        i.addCategory(Intent.CATEGORY_OPENABLE);
//        i.setType("image/*");
        ((VideoChatActivity)context).startActivityForResult(Intent.createChooser(getIntentSelectFile(), "File Chooser"), FILECHOOSER_RESULTCODE);
    }

    // For Android 3.0+
    public void openFileChooser(ValueCallback uploadMsg, String acceptType) {
        mUploadMessage = uploadMsg;
//        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
//        i.addCategory(Intent.CATEGORY_OPENABLE);
//        i.setType("*/*");
        ((VideoChatActivity)context).startActivityForResult(
                Intent.createChooser(getIntentSelectFile(), "File Browser"),
                FILECHOOSER_RESULTCODE);
    }

    //For Android 4.1
    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
        mUploadMessage = uploadMsg;
//        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
//        i.addCategory(Intent.CATEGORY_OPENABLE);
//        i.setType("image/*");
        ((VideoChatActivity)context).startActivityForResult(Intent.createChooser(getIntentSelectFile(), "File Chooser"), FILECHOOSER_RESULTCODE);

    }

    private Intent getIntentSelectFile(){
        String[] mimeTypes =
                {"application/pdf",
                        "image/jpeg",
                        "image/jpg",
                        "image/png"
                };

        Intent intent;
        if (Build.VERSION.SDK_INT >= 19) {
            intent = new Intent("android.intent.action.OPEN_DOCUMENT");
            intent.setType("*/*");
        } else {
            intent = new Intent("android.intent.action.GET_CONTENT");
            intent.setType("file*//*");
        }

        return intent;
    }
}
