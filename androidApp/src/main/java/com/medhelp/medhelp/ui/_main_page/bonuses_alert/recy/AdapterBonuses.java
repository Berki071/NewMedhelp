package com.medhelp.medhelp.ui._main_page.bonuses_alert.recy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.medhelp.medhelp.R;
import com.medhelp.medhelp.data.model.BonusesItem;

import java.util.List;

public class AdapterBonuses extends RecyclerView.Adapter<HolderBonuses> {
    Context context;
    List<BonusesItem> list;

    public AdapterBonuses(Context context, List<BonusesItem> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public HolderBonuses onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_bonuses,parent,false);
        return new HolderBonuses(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderBonuses holder, int position) {
        holder.onBind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
