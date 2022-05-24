package com.medhelp.medhelp.ui.analise_list.recy_folder;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.medhelp.medhelp.R;
import com.medhelp.medhelp.data.model.AnaliseListData;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

public class ALAdapter extends ExpandableRecyclerViewAdapter<ALGroupHolder, ALAnaliseHolder> {
    private Context context;
    private List<? extends ExpandableGroup> list;



    public ALAdapter(Context context ,List<? extends ExpandableGroup> groups) {
        super(groups);
        this.context=context;
        list=groups;

        checkList();
    }

    @Override
    public ALGroupHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.item_groupe, parent, false);
        return new ALGroupHolder(view);
    }

    @Override
    public ALAnaliseHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.item_analise_list, parent, false);
        return new ALAnaliseHolder(context,view);
    }

    @Override
    public void onBindChildViewHolder(ALAnaliseHolder holder, int flatPosition, ExpandableGroup group, int childIndex) {
        AnaliseListData analiseItem = (AnaliseListData)group.getItems().get(childIndex);
        holder.onBind(analiseItem);

        if(childIndex==0)
        {
            holder.testShowTooltip(true);
        }
        else
        {
            holder.testShowTooltip(false);
        }
    }

    @Override
    public void onBindGroupViewHolder(ALGroupHolder holder, int flatPosition, ExpandableGroup group) {
        holder.setTitle(group);
    }

    private void checkList()
    {

        for (int j = 0; j < list.size(); j++) {

            boolean latchFalse = true;
            boolean latchTrue = true;

            ExpandableGroup gr=list.get(j);

            for (int i = 0; i < gr.getItems().size(); i++) {
                if(!latchFalse  &&  !latchTrue)
                    break;

                AnaliseListData data = (AnaliseListData) gr.getItems().get(i);

                if (data.getStatus().toLowerCase().equals(ALAnaliseHolder.YES) ) {

                    if(latchTrue) {
                        latchTrue = false;
                        data.setShowTooltip(true);
                    }
                    else
                    {
                        data.setShowTooltip(false);
                    }
                }

                if (!data.getStatus().toLowerCase().equals(ALAnaliseHolder.YES)) {

                    if(latchFalse) {
                        latchFalse = false;
                        data.setShowTooltip(true);
                    }
                    else
                    {
                        data.setShowTooltip(false);
                    }
                }
            }
        }
    }
}
