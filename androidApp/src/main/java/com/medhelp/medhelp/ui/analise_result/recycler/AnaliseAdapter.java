package com.medhelp.medhelp.ui.analise_result.recycler;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.medhelp.medhelp.R;
import com.medhelp.medhelp.data.model.AnaliseResponse;

import java.util.List;

public class AnaliseAdapter extends RecyclerView.Adapter<AnaliseHolder> {

    private Context context;
    private List<AnaliseResponse> list;
    private AnaliseRecyItemListener recyItemListener;

    public AnaliseAdapter(Context context, List<AnaliseResponse> list, AnaliseRecyItemListener recyItemListener) {
        this.context = context;
        this.list = list;
        this.recyItemListener = recyItemListener;

        checkList();
    }

    @NonNull
    @Override
    public AnaliseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_analise, parent, false);
        return new AnaliseHolder(view, recyItemListener, context);
    }

    @Override
    public void onBindViewHolder(@NonNull AnaliseHolder holder, int position) {
        try {
            holder.onBindViewHolder(position,
                    getItem(position).getDate(),
                    getItem(position).isDownloadIn(),
                    getItem(position).isHideDownload(),
                    getItem(position).isShowTooltip());
        } catch (Exception e) {
            Log.wtf("fat", e.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private AnaliseResponse getItem(int num) {
        return list.get(num);
    }

    public void setList(List<AnaliseResponse> list) {
        this.list = list;
        checkList();
        notifyDataSetChanged();
    }

    public List<AnaliseResponse> getList() {
        return list;
    }

    public void checkList() {
        //подсказки, определение элемента от которого будет показываться

        boolean load = true;
        boolean delete = true;

        for (int i = 0; i < list.size(); i++) {
            if (!load && !delete) {
                break;
            }

            AnaliseResponse ar = list.get(i);

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


}
