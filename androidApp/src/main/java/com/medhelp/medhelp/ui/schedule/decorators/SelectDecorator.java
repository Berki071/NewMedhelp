package com.medhelp.medhelp.ui.schedule.decorators;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import androidx.core.content.ContextCompat;

import com.medhelp.medhelp.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

public class SelectDecorator implements DayViewDecorator {

    private final Drawable drawable;

    public SelectDecorator(Activity context) {
        drawable = ContextCompat.getDrawable(context,R.drawable.date_selector);
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return true;
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.setSelectionDrawable(drawable);
    }
}
