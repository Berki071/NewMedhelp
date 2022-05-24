//package com.medhelp.medhelp.ui._chat.all_rooms.recycler;
//
//import android.content.Context;
//import android.content.Intent;
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//import android.view.View;
//import android.widget.TextView;
//
//import com.medhelp.medhelp.R;
//import com.medhelp.medhelp.data.model.chat.InfoAboutDoc;
//import com.medhelp.medhelp.ui._chat.all_rooms.AllRoomsActivity;
//import com.medhelp.medhelp.ui._chat.room.RoomActivity;
//import com.medhelp.medhelp.utils.workToFile.show_file.ShowFile2;
//
//import java.io.File;
//
//
//
//import de.hdodenhof.circleimageview.CircleImageView;
//
//public class AllRoomHolder extends RecyclerView.ViewHolder {
//    public AllRoomHolder(@NonNull View itemView) {
//        super(itemView);
//    }
//    CircleImageView ico;
//    TextView name;
//
//    private Context context;
//    private InfoAboutDoc inf;
//
//    private String token;
//
//    public AllRoomHolder(Context context, View itemView, String token) {
//        super(itemView);
//
//        this.context=context;
//        this.token=token;
//        ico=itemView.findViewById(R.id.ico);
//        name=itemView.findViewById(R.id.name);
//
//        itemView.setOnClickListener(click->
//            showRoomActivity()
//        );
//    }
//
//    public void onBindView(InfoAboutDoc inf)
//    {
//        this.inf=inf;
//        name.setText(inf.getName());
//
//        new ShowFile2.BuilderImage(ico.getContext())
//                .setType(ShowFile2.TYPE_ICO)
//                .load(inf.getImgLink())
//                .token(token)
//                .imgError(R.drawable.sh_doc)
//                .into(ico)
//                .setListener(new ShowFile2.ShowListener() {
//                    @Override
//                    public void complete(File file) {
//                    }
//
//                    @Override
//                    public void error(String error) {
//
//                    }
//                })
//                .build();
//    }
//
//    private void showRoomActivity()
//    {
//        Intent intent=new Intent(context, RoomActivity.class);
//        intent.putExtra(InfoAboutDoc.class.getCanonicalName(),inf);
//        //intent.putExtra(ShowImage.KEY_SCROLL,0);
//        context.startActivity(intent);
//        ((AllRoomsActivity)context).finish();
//    }
//}
