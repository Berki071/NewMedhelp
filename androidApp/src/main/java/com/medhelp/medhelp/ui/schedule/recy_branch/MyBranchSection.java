package com.medhelp.medhelp.ui.schedule.recy_branch;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import com.medhelp.medhelp.R;
import com.medhelp.shared.model.SettingsAllBranchHospitalResponse;
import java.util.List;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;

public class MyBranchSection extends StatelessSection {
    List<SettingsAllBranchHospitalResponse> list;
    ItemBranchListener listener;
    Context context;

    public MyBranchSection(Context context,List<SettingsAllBranchHospitalResponse> list,ItemBranchListener listener) {
        super(new SectionParameters.Builder(R.layout.item_section_branch)
                .headerResourceId(R.layout.item_section_branch_title_empty)
                .build());

        this.list=list;
        this.listener=listener;
        this.context=context;
    }

    @Override
    public int getContentItemsTotal() {
        return list.size();
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        return new BranchItemHolder(context,view,listener);
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        BranchItemHolder itemHolder = (BranchItemHolder) holder;

        // bind your view here
        itemHolder.onBind(list.get(position));
    }

    public interface ItemBranchListener{
        void onClick(SettingsAllBranchHospitalResponse branch);

    }

    public void selectNewItem(SettingsAllBranchHospitalResponse branch)
    {
        for(SettingsAllBranchHospitalResponse itm  :  list)
        {
            itm.setFavorite(false);
        }

        branch.setFavorite(true);
    }
}
