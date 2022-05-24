package com.medhelp.medhelp.ui.electronic_conclusions_fragment.adapters.recy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.medhelp.medhelp.R;
import com.medhelp.medhelp.data.model.AnaliseResponse;

import java.util.List;

import kotlin.Triple;

public class ElectronicConclusionsAdapter extends RecyclerView.Adapter<ElectronicConclusionsHolder> {
    Context context;
    List<DataClassForElectronicRecy> list;
    ElectronicConclusionsHolder.ElectronicConclusionsHolderListener listener;

    public ElectronicConclusionsAdapter(Context context, List<DataClassForElectronicRecy> list, ElectronicConclusionsHolder.ElectronicConclusionsHolderListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ElectronicConclusionsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.item_electronic_conclusions,parent,false);

        return new ElectronicConclusionsHolder(v,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ElectronicConclusionsHolder holder, int position) {
        holder.onBind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void checkList() {
        //подсказки, определение элемента от которого будет показываться

        boolean load = true;
        boolean delete = true;

        for (int i = 0; i < list.size(); i++) {
            if (!load && !delete) {
                break;
            }

            DataClassForElectronicRecy ar = list.get(i);

            if (ar.isDownloadIn()) {
                ar.setShowTooltip(load);

                if (load)
                    load = false;
            }

            if (!ar.isDownloadIn()) {
                ar.setShowTooltip(delete);

                if (delete)
                    delete = false;
            }

        }
    }

    public void updateItemInRecy(DataClassForElectronicRecy item){
        for(int i=0; i<list.size(); i++){
            if(list.get(i).getDate().equals(item.getDate())){
                notifyItemChanged(i);
            }
        }
    }

    public void processingShowHideBox(DataClassForElectronicRecy item){
        for(int i=0; i<list.size(); i++){
            if(!list.get(i).getDate().equals(item.getDate())){
                list.get(i).setShowHideBox(false);
            }
        }

        notifyDataSetChanged();
    }

}
