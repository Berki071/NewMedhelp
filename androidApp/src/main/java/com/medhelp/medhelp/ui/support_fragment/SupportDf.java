package com.medhelp.medhelp.ui.support_fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import com.medhelp.medhelp.R;
import com.medhelp.medhelp.utils.main.MainUtils;
import br.com.sapereaude.maskedEditText.MaskedEditText;


public class SupportDf extends DialogFragment {
    String startLogin;
    String startEmail;

    public void setData(String startLogin, String startEmail){
        this.startLogin=startLogin;
        this.startEmail=startEmail;
    }

    ConstraintLayout rootLayout;
    Toolbar toolbar;
    MaskedEditText et_login_username;
    EditText et_email;
    EditText et_msg;
    Button btnSend;

    SupportPresenter presenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.df_support,container,false);

        initValue(rootView);
        setSizeDialog();

        return rootView;
    }
    private void initValue(View v) {
        rootLayout=v.findViewById(R.id.rootLayout);
        toolbar=v.findViewById(R.id.toolbar);
        et_login_username=v.findViewById(R.id.et_login_username);
        et_email=v.findViewById(R.id.et_email);
        et_msg=v.findViewById(R.id.et_msg);
        btnSend=v.findViewById(R.id.btnSend);

        presenter=new SupportPresenter(this);
    }
    private void setSizeDialog(){
        Dialog dialog = getDialog();
        if (dialog != null)
        {
            int forWidthPix= getActivity().getWindow().getDecorView().getWidth()-(((int) MainUtils.dpToPx(getContext(), 16)) * 2);
            //int forHeightPix= getActivity().getWindow().getDecorView().getHeight()-(((int) MainUtils.dpToPx(getContext(), 64)) * 2);

            dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawableResource(R.color.transparent);

            rootLayout.getLayoutParams().width=forWidthPix;
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(startLogin!=null)
            et_login_username.setText(startLogin);

        if(startEmail!=null)
            et_email.setText(startEmail);

        setClickListener();
    }
    private void setClickListener() {
        toolbar.setNavigationOnClickListener( v -> {
            getDialog().dismiss();
        });

        et_login_username.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId== EditorInfo.IME_ACTION_NEXT)
                {
                    et_email.setFocusable(true);
                }
                return false;
            }
        });

        et_email.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId== EditorInfo.IME_ACTION_NEXT)
                {
                    et_msg.setFocusable(true);
                }
                return false;
            }
        });

        et_msg.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId== EditorInfo.IME_ACTION_DONE)
                {
                    clickSave();
                }
                return false;
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickSave();
            }
        });
    }

    public void hideKeyboard() {
        if(getDialog().getCurrentFocus()!=null && getDialog().getCurrentFocus().getWindowToken()!=null) {
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(getContext().INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getDialog().getCurrentFocus().getWindowToken(), 0);
        }
    }

    private void clickSave(){
        hideKeyboard();

        String loginTmp= presenter.getPhone(et_login_username.getText().toString());
        String emailTmp=et_email.getText().toString();
        String msgTmp=et_msg.getText().toString();

        if(loginTmp.length()!=10) {
            MainUtils.showAlertInfo(getContext(), "Ошибка!", "Телефон не указан или указан не корректно");
            return;
        }

        if(!MainUtils.isValidEmail(emailTmp)){
            MainUtils.showAlertInfo(getContext(), "Ошибка!", "Email не указан или указан не корректно");
            return;
        }

        if(msgTmp.length()<10){
            MainUtils.showAlertInfo(getContext(), "Ошибка!", "Сообщения нет или оно сильно короткое. Попробуйте написать более развернуто");
            return;
        }

        presenter.sendMsg(loginTmp,emailTmp,msgTmp);
    }

    public void showAlertError(){
        MainUtils.showAlertInfo(getContext(), "Ошибка!", getContext().getResources().getString(R.string.api_default_error));
    }

    public void showAlertSendComplete(){
        getDialog().dismiss();
        MainUtils.showAlertInfo(getContext(), "Отправлено успешно!", "Ваше обращение отправлено и будет обработано в рабочие часы техподдержки. Ответ будет предоставлен на указанный адрес электронной почты.");
    }

}
