package com.medhelp.medhelp.ui.settings.favorites_tab.recy;

import android.content.Context;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.medhelp.medhelp.R;
import com.medhelp.medhelp.data.model.settings.AllTabsResponse;

public class FabHolder extends RecyclerView.ViewHolder {
    private Context context;
    private AllTabsResponse item;

    private TextView title;
    private ConstraintLayout loadingRoot;

    private FabAdapter.FabRecyListener listener;

    public FabHolder(View itemView,FabAdapter.FabRecyListener listener) {
        super(itemView);

        this.context=itemView.getContext();
        this.listener=listener;

        title=itemView.findViewById(R.id.title);
        loadingRoot=itemView.findViewById(R.id.loadingRoot);

        itemView.setOnClickListener(v -> listener.recyItemClick(item));
    }

    public void onBind(AllTabsResponse item)
    {
        this.item=item;
        title.setText(item.getTitle());

        if(this.item.isLoading())
        {
            loadingRoot.setVisibility(View.VISIBLE);
        }
        else
        {
            loadingRoot.setVisibility(View.GONE);
        }
    }
}
