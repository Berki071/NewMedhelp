package com.medhelp.medhelp.ui.view.shopping_basket.recy;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.medhelp.medhelp.R;
import com.medhelp.medhelp.data.model.YandexKey;
import com.medhelp.medhelp.ui.view.shopping_basket.sub.PaymentData;
import com.medhelp.newmedhelp.model.VisitResponse;
import com.medhelp.newmedhelp.model.VisitResponseAndroid;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

import java.util.List;

public class ShoppingBasketTitleViewHolder extends GroupViewHolder {
   // private String sum;

    private TextView title;
    private Button pay;
    private ImageView img;

    List<VisitResponseAndroid> list;
    YandexKey yKeys;
    String idBranch;
    TitleListener titleListener;

    public ShoppingBasketTitleViewHolder(View itemView,TitleListener titleListener) {
        super(itemView);
        this.titleListener=titleListener;


        title=itemView.findViewById(R.id.title);
        pay=itemView.findViewById(R.id.pay);
        img=itemView.findViewById(R.id.img);

        img.setVisibility(View.INVISIBLE);
        pay.setVisibility(View.VISIBLE);

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sum=getSumList(list);
                String description=getDescription(list);

                PaymentData data= new PaymentData();
                data.setDescription(description);
                data.setKeys(yKeys);
                data.setSum(sum);
                data.setIdBranch(idBranch);
                data.setVisitList(list);
                titleListener.actionPay(data);
            }
        });
    }



    public void setData(List<VisitResponseAndroid> list, YandexKey yKeys, String idBranch)
    {
        this.list=list;
        this.yKeys=yKeys;
        this.idBranch=idBranch;

        title.setText("Платеж на сумму: " +getSumList(list)+"р.");

        if(list.get(0).getStatus().equals("p"))
        {
            setStatusPayPurchased();
        }
    }

    public void setStatusPayPurchased()
    {
        pay.setVisibility(View.INVISIBLE);
        img.setVisibility(View.VISIBLE);
    }

    @Override
    public void expand() {
        //title.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_up, 0);
    }

    @Override
    public void collapse() {
        //title.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_down, 0);
    }

    private String getSumList(List<VisitResponseAndroid> list)
    {
        int sum=0;
        for(VisitResponse tmp : list)
        {
            sum+=tmp.getPrice();
        }

        return String.valueOf(sum);
    }

    private String getDescription(List<VisitResponseAndroid> list)
    {
        String description="";
        for(VisitResponse vr : list)
        {
            if(!description.equals(""))
            {
                description+=", ";
            }
            description+=vr.getNameServices();
        }
        //Transliteration trl=new Transliteration(description);
       //return trl.getModStr();
        return description;
    }

    public interface TitleListener {
        void actionPay( PaymentData data);
    }
}
