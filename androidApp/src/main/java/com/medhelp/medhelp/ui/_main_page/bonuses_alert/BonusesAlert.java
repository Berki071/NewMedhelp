package com.medhelp.medhelp.ui._main_page.bonuses_alert;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.medhelp.medhelp.R;
import com.medhelp.medhelp.data.model._heritable.BonusesItemAndroid;
import com.medhelp.medhelp.ui._main_page.bonuses_alert.recy.AdapterBonuses;
import com.medhelp.medhelp.utils.main.MainUtils;

import java.util.List;
import java.util.Objects;




public class BonusesAlert extends DialogFragment {
    List<BonusesItemAndroid> list;

    public void setList(List<BonusesItemAndroid> list) {
        this.list = list;
    }

    RecyclerView recy;
    TextView amount;
    Button cancel;

    @Override
    public void onStart() {
        super.onStart();

        setHasOptionsMenu(true);

        //костыль, по умолчанию окно показывается не во весь размер
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            Objects.requireNonNull(dialog.getWindow()).setLayout(width, height);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.df_bonuses_balance, null);

        recy=view.findViewById(R.id.recy);
        amount=view.findViewById(R.id.amount);
        cancel=view.findViewById(R.id.cancel);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cancel.setOnClickListener(v -> {
            dismiss();
        });

        if (list == null || list.size() == 0)
            return;
        else {
            amount.setText(MainUtils.getSumBonuses(list));
            initRecy();
        }
    }



    private void initRecy() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        AdapterBonuses adapterBonuses = new AdapterBonuses(getContext(), list);

        recy.setLayoutManager(layoutManager);
        recy.setAdapter(adapterBonuses);
    }
}
