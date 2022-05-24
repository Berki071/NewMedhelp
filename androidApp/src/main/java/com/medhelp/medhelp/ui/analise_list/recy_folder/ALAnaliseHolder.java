package com.medhelp.medhelp.ui.analise_list.recy_folder;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.medhelp.medhelp.Constants;
import com.medhelp.medhelp.R;
import com.medhelp.medhelp.data.model.AnaliseListData;
import com.medhelp.medhelp.data.pref.PreferencesManager;
import com.medhelp.medhelp.ui._main_page.MainActivity;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;



import it.sephiroth.android.library.xtooltip.ClosePolicy;
import it.sephiroth.android.library.xtooltip.Tooltip;


import static com.medhelp.medhelp.ui._main_page.MainActivity.ANALISE_LIST_BACK;

public class ALAnaliseHolder extends ChildViewHolder {
    TextView title;
    TextView date;
    ImageView ico;

    public static final String YES="да";

   // @SuppressWarnings("FieldCanBeLocal") private final String FALSE_TYPE="FALSE_TYPE";
    private final String TRUE_TYPE="TRUE_TYPE";

    private Context context;
    private PreferencesManager pref;
    private AnaliseListData analiseItem;

    public ALAnaliseHolder(Context context,View itemView) {
        super(itemView);
        this.context=context;

        title= itemView.findViewById(R.id.title);
        date= itemView.findViewById(R.id.date);
        ico= itemView.findViewById(R.id.ico);

        View.OnClickListener ocl = v -> {
            if ((boolean) ico.getTag()) {
                nextPage();
            } else {
                showAlert();
            }
        };
        itemView.setOnClickListener(ocl);

        pref=new PreferencesManager(context);
    }

    public void onBind(AnaliseListData analiseItem) {
        this.analiseItem=analiseItem;

        title.setText(analiseItem.getTitle());
        date.setText(analiseItem.getDateResult());

        String status=analiseItem.getStatus().toLowerCase();
        if(status.equals(YES))
        {
            ico.setImageResource(R.drawable.ic_done_green_500_36dp);
            ico.setTag(true);
        }
        else
        {
            ico.setImageResource(R.drawable.ic_hourglass_empty_red_500_24dp);
            ico.setTag(false);
        }
    }

    public void testShowTooltip(boolean topTooltip)
    {
        String msg;

        Tooltip.Gravity gravity;

        if(topTooltip)
        {
            gravity =Tooltip.Gravity.TOP;
        }
        else
        {
            gravity =Tooltip.Gravity.BOTTOM;
        }

        if(analiseItem.getStatus().toLowerCase().equals(YES))
        {
            msg=context.getResources().getString(R.string.analiseListTooltipTrue);

            if(pref.getShowTooltipAnaliseListTrue())
            {
                testTrueShowTooltip(ico,msg, gravity);
                pref.setShowTooltipAnaliseListTrue();
            }
        }
        else
        {
            msg=context.getResources().getString(R.string.analiseListTooltipFalse);

            if(pref.getShowTooltipAnaliseListFalse())
            {
                testTrueShowTooltip(ico,msg, gravity);
                pref.setShowTooltipAnaliseListFalse();
            }
        }
    }

    private void testTrueShowTooltip(View view, String msg,Tooltip.Gravity gravity)
    {
        //+
        view.post(()->{
            Tooltip builder= new Tooltip.Builder(context)
                    .anchor(view, 0, 0,false)
                    .closePolicy(
                            new ClosePolicy(300))
                    .activateDelay(10)
                    .showDuration(40000)
                    .text(msg)
                    .maxWidth(600)
                    .arrow(true)
                    .styleId(R.style.ToolTipLayoutCustomStyle)
                    .overlay(true)
                    .create();

            builder.show(view, gravity,false);
        });
    }


    private void showAlert()
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setMessage(R.string.analysisNotReady)
                .setPositiveButton("Ok", (dialog, which) -> dialog.cancel());

        AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }

    private void nextPage()
    {
        Bundle bundle=((MainActivity)context).getIntent().getExtras();
        if(bundle==null)
        {
            ((MainActivity)context).getIntent().replaceExtras(new Bundle());
        }

        ((MainActivity)context).getIntent().putExtra(ANALISE_LIST_BACK,true);
        ((MainActivity)context).selectedFragmentItem(Constants.MENU_RESULT_ANALISES,false);
    }
}
