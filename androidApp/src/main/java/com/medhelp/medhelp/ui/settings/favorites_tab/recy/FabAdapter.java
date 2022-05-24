package com.medhelp.medhelp.ui.settings.favorites_tab.recy;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.medhelp.medhelp.R;
import com.medhelp.medhelp.data.model.settings.AllTabsResponse;

import java.util.List;

public class FabAdapter extends RecyclerView.Adapter<FabHolder> {
    private Context context;
    private List<AllTabsResponse> list;
    private FabRecyListener listener;

    public FabAdapter(Context context, List<AllTabsResponse> list, FabRecyListener listener)
    {
        this.context=context;
        this.list=list;
        this.listener=listener;
    }


    @NonNull
    @Override
    public FabHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.item_setting_fab,parent,false);
        return new FabHolder(view,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull FabHolder holder, int position) {
        holder.onBind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface FabRecyListener{
        void recyItemClick(AllTabsResponse item);
    }

    public void deleteItemList(AllTabsResponse item)
    {
        list.remove(item);
        notifyDataSetChanged();
    }
}
