package com.medhelp.medhelp.ui.profile;

import android.content.Context;
import android.view.View;

import com.medhelp.medhelp.R;

import it.sephiroth.android.library.xtooltip.ClosePolicy;
import it.sephiroth.android.library.xtooltip.Tooltip;

public class ProfileShowTooltip {
    public void show1(View view) {
        Tooltip builder = new Tooltip.Builder(view.getContext())
                .anchor(view, 0, 0, false)
                .closePolicy(
                        new ClosePolicy(300))
                .activateDelay(10)
                .text(view.getContext().getResources().getString(R.string.searchTooltipProfileBranch))
                .maxWidth(600)
                .showDuration(40000)
                .arrow(true)
                .styleId(R.style.ToolTipLayoutCustomStyle)
                .overlay(true)
                .create();
        builder.show(view, Tooltip.Gravity.BOTTOM, false);
    }
}
