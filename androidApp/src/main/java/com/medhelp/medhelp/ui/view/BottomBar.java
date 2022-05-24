package com.medhelp.medhelp.ui.view;

import android.content.Context;
import android.content.res.Resources;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.medhelp.medhelp.R;


public class BottomBar extends ConstraintLayout {
    Button next;
    Button skip;
    LinearLayout dotBar;

    Context context;

    int totalDot;
    int current;

    BottomBarListener listener;

    public BottomBar(Context context) {
        super(context);
        init(context);
    }

    public BottomBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BottomBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }



    private void init(Context context)
    {
        this.context=context;
        inflate(context, R.layout.bottom_view_bar,this);

        next=findViewById(R.id.next);
        skip=findViewById(R.id.skip);
        dotBar=findViewById(R.id.dotBar);

        next.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ++current;
                arrangeDot(totalDot,current);
                listener.clickNext(current);
            }
        });

        skip.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                arrangeDot(totalDot, totalDot -1);
                listener.clickSkip();
            }
        });
    }


    public void setItemData(int cnt,BottomBarListener listener )
    {
        totalDot =cnt;
        this.listener=listener;
        current=0;
        arrangeDot(totalDot,current);
    }

    public void arrangeDot(int total, int current)
    {
        if(current>=total)
            return;

        if(current==total-1)
        {
            next.setEnabled(false);
            next.setVisibility(View.INVISIBLE);
        }
        else
        {
            next.setEnabled(true);
            next.setVisibility(View.VISIBLE);
        }

        totalDot=total;
        this.current=current;
        dotBar.removeAllViews();

        for(int i=0;i<total;i++) {
            ImageView tmp;
            if(i==current)
            {
                tmp = new ImageView(context);
                dotBar.addView(tmp);
                tmp.setImageResource(R.drawable.dot_full);

                ViewGroup.LayoutParams lp=tmp.getLayoutParams();
                lp.height = dpToPx(context, 14);
                lp.width = dpToPx(context, 14);
                tmp.setLayoutParams(lp);
            }
            else
            {
                tmp = new ImageView(context);
                dotBar.addView(tmp);
                tmp.setImageResource(R.drawable.dot_empty);
                tmp.getLayoutParams().height = dpToPx(context, 14);
                tmp.getLayoutParams().width = dpToPx(context, 14);
            }
        }
    }



    public int dpToPx(Context context , int num)
    {
        Resources r = context.getResources();
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                num,
                r.getDisplayMetrics());
    }

    public interface BottomBarListener{
        void clickNext(int current);
        void clickSkip();
    }

}
