package com.medhelp.medhelp.ui.schedule.recy_branch;

import android.content.Context;
import android.graphics.Color;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import com.medhelp.medhelp.R;
import com.medhelp.shared.model.SettingsAllBranchHospitalResponse;

public class BranchItemHolder extends RecyclerView.ViewHolder {
    ConstraintLayout root;
    CardView cardView;
    TextView nameBranch;
    SettingsAllBranchHospitalResponse branch;
    MyBranchSection.ItemBranchListener listener;
    Context context;

    public BranchItemHolder(Context context ,View itemView, MyBranchSection.ItemBranchListener listener) {
        super(itemView);
        root=(ConstraintLayout)itemView;
        cardView=itemView.findViewById(R.id.cardView);
        nameBranch=itemView.findViewById(R.id.nameBranch);
        this.listener=listener;
        this.context=context;

        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!branch.isFavorite())
                    listener.onClick(branch);
            }
        });
    }

    public void onBind(SettingsAllBranchHospitalResponse branch){
        this.branch=branch;
        nameBranch.setText(branch.getNameBranch());

        if(branch.isFavorite())
        {
            cardView.setCardBackgroundColor(context.getResources().getColor(R.color.color_btn));
        }
        else
        {
            cardView.setCardBackgroundColor(Color.WHITE);

        }
    }



}
