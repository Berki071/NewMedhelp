//package com.medhelp.medhelp.ui._chat.room.recycler;
//
//import android.content.Context;
//import android.view.View;
//import android.widget.TextView;
//import androidx.annotation.NonNull;
//import androidx.constraintlayout.widget.ConstraintLayout;
//import androidx.recyclerview.widget.RecyclerView;
//import com.medhelp.medhelp.R;
//import com.medhelp.medhelp.data.model.chat.Message;
//import com.medhelp.medhelp.utils.main.MainUtils;
//import com.medhelp.medhelp.utils.main.TimesUtils;
//
//
//
//public class RoomHolderMsg extends RecyclerView.ViewHolder {
//    public RoomHolderMsg(@NonNull View itemView) {
//        super(itemView);
//    }
//    private Context context;
//    private Message message;
//
//    ConstraintLayout constraint;
//    TextView msg;
//    TextView time;
//
//    public RoomHolderMsg(Context context, View itemView) {
//        super(itemView);
//
//        this.context=context;
//        constraint=itemView.findViewById(R.id.constraint);
//        msg=itemView.findViewById(R.id.msg);
//        time=itemView.findViewById(R.id.time);
//    }
//
//    public void onBinView(Message msg)
//    {
//        this.message=msg;
//
//        setData();
//        tuningView();
//    }
//
//    private void setData()
//    {
//        msg.setText(message.getMsg());
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
