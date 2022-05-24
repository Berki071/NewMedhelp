package com.medhelp.medhelp.ui.view.carouselWithUsers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.medhelp.medhelp.R;
import com.medhelp.shared.model.UserResponse;
import java.util.List;


public class MyPagerAdapter extends PagerAdapter {
    List<UserResponse> dataList;
    Context context;
    MyPagerListener listener;

    int colorLogoText;
    float sizeLogoPx;
    int width;
    int height;

    public MyPagerAdapter(Context context, List<UserResponse> dataList, MyPagerListener listener, int colorLogoText, float sizeLogoPx, int width, int height) {
        this.dataList = dataList;
        this.context=context;
        this.listener=listener;
        this.colorLogoText =colorLogoText;
        this.sizeLogoPx=sizeLogoPx;
        this.width=width;
        this.height=height;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_viewpager,null);
        view.setTag(position);
        ImageView image = view.findViewById(R.id.image);

        String initials = getInitials(position);
        image.setImageBitmap(creteImageIco(initials, position));

//        if(position!=(dataList.size()-1)) {
//            String initials = getInitials(position);
//            image.setImageBitmap(creteImageIco(initials, position));
//        }
//        else {
//            //Log.wtf("mLogg","MyPagerAdapter  position"+position);
//
//            image.setImageBitmap(createImageAdd(position));
//
//            view.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    //Log.wtf("mLogg","MyPagerAdapter  onClick");
//                    listener.onClickAdd();
//                }
//            });
//        }

        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
               // int t=(int)v.getTag();
//                if(t==(dataList.size()-1))
//                    listener.onClickAdd();
//                else
//                    listener.onLongClick();
                return true;
            }
        });

        container.addView(view);
        return view;
    }

    private String getInitials(int position)
    {
        UserResponse user=dataList.get(position);
        String initials="";

        if(user.getName()!=null && user.getName().length()>0)
            initials+=user.getName().substring(0,1);

        if(user.getPatronymic()!=null && user.getPatronymic().length()>0)
            initials+=user.getPatronymic().substring(0,1);
        return initials;
    }

    private Bitmap creteImageIco(String text, int position)
    {
//        final int width=150;
//        final int height =150;

        Bitmap bitmap= Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas=new Canvas(bitmap);

        Paint paint=new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(00000000);
        canvas.drawPaint(paint);

        paint.setColor(context.getResources().obtainTypedArray(R.array.carouselIcoBackground).getColor(position%5,0));
        canvas.drawCircle(width/ 2, height / 2, width/2, paint);

        Paint sp14Normal=new Paint();
        sp14Normal.setColor(colorLogoText);
        sp14Normal.setTextSize(sizeLogoPx);
        sp14Normal.setAntiAlias(true);

        float widthText = sp14Normal.measureText(text);
        Rect bounds = new Rect();
        sp14Normal.getTextBounds(text, 0, text.length(), bounds);
        int heightText = bounds.height();
        canvas.drawText(text,(bitmap.getWidth() - widthText)/2, ((bitmap.getHeight() - heightText)/2)+ heightText, sp14Normal);

        return bitmap;
    }

    private Bitmap createImageAdd(int position)
    {
//        final int width=100;
//        final int height =100;

        Bitmap bitmap= Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas=new Canvas(bitmap);

        Paint paint=new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(00000000);
        canvas.drawPaint(paint);

        paint.setColor(context.getResources().obtainTypedArray(R.array.carouselIcoBackground).getColor(position%5,0));
        canvas.drawCircle(width/ 2, height / 2, width/2, paint);

        Paint paintText =new Paint();
        paintText.setColor(colorLogoText);
        paintText.setTextSize(spToPx(60));
        paintText.setAntiAlias(true);

        float widthText = paintText.measureText("+");

        Rect bounds = new Rect();
        paintText.getTextBounds("+", 0, 1, bounds);
        int heightText = bounds.height();

        canvas.drawText("+",(width - widthText)/2, ((height - heightText)/2)+ heightText -bounds.bottom, paintText);  //bounds.bottom отрицательный поэтому все ок

        return bitmap;
    }

    //пподумать над тем чо бы убрать
    public int spToPx(float sp) {
        return  (int)(sp * context.getResources().getDisplayMetrics().scaledDensity);
    }


    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return (view == o);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
    }


    interface MyPagerListener{
    //    void onClickAdd();
        //void onLongClick();
    }

}
