package com.medhelp.medhelp.ui.profile.allertDialog;


import android.content.Context;
import android.graphics.Color;
import androidx.appcompat.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import com.medhelp.medhelp.R;
import java.util.ArrayList;
import java.util.Arrays;

public class AlertForCancel {
    private Context context;
    private AlertDialog alertDialog;
    private AlertForCancel.mOnClickListener listener;

    Spinner spinner;
    TextView tvError;
    Button pozitivBtn;
    Button negativeBtn;

    private final int colorHint= Color.GRAY;
    private final int colorText=Color.BLACK;


    public AlertForCancel(Context context, AlertForCancel.mOnClickListener listener){
        this.context = context;
        this.listener=listener;

        LayoutInflater li= LayoutInflater.from(context);
        View view=li.inflate(R.layout.dialog_cansel,null);

        String[] mas=context.getResources().getStringArray(R.array.cancelSpinner);
        ArrayList<String> list=new ArrayList<>(Arrays.asList(mas));

        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setView(view);


        alertDialog=builder.create();
        alertDialog.show();

        spinner=view.findViewById(R.id.spinner);
        tvError=view.findViewById(R.id.tvError);
        pozitivBtn=view.findViewById(R.id.pozitivBtn);
        negativeBtn=view.findViewById(R.id.negativeBtn);

        pozitivBtn.setOnClickListener(v -> {
            clickPositiveBtn();
        });
        negativeBtn.setOnClickListener(v -> {
            alertDialog.cancel();
        });

        ArrayAdapter<String> spinnerArrayAdapter= getAdapterSpinner(list);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.item_spinner_cancel);
        spinner.setAdapter(spinnerArrayAdapter);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                // If user change the default selection
                // First item is disable and it is used for hint
                if(position > 0){
                    // Notify the selected item text

                    TextView tv = (TextView) view;
                    tv.setTextColor(colorText);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        spinner.setOnTouchListener((v, event) -> {
            if(event.getAction()==MotionEvent.ACTION_DOWN)
            {
                spinner.performClick();
                tvError.setVisibility(View.GONE);
            }
            return true;
        });

    }

    private void clickPositiveBtn()
    {
        String message=testData();

        if(message!=null)
        {
            listener.positiveClickButton(message);
            alertDialog.cancel();
        }
        else
        {
            // выдать ошиибку
            tvError.setVisibility(View.VISIBLE);
        }
    }

    private String testData()
    {
        String nothingSelected = context.getResources().getStringArray(R.array.cancelSpinner)[0] ;
        String sp=spinner.getSelectedItem().toString();

        String result="";

        if(sp.equals(nothingSelected))
            return null;

        if(!sp.equals(nothingSelected))
        {
            //result+="Spinner selected: "+sp+";";
            result+=sp;
        }

      return result;
    }


    private ArrayAdapter<String> getAdapterSpinner(ArrayList<String> list)
    {
        // Initializing an ArrayAdapter

        return new ArrayAdapter<String>(context,R.layout.item_spinner_cancel,list)
        {
            @Override
            public boolean isEnabled(int position){
                return position != 0;
            }
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent)
            {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(colorHint);
                }
                else {
                    tv.setTextColor(colorText);
                }
                return view;
            }
        };
    }


    public interface mOnClickListener{
        void positiveClickButton(String message);

    }
}
