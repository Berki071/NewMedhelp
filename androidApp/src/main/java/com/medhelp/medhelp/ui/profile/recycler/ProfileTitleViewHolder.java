package com.medhelp.medhelp.ui.profile.recycler;

import android.view.View;
import android.widget.TextView;

import com.medhelp.medhelp.R;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

class ProfileTitleViewHolder extends GroupViewHolder {

    private TextView title;


    ProfileTitleViewHolder(View itemView) {
        super(itemView);
        title = itemView.findViewById(R.id.tv_profile_item_title);
        itemView.findViewById(R.id.inf).setVisibility(View.GONE);
        itemView.findViewById(R.id.ico).setVisibility(View.GONE);
    }

    @Override
    public void expand() {
        title.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_up, 0);
    }

    @Override
    public void collapse() {
        title.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_down, 0);
    }

    void setGroupName(ExpandableGroup group) {
        if (group.getTitle() != null) {
            String tmp=group.getTitle()+" приемы";
            title.setText(tmp.toUpperCase());
        }

        if (group.getTitle().equals("Предстоящие")) {
            title.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_up, 0);
        }
    }
}
