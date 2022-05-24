package com.medhelp.medhelp.ui.settings;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.app.ActionBar;
import androidx.cardview.widget.CardView;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import com.medhelp.medhelp.R;
import com.medhelp.medhelp.ui._main_page.MainActivity;
import com.medhelp.medhelp.ui._main_page.MainFragmentHelper;
import com.medhelp.medhelp.ui.base.BaseFragment;
import com.medhelp.medhelp.ui.settings.favorites_tab.FavoritesTabActivity;
import com.medhelp.shared.model.SettingsAllBranchHospitalResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import timber.log.Timber;

public class SettingsFragment extends BaseFragment {

    SettingsPresenter presenter;
    MainFragmentHelper mainFragmentHelper;

    Toolbar toolbar;
    Spinner spinnerBranchHospital;
    LinearLayout rootErr;
    TextView errMessage;
    Button errLoadBtn;
    ConstraintLayout mainConstraint;
    Switch sw;
    CardView spin;
    CardView fingerprintCard;
    CardView favoritesTab;
    TextView versionCode;

    List<SettingsAllBranchHospitalResponse> allBranchHospitalList;

    Context context;
    Activity activity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Timber.tag("my").i("Настройки");

        context =getContext();
        activity =getActivity();
        mainFragmentHelper=(MainFragmentHelper)context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        presenter=new SettingsPresenter(context,this);
        View rootView=inflater.inflate(R.layout.activity_settings,container,false);

        initValue(rootView);

        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            int verCode = pInfo.versionCode;
            versionCode.setText("v. "+verCode);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return rootView;
    }
    private void initValue(View v){
        toolbar=v.findViewById(R.id.toolbar_settings);
        spinnerBranchHospital=v.findViewById(R.id.spinnerBranchHospital);
        rootErr=v.findViewById(R.id.rootErr);
        errMessage=v.findViewById(R.id.err_tv_message);
        errLoadBtn=v.findViewById(R.id.err_load_btn);
        mainConstraint=v.findViewById(R.id.mainConstraint);
        sw=v.findViewById(R.id.switch1);
        spin=v.findViewById(R.id.spin);
        fingerprintCard=v.findViewById(R.id.fingerprintCard);
        favoritesTab=v.findViewById(R.id.favoritesTabs);
        versionCode=v.findViewById(R.id.versionCode);
    }


    @Override
    protected void setUp(View view) {
        spinnerBranchHospital.setTag(spinnerBranchHospital.getSelectedItemPosition());
        setupToolbar();
        testStartStateSwitch();
        presenter.getAllHospitalBranch();


        sw.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if(isChecked)
            {
                presenter.setNeedShowLockScreen(true);

                Timber.tag("my").v("Авторизация по отпечатку пальца включена");
            }
            else
            {
                presenter.setNeedShowLockScreen(false);
                Timber.tag("my").v("Авторизация по отпечатку пальца отключена");
            }
        });


        favoritesTab.setOnClickListener(v -> {
            Intent intent=new Intent(context,FavoritesTabActivity.class);
            startActivity(intent);
        });
    }


    private void testStartStateSwitch()
    {
        sw.setChecked(presenter.getNeedShowLockScreen());
    }


    @SuppressWarnings("unused")
    private void setupToolbar() {
        ((MainActivity)context).setSupportActionBar(toolbar);
        ActionBar actionBar = ((MainActivity)context).getSupportActionBar();

        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainFragmentHelper.showNavigationMenu();
            }
        });
    }

    public void showErrorScreen() {
        errMessage.setVisibility(View.VISIBLE);
        errLoadBtn.setVisibility(View.VISIBLE);
        spin.setVisibility(View.GONE);
        fingerprintCard.setVisibility(View.GONE);
        favoritesTab.setVisibility(View.GONE);
        errLoadBtn.setOnClickListener(v ->
                presenter.getAllHospitalBranch());
    }

    private void hideErrorScreen()
    {
        errMessage.setVisibility(View.GONE);
        errLoadBtn.setVisibility(View.GONE);
        spin.setVisibility(View.VISIBLE);
        fingerprintCard.setVisibility(View.VISIBLE);
        favoritesTab.setVisibility(View.VISIBLE);
    }

    public void updateBranchHospital(List<SettingsAllBranchHospitalResponse> response) {
        hideErrorScreen();

        Collections.sort(response);

        allBranchHospitalList=response;

        ArrayList<String> namesBranch =new ArrayList<>();

        for(SettingsAllBranchHospitalResponse itm : response)
        {
            namesBranch.add(itm.getNameBranch());
        }

        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, namesBranch);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBranchHospital.setAdapter(arrayAdapter);

        int numCurretBranch=presenter.getNumPositionInListBranch(allBranchHospitalList);
        spinnerBranchHospital.setSelection(numCurretBranch);

        spinnerBranchHospital.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Timber.tag("my").v("Новый любимый филиал: "+allBranchHospitalList.get(position).getNameBranch()+", id "+allBranchHospitalList.get(position).getIdBranch());

                presenter.selectedNewHospitalBranch(allBranchHospitalList.get(position).getIdBranch());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void spinnerRefresh(Boolean boo) {
        if(boo)
        {
            spinnerBranchHospital.setTag(spinnerBranchHospital.getSelectedItemPosition());
        }
        else
        {
            spinnerBranchHospital.setSelection((int)spinnerBranchHospital.getTag());
        }

    }

    public String getCurrentNameBranch() {
        return (allBranchHospitalList.get(spinnerBranchHospital.getSelectedItemPosition())).getNameBranch() ;
    }


    @Override
    public void userRefresh() {
        presenter.getAllHospitalBranch();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.destroy();
    }
}
