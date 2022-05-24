package com.medhelp.medhelp.ui.schedule.alert_question_at_exit;

import android.content.Context;
import com.medhelp.medhelp.data.pref.PreferencesManager;
import timber.log.Timber;

public class AlertQuestionAtExitPresenter {

    PreferencesManager prefManager;

    public AlertQuestionAtExitPresenter(Context context)
    {
        prefManager=new PreferencesManager(context);
    }

    public void setDoNotShowAlertExit(boolean boo)
    {
        prefManager.setNotShowAlertQuestionAtExit(boo);
    }

    public void sendToServerMsg(String msg)
    {
        Timber.tag("my").d(msg);
    }
}
