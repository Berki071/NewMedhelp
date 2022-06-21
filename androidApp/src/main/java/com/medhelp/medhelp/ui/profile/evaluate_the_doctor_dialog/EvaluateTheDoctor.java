package com.medhelp.medhelp.ui.profile.evaluate_the_doctor_dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import androidx.core.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import com.medhelp.medhelp.R;
import com.medhelp.newmedhelp.model.VisitResponse;
import timber.log.Timber;

public class EvaluateTheDoctor {

    Context context;
    AlertDialog alertDialog;

    TextView title;
    RatingBar rating;
    TextInputLayout tilMsg;
    TextInputEditText message;
    Button close;
    Button send;

    VisitResponse info;

    public EvaluateTheDoctor(Context context, VisitResponse info)
    {
        this.context=context;
        this.info=info;

        LayoutInflater li=LayoutInflater.from(context);
        View view=li.inflate(R.layout.dialog_evaluate_the_doctor,null);

        title=view.findViewById(R.id.title);
        rating=view.findViewById(R.id.rating);
        tilMsg=view.findViewById(R.id.tilMsg);
        message=view.findViewById(R.id.message);
        close=view.findViewById(R.id.close);
        send=view.findViewById(R.id.send);

        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setView(view);

        alertDialog=builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();

        initData();
    }

    private void initData()
    {
        title.setText("Вы были на приеме у нашего специалиста, пожалуйста, оцените качество оказанных услуг"
        +"\n\n Доктор: "+info.getNameSotr()
                +"\n Дата: "+info.getDateOfReceipt());

        tilMsg.setVisibility(View.GONE);

        LayerDrawable stars = (LayerDrawable) rating.getProgressDrawable();
        stars.getDrawable(0).setColorFilter(ContextCompat.getColor(context, R.color.light_gray), PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(1).setColorFilter(ContextCompat.getColor(context, R.color.light_gray), PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(2).setColorFilter(ContextCompat.getColor(context, R.color.yellow), PorterDuff.Mode.SRC_ATOP);

        rating.setRating(0);
        send.setVisibility(View.GONE);

        rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                int r = ((int)rating);

                if((float)r!=rating) {
                    ratingBar.setRating((float) r+1);
                }
                else
                {
                    if (rating == 5) {
                        tilMsg.setVisibility(View.GONE);
                        send.setVisibility(View.VISIBLE);
                    } else if(rating!=0) {
                        tilMsg.setVisibility(View.VISIBLE);
                        message.requestFocus();
                        send.setVisibility(View.VISIBLE);
                    }else
                    {
                        send.setVisibility(View.GONE);
                        tilMsg.setVisibility(View.GONE);
                    }
                }
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg=getMsg();
                Timber.tag("my").d(msg);

                alertDialog.cancel();
            }
        });
    }


    private String getMsg()
    {
        String msg="RatingDoc ";
        msg+= (int) rating.getRating() +" star";

        if(message.getText().length()!=0)
        {
            msg+=", ";
            msg+=message.getText();
        }

        return msg;
    }

}
