package com.medhelp.medhelp.ui.schedule.recycler;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.medhelp.medhelp.R;

public class HeaderViewHolder extends RecyclerView.ViewHolder {
    public final View rootView;
    public final TextView tvTitle;
    public final ImageView ico;

    public View getRootView() {
        return rootView;
    }

    public TextView getTvTitle() {
        return tvTitle;
    }

    public HeaderViewHolder(View view) {
        super(view);
        rootView = view;
        tvTitle = view.findViewById(R.id.tv_profile_item_title);
        ico=view.findViewById(R.id.ico);
    }
}