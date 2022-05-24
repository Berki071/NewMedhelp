package com.medhelp.medhelp.ui.search;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
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
import android.widget.Spinner;
import android.widget.TextView;
import com.medhelp.medhelp.R;
import com.medhelp.medhelp.data.model.CategoryResponse;
import com.medhelp.medhelp.data.model.ServiceResponse;
import com.medhelp.medhelp.ui._main_page.MainActivity;
import com.medhelp.medhelp.ui._main_page.MainFragmentHelper;
import com.medhelp.medhelp.ui.base.BaseFragment;
import com.medhelp.medhelp.ui.search.recy_spinner.SearchAdapter;
import com.medhelp.medhelp.ui.search.recy_spinner.SearchSpinnerAdapter;
import com.medhelp.medhelp.utils.view.ItemListDecorator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


import it.sephiroth.android.library.xtooltip.ClosePolicy;
import it.sephiroth.android.library.xtooltip.Tooltip;
import timber.log.Timber;

public class SearchFragment extends BaseFragment implements SearchHelper.View, Spinner.OnItemSelectedListener{
    public static Intent getStartIntent(Context context) {
        return new Intent(context, SearchFragment.class);
    }

    SearchPresenter presenter;
    MainFragmentHelper mainFragmentHelper;

    RecyclerView recyclerView;
    Spinner spinner;
    Toolbar toolbar;
    CollapsingToolbarLayout toolbarLayout;
    TextView errMessage;
    TextView errLoadBtn;

    private SearchAdapter adapter;
    private List<CategoryResponse> filterList;
    private List<ServiceResponse> serviceCash;

    Context context;
    Activity activity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.tag("my").i("Прейскурант услуг");

        context =getContext();
        activity =getActivity();
        mainFragmentHelper=(MainFragmentHelper)context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        presenter=new SearchPresenter(context,this);
        View rootView=inflater.inflate(R.layout.activity_search,container,false);
        initValue(rootView);
        setHasOptionsMenu(true);

        return rootView;
    }
    private void initValue(View v ){
        recyclerView=v.findViewById(R.id.rv_search);
        spinner=v.findViewById(R.id.spinner_search);
        toolbar=v.findViewById(R.id.toolbar_search);
        toolbarLayout=v.findViewById(R.id.collapsing_toolbar_search);
        errMessage=v.findViewById(R.id.err_tv_message);
        errLoadBtn=v.findViewById(R.id.err_load_btn);
    }

    @Override
    protected void setUp(View view) {
        spinner.setVisibility(View.GONE);
        setupToolbar();
        serviceCash = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
    }


    @Override
    public void onResume() {
        super.onResume();

        if(searchViewAndroidActionBar==null)
        {
            presenter.getData();
        }
    }

    @SuppressWarnings("unused")
    private void setupToolbar() {
        ((MainActivity)context).setSupportActionBar(toolbar);

        toolbar.setOnClickListener(v -> searchViewItem.expandActionView());

        showTooltip(toolbar);

        ActionBar actionBar = ((MainActivity)context).getSupportActionBar();
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


    MenuItem searchViewItem;
    SearchView searchViewAndroidActionBar;
    @SuppressWarnings("all")
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);
        searchViewItem = menu.findItem(R.id.action_search);
        searchViewAndroidActionBar = (SearchView) MenuItemCompat.getActionView(searchViewItem);

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
                final List<ServiceResponse> filteredModelList = filterService(serviceCash, newText);
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

    @Override
    public void showErrorScreen() {
        recyclerView.setVisibility(View.GONE);
        spinner.setVisibility(View.GONE);
        errMessage.setVisibility(View.VISIBLE);
        errLoadBtn.setVisibility(View.VISIBLE);
        errLoadBtn.setOnClickListener(v -> presenter.getData());
    }

    @Override
    public void updateView(List<CategoryResponse> categories, List<ServiceResponse> services) {
        spinner.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
        errMessage.setVisibility(View.GONE);
        errLoadBtn.setVisibility(View.GONE);

        adapter = new SearchAdapter(serviceCash, context, new SearchAdapter.ItemListener() {
            @Override
            public void clickFab(ServiceResponse item) {
                presenter.changeFabFavorites(item);
            }

            @Override
            public void clickRecord(SearchAdapter.ViewHolder holder, int service, int limit) {
                presenter.testToSpam(holder, service, limit);
            }
        });

        recyclerView.addItemDecoration(new ItemListDecorator(context));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        filterList = new ArrayList<>();
        filterList.add(0, new CategoryResponse("Все специальности"));
        filterList.add(1, new CategoryResponse("Избранные услуги"));
        filterList.addAll(categories);

        SearchSpinnerAdapter spinnerAdapter = new SearchSpinnerAdapter(context, filterList);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if (position == 0) {
                   //adapter.addItems(sortByWight(services));
                    Collections.sort(services);
                    adapter.addItems(services);
                }else if(position==1)
                {
                    adapter.addItems(sortByWight(selectItemWithTab(services)));
                }
                else {
                    List<ServiceResponse> serviceList = new ArrayList<>();
                    for (ServiceResponse serviceResponse : services) {
                        if (serviceResponse.getIdSpec() == filterList.get(position).getId()) {
                            serviceList.add(serviceResponse);
                        }
                    }
                    adapter.addItems(sortByWight(serviceList));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private List<ServiceResponse> sortByWight(List<ServiceResponse> list) {
        ServiceResponse tmp;

        for (int j=0;j<=list.size()-2;j++) {
            for (int i =list.size()-1; j<i; i--) {
                int wight1 = list.get(i).getPoryadok() == 0 ? 0 : 11 - list.get(i).getPoryadok();
                int wight2 = list.get(i - 1).getPoryadok() == 0 ? 0 : 11 - list.get(i-1).getPoryadok();

                if (wight1 > wight2) {
                    tmp = list.get(i - 1);
                    list.set(i - 1, list.get(i));
                    list.set(i, tmp);
                }
            }
        }

        return list;
    }


    private List<ServiceResponse> selectItemWithTab (List<ServiceResponse> services)
    {
        List<ServiceResponse> newList=new ArrayList<>();

        for(ServiceResponse item  :  services)
        {
            if(item.getFavorites().equals("1"))
            {
                newList.add(item);
            }
        }
        return newList;
    }

    @Override
    public void refreshRecy() {
        adapter.notifyDataSetChanged();
    }

    private List<ServiceResponse> filterService(List<ServiceResponse> models, String query) {
        query = query.toLowerCase();
        final List<ServiceResponse> filteredModelList = new ArrayList<>();
        for (ServiceResponse model : models) {
            final String text = model.getTitle().toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

    private void showTooltip(View view)
    {
        //+
        view.post(()->{
            if(presenter.prefManager.getShowTooltipSearchLoupe() )
            {
                Tooltip builder = new Tooltip.Builder(context)
                        .anchor(view, 0, 0,false)
                        .closePolicy(
                                new ClosePolicy(300))
                        .activateDelay(10)
                        .text("Для поиска по услугам нажмите на эту область")
                        .maxWidth(600)
                        .showDuration(40000)
                        .arrow(true)
                        .styleId(R.style.ToolTipLayoutCustomStyle)
                        .overlay(true)
                        .create();

                builder.show(view, Tooltip.Gravity.BOTTOM,false);

                presenter.prefManager.setShowTooltipSearchLoupe();
            }
        });
    }

    @Override
    public void userRefresh() {
        presenter.getData();
    }
}

