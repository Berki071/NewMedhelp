//package com.medhelp.medhelp.ui._chat.room.recycler;
//
//import android.content.Context;
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import com.medhelp.medhelp.R;
//import com.medhelp.medhelp.data.model.chat.Message;
//import com.medhelp.medhelp.ui._chat.room.RoomActivity;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class RoomAdapter extends RecyclerView.Adapter {
//    private Context contex;
//    private List<Message> list;
//    private List<Message> recyList=new ArrayList<>();
//    private String token;
//
//    private RoomActivity.RecyListener recyListener;
//
//
//    private final int visibleThreshold=10;
//
//    private boolean isLoading=false;
//
//    public RoomAdapter(Context contex, List<Message> listt, RoomActivity.RecyListener recyListener, RecyclerView recy, String token) {
//        this.contex = contex;
//        this.list = listt;
//        this.recyListener=recyListener;
//        this.token=token;
//
//        addItemToRecyList();
//
//        LinearLayoutManager llm=(LinearLayoutManager) recy.getLayoutManager();
//
//        recy.setOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//
//                int visibleItemCount = recyclerView.getChildCount();
//                int totalItemCount = recyclerView.getLayoutManager().getItemCount();
//                int firstVisibleItem = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
//
//                if(!isLoading  && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold))
//                {
//                    if(recyList.size()!=list.size())
//                    {
//                        isLoading=true;
//                        addItemToRecyList();
//                        notifyDataSetChanged();
//                        isLoading=false;
//                    }
//                }
//            }
//
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                ((RoomActivity)contex).latchScrollToEnd=false;
//            }
//        });
//
//    }
//
//    private void addItemToRecyList()
//    {
//        int start=list.size()- recyList.size();
//
//        if(start==0)
//            return;
//
//        --start;
//
//        int end=start-visibleThreshold<0  ?  0  :  start-visibleThreshold;
//
//        for(int i=start;i>=end;i--)
//        {
//            recyList.add(list.get(i));
//        }
//
//    }
//
//
////    @NonNull
////    @Override
////    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
////        return null;
////    }
////
////    @Override
////    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
////
////    }
//
//    @Override
//    public int getItemViewType(int position) {
//
//        switch (recyList.get(position).type) {
//            case Message.DATE:
//                return Message.DATE;
//            case Message.MSG:
//                return Message.MSG;
//            case Message.Img:
//                return Message.Img;
//
//            default:
//                return -1;
//        }
//    }
//
//
//    @NonNull
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view;
//
//        switch(viewType)
//        {
//            case(Message.DATE):
//                view= LayoutInflater.from(contex).inflate(R.layout.item_chat_msg_date,parent,false);
//                return new RoomHolderTitle(view);
//
//            case(Message.MSG):
//                view= LayoutInflater.from(contex).inflate(R.layout.item_chat_msg,parent,false);
//                return new RoomHolderMsg(contex,view);
//
//            case(Message.Img):
//                view= LayoutInflater.from(contex).inflate(R.layout.item_chat_img,parent,false);
//                return new RoomHolderImg(contex,view,recyListener,token);
//        }
//
//        return null;
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
//
//        int type=recyList.get(position).getType();
//
//        switch(type)
//        {
//            case Message.DATE:
//                ((RoomHolderTitle)holder).onBinView(recyList.get(position));
//                break;
//
//            case Message.MSG:
//                ((RoomHolderMsg)holder).onBinView(recyList.get(position));
//                break;
//
//            case Message.Img:
//                ((RoomHolderImg)holder).onBinView(recyList.get(position));
//                break;
//        }
//    }
//
//
//    @Override
//    public int getItemCount() {
//        return recyList.size();
//    }
//
//    public void addRealmMessage(Message msg)
//    {
//        list.add(msg);
//        recyList.add(msg);
//        notifyDataSetChanged();
//       // notifyItemChanged(recyList.size()-1);
//    }
//
//    public void updateList(List<Message> list)
//    {
//        this.list=list;
//        recyList=new ArrayList<>();
//        addItemToRecyList();
//        notifyDataSetChanged();
//        isLoading=false;
//    }
//
//    public List<Message> getMainList()
//    {
//        return list;
//    }
//}
