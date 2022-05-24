package com.medhelp.medhelp.ui.analise_list.recy_folder;

import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.medhelp.medhelp.R;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

public class ALGroupHolder  extends GroupViewHolder {
    private TextView title;

    public ALGroupHolder(View itemView) {
        super(itemView);
        title = itemView.findViewById(R.id.tv_profile_item_title);

        itemView.findViewById(R.id.inf).setVisibility(View.GONE);
        itemView.findViewById(R.id.ico).setVisibility(View.GONE);
    }

    public void setTitle(ExpandableGroup group) {
        title.setText(Html.fromHtml("<b>Дата сдачи:</b> "+group.getTitle()));
    }

    @Override
    public void expand() {
        title.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_up, 0);
    }

    @Override
    public void collapse() {
        title.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_down, 0);
    }
}
