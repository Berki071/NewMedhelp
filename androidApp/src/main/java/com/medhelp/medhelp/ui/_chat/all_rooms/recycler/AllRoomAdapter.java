//package com.medhelp.medhelp.ui._chat.all_rooms.recycler;
//
//import android.content.Context;
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import com.medhelp.medhelp.R;
//import com.medhelp.medhelp.data.model.chat.InfoAboutDoc;
//
//import java.util.List;
//
//public class AllRoomAdapter extends RecyclerView.Adapter<AllRoomHolder> {
//    private List<InfoAboutDoc> list;
//    private Context context;
//    private String token;
//
//    public AllRoomAdapter(Context context,List<InfoAboutDoc> list,String token) {
//        this.context=context;
//        this.list=list;
//        this.token=token;
//    }
//
//    public void setList(List<InfoAboutDoc> list)
//    {
//        this.list=list;
//    }
//
//    @NonNull
//    @Override
//    public AllRoomHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        LayoutInflater inf=LayoutInflater.from(context);
//        View view =inf.inflate(R.layout.item_chat_all_rooms,parent,false);
//
//        return new AllRoomHolder(context, view, token);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull AllRoomHolder holder, int position) {
//        holder.onBindView(list.get(position));
//    }
//
//    @Override
//    public int getItemCount() {
//        return list.size();
//    }
//}
