package com.medhelp.medhelp.ui.doctor;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import androidx.fragment.app.Fragment;
import androidx.core.view.MenuItemCompat;
import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.medhelp.medhelp.R;
import com.medhelp.medhelp.data.model.AllDoctorsResponse;
import com.medhelp.medhelp.data.model.CategoryResponse;
import com.medhelp.medhelp.ui._main_page.MainActivity;
import com.medhelp.medhelp.ui._main_page.MainFragmentHelper;
import com.medhelp.medhelp.ui.base.BaseFragment;
import com.medhelp.medhelp.ui.doctor.alertDoc.AlertCardDoctor;
import com.medhelp.medhelp.ui.doctor.adapters.DocSpinnerAdapter;
import com.medhelp.medhelp.ui.doctor.adapters.DoctorsAdapter;
import com.medhelp.medhelp.utils.view.ItemListDecorator;
import com.medhelp.medhelp.utils.view.RecyclerViewClickListener;
import com.medhelp.medhelp.utils.view.RecyclerViewTouchListener;
import com.rx2androidnetworking.Rx2AndroidNetworking;

import java.util.ArrayList;
import java.util.List;



import timber.log.Timber;

public class DoctorsFragment extends BaseFragment implements DoctorsHelper.View, Spinner.OnItemSelectedListener {

    DoctorsHelper.Presenter presenter;
    MainFragmentHelper mainFragmentHelper;
    DoctorsAdapter adapter;

    Toolbar toolbar;
    CollapsingToolbarLayout toolbarLayout;
    Spinner spinner;
    TextView errMessage;
    Button errLoadBtn;
    RecyclerView recyclerView;
    LinearLayout rootErr;
    ConstraintLayout rootEmpty;

    private List<AllDoctorsResponse> resList;

    private Context context;
    private Activity activity;

    public static Fragment newInstance()
    {
        DoctorsFragment myFragment = new DoctorsFragment();
        return myFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.tag("my").i("Специалисты");
        context=getContext();
        activity=getActivity();
        mainFragmentHelper=(MainFragmentHelper)context;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        presenter=new DoctorsPresenter(context,this);

        View rootView=inflater.inflate(R.layout.activity_doctors,container,false);

        initValue(rootView);

        setHasOptionsMenu(true);
        return rootView;
    }
    private void initValue(View v){
        toolbar= v.findViewById(R.id.toolbar_doctors);
        toolbarLayout= v.findViewById(R.id.collapsing_toolbar_doctors);
        spinner= v.findViewById(R.id.spinner_doctors);
        errMessage= v.findViewById(R.id.err_tv_message);
        errLoadBtn= v.findViewById(R.id.err_load_btn);
        recyclerView= v.findViewById(R.id.rv_doctors);
        rootErr= v.findViewById(R.id.rootErr);
        rootEmpty= v.findViewById(R.id.rootEmpty);
    }

    @Override
    protected void setUp(View view) {
        presenter=new DoctorsPresenter(context,this);
        setupToolbar();

        resList = new ArrayList<>();
        presenter.getSpecialtyByCenter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public void updateView(List<AllDoctorsResponse> response) {

        errMessage.setVisibility(View.GONE);
        errLoadBtn.setVisibility(View.GONE);
        rootErr.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);

        if(response==null  || response.size()==0)
        {
            recyclerView.setVisibility(View.GONE);
            rootEmpty.setVisibility(View.VISIBLE);
            return;
        }

        resList=response;
        adapter = new DoctorsAdapter(response);
        recyclerView.addItemDecoration(new ItemListDecorator(context));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(new RecyclerViewTouchListener(context, recyclerView, new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {

                AllDoctorsResponse doc=adapter.getItem(position);
                int idSpec=((DocSpinnerAdapter)(spinner.getAdapter())).getIdSpec(spinner.getSelectedItemPosition());
                new AlertCardDoctor(context, doc,idSpec,presenter.getUserToken());

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }


    DocSpinnerAdapter spinnerAdapter;

    @Override
    public void updateSpecialty(List<CategoryResponse> response) {
        spinnerAdapter = new DocSpinnerAdapter(context, response);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if(position==0)
                {
                    presenter.getDoctorList(-1);
                }
                else
                {
                    presenter.getDoctorList(response.get(position-1).getId());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    MenuItem searchViewItem;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //inflater.inflate(R.menu.menu_search, menu);

        inflater.inflate(R.menu.menu_search, menu);
        searchViewItem = menu.findItem(R.id.action_search);
        final SearchView searchViewAndroidActionBar = (SearchView) MenuItemCompat.getActionView(searchViewItem);

        if(searchViewAndroidActionBar==null)
            return;

        searchViewAndroidActionBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchViewAndroidActionBar.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                spinner.setSelection(0);
                final List<AllDoctorsResponse> filteredModelList = filterDoctor(resList, newText);
                adapter.setFilter(filteredModelList);
                return true;
            }
        });
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private List<AllDoctorsResponse> filterDoctor(List<AllDoctorsResponse> models, String query) {
        query = query.toLowerCase();
        final List<AllDoctorsResponse> filteredModelList = new ArrayList<>();
        for (AllDoctorsResponse model : models) {
            if (model.getFio_doctor() != null) {
                final String text = model.getFio_doctor().toLowerCase();
                if (text.contains(query)) {
                    filteredModelList.add(model);
                }
            }
        }
        return filteredModelList;
    }

    @SuppressWarnings("unused")
    private void setupToolbar() {
        ((MainActivity)context).setSupportActionBar(toolbar);
        ActionBar actionBar = ((MainActivity)context).getSupportActionBar();
        toolbarLayout.setTitleEnabled(false);
        actionBar.setTitle("Поиск по сотрудникам");

        toolbar.setOnClickListener(v -> {
            searchViewItem.expandActionView();
        });

        AppBarLayout.LayoutParams appBarParams = (AppBarLayout.LayoutParams) toolbarLayout.getLayoutParams();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        toolbar.setNavigationOnClickListener(v ->
                mainFragmentHelper.showNavigationMenu());
    }

    @Override
    public void showErrorScreen() {
        recyclerView.setVisibility(View.GONE);
        errMessage.setVisibility(View.VISIBLE);
        errLoadBtn.setVisibility(View.VISIBLE);
        rootErr.setVisibility(View.VISIBLE);
        errLoadBtn.setVisibility(View.VISIBLE);
        errLoadBtn.setOnClickListener(v ->
                presenter.getSpecialtyByCenter());
    }

    @Override
    public void userRefresh() {
        presenter.getSpecialtyByCenter();
    }

}
