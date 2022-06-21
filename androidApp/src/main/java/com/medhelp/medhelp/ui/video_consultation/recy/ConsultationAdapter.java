package com.medhelp.medhelp.ui.video_consultation.recy;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.medhelp.medhelp.R;
import com.medhelp.newmedhelp.model.VisitResponse;
import java.util.List;


public class ConsultationAdapter extends RecyclerView.Adapter<ConsultationHolder> {
    private List<VisitResponse> list;
    private Context context;
    private String token;
    ConsultationListener listener;

    public ConsultationAdapter(Context context, List<VisitResponse> list, String token, ConsultationListener listener) {
        this.context=context;
        this.list=list;
        this.token=token;
        this.listener=listener;
    }

    public void setList(List<VisitResponse> list)
    {
        this.list=list;
    }

    @NonNull
    @Override
    public ConsultationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inf=LayoutInflater.from(context);
        View view =inf.inflate(R.layout.item_online_consultation,parent,false);

        return new ConsultationHolder(context, view, token, listener);
    }

//    @NonNull
//    @Override
//    public ConsultationHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
//        return null;
//    }

    @Override
    public void onBindViewHolder(@NonNull ConsultationHolder holder, int position) {
        holder.onBindView(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public long getItemId(int position) {
        return position;
    }

    public interface ConsultationListener{
        void onClickItemRecy(VisitResponse itm);
    }
}
