package com.medhelp.medhelp.ui.doctor.service_activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import androidx.core.view.MenuItemCompat;
import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.medhelp.medhelp.R;
import com.medhelp.medhelp.data.model.CategoryResponse;
import com.medhelp.medhelp.data.model.ServiceResponse;
import com.medhelp.medhelp.ui._main_page.MainActivity;
import com.medhelp.medhelp.ui.base.BaseActivity;
import com.medhelp.medhelp.utils.view.ItemListDecorator;

import java.util.ArrayList;
import java.util.List;



import timber.log.Timber;

import static com.medhelp.medhelp.ui._main_page.MainActivity.POINTER_TO_PAGE;
import static com.medhelp.medhelp.ui.schedule.ScheduleFragment.EXTRA_BACK_PAGE;

public class ServiceActivity extends BaseActivity implements ServiceHelper.View, Spinner.OnItemSelectedListener
{
    public static final String EXTRA_DATA_ID_DOCTOR = "EXTRA_DATA_ID_DOCTOR";
    public static final String EXTRA_DATA_SERVICE = "EXTRA_DATA_SERVICE";
    public static final String EXTRA_DATA_ID_BRANCH = "EXTRA_DATA_ID_BRANCH";
    public static final String EXTRA_DATA_ID_USER = "EXTRA_DATA_ID_USER";

    ServicePresenter presenter;

    RecyclerView recyclerView;
    TextView errMessage;
    Button errLoadBtn;
    Spinner spinner;
    Toolbar toolbar;
    CollapsingToolbarLayout toolbarLayout;
    ConstraintLayout noServices;

    private ServiceAdapter adapter;
    private List<CategoryResponse> filterList;
    private List<ServiceResponse> serviceCash;

    private int idDoctor;
    private int idService;
    int idBranch;
    int idUser;

    int backPage=0;

    public int getBackPage()
    {
        return backPage;
    }

    public static Intent getStartIntent(Context context)
    {
        return new Intent(context, ServiceActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);

        Timber.tag("my").i("Прейскурант услуг для конкретного доктора");

        initValue();

        setUp();
    }
    private void initValue(){
        recyclerView=findViewById(R.id.rv_service);
        errMessage=findViewById(R.id.err_tv_message);
        errLoadBtn=findViewById(R.id.err_load_btn);
        spinner=findViewById(R.id.spinner_service);
        toolbar=findViewById(R.id.toolbar_service);
        toolbarLayout=findViewById(R.id.collapsing_toolbar_service);
        noServices=findViewById(R.id.noServices);
    }

    @Override
    protected void setUp()
    {
        presenter=new ServicePresenter(this,this);
        idService=getIntent().getExtras().getInt(EXTRA_DATA_SERVICE);

        spinner.setVisibility(android.view.View.GONE);
        setupToolbar();
        serviceCash = new ArrayList<>();
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
        {
            backPage=bundle.getInt(EXTRA_BACK_PAGE);

            idDoctor = bundle.getInt(EXTRA_DATA_ID_DOCTOR,0);

            try {
                idBranch = bundle.getInt(EXTRA_DATA_ID_BRANCH);
                idUser = bundle.getInt(EXTRA_DATA_ID_USER);
            } catch (Exception e) {
                // all right
            } finally {
                if(idBranch==0 || idUser==0) {
                    idBranch = presenter.prefManager.getCurrentUserInfo().getIdBranch();
                    idUser = presenter.prefManager.getCurrentUserInfo().getIdUser();
                }
            }

           // presenter.getFilesForRecy(idDoctor,idBranch,idUser);
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        if(searchViewAndroidActionBar!=null)
        {
            searchViewAndroidActionBar.setQuery("", false);
            searchViewItem.collapseActionView();
        }

        presenter.getData(idDoctor,idBranch,idUser);
    }

    @SuppressWarnings("unused")
    private void setupToolbar()
    {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        AppBarLayout.LayoutParams appBarParams = (AppBarLayout.LayoutParams) toolbarLayout.getLayoutParams();

        toolbar.setOnClickListener(v -> searchViewItem.expandActionView());

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if (actionBar != null)
        {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back_white_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
    }


    MenuItem searchViewItem;
    SearchView searchViewAndroidActionBar;
    @Override
    @SuppressWarnings("all")
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        searchViewItem = menu.findItem(R.id.action_search);
        searchViewAndroidActionBar = (SearchView) MenuItemCompat.getActionView(searchViewItem);

        if(searchViewAndroidActionBar==null)
            return false;

        searchViewAndroidActionBar.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                searchViewAndroidActionBar.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                spinner.setSelection(0);
                final List<ServiceResponse> filteredModelList = filterService(serviceCash, newText);
                if (filteredModelList != null && filteredModelList.size() > 0)
                {
                    adapter.setFilter(filteredModelList);

                }
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, android.view.View view, int i, long l)
    {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView)
    {

    }

    ServiceSpinnerAdapter spinnerAdapter;
    boolean latchFirstChoiceSpinner=false;

    @Override
    public void updateView(List<CategoryResponse> categories, List<ServiceResponse> services)
    {
        errMessage.setVisibility(android.view.View.GONE);
        errLoadBtn.setVisibility(android.view.View.GONE);
        spinner.setVisibility(android.view.View.VISIBLE);
        recyclerView.setVisibility(android.view.View.VISIBLE);

        if(services.get(0).getTitle()==null)
        {
            if(noServices!=null)
                noServices.setVisibility(View.VISIBLE);
            return;
        }

        adapter = new ServiceAdapter(serviceCash, idDoctor, new ServiceAdapter.ItemListener() {
            @Override
            public void clickFab(ServiceResponse item) {
                presenter.changeFabFavorites(item);
            }


            @Override
            public void clickRecord(ServiceAdapter.ViewHolder holder, int service, int limit) {
                presenter.testToSpam(holder, service, limit);
            }
        });
        recyclerView.addItemDecoration(new ItemListDecorator(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        filterList = new ArrayList<>();
        filterList.add(0, new CategoryResponse("Все"));
        filterList.add(1, new CategoryResponse("Избранные услуги"));
        filterList.addAll(categories);

        spinnerAdapter = new ServiceSpinnerAdapter(this, filterList);
        spinner.setAdapter(spinnerAdapter);

        if(!latchFirstChoiceSpinner)
        {
            setSelectedItemForSpinner();
            latchFirstChoiceSpinner=true;
        }

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, android.view.View view, int position, long id)
            {
                if (position == 0)
                {
                    adapter.addItems(sortByWight(services));
                }else if(position==1)
                {
                    adapter.addItems(selectItemWithTab(sortByWight(services)));
                }
                else
                {
                    List<ServiceResponse> serviceList = new ArrayList<>();
                    for (ServiceResponse serviceResponse : services)
                    {
                        if (serviceResponse.getIdSpec() == filterList.get(position).getId())
                        {
                            serviceList.add(serviceResponse);
                        }
                    }
                    adapter.addItems(sortByWight(serviceList));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {

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

    private void setSelectedItemForSpinner()
    {
        int i=0;
        for(;i<filterList.size();i++)
        {
            if(filterList.get(i).getId()==idService)
            {
                break;
            }
        }

        if(i==filterList.size())
        {
            i=0;
        }

        spinner.setSelection(i);
    }

    @Override
    public void showErrorScreen()
    {
        recyclerView.setVisibility(android.view.View.GONE);
        spinner.setVisibility(android.view.View.GONE);
        errMessage.setVisibility(android.view.View.VISIBLE);
        errLoadBtn.setVisibility(android.view.View.VISIBLE);
        errLoadBtn.setOnClickListener(v ->
                presenter.getData(idDoctor,idBranch,idUser));
    }

    @Override
    public void refreshRecy() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed()
    {
//            presenter.unSubscribe();
//            super.onBackPressed();
        Intent intent=new Intent(this, MainActivity.class);
        intent.putExtra(POINTER_TO_PAGE,backPage);
        startActivity(intent);
    }


    private List<ServiceResponse> filterService(List<ServiceResponse> models, String query)
    {
        query = query.toLowerCase();
        final List<ServiceResponse> filteredModelList = new ArrayList<>();
        for (ServiceResponse model : models)
        {
            final String text = model.getTitle().toLowerCase();
            if (text.contains(query))
            {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

    @Override
    public void userRefresh() {

    }
}

