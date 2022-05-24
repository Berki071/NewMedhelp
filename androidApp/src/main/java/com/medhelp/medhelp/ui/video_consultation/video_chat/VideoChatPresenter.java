package com.medhelp.medhelp.ui.video_consultation.video_chat;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Vibrator;

import com.medhelp.medhelp.Constants;
import com.medhelp.medhelp.R;
import com.medhelp.medhelp.data.model.VisitResponse;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class VideoChatPresenter {
    VideoChatActivity mainView;

    MediaPlayer mediaPlayer;
    private SoundPool sp;
    private int endCollSp;

    private Timer timer;

    public VideoChatPresenter(VideoChatActivity mainView) {
        this.mainView = mainView;

        sp = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        endCollSp =sp.load(mainView, R.raw.end_call, 1);
    }

    public byte[] readFileAndConvertToBlob(File file) {
        ByteArrayOutputStream bos = null;
        try {
            FileInputStream fis = new FileInputStream(file);
            byte[] buffer = new byte[1024];
            bos = new ByteArrayOutputStream();
            for (int len; (len = fis.read(buffer)) != -1;) {
                bos.write(buffer, 0, len);
            }
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        } catch (IOException e2) {
            System.err.println(e2.getMessage());
        }
        return bos != null ? bos.toByteArray() : null;
    }

    public void endOfResponseTime() {   //недозвон, пациент не ответил в установленное время
        mainView.webViewEasyRtc.stopCall();

        stopMusic();
        sp.play(endCollSp, 1, 1, 0, 0, 1);

        mainView.webViewEasyRtc.stopTimer();
        timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                //выполняется втом же потоке что и таймер
                mainView.onBack();
            }

        }, 1000);
    }

    public void selectFileToSend(){
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

        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        mainView.startActivityForResult(intent, VideoChatActivity.SELECT_FILE_RESULT_CODE);
    }

    public void closeNotificationAboutCall()
    {
//        NotificationManager notificationManager = (NotificationManager) mainView.getSystemService(Context.NOTIFICATION_SERVICE);
//        notificationManager.cancel(MyNotificationVideoChat.ID_NOTI_FOR_VIDEO_CALL);
    }

    public void showNotification(String id, String name, String duration, String timeStart,String companionId,String callerName)
    {
//        startVibrations();
//
//        new MyNotificationVideoChat(mainView, companionId,callerName);
//        mainView.data.setExecuteTheScenario(Constants.SCENARIO_INCOMING_WAIT);
//        mainView.data.setDurationSec(Integer.valueOf(duration));
//
//        Intent intOpen = new Intent(mainView, VideoChatActivity.class);
//        intOpen.putExtra(VisitResponse.class.getCanonicalName(),mainView.data);
//        intOpen.putExtra("companionid",companionId);
//
//        mainView.startActivity(intOpen);
    }

    public void stopAudioVibrationTimer()
    {
        stopVibrations();
        stopMusic();
        mainView.webViewEasyRtc.stopTimer();
    }

    public void startVibrations()
    {
        Vibrator vibrator = (Vibrator) mainView.getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = { 1000, 1000, 1000, 1000 };
        if (vibrator.hasVibrator()) {
            vibrator.vibrate(pattern, 0);
        }
    }

    public void stopVibrations()
    {
        Vibrator vibrator = (Vibrator) mainView.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.cancel();

    }

    public void playWaitMusic()
    {
        if(mediaPlayer==null) {

            mediaPlayer = MediaPlayer.create(mainView, R.raw.wait_music);
            mediaPlayer.setLooping(true);
            mediaPlayer.start();

            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {

                }
            });
        }
    }

    public void stopMusic()
    {
        if (mediaPlayer != null) {
            try {
                mediaPlayer.release();
                mediaPlayer = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
