package com.medhelp.medhelp.ui.schedule.decorators;

import android.content.Context;
import android.graphics.drawable.Drawable;
import androidx.core.content.ContextCompat;

import com.medhelp.medhelp.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

public class DayDecorator implements DayViewDecorator {

    public static final int DAY_MODE_MANY = 1;
    public static final int DAY_MODE_FEW = 2;
    public static final int DAY_MODE_NOT = 3;
    public static final int DAY_MODE_NO = 4;
    public static final int DAY_MODE_CLEAR = 5;


    private Context context;
    private final CalendarDay day;
    private int dayMode;
    private final Drawable bgDrawableGreen;
    private final Drawable bgDrawableYellow;
    private final Drawable bgDrawableRed;
    private final Drawable bgDrawableClear;


    public int getDayMode() {
        return dayMode;
    }

    public DayDecorator(Context context, CalendarDay day, int dayMode) {
        this.context = context;
        this.day = day;
        this.dayMode = dayMode;

        bgDrawableGreen=ContextCompat.getDrawable(context,R.drawable.date_item_default);
        bgDrawableYellow=ContextCompat.getDrawable(context,R.drawable.date_orange);
        bgDrawableRed=ContextCompat.getDrawable(context,R.drawable.date_red);
        bgDrawableClear=ContextCompat.getDrawable(context,R.drawable.date_clear);
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return this.day.equals(day);
    }

    @Override
    public void decorate(DayViewFacade view) {
        switch (dayMode) {
            case DAY_MODE_MANY:
                view.setBackgroundDrawable(bgDrawableGreen);
                break;
            case DAY_MODE_FEW:
                view.setBackgroundDrawable(bgDrawableGreen);
                break;
            case DAY_MODE_NO:
                view.setBackgroundDrawable(bgDrawableClear);
                break;
            case DAY_MODE_CLEAR:
                view.setBackgroundDrawable(bgDrawableClear);
                break;
        }
    }
}