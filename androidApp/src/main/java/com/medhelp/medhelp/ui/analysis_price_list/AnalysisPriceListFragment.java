package com.medhelp.medhelp.ui.analysis_price_list;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import androidx.appcompat.app.AppCompatActivity;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.medhelp.medhelp.R;
import com.medhelp.medhelp.data.model.analise_price.AnalisePriceResponse;
import com.medhelp.medhelp.ui._main_page.MainActivity;
import com.medhelp.medhelp.ui._main_page.MainFragmentHelper;
import com.medhelp.medhelp.ui.analysis_price_list.adapters.AnalisePriceAdapter;
import com.medhelp.medhelp.ui.analysis_price_list.adapters.AnalisePriceSpinnerAdapter;
import com.medhelp.medhelp.ui.base.BaseFragment;
import com.medhelp.medhelp.utils.view.ItemListDecorator;

import java.util.ArrayList;
import java.util.List;



import timber.log.Timber;

public class AnalysisPriceListFragment extends BaseFragment implements Spinner.OnItemSelectedListener, AnalisePriceListHelper.View{

    RecyclerView recyclerView;
    Spinner spinner;
    Toolbar toolbar;
    CollapsingToolbarLayout toolbarLayout;
    TextView errMessage;
    TextView errLoadBtn;

    private AnalisePriceAdapter adapter;
    private List<String> filterList;
    private List<AnalisePriceResponse> serviceCash;

    AnalisePriceListHelper.Presenter presenter;
    MainFragmentHelper mainFragmentHelper;

    SearchView searchViewAndroidActionBar;
    MenuItem searchViewItem;

    Context context;
    Activity activity;

    public static Fragment newInstance()
    {
        AnalysisPriceListFragment myFragment = new AnalysisPriceListFragment();
        return myFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.tag("my").i("Прейскурант анализов");

        context =getContext();
        activity =getActivity();
        mainFragmentHelper=(MainFragmentHelper)context;
    }

    @Override
    protected void setUp(View view) {
        presenter=new AnalysisPriceListPresenter(context,this);
        spinner.setVisibility(View.GONE);
        setupToolbar();
        serviceCash = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                presenter.getAnalisePrice();
            }
        });


        recyclerView.setOnTouchListener((v, event) -> {
            try {
                InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
                if(((AppCompatActivity) context).getCurrentFocus()!= null && ((AppCompatActivity) context).getCurrentFocus().getWindowToken()!=null)
                    inputMethodManager.hideSoftInputFromWindow(((AppCompatActivity) context).getCurrentFocus().getWindowToken(), 0);
            }catch (Exception e){}
            return false;
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        presenter=new AnalysisPriceListPresenter(context,this);
        View rootView=inflater.inflate(R.layout.activity_analises_price,container,false);
        initValue(rootView);
        setHasOptionsMenu(true);

        return rootView;
    }
    private void initValue(View v){
        recyclerView=v.findViewById(R.id.rv_search);
        spinner=v.findViewById(R.id.spinner_search);
        toolbar=v.findViewById(R.id.toolbar_search);
        toolbarLayout=v.findViewById(R.id.collapsing_toolbar_search);
        errMessage=v.findViewById(R.id.err_tv_message);
        errLoadBtn=v.findViewById(R.id.err_load_btn);
    }

    @Override
    public void showErrorScreen() {
        recyclerView.setVisibility(View.GONE);
        spinner.setVisibility(View.GONE);
        errMessage.setVisibility(View.VISIBLE);
        errLoadBtn.setVisibility(View.VISIBLE);
        errLoadBtn.setOnClickListener(v -> presenter.getAnalisePrice());
    }

    @SuppressWarnings("unused")
    private void setupToolbar() {
        ((MainActivity)context).setSupportActionBar(toolbar);
        ActionBar actionBar = ((MainActivity)context).getSupportActionBar();
        AppBarLayout.LayoutParams appBarParams = (AppBarLayout.LayoutParams) toolbarLayout.getLayoutParams();

        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchViewItem.expandActionView();
            }
        });

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
    public void updateView(List<String> categories, List<AnalisePriceResponse> analise) {
        spinner.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
        errMessage.setVisibility(View.GONE);
        errLoadBtn.setVisibility(View.GONE);

        adapter = new AnalisePriceAdapter(serviceCash, context);

        recyclerView.addItemDecoration(new ItemListDecorator(context));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        filterList = new ArrayList<>();
        filterList.add(0, "Все анализы ");
        filterList.addAll(categories);

        AnalisePriceSpinnerAdapter spinnerAdapter = new AnalisePriceSpinnerAdapter(context, filterList);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if(searchViewAndroidActionBar!=null  && !searchViewAndroidActionBar.getQuery().toString().equals(""))
                    searchViewAndroidActionBar.setQuery("", false);

                if (position == 0) {
                    adapter.addItems(analise);
                }else {
                    List<AnalisePriceResponse> serviceList = new ArrayList<>();
                    for (AnalisePriceResponse serviceResponse : analise) {
                        if (serviceResponse.getGroup().equals(filterList.get(position))) {
                            serviceList.add(serviceResponse);
                        }
                    }
                    adapter.addItems(serviceList);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                searchViewAndroidActionBar.setQuery("", false);
            }
        });

        spinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_DOWN)
                {
                    if(searchViewAndroidActionBar!= null)
                        searchViewAndroidActionBar.clearFocus();

                    InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
                    if(((AppCompatActivity)context).getCurrentFocus()!=null && ((AppCompatActivity)context).getCurrentFocus().getWindowToken()!=null)
                        inputMethodManager.hideSoftInputFromWindow(((AppCompatActivity)context).getCurrentFocus().getWindowToken(), 0);
                }
                return false;
            }
        });
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);
        searchViewItem = menu.findItem(R.id.action_search);
        searchViewAndroidActionBar = (SearchView) MenuItemCompat.getActionView(searchViewItem);

        if(searchViewAndroidActionBar==null)
            return ;

        searchViewAndroidActionBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchViewAndroidActionBar.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //if(!newText.equals("")) {
                final List<AnalisePriceResponse> filteredModelList = filterService(serviceCash, newText);
                adapter.setFilter(filteredModelList);
                //}
                return true;
            }
        });

        searchViewAndroidActionBar.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                    spinner.setSelection(0);
            }
        });
    }

    private List<AnalisePriceResponse> filterService(List<AnalisePriceResponse> models, String query) {
        query = query.toLowerCase();
        final List<AnalisePriceResponse> filteredModelList = new ArrayList<>();
        for (AnalisePriceResponse model : models) {
            final String text = model.getTitle().toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void userRefresh() {
        presenter.getAnalisePrice();
    }
}
