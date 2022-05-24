package com.medhelp.medhelp.ui.schedule.recycler;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.medhelp.medhelp.R;

public class ItemViewHolder extends RecyclerView.ViewHolder {
    public final View rootView;
    public final TextView tvItem;

    public ItemViewHolder(View view) {
        super(view);
        rootView = view;
        tvItem = view.findViewById(R.id.tv_date_item_row);
    }
}