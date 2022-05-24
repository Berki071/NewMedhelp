package com.medhelp.medhelp.ui.analise_list;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.medhelp.medhelp.R;
import com.medhelp.medhelp.ui._main_page.MainActivity;
import com.medhelp.medhelp.ui._main_page.MainFragmentHelper;
import com.medhelp.medhelp.ui.analise_list.recy_folder.ALAdapter;
import com.medhelp.medhelp.ui.analise_list.recy_folder.DataForAnaliseList;
import com.medhelp.medhelp.ui.base.BaseFragment;
import java.util.List;


import timber.log.Timber;

public class AnaliseListFragment extends BaseFragment implements AnaliseListHelper.View{

    RecyclerView recy;
    CollapsingToolbarLayout toolbarLayout;
    TextView errLoadBtn;
    Toolbar toolbar;
    TextView errMessage;
    ConstraintLayout rootEmpty;
    LinearLayout errRoot;

    LinearLayoutManager layoutManager;

    AnaliseListHelper.Presenter presenter;
    MainFragmentHelper mainFragmentHelper;

    private ALAdapter adapter;

    public static String KEY_FIELD="nameClass";
    public static String NAME_CLASS="AnaliseListActivity";

    Context context;
    Activity activity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Timber.tag("my").i("Список сданных анализов");

        context =getContext();
        activity =getActivity();
        mainFragmentHelper=(MainFragmentHelper)context;
        clearChatNotification();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        presenter=new AnaliseListPresenter(context,this);
        View rootView=inflater.inflate(R.layout.activity_analise_list,container,false);
        initValue(rootView);
        layoutManager=new LinearLayoutManager(context);

        return rootView;
    }
    private void initValue(View v){
        recy= v.findViewById(R.id.recy);
        toolbarLayout= v.findViewById(R.id.collapsing_toolbar_service);
        errLoadBtn= v.findViewById(R.id.err_load_btn);
        toolbar= v.findViewById(R.id.toolbar);
        errMessage= v.findViewById(R.id.err_tv_message);
        rootEmpty= v.findViewById(R.id.rootEmpty);
        errRoot= v.findViewById(R.id.errRoot);
    }

    @Override
    protected void setUp(View view) {
        presenter=new AnaliseListPresenter(context,this);
        presenter.updateAnaliseList();

        setupToolbar();

        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recy.setLayoutManager(layoutManager);
        recy.setItemAnimator(new DefaultItemAnimator());

        if(adapter!=null)
            recy.setAdapter(adapter);
    }

    private void clearChatNotification()
    {
//        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        notificationManager.cancel(ShowNotification.ID_ANALIZ_GROUP);
    }

    private void setupToolbar() {
        ((MainActivity)context).setSupportActionBar(toolbar);
        ActionBar actionBar = ((MainActivity)context).getSupportActionBar();
        toolbarLayout.setTitleEnabled(false);
        actionBar.setTitle(getString(R.string.nav_list_analyzes));

        AppBarLayout.LayoutParams appBarParams =
                (AppBarLayout.LayoutParams) toolbarLayout.getLayoutParams();

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
        errRoot.setVisibility(View.VISIBLE);
        errMessage.setVisibility(View.VISIBLE);
        errLoadBtn.setVisibility(View.VISIBLE);
        recy.setVisibility(View.GONE);
        errLoadBtn.setOnClickListener(v ->
                presenter.updateAnaliseList());
    }


    @Override
    public void updateAnaliseData(List<DataForAnaliseList> response) {
        recy.setVisibility(View.VISIBLE);
        errMessage.setVisibility(View.GONE);
        errLoadBtn.setVisibility(View.GONE);
        errRoot.setVisibility(View.GONE);

        if (response != null && response.get(0).getTitle() != null) {
            rootEmpty.setVisibility(View.GONE);
            adapter = new ALAdapter(context, response);
            recy.setAdapter(adapter);
            if (response.size() == 1)
                adapter.onGroupClick(0);
        } else {
            recy.setVisibility(View.GONE);
            rootEmpty.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void userRefresh() {
        presenter.updateAnaliseList();
    }
}
