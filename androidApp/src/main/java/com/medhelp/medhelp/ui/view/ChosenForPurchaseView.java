package com.medhelp.medhelp.ui.view;

import android.content.Context;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.medhelp.medhelp.R;
import com.medhelp.medhelp.data.model.VisitResponse;

import java.util.ArrayList;
import java.util.List;




public class ChosenForPurchaseView extends ConstraintLayout {
    private List<VisitResponse> items=new ArrayList<>();
    ChosenForPurchaseViewListener listener;
    boolean stateRootVisibility=true;

    ConstraintLayout root;
    TextView amountServices;
    TextView amountCash;
    ImageButton btnCross;

    private int limitItems=5;


    public ChosenForPurchaseView(Context context) {
        super(context);
        init(context);
    }

    public ChosenForPurchaseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }


    public ChosenForPurchaseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void setListener(ChosenForPurchaseViewListener listener)
    {
        this.listener=listener;
    }



    public void addItem(VisitResponse item)
    {
        items.add(item);
        testShowRootView();
        refreshDataAmount();

        if(items.size()>=limitItems)
        {
            listener.limitReached();
            Toast.makeText(getContext(),"Достигнуто ограничение на добавление в корзину", Toast.LENGTH_LONG).show();
        }
    }

    public void deleteItem(VisitResponse item)
    {
       if(items.size()>=limitItems)
       {
           listener.limitIsOver();
       }

        items.remove(item);
        testShowRootView();
        refreshDataAmount();
    }

    public void closeView()
    {
        items=new ArrayList<>();
        stateRootVisibility=false;
        root.setVisibility(View.GONE);
    }

    private void init(Context context)
    {
        View v=inflate(context, R.layout.view_chosen_for_purchase,this);

        root=v.findViewById(R.id.root);
        amountServices=v.findViewById(R.id.amountServices);
        amountCash=v.findViewById(R.id.amountCash);
        btnCross=v.findViewById(R.id.btnCross);

        root.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                root.setVisibility(View.GONE);
                listener.isShownView(false);
                stateRootVisibility=false;
                listener.onClickBtn(items);
                items=new ArrayList<>();
            }
        });

        btnCross.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                closeView();
                listener.isShownView(false);
                listener.onClickCross();
            }
        });

        testShowRootView();
    }

    private void testShowRootView()
    {
        if(items.size()==0)
        {
            if(!stateRootVisibility) {
                return;
            }

            stateRootVisibility=false;
            root.setVisibility(View.GONE);

            if(listener!=null)
                listener.isShownView(false);
        }
        else
        {
            if(stateRootVisibility==true) {
                return;
            }

            stateRootVisibility=true;
            root.setVisibility(View.VISIBLE);

            if(listener!=null)
                listener.isShownView(true);
        }
    }

    private void refreshDataAmount(){
        if(items.size()==0)
            return;

        int services;
        int cash=0;

        services=items.size();

        for(VisitResponse tmp : items)
        {
            cash+=tmp.getPrice();
        }

        amountServices.setText(services+"");
        amountCash.setText(cash+" руб.");
    }


    public interface ChosenForPurchaseViewListener{
        void isShownView(Boolean boo);
        void onClickBtn(List<VisitResponse> items);
        void onClickCross();
        void limitReached();
        void limitIsOver();
    }

    private String wordDeclension(int number)
    {
        if(number>20)
        {
            number%=10;
        }

        switch (number) {
            case 1:
                return " позицию";
            case 2:
                return " позиции";
            case 3:
                return " позиции";
            case 4:
                return " позиции";
            case 5:
                return " позиций";
            case 6:
                return " позиций";
            case 7:
                return " позиций";
            case 8:
                return " позиций";
            case 9:
                return " позиций";
            case 10:
                return " позиций";
            case 11:
                return " позиций";
            case 12:
                return " позиций";
            case 13:
                return " позиций";
            case 14:
                return " позиций";
            case 15:
                return " позиций";
            case 16:
                return " позиций";
            case 17:
                return " позиций";
            case 18:
                return " позиций";
            case 19:
                return " позиций";
            case 20:
                return " позиций";
            default:
                return " позиций";
        }
    }

}
