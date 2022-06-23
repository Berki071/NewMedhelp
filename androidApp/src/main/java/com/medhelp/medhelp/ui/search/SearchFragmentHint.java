package com.medhelp.medhelp.ui.search;

import android.content.Context;
import android.view.View;

import com.medhelp.medhelp.R;

import it.sephiroth.android.library.xtooltip.ClosePolicy;
import it.sephiroth.android.library.xtooltip.Tooltip;

public class SearchFragmentHint {

    public static void show(View view){

        Tooltip builder= new Tooltip.Builder(view.getContext())
                .anchor(view, 0, 0,false)
                .closePolicy(
                        new ClosePolicy(300))
                .activateDelay(10)
                .showDuration(40000)
                .text("Для поиска по услугам нажмите на эту область")
                .maxWidth(600)
                .arrow(true)
                .styleId(R.style.ToolTipLayoutCustomStyle)
                .overlay(true)
                .create();

        builder.show(view, Tooltip.Gravity.BOTTOM, false);

//        val builder: Tooltip = Builder(context)
//                .anchor(view, 0, 0, false)
//                .closePolicy(
//                        ClosePolicy(300)
//                )
//                .activateDelay(10)
//                .text("Для поиска по услугам нажмите на эту область")
//                .maxWidth(600)
//                .showDuration(40000)
//                .arrow(true)
//                .styleId(R.style.ToolTipLayoutCustomStyle)
//                .overlay(true)
//                .create()
//        builder.show(view, Tooltip.Gravity.BOTTOM, false)
    }
}
