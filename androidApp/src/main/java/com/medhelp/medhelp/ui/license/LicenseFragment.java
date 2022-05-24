package com.medhelp.medhelp.ui.license;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.medhelp.medhelp.R;
import com.medhelp.medhelp.data.network.NetworkManager;
import com.medhelp.medhelp.data.pref.PreferencesManager;
import com.medhelp.medhelp.ui.base.BaseDialog;
import com.medhelp.medhelp.ui.login.LoginActivity;

public class LicenseFragment extends BaseDialog{

    public static final String TAG = "LicenseFragment";

    Button license_cancel;
    Button license_confirm;

    PreferencesManager prefManager;
    NetworkManager networkManager;

    public static LicenseFragment newInstance() {
        Bundle args = new Bundle();
        LicenseFragment fragment = new LicenseFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        prefManager=new PreferencesManager(getContext());
        networkManager=new NetworkManager(prefManager);

        View view = inflater.inflate(R.layout.fragment_license, container, false);

        license_cancel=view.findViewById(R.id.license_cancel);
        license_confirm=view.findViewById(R.id.license_confirm);
        license_cancel.setOnClickListener(v -> {
            dismissDialog();
        });
        license_confirm.setOnClickListener(v -> {
            confirmLicense();
        });

        return view;
    }

    @Override
    protected void setUp(View view) {
    }

    public void show(FragmentManager fragmentManager) {
        super.show(fragmentManager, TAG);
    }

    private void dismissDialog() {
        if (getActivity() != null) {
            getActivity().finish();
        }
        super.dismissDialog(TAG);
    }

    public void showLoginActivity() {
        Intent intent = LoginActivity.getStartIntent(getContext());
        startActivity(intent);
        dismissDialog();
    }

    public void confirmLicense() {
        prefManager.isStartMode(false);
        showLoginActivity();
    }

    @Override
    public void userRefresh() {}
}
