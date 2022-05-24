package com.medhelp.medhelp.ui.doctor.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.medhelp.medhelp.R;
import com.medhelp.medhelp.data.model.CategoryResponse;

import java.util.ArrayList;
import java.util.List;

public class DocSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {

    private final Context context;
    private List<CategoryResponse> list = new ArrayList<>();
    final int colorHint= Color.parseColor("#E1F5FE");
    final int colorText=Color.parseColor("#FFFFFF");

    public DocSpinnerAdapter(Context context, List<CategoryResponse> list) {
        CategoryResponse cr=new CategoryResponse(context.getString(R.string.specOneElement));
        this.list.add(cr);

        this.list.addAll(list);
        this.context = context;
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
        txt.setText(list.get(position).getTitle());

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
        txt.setText(list.get(position).getTitle());

        if(position == 0){
            // Set the hint text color gray
            txt.setTextColor(colorHint);
        }
        else {
            txt.setTextColor(colorText);
        }
        return txt;
    }


    public int getIdSpec(int numberInList)
    {
        return list.get(numberInList).getId();
    }


}