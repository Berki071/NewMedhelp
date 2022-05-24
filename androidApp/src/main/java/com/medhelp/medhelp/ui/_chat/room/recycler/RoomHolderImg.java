//package com.medhelp.medhelp.ui._chat.room.recycler;
//
//import android.content.Context;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.TextView;
//import androidx.annotation.NonNull;
//import androidx.constraintlayout.widget.ConstraintLayout;
//import androidx.recyclerview.widget.RecyclerView;
//import com.medhelp.medhelp.R;
//import com.medhelp.medhelp.data.model.chat.Message;
//import com.medhelp.medhelp.ui._chat.room.RoomActivity;
//import com.medhelp.medhelp.utils.main.MainUtils;
//import com.medhelp.medhelp.utils.main.TimesUtils;
//import com.medhelp.medhelp.utils.workToFile.show_file.ShowFile2;
//import java.io.File;
//
//
//
//public class RoomHolderImg extends RecyclerView.ViewHolder {
//    public RoomHolderImg(@NonNull View itemView) {
//        super(itemView);
//    }
//    private Context context;
//    private Message message;
//
//    ConstraintLayout constraint;
//    ImageView img;
//    TextView time;
//    ConstraintLayout fr;
//
//    private RoomActivity.RecyListener recyListener;
//
//    private String token;
//
//    public RoomHolderImg(Context context,View itemView, RoomActivity.RecyListener recyListener, String token) {
//        super(itemView);
//
//        this.context=context;
//        this.token=token;
//
//        constraint=itemView.findViewById(R.id.constraint);
//        img=itemView.findViewById(R.id.img);
//        time=itemView.findViewById(R.id.time);
//        fr=itemView.findViewById(R.id.fr);
//
//        this.recyListener=recyListener;
//    }
//
//    public void onBinView(Message message)
//    {
//        this.message=message;
//
//        setTime();
//        setImage();
//        tuningView();
//
//        img.setOnClickListener(ocl);
//    }
//
//    public View.OnClickListener ocl=new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            recyListener.clickedTheButton(message.getMsg(), message.getId());
//        }
//    };
//
//    private void setImage() {
//
//        if (message == null || message.getMsg() == null || message.getMsg().equals(""))
//            return;
//
//        fr.post(() -> {
//
//            File f = new File(message.getMsg());
//            if (!f.exists())
//                return;
//
//            new ShowFile2.BuilderImageFile(img.getContext())
//                    .load(f)
//                    .into(img)
//                    .setListener(new ShowFile2.ShowListener() {
//                        @Override
//                        public void complete(File file) {
//                        }
//
//                        @Override
//                        public void error(String error) {
//                        }
//                    })
//                    .build();
//        });
//    }
//
//
//    private void setTime()
//    {
//        if(message.getTimeUtc()!=null  && message.getTimeUtc()!=0) {
//            String d = TimesUtils.UtcLongToStrLocal(message.getTimeUtc());
//            time.setText(TimesUtils.getNewFormatString(d,TimesUtils.DATE_FORMAT_HHmmss_ddMMyyyy,TimesUtils.DATE_FORMAT_HHmm));
//        }
//        else
//            time.setText("...");
//    }
//
//    private void tuningView()
//    {
//        if(message.isMyMsg())
//        {
//            constraint.setBackgroundResource(R.drawable.msg_right);
//
//            ConstraintLayout.LayoutParams lp= (ConstraintLayout.LayoutParams) constraint.getLayoutParams();
//            lp.setMargins(MainUtils.dpToPx(context,60),0,0,0);
//            //lp.gravity= Gravity.RIGHT;
//
//            constraint.setLayoutParams(lp);
//        }
//        else
//        {
//            constraint.setBackgroundResource(R.drawable.msg_left);
//
//            ConstraintLayout.LayoutParams lp= (ConstraintLayout.LayoutParams) constraint.getLayoutParams();
//            lp.setMargins(0,0, MainUtils.dpToPx(context,60),0);
//            //lp.gravity= Gravity.LEFT;
//
//            constraint.setLayoutParams(lp);
//        }
//    }
//}
