package com.medhelp.medhelp.ui.finances_and_services;

import android.app.Activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
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
import com.medhelp.medhelp.data.model.VisitResponse;
import com.medhelp.medhelp.ui._main_page.MainActivity;
import com.medhelp.medhelp.ui._main_page.MainFragmentHelper;
import com.medhelp.medhelp.ui.base.BaseFragment;
import com.medhelp.medhelp.ui.finances_and_services.recy.FinancesAdapter;
import com.medhelp.medhelp.ui.finances_and_services.recy.FinancesHolder;
import com.medhelp.medhelp.ui.view.ChosenForPurchaseView;
import com.medhelp.medhelp.ui.view.shopping_basket.ShoppingBasketFragment;
import com.medhelp.medhelp.ui.view.shopping_basket.sub.ShoppingBasketFragmentListener;
import com.medhelp.medhelp.utils.main.MainUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


import timber.log.Timber;

public class FinancesAndServicesFragment extends BaseFragment implements FinancesAndServicesHelper.View/*, ShoppingBasketFragmentListener */{

    FinancesAndServicesHelper.Presenter presenter;
    MainFragmentHelper mainFragmentHelper;

    RecyclerView recyclerView;
    Toolbar toolbar;
    TextView errMessage;
    TextView errLoadBtn;
    CollapsingToolbarLayout toolbarLayout;
    ConstraintLayout rootEmpty;
    ChosenForPurchaseView cfpv;

    private DialogFragment shoppingBasketFragment;

    Context context;
    Activity activity;

    public static Fragment newInstance()
    {
        FinancesAndServicesFragment myFragment = new FinancesAndServicesFragment();
        return myFragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Timber.tag("my").i("Финансы и услуги");

        context =getContext();
        activity =getActivity();
        mainFragmentHelper=(MainFragmentHelper)context;
    }

    @Override
    protected void setUp(View view) {
        cfpv.setListener(new ChosenForPurchaseView.ChosenForPurchaseViewListener() {
            @Override
            public void isShownView(Boolean boo) {
                if (boo) {
                    setBottomPaddingRecy(108);
                } else {
                    setBottomPaddingRecy(0);
                }
            }

            @Override
            public void onClickBtn(List<VisitResponse> items) {
                FragmentTransaction ft =getFragmentManager().beginTransaction();
                Fragment prev = getFragmentManager().findFragmentByTag("dialog");
                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);

                shoppingBasketFragment = new ShoppingBasketFragment();

                Bundle bundle=new Bundle();
                bundle.putParcelableArrayList("mList", (ArrayList<? extends Parcelable>) items);

                ((ShoppingBasketFragment)shoppingBasketFragment).setListener(new ShoppingBasketFragmentListener() {
                    @Override
                    public void closeBasketFragment() {
                        recyclerView.post(() -> presenter.getVisits());
                    }
                });

                shoppingBasketFragment.setArguments(bundle);
                shoppingBasketFragment.show(ft, "dialog");
            }

            @Override
            public void onClickCross() {
                recyclerView.post(() ->
                        presenter.getVisits());
            }

            @Override
            public void limitReached() {
                ((FinancesAdapter)recyclerView.getAdapter()).setInaccessibleBtnPay();
            }

            @Override
            public void limitIsOver() {
                ((FinancesAdapter)recyclerView.getAdapter()).setAffordableBtnPay();
            }
        });

        presenter.getVisits();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        presenter=new FinancesAndServicesPresenter(context,this);
        View rootView=inflater.inflate(R.layout.activity_finances_and_services,container,false);

        recyclerView=rootView.findViewById(R.id.rv_sale);
        toolbar=rootView.findViewById(R.id.toolbar_sale);
        errMessage=rootView.findViewById(R.id.err_tv_message);
        errLoadBtn=rootView.findViewById(R.id.err_load_btn);
        toolbarLayout=rootView.findViewById(R.id.collapsing_toolbar_sale);
        rootEmpty=rootView.findViewById(R.id.rootEmpty);
        cfpv=rootView.findViewById(R.id.cfpv);

        setupToolbar();

        return rootView;
    }


    private void setupToolbar() {
        ((MainActivity)context).setSupportActionBar(toolbar);
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


    private void setBottomPaddingRecy(int dp)
    {
        if(dp!=0)
            dp=MainUtils.dpToPx(context, dp);

        recyclerView.setPadding(0,0,0,dp);
    }




    @Override
    public void showErrorScreen() {
        if(recyclerView!=null)
            recyclerView.setVisibility(View.GONE);

        if(errMessage!=null)
            errMessage.setVisibility(View.VISIBLE);

        if(errLoadBtn!=null) {
            errLoadBtn.setVisibility(View.VISIBLE);
            errLoadBtn.setOnClickListener(v -> presenter.getVisits());
        }
    }

    @Override
    public void updateData(List<VisitResponse> list, String today, String time) {
        if(list.size()==1 && list.get(0).getNameSotr()==null)
        {
            rootEmpty.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            return;
        }

        rootEmpty.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);

        Collections.sort(list);
        Collections.reverse(list);

        FinancesAdapter adapter=new FinancesAdapter(context, list, new FinancesHolder.FinancesHolderListener() {
            @Override
            public void toBasket(VisitResponse item,boolean toPay) {
                if(toPay)
                    cfpv.addItem(item);
                else
                    cfpv.deleteItem(item);
            }
        },time,today);

        LinearLayoutManager layoutManager=new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }


/*    @Override
    public void closeBasketFragment() {
        recyclerView.post(() ->
                presenter.getVisits());
    }*/

    @Override
    public void userRefresh() {
        presenter.getVisits();
    }
}
