//package com.medhelp.medhelp.ui._chat.started_chats.recycler;
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
//import com.medhelp.medhelp.data.model.chat.Message;
//import com.medhelp.medhelp.data.model.chat.Room;
//import com.medhelp.medhelp.ui._chat.room.RoomActivity;
//import com.medhelp.medhelp.ui._main_page.MainActivity;
//import com.medhelp.medhelp.utils.workToFile.show_file.ShowFile2;
//import com.medhelp.medhelp.utils.main.TimesUtils;
//
//import java.io.File;
//
//
//
//import de.hdodenhof.circleimageview.CircleImageView;
//
//public class StartedChatsHolder extends RecyclerView.ViewHolder {
//    public StartedChatsHolder(@NonNull View itemView) {
//        super(itemView);
//    }
//
//    CircleImageView ico;
//    TextView name;
//    TextView lastMsg;
//    TextView date;
//    TextView time;
//
//    private Context context;
//    private Room room;
//
//    private String token;
//
//    public StartedChatsHolder(Context context, View itemView, String token) {
//        super(itemView);
//
//        this.context=context;
//        this.token=token;
//
//        ico=itemView.findViewById(R.id.ico);
//        name=itemView.findViewById(R.id.name);
//        lastMsg=itemView.findViewById(R.id.lastMsg);
//        date=itemView.findViewById(R.id.date);
//        time=itemView.findViewById(R.id.time);
//
//        itemView.setOnClickListener(v -> showRoomActivity());
//    }
//
//    public void onBindView(Room room)
//    {
//        this.room=room;
//        name.setText(room.getInfoAboutDoc().getName());
//
//        if(room.getListMsg().get(room.getListMsg().size()-1).getType()== Message.MSG)
//            lastMsg.setText(room.getListMsg().get(room.getListMsg().size()-1).getMsg());
//        else {
//            lastMsg.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_insert_photo_white_18dp, 0, 0, 0);
//            lastMsg.setText(" Изображение");
//        }
//
//        if(room.getListMsg().get(room.getListMsg().size()-1).getTimeUtc()!=0) {
//            String d=TimesUtils.UtcLongToStrLocal(room.getListMsg().get(room.getListMsg().size() - 1).getTimeUtc());
//
//            date.setText(TimesUtils.getNewFormatString(d, TimesUtils.DATE_FORMAT_HHmmss_ddMMyyyy, TimesUtils.DATE_FORMAT_ddMMyyyy));
//            time.setText(TimesUtils.getNewFormatString(d, TimesUtils.DATE_FORMAT_HHmmss_ddMMyyyy, TimesUtils.DATE_FORMAT_HHmm));
//        }
//        else {
//            date.setText("...");
//            time.setText("...");
//        }
//
//        new ShowFile2.BuilderImage(ico.getContext())
//                .setType(ShowFile2.TYPE_ICO)
//                .load(room.getInfoAboutDoc().getImgLink())
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
//                    }
//                })
//                .build();
//    }
//
//    private void showRoomActivity()
//    {
//        Intent intent=new Intent(context, RoomActivity.class);
//        intent.putExtra(InfoAboutDoc.class.getCanonicalName(),room.getInfoAboutDoc());
//        context.startActivity(intent);
//        ((MainActivity)context).finish();
//    }
//
//
//}
