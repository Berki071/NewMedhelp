package com.medhelp.medhelp.ui.profile.allertDialog;


import android.content.Context;
import androidx.appcompat.app.AlertDialog;

public class AlertForConfirmation {
    //private Context context;
    private AlertDialog alertDialog;
    private mOnClickListener listener;

    public AlertForConfirmation(Context context, mOnClickListener listener){
        //this.context = context;
        this.listener=listener;

        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setMessage("Подтвердить выбор")
                .setPositiveButton("Да", (dialog, which) -> listener.positiveClickButton())
                .setNegativeButton("Нет", (dialog, which) -> dialog.cancel());

        alertDialog=builder.create();
        alertDialog.show();
    }


    public interface mOnClickListener{
        void positiveClickButton();

    }
}
