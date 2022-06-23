package com.medhelp.medhelp.ui.electronic_conclusions_fragment.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.medhelp.medhelp.R;

import java.util.ArrayList;
import java.util.List;

public class MySpinnerAdapter extends BaseAdapter implements android.widget.SpinnerAdapter {
    private  Context context;
    private List<String> list = new ArrayList<>();
    final int colorHint= Color.parseColor("#E1F5FE");
    final int colorText=Color.parseColor("#FFFFFF");

    public MySpinnerAdapter(Context context, List<String> list) {
        this.context = context;
        this.list.add(context.getString(R.string.specOneElement));
        this.list.addAll(list);
    }

    public int getCount() {
        return list.size();
    }

    public Object getItem(int i) {
        return list.get(i);
    }

    public long getItemId(int i) {
        return (long) i;
    }


    @Override
    public View getDropDownView(int position, View view, ViewGroup parent) {
        TextView txt = new TextView(context);
        txt.setPadding(16, 16, 16, 16);
        txt.setTextSize(18);
        txt.setGravity(Gravity.CENTER_VERTICAL);
        txt.setText(list.get(position));

        if(position == 0){
            // Set the hint text color gray
            txt.setTextColor(colorHint);
        }
        else {
            txt.setTextColor(colorText);
        }

        return txt;
    }

    public View getView(int position, View view, ViewGroup viewgroup) {
        TextView txt = new TextView(context);
        txt.setGravity(Gravity.START);
        txt.setPadding(16, 16, 16, 16);
        txt.setTextSize(16);
        txt.setText(list.get(position));

        if(position == 0){
            // Set the hint text color gray
            txt.setTextColor(colorHint);
        }
        else {
            txt.setTextColor(colorText);
        }
        return txt;
    }
}
