package com.medhelp.medhelp.ui.finances_and_services.recy;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.medhelp.medhelp.R;
import com.medhelp.medhelp.data.model.VisitResponse;
import com.medhelp.medhelp.utils.main.TimesUtils;
import com.medhelp.shared.model.CenterResponse;

public class FinancesHolder extends RecyclerView.ViewHolder {
    private Context context;
    private VisitResponse item;

    private final String addToPayment ="Добавить к оплате";
    private final String inBasket ="В корзине";


    private TextView date;
    private TextView services;
    private TextView price;
    private ImageView ruble;
    private Button btnBasket;

    private FinancesHolderListener listener;
    private CenterResponse centerResponse;
    private String time;
    private String today;

    public FinancesHolder(View itemView, FinancesHolderListener listener, CenterResponse centerResponse,String time , String today) {
        super(itemView);

        this.context=itemView.getContext();
        this.listener=listener;
        this.centerResponse=centerResponse;
        this.time=time;
        this.today=today;


        date=itemView.findViewById(R.id.date);
        services=itemView.findViewById(R.id.services);
        price=itemView.findViewById(R.id.price);
        ruble=itemView.findViewById(R.id.imgRuble);
        btnBasket=itemView.findViewById(R.id.btnBasket);

        btnBasket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btnBasket.getText().equals(addToPayment))
                {
                    item.setAddInBasket(true);
                    btnBasket.setText(inBasket);
                    listener.toBasket(item,true);
                } else {
                    item.setAddInBasket(false);
                    btnBasket.setText(addToPayment);
                    listener.toBasket(item,false);
                }

            }
        });
    }

    public void onBind(VisitResponse item, boolean blockBasket)
    {
        this.item=item;
        date.setText(item.getDateOfReceipt());
        services.setText(item.getNameServices());
        price.setText(item.getPrice()+"");


//        btnBasket.setVisibility(View.VISIBLE);
//        btnBasket.setText(addToPayment);
//        ruble.setImageResource(R.drawable.rubl_red);

        if(!(item.getStatus().equals("p")|| item.getStatus().equals("wkp") || item.getStatus().equals("kpp")))
        {
            if(testDate())
            {
                btnBasket.setVisibility(View.VISIBLE);
                btnBasket.setText(addToPayment);
                ruble.setImageResource(R.drawable.rubl_red);
            }
            else
            {
                btnBasket.setVisibility(View.GONE);
                ruble.setImageResource(R.drawable.rubl_red);
            }


        }
        else
        {
            btnBasket.setVisibility(View.GONE);
            ruble.setImageResource(R.drawable.rubl);

        }


        if(item.isAddInBasket())
        {
            btnBasket.setText(inBasket);
        }
        else
        {
            btnBasket.setText(addToPayment);
        }

        if(blockBasket  && !item.isAddInBasket())
        {
            btnBasket.setEnabled(false);
            btnBasket.setBackgroundResource(R.drawable.rounded_bg_inactive_24dp);
        }
        else
        {
            btnBasket.setEnabled(true);
            btnBasket.setBackgroundResource(R.drawable.rounded_bg_primary_24dp);
        }
    }

    private boolean testDate()
    {
        Long analysis = TimesUtils.stringToLong(/*item.getTimeOfReceipt()+" "+*/item.getDateOfReceipt(),TimesUtils.DATE_FORMAT_ddMMyyyy);
        Long tDay = TimesUtils.stringToLong(/*time+" "+*/today, TimesUtils.DATE_FORMAT_ddMMyyyy);
        //int timeTo=1000*60*centerResponse.getTimeForDenial();

        return analysis >= tDay;
    }


    public interface FinancesHolderListener{
        void toBasket(VisitResponse item,boolean toPay);
    }
}
