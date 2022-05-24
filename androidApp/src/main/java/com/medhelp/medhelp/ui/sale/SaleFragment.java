package com.medhelp.medhelp.ui.sale;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.medhelp.medhelp.R;
import com.medhelp.medhelp.data.model.SaleResponse;
import com.medhelp.medhelp.ui._main_page.MainActivity;
import com.medhelp.medhelp.ui._main_page.MainFragmentHelper;
import com.medhelp.medhelp.ui.base.BaseFragment;
import com.medhelp.medhelp.ui.sale.recy.SaleAdapter;
import com.medhelp.shared.model.CenterResponse;

import java.util.ArrayList;
import java.util.List;
import timber.log.Timber;

public class SaleFragment extends BaseFragment implements SaleHelper.View {

    RecyclerView recyclerView;
    Toolbar toolbar;
    TextView errMessage;
    TextView errLoadBtn;
    CollapsingToolbarLayout toolbarLayout;
    FloatingActionButton fab;
    ConstraintLayout rootEmpty;
    FloatingActionButton fab_sale;

    SaleAdapter adapter;
    private String phoneNumber;
    SaleHelper.Presenter presenter;
    MainFragmentHelper mainFragmentHelper;
    Context context;
    Activity activity;

    public static Fragment newInstance()
    {
        SaleFragment myFragment = new SaleFragment();
        return myFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.tag("my").i("Акции");
        context =getContext();
        activity =getActivity();
        clearChatNotification();
        mainFragmentHelper=(MainFragmentHelper)context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        presenter=new SalePresenter(context,this);
        View rootView=inflater.inflate(R.layout.activity_sale,container,false);
        initValue(rootView);

        return rootView;
    }
    private void initValue(View v){
        recyclerView=v.findViewById(R.id.rv_sale);
        toolbar=v.findViewById(R.id.toolbar_sale);
        errMessage=v.findViewById(R.id.err_tv_message);
        errLoadBtn=v.findViewById(R.id.err_load_btn);
        toolbarLayout=v.findViewById(R.id.collapsing_toolbar_sale);
        fab=v.findViewById(R.id.fab_sale);
        rootEmpty=v.findViewById(R.id.rootEmpty);
        fab_sale=v.findViewById(R.id.fab_sale);

        fab_sale.setOnClickListener(c -> callToCenter(phoneNumber));
    }

    @Override
    protected void setUp(View view) {
        CenterResponse tmp= presenter.getCenterInfo();

        if (tmp.getPhone() != null && tmp.getPhone().length() > 0) {
            phoneNumber = tmp.getPhone();
            fab.setVisibility(View.VISIBLE);
        } else {
            fab.setVisibility(View.GONE);
        }

        presenter.updateSaleList();
        setupToolbar();

        adapter=new SaleAdapter(new ArrayList<>());
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }



    private void clearChatNotification()
    {
//        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
//        notificationManager.cancel(ShowNotification.ID_SHARE_GROUP);
    }


    @SuppressWarnings("unused")
    private void setupToolbar() {
        ((MainActivity)context).setSupportActionBar(toolbar);
        ActionBar actionBar = ((MainActivity)context).getSupportActionBar();
        toolbarLayout.setTitleEnabled(false);

        AppBarLayout.LayoutParams appBarParams = (AppBarLayout.LayoutParams) toolbarLayout.getLayoutParams();
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

    @Override
    public void showErrorScreen() {
        errMessage.setVisibility(View.VISIBLE);
        errLoadBtn.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        errLoadBtn.setOnClickListener(v ->
                presenter.updateSaleList());
    }

    @Override
    public void updateSaleData(List<SaleResponse> response) {
        if(response.get(0).getSaleDescription()==null)
        {
            rootEmpty.setVisibility(View.VISIBLE);
            return;
        }

        rootEmpty.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        errMessage.setVisibility(View.GONE);
        errLoadBtn.setVisibility(View.GONE);
        adapter.addItems(response);
    }


    private void callToCenter(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phone));
        startActivity(intent);
    }


    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public boolean isLoading() {
        return false;
    }


    @Override
    public void showError(String message) {

    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    public void showMessage(int resId) {

    }


    @Override
    public void hideKeyboard() {

    }

    @Override
    public void userRefresh() {
        presenter.updateSaleList();
    }
}
