package com.medhelp.medhelp.ui.settings.favorites_tab;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.medhelp.medhelp.Constants;
import com.medhelp.medhelp.R;
import com.medhelp.medhelp.data.model.settings.AllTabsResponse;
import com.medhelp.medhelp.data.network.NetworkManager;
import com.medhelp.medhelp.data.pref.PreferencesManager;
import com.medhelp.medhelp.ui._main_page.MainActivity;
import com.medhelp.medhelp.ui.base.BaseActivity;
import com.medhelp.medhelp.ui.settings.favorites_tab.recy.FabAdapter;
import com.medhelp.medhelp.utils.rx.AppSchedulerProvider;
import com.medhelp.medhelp.utils.rx.SchedulerProvider;
import com.medhelp.medhelp.utils.timber_log.LoggingTree;
import java.util.List;
import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

import static com.medhelp.medhelp.ui._main_page.MainActivity.POINTER_TO_PAGE;

public class FavoritesTabActivity extends BaseActivity  implements FabAdapter.FabRecyListener{

    Toolbar toolbar;
    RecyclerView recy;
    TextView errMessage;
    TextView errLoadBtn;

    PreferencesManager preferencesManager;
    NetworkManager networkManager;
    SchedulerProvider schedulerProvider;

    FabAdapter fabAdapter;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites_tab);

        Timber.tag("my").i("Настройка выбранных закладок");
        initValue();
        setUp();
    }
    private void initValue(){
        toolbar=findViewById(R.id.toolbar);
        recy=findViewById(R.id.recy);
        errMessage=findViewById(R.id.err_tv_message);
        errLoadBtn=findViewById(R.id.err_load_btn);
    }


    @Override
    protected void setUp() {
        initToolbar();
        initValues();

        recy.post(() -> getAllTabs());

    }

    private void initValues()
    {
        context=this;
        preferencesManager=new PreferencesManager(this);
        networkManager=new NetworkManager(preferencesManager);
        schedulerProvider=new AppSchedulerProvider();
    }

    private void initToolbar()
    {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Избранные услуги");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_white_24dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
        {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(this, MainActivity.class);
        intent.putExtra(POINTER_TO_PAGE, Constants.MENU_SETTINGS);
        startActivity(intent);
    }


    private void getAllTabs()
    {
        showLoading();
        CompositeDisposable cd=new CompositeDisposable();
        cd.add(networkManager
                .getTabPrice()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(response -> {
                            initRecy (response.getServices());

                            hideLoading();
                            cd.dispose();
                        }, throwable -> {
                            Timber.tag("my").e(LoggingTree.getMessageForError(throwable,"FavoritesTabActivity$getAllTabs "));
                            hideLoading();
                            cd.dispose();
                        }

                )

        );
    }

    private void initRecy(List<AllTabsResponse> data)
    {
        if(data.get(0).getTitle()==null)
        {
            showEmptyListMessage(true);
            return;
        }

        showEmptyListMessage(false);
        fabAdapter=new FabAdapter(this, data,this);
        recy.setLayoutManager(new LinearLayoutManager(this));
        recy.setAdapter(fabAdapter);
    }

    private void showEmptyListMessage(Boolean boo)
    {
        if(boo) {
            errMessage.setText(R.string.noTab);
            errMessage.setVisibility(View.VISIBLE);
            recy.setVisibility(View.GONE);
        }else{
            recy.setVisibility(View.VISIBLE);
            errMessage.setVisibility(View.GONE);
            errLoadBtn.setVisibility(View.GONE);
        }
    }


    @Override
    public void recyItemClick(AllTabsResponse item) {
        showAlert(item);
    }

    AlertDialog alert;
    private void showAlert(AllTabsResponse item)
    {
        LayoutInflater inflater= this.getLayoutInflater();
        View view=inflater.inflate(R.layout.dialog_2textview_btn,null);

        TextView title=view.findViewById(R.id.title);
        TextView text=view.findViewById(R.id.text);
        Button btnYes =view.findViewById(R.id.btnYes);
        Button btnNo =view.findViewById(R.id.btnNo);
        ImageView img=view.findViewById(R.id.img);

        title.setText("Вы действительно хотите удалить данную услугу из закладок");

        btnYes.setOnClickListener(v -> {
            alert.dismiss();
            deleteFab(item);
        });

        btnNo.setOnClickListener(v -> alert.dismiss());

        img.setVisibility(View.GONE);
        text.setVisibility(View.GONE);

        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setView(view);

        alert=builder.create();
        alert.show();
    }

    private void deleteFab(AllTabsResponse item)
    {
        setLoadingItem(item,true);

        CompositeDisposable cd=new CompositeDisposable();
        cd.add(networkManager
                .deleteFavoritesService(item.getId())
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(response->{
                            fabAdapter.deleteItemList(item);
                            setLoadingItem(item,false);

                            if(fabAdapter.getItemCount()==0)
                            {
                                showEmptyListMessage(true);
                            }

                            cd.dispose();
                        },
                        throwable ->{
                            Timber.tag("my").e(LoggingTree.getMessageForError(throwable,"FavoritesTabActivity$deleteFab "));

                            setLoadingItem(item,false);
                            showError("Не удалось удалить из избранного");
                            //Log.wtf("mLog", throwable.getMessage());
                            cd.dispose();
                        } )
        );
    }

    private void setLoadingItem(AllTabsResponse item, boolean showFlag)
    {
        item.setLoading(showFlag);
        fabAdapter.notifyDataSetChanged();
    }

    @Override
    public void userRefresh() {}
}
