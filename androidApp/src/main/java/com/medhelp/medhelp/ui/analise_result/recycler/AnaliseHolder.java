package com.medhelp.medhelp.ui.analise_result.recycler;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.medhelp.medhelp.R;
import com.medhelp.medhelp.data.pref.PreferencesManager;


import it.sephiroth.android.library.xtooltip.ClosePolicy;
import it.sephiroth.android.library.xtooltip.Tooltip;


public class AnaliseHolder extends RecyclerView.ViewHolder {

    TextView title;
    ImageView imgLoadDelete;
    View shade;
    ProgressBar progressBar;
    View line;
    ImageView imgEye;

    private int position;
    private boolean downloadIn;

    private PreferencesManager pref;
    private Context context;

    private final String LOAD_TYPE = "LOAD_TYPE";
    private final String DELETE_TYPE = "DELETE_TYPE";


    public AnaliseHolder(View itemView, AnaliseRecyItemListener recyItemListener, Context context) {
        super(itemView);

        title=itemView.findViewById(R.id.title);
        imgLoadDelete=itemView.findViewById(R.id.imgLoad);
        shade=itemView.findViewById(R.id.shade);
        progressBar=itemView.findViewById(R.id.progressBar);
        line=itemView.findViewById(R.id.line);
        imgEye=itemView.findViewById(R.id.eye);

        this.context = context;

        pref = new PreferencesManager(context);

        itemView.setOnClickListener(v -> {
            if (downloadIn) {
                recyItemListener.openPDF(position);
            } else {
                recyItemListener.downloadPDF(position);
            }
        });

        imgLoadDelete.setOnClickListener(v ->
                recyItemListener.deletePDFDialog(position));
    }


    public void onBindViewHolder(int position, String date, boolean downloadIn, boolean hideD, boolean showTooltip) {
        try {
            this.position = position;
            this.downloadIn = downloadIn;
            title.setText(date);

            hideDownload(hideD);

            if (downloadIn) {
                imgLoadDelete.setClickable(true);
                imgLoadDelete.setImageResource(R.drawable.ic_delete_red_500_24dp);
                if (imgEye != null)
                    imgEye.setVisibility(View.VISIBLE);
                if (line != null)
                    line.setVisibility(View.VISIBLE);

                if (showTooltip && hideD)
                    testForShowTooltip(imgLoadDelete, context.getResources().getString(R.string.analiseResultTooltipDelete), DELETE_TYPE);
            } else {
                imgLoadDelete.setClickable(false);
                imgLoadDelete.setImageResource(R.drawable.ic_file_download_blue_600_24dp);
                if (imgEye != null)
                    imgEye.setVisibility(View.GONE);
                if (line != null)
                    line.setVisibility(View.GONE);

                if (showTooltip && hideD)
                    testForShowTooltip(imgLoadDelete, context.getResources().getString(R.string.analiseResultTooltipLoad), LOAD_TYPE);
            }
        } catch (Exception e) {
            Log.wtf("fat", e.getMessage());
        }

    }

    private void testForShowTooltip(View view, String msg, String type) {
        if (type.equals(LOAD_TYPE)) {
            if (pref.getShowTooltipAnaliseResultLoad()) {
                showTooltip(view, msg, Tooltip.Gravity.LEFT);
                pref.setShowTooltipAnaliseResultLoad();
            }
        } else {
            if (pref.getShowTooltipAnaliseResultDelete()) {
                //showTooltip(view, msg, Tooltip.Gravity.BOTTOM);
                showTooltip(imgEye, "Нажмите для просмотра", Tooltip.Gravity.LEFT);
                pref.setShowTooltipAnaliseResultDelete();
            }
        }
    }


    public void showTooltip(View view, String msg, Tooltip.Gravity gravity) {
        //+
        view.post(()->{
            Tooltip builder = new Tooltip.Builder(context)
                    .anchor(view, 0, 0,false)
                    .closePolicy(
                            new ClosePolicy(300))
                    .activateDelay(10)
                    .text(msg)
                    .maxWidth(600)
                    .showDuration(40000)
                    .arrow(true)
                    .styleId(R.style.ToolTipLayoutCustomStyle)
                    .overlay(true)
                    .create();

            builder.show(view, Tooltip.Gravity.LEFT,false);
        });
    }


    public void hideDownload(boolean boo) {
        if (boo) {
            if (shade != null)
                shade.setVisibility(View.GONE);
            if (progressBar != null)
                progressBar.setVisibility(View.GONE);
        } else {
            if (shade != null)
                shade.setVisibility(View.VISIBLE);
            if (progressBar != null)
                progressBar.setVisibility(View.VISIBLE);
        }
    }


}
