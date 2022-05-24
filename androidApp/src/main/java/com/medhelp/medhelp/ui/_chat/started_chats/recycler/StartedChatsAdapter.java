//package com.medhelp.medhelp.ui._chat.started_chats.recycler;
//
//import android.content.Context;
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import com.medhelp.medhelp.R;
//import com.medhelp.medhelp.data.model.chat.Room;
//
//import java.util.List;
//
//public class StartedChatsAdapter extends RecyclerView.Adapter<StartedChatsHolder> {
//    private List<Room> list;
//    private Context context;
//    private String token;
//
//    public StartedChatsAdapter(Context context, List<Room> list, String token) {
//        this.context=context;
//        this.list=list;
//        this.token=token;
//    }
//
//    public void setList(List<Room> list)
//    {
//        this.list=list;
//    }
//
//    @NonNull
//    @Override
//    public StartedChatsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        LayoutInflater inf=LayoutInflater.from(context);
//        View view =inf.inflate(R.layout.item_chat_current_rooms,parent,false);
//
//        return new StartedChatsHolder(context, view, token);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull StartedChatsHolder holder, int position) {
//        holder.onBindView(list.get(position));
//    }
//
////    @NonNull
////    @Override
////    public StartedChatsHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
////        return null;
////    }
////
////    @Override
////    public void onBindViewHolder(@NonNull StartedChatsHolder startedChatsHolder, int i) {
////
////    }
//
//    @Override
//    public int getItemCount() {
//        return list.size();
//    }
//}
