package com.medhelp.medhelp.ui.tax_certifacate.recy;

import android.view.View;

import com.medhelp.medhelp.R;

import it.sephiroth.android.library.xtooltip.ClosePolicy;
import it.sephiroth.android.library.xtooltip.Tooltip;

public class ShowTooltipTax {
    public void showTooltip(View view, String msg){
        view.post(()->{
            Tooltip builder= new Tooltip.Builder(view.getContext())
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

            builder.show(view, Tooltip.Gravity.BOTTOM,false);
        });

    }
}
