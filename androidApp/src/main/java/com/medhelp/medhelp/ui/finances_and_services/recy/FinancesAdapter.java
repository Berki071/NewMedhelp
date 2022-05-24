package com.medhelp.medhelp.ui.finances_and_services.recy;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.medhelp.medhelp.R;
import com.medhelp.medhelp.data.model.VisitResponse;
import com.medhelp.medhelp.data.pref.PreferencesManager;
import com.medhelp.shared.model.CenterResponse;

import java.util.List;

public class FinancesAdapter extends RecyclerView.Adapter<FinancesHolder> {
    private Context context;
    private List<VisitResponse> list;
    private FinancesHolder.FinancesHolderListener listener;

    private CenterResponse centerResponse;
    private String time;
    private String today;

    private boolean blockBasket=false;

    public FinancesAdapter(Context context, List<VisitResponse> list,FinancesHolder.FinancesHolderListener listener, String time , String today)
    {
        this.context=context;
        this.list=list;
        this.listener=listener;
        this.time=time;
        this.today=today;

        PreferencesManager ph= new PreferencesManager(context);
        centerResponse= ph.getCenterInfo();
    }


    @NonNull
    @Override
    public FinancesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.item_pay_fake,parent,false);
        return new FinancesHolder(view,listener,centerResponse,time,today);
    }

    @Override
    public void onBindViewHolder(@NonNull FinancesHolder holder, int position) {
        holder.onBind(list.get(position),blockBasket);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setInaccessibleBtnPay()
    {
        blockBasket=true;
        notifyDataSetChanged();
    }
    public void setAffordableBtnPay()
    {
        blockBasket=false;
        notifyDataSetChanged();
    }

}
