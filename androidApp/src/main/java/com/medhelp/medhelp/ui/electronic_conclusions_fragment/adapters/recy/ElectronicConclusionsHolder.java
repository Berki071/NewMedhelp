package com.medhelp.medhelp.ui.electronic_conclusions_fragment.adapters.recy;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.medhelp.medhelp.R;
import com.medhelp.medhelp.data.model.AnaliseResponse;


import kotlin.Triple;

public class ElectronicConclusionsHolder extends RecyclerView.ViewHolder {
    ElectronicConclusionsHolderListener listener;
    DataClassForElectronicRecy data;

    ConstraintLayout dataBox;
    TextView date;
    TextView title;
    LinearLayout hideBox;
    ImageView btnShow;
    ImageView btnDelete;
    View dividedLine;

    View shade;
    ProgressBar progressBar;

    public ElectronicConclusionsHolder(@NonNull View itemView, ElectronicConclusionsHolderListener listener) {
        super(itemView);
        this.listener=listener;

        date=itemView.findViewById(R.id.date);
        title=itemView.findViewById(R.id.title);
        hideBox=itemView.findViewById(R.id.hideBox);
        btnShow=itemView.findViewById(R.id.btnShow);
        btnDelete=itemView.findViewById(R.id.btnDelete);
        dataBox=itemView.findViewById(R.id.dataBox);
        dividedLine = itemView.findViewById(R.id.dividedLine);

        shade = itemView.findViewById(R.id.shade);
        progressBar = itemView.findViewById(R.id.progressBar);

        dataBox.setOnClickListener(v -> {
            data.setShowHideBox(!data.isShowHideBox());
            if(listener!=null)
                listener.openHideBox(data);
        });

        btnShow.setOnClickListener(v -> {
            if(listener!=null)
                listener.showDoc(data);
        });

        btnDelete.setOnClickListener(v -> {
            if(listener!=null)
                listener.deleteDoc(data);
        });
    }

    void processingHideBox(){
        if(!data.isShowHideBox()){
            hideBox.setVisibility(View.GONE);
        }else{
            hideBox.setVisibility(View.VISIBLE);
        }
    }

    public void onBind( DataClassForElectronicRecy data){
        this.data=data;

        hideDownload(data.isHideDownload());

        title.setText(data.getTitle());

        processingHideBox();

        if(data.isDownloadIn()){
            btnShow.setImageResource(R.drawable.ic_visibility_light_blue_400_24dp);
            dividedLine.setVisibility(View.VISIBLE);
            btnDelete.setVisibility(View.VISIBLE);
        }else{
            btnShow.setImageResource(R.drawable.ic_file_download_blue_600_24dp);
            dividedLine.setVisibility(View.GONE);
            btnDelete.setVisibility(View.GONE);
        }
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

    public interface ElectronicConclusionsHolderListener{
        void openHideBox(DataClassForElectronicRecy data);
        void showDoc(DataClassForElectronicRecy data);
        void deleteDoc(DataClassForElectronicRecy data);
    }
}
