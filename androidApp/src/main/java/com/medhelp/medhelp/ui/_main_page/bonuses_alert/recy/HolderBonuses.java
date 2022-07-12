package com.medhelp.medhelp.ui._main_page.bonuses_alert.recy;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.medhelp.medhelp.R;
import com.medhelp.medhelp.data.model._heritable.BonusesItemAndroid;


public class HolderBonuses extends RecyclerView.ViewHolder {
    TextView date;
    TextView value;

    BonusesItemAndroid data;
    Context context;

    public HolderBonuses(@NonNull View itemView) {
        super(itemView);

        date = itemView.findViewById(R.id.date);
        value = itemView.findViewById(R.id.value);

        context=itemView.getContext();
    }

    public void onBind(BonusesItemAndroid data)
    {
        this.data=data;

        date.setText(data.getDate());

        if(data.getStatus().equals("popoln"))
        {
            value.setText("+"+data.getValue()+"");
            date.setTextColor(ContextCompat.getColor(context, R.color.light_green));
            value.setTextColor(ContextCompat.getColor(context, R.color.light_green));
        }else if(data.getStatus().equals("snyatie")){
            value.setText("-"+data.getValue()+"");
            date.setTextColor(ContextCompat.getColor(context, R.color.red));
            value.setTextColor(ContextCompat.getColor(context, R.color.red));
        }

    }
}
