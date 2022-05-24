package com.medhelp.medhelp.ui.video_consultation.recy;

import android.content.Context;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.medhelp.medhelp.Constants;
import com.medhelp.medhelp.R;
import com.medhelp.medhelp.data.model.VisitResponse;
import com.medhelp.medhelp.ui._main_page.MainActivity;
import com.medhelp.medhelp.utils.workToFile.show_file.ShowFile2;
import com.medhelp.medhelp.utils.main.TimesUtils;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;



import de.hdodenhof.circleimageview.CircleImageView;

public class ConsultationHolder extends RecyclerView.ViewHolder {
    public ConsultationHolder(@NonNull View itemView) {
        super(itemView);
    }
    CircleImageView ico;
    TextView name;
    TextView service;
    TextView dateStart;
    TextView timeStartAndEnd;
    TextView timerTitle;
    TextView timerTV;

    private Context context;
    private VisitResponse data;

    private String token;

    private Timer timer;
    private MyTimerClass myTimer;

    ConsultationAdapter.ConsultationListener listener;

    public ConsultationHolder(Context context, View itemView, String token,ConsultationAdapter.ConsultationListener listener) {
        super(itemView);

        this.context=context;
        this.token=token;
        this.listener=listener;

        ico=itemView.findViewById(R.id.ico);
        name=itemView.findViewById(R.id.name);
        service=itemView.findViewById(R.id.service);
        dateStart=itemView.findViewById(R.id.dateStart);
        timeStartAndEnd=itemView.findViewById(R.id.timeStartAndEnd);
        timerTitle=itemView.findViewById(R.id.timerTitle);
        timerTV=itemView.findViewById(R.id.timer);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timerTitle != null && timerTitle.getVisibility() == View.VISIBLE) {
                    listener.onClickItemRecy(data);
                }
            }
        });

    }

    public void onBindView(VisitResponse data)
    {
        this.data=data;
        name.setText(data.getNameSotr());
        service.setText(data.getNameServices());
        dateStart.setText(data.getDateOfReceipt());
        timeStartAndEnd.setText(getTimeStartAndEnd());

        if(isShowTimer())
        {
            timerTitle.setVisibility(View.VISIBLE);
            timerTV.setVisibility(View.VISIBLE);
            startTimer();
        }
        else
        {
            timerTitle.setVisibility(View.GONE);
            timerTV.setVisibility(View.GONE);
        }


        if(data.getPhotoSotr()!=null  && !data.getPhotoSotr().equals("")) {
            new ShowFile2.BuilderImage(ico.getContext())
                    .setType(ShowFile2.TYPE_ICO)
                    .load(data.getPhotoSotr())
                    .token(token)
                    .imgError(R.drawable.sh_doc)
                    .into(ico)
                    .setListener(new ShowFile2.ShowListener() {
                        @Override
                        public void complete(File file) {
                        }

                        @Override
                        public void error(String error) {
                        }
                    })
                    .build();
        }
    }

    private String getTimeStartAndEnd()
    {
        long timeEnd=data.getTimeMills();
        timeEnd+=data.getDurationSec()*1000;

        String str="c "+ data.getTimeOfReceipt();
        str+=" до "+TimesUtils.longToString(timeEnd,TimesUtils.DATE_FORMAT_HHmm);

        return str;
    }

    private boolean isShowTimer()
    {
        long currentTimeAndDate=System.currentTimeMillis();
        long dateTimeLongStart =data.getTimeMills();
        long dateTimeLongStartMinusBeforeAdmission=dateTimeLongStart-(Constants.MIN_TIME_BEFORE_VIDEO_CALL*60*1000);
        long timeDataEnd =dateTimeLongStart+data.getDurationSec()*1000;

//        String d1 = TimesUtils.longToString(currentTimeAndDate, TimesUtils.DATE_FORMAT_HHmmss_ddMMyyyy);
//        String d21 = TimesUtils.longToString(dateTimeLongStart,TimesUtils.DATE_FORMAT_HHmmss_ddMMyyyy);
//        String d22 = TimesUtils.longToString(dateTimeLongStartMinusBeforeAdmission,TimesUtils.DATE_FORMAT_HHmmss_ddMMyyyy);
//        String d3 = TimesUtils.longToString(timeDataEnd,TimesUtils.DATE_FORMAT_HHmmss_ddMMyyyy);

        return (currentTimeAndDate>dateTimeLongStartMinusBeforeAdmission)  && (currentTimeAndDate<timeDataEnd);
    }

    private void startTimer()
    {
        if(timer!=null)
        {
            timer.cancel();
            timer=null;
            myTimer=null;
        }

        timer=new Timer();
        myTimer=new MyTimerClass();

        timer.schedule(myTimer,0,1000);
    }

    private void stopTimer()
    {
        if(timer!=null)
        {
            timer.cancel();
            timer=null;
            myTimer=null;
        }
    }


    class MyTimerClass extends TimerTask
    {
        @Override
        public void run() {

            if(!isShowTimer())
            {
                timerTitle.setVisibility(View.GONE);
                timerTV.setVisibility(View.GONE);
                stopTimer();
                return;
            }

            long currentTimeAndDate=System.currentTimeMillis();
            String m1=TimesUtils.longToString(currentTimeAndDate,TimesUtils.DATE_FORMAT_HHmmss_ddMMyyyy);
            long receptionTimeAndDate=data.getTimeMills();
            String m2=TimesUtils.longToString(receptionTimeAndDate,TimesUtils.DATE_FORMAT_HHmmss_ddMMyyyy);
            long timeLeft=receptionTimeAndDate-currentTimeAndDate;
            timeLeft=timeLeft/1000;

            boolean isNegative=false;
            if(timeLeft<0)
            {
                isNegative=true;
                timeLeft*=-1;
            }

            final long finalTimeLeft = timeLeft;
            boolean finalIsNegative = isNegative;
            ((MainActivity)context).runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    String hour=finalTimeLeft /3600+"";
                    String min=(((finalTimeLeft /60)%60)>9 ? ((finalTimeLeft /60)%60) +"" : ("0"+((finalTimeLeft /60)%60)));
                    String sec=(finalTimeLeft %60>9 ? finalTimeLeft %60+"" : ("0"+finalTimeLeft %60));

                    String time="";

                    if(finalIsNegative)
                    {
                        timerTV.setTextColor(Color.RED);
                        time+="- ";
                    }
                    else
                    {
                        timerTV.setTextColor(Color.BLACK);
                    }

                    time+=(hour.equals("0") ? "" : hour+":") + ((hour.equals("0")  && min.equals("00")) ? "" : min+":") + sec;
                    timerTV.setText(time);
                }
            });
        }
    }


    public void onDestroy()
    {
        stopTimer();
        context=null;
    }


}
