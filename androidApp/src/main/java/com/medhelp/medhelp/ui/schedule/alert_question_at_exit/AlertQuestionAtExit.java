package com.medhelp.medhelp.ui.schedule.alert_question_at_exit;

import android.content.Context;
import com.google.android.material.textfield.TextInputEditText;
import androidx.appcompat.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import com.medhelp.medhelp.R;

public class AlertQuestionAtExit {

    RadioGroup radioGroup;
    TextInputEditText ownVersion;
    CheckedTextView doNotShow;
    RadioButton rb1;
    RadioButton rb2;
    RadioButton rb3;
    RadioButton rb4;
    Button close;
    Button send;

    AlertDialog alertDialog;

    Context context;
    View mainView;

    AlertQuestionAtExitPresenter presenter;
    ExitListener listener;

    public AlertQuestionAtExit(Context context,ExitListener listener)
    {
        this.context=context;
        this.listener=listener;

        LayoutInflater li=LayoutInflater.from(context);
        mainView=li.inflate(R.layout.dialog_question_at_exit, null);

        initValue(mainView);

        initButtonClick();

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(mainView);
        alertDialog=builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();

        presenter=new AlertQuestionAtExitPresenter(context);
    }
    private void initValue(View v){
        radioGroup=v.findViewById(R.id.radioGroup);
        ownVersion=v.findViewById(R.id.ownVersion);
        doNotShow=v.findViewById(R.id.doNotShow);
        rb1=v.findViewById(R.id.rb1);
        rb2=v.findViewById(R.id.rb2);
        rb3=v.findViewById(R.id.rb3);
        rb4=v.findViewById(R.id.rb4);
        close=v.findViewById(R.id.close);
        send=v.findViewById(R.id.send);
    }

    private void initButtonClick() {

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                ownVersion.clearFocus();
                InputMethodManager imm = (InputMethodManager) ownVersion.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(ownVersion.getWindowToken(), 0);
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listener.alertClose();
                alertDialog.cancel();
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg= getDataToSend();
                if(msg.length()>0)
                {
                    String tmp="QuestionAtExit ";
                    tmp+=msg;
                    presenter.sendToServerMsg(tmp);
                }
                listener.alertClose();
                alertDialog.cancel();
            }
        });

        doNotShow.setChecked(false);
        doNotShow.setCheckMarkDrawable(android.R.drawable.checkbox_off_background);
        doNotShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doNotShow.setChecked(!doNotShow.isChecked());
                doNotShow.setCheckMarkDrawable(doNotShow.isChecked() ? android.R.drawable.checkbox_on_background : android.R.drawable.checkbox_off_background);
                presenter.setDoNotShowAlertExit(doNotShow.isChecked());

                ownVersion.clearFocus();
                InputMethodManager imm = (InputMethodManager) ownVersion.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(ownVersion.getWindowToken(), 0);
            }
        });

        ownVersion.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_DOWN)
                {
                    radioGroup.clearCheck();
                }

                return false;
            }
        });
    }

    private String getDataToSend()
    {
        String str="";

        if(radioGroup.getCheckedRadioButtonId()!=-1)
        {
            RadioButton myRadioButton = (RadioButton) mainView.findViewById(radioGroup.getCheckedRadioButtonId());
            str=myRadioButton.getText().toString();
        }

        if(str.length()!=0 && ownVersion.getText().length()!=0)
        {
            str+=" & ";
        }

        str+=ownVersion.getText();

        return str;
    }

    public interface ExitListener{
        void alertClose();
    }
}
