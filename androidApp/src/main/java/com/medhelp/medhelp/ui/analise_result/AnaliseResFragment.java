package com.medhelp.medhelp.ui.analise_result;

import android.Manifest;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.medhelp.medhelp.Constants;
import com.medhelp.medhelp.R;
import com.medhelp.medhelp.data.model.AnaliseResponse;
import com.medhelp.medhelp.ui._main_page.MainActivity;
import com.medhelp.medhelp.ui._main_page.MainFragmentHelper;
import com.medhelp.medhelp.ui.analise_result.recycler.AnaliseAdapter;
import com.medhelp.medhelp.ui.analise_result.recycler.AnaliseRecyItemListener;
import com.medhelp.medhelp.ui.analise_result.showPdf.ShowPDFActivity;
import com.medhelp.medhelp.ui.base.BaseFragment;
import com.medhelp.medhelp.utils.main.MainUtils;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionButton;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionHelper;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionLayout;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RFACLabelItem;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RapidFloatingActionContentLabelList;

import java.util.ArrayList;
import java.util.List;



import timber.log.Timber;

/*todo сделать удаление файлов на стороне ShowFile или отправлятьт туда событие; проверку на входе на загрузку в данный момент; возможно перенести получение всех файлов в папке на сторону ShowFile*/

public class AnaliseResFragment extends BaseFragment implements AnaliseRecyItemListener, RapidFloatingActionContentLabelList.OnRapidFloatingActionContentLabelListListener {
    public static Fragment newInstance() {
        return new AnaliseResFragment();
    }

    RecyclerView recyclerView;
    Toolbar toolbar;
    TextView errMessage;
    TextView errLoadBtn;
    CollapsingToolbarLayout toolbarLayout;
    ConstraintLayout rootEmpty;
    RapidFloatingActionLayout rfaLayout;
    RapidFloatingActionButton rfaBtn;

    private AnaliseResPresenter presenter;
    private MainFragmentHelper mainFragmentHelper;
    private RapidFloatingActionHelper rfabHelper;
    private AnaliseAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_analise, container, false);
        initValue( rootView);
        Timber.tag("my").i("Список результатов анализов");
        return rootView;
    }
    private void initValue(View v){
        recyclerView=v.findViewById(R.id.rv_analise);
        toolbar=v.findViewById(R.id.toolbar_analise);
        errMessage=v.findViewById(R.id.err_tv_message);
        errLoadBtn=v.findViewById(R.id.err_load_btn);
        toolbarLayout=v.findViewById(R.id.collapsing_toolbar_analise);
        rootEmpty=v.findViewById(R.id.rootEmpty);
        rfaLayout=v.findViewById(R.id.activity_main_rfal);
        rfaBtn=v.findViewById(R.id.activity_main_rfab);
    }

    @Override
    protected void setUp(View view) {
        mainFragmentHelper = (MainFragmentHelper) getContext();
        clearChatNotification();

        presenter = new AnaliseResPresenter(getContext(), this);

        setupToolbar();
        initRFAB();
//
//        Boolean bb=checkPermissions();
//        Boolean bb2=android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R;
//        int dd= ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (checkPermissions() || android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            presenter.updateAnaliseList();
        } else {
            ((MainActivity) getContext()).setListener(new MainActivity.ListenerMainActivity() {
                @Override
                public void permissionGranted() {
                    presenter.updateAnaliseList();
                }

                @Override
                public void permissionDenied() {}
            });
            ((MainActivity) getContext()).requestPermissions();
        }
    }

    private void setupToolbar() {
        ((MainActivity) getContext()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((MainActivity) getContext()).getSupportActionBar();
        toolbarLayout.setTitleEnabled(false);

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

    public void showErrorScreen() {
        errMessage.setVisibility(View.VISIBLE);
        errLoadBtn.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        errLoadBtn.setOnClickListener(v -> presenter.updateAnaliseList());
    }

    //region rfab
    @Override
    public void onRFACItemLabelClick(int position, RFACLabelItem item) {
        rfabHelper.toggleContent();
        rfabClickSubItem(position);
    }

    @Override
    public void onRFACItemIconClick(int position, RFACLabelItem item) {
        rfabHelper.toggleContent();
        rfabClickSubItem(position);
    }

    private void initRFAB() {
        RapidFloatingActionContentLabelList rfaContent = new RapidFloatingActionContentLabelList(getContext());
        rfaContent.setOnRapidFloatingActionContentLabelListListener(this);
        List<RFACLabelItem> items = new ArrayList<>();

        int corner = MainUtils.dip2px(getContext(), 4);
        ShapeDrawable sd = MainUtils.generateCornerShapeDrawable(0xFF00C853, corner, corner, corner, corner);
        Integer color = Color.WHITE;

        items.add(new RFACLabelItem<Integer>()
                .setLabel("Записаться")
                .setResId(R.drawable.ic_create_white_24dp)
                .setIconNormalColor(0xFF00C853)
                .setIconPressedColor(0xFF00E676)
                .setLabelBackgroundDrawable(sd)
                .setLabelColor(color)
                .setWrapper(0)
        );
        items.add(new RFACLabelItem<Integer>()
                .setLabel("Позвонить ")
                .setResId(R.drawable.ic_call_white_24dp)
                .setIconNormalColor(0xFF00C853)
                .setIconPressedColor(0xFF00E676)
                .setLabelBackgroundDrawable(sd)
                .setLabelColor(color)
                .setWrapper(2)
        );
        items.add(new RFACLabelItem<Integer>()
                .setLabel("Перейти на сайт")
                .setResId(R.drawable.ic_web_white_24dp)
                .setIconNormalColor(0xFF00C853)
                .setIconPressedColor(0xFF00E676)
                .setLabelBackgroundDrawable(sd)
                .setLabelColor(color)
                .setWrapper(3)
        );

        rfaContent
                .setItems(items)
                .setIconShadowRadius(2)
                .setIconShadowColor(0xff888888)
                .setIconShadowDy(2)
        ;
        rfabHelper = new RapidFloatingActionHelper(
                getContext(),
                rfaLayout,
                rfaBtn,
                rfaContent
        ).build();
    }

    private void rfabClickSubItem(int num) {
        switch (num) {
            case 0:
                ((MainActivity) getContext()).selectedFragmentItem(Constants.MENU_RECORD,false);
                break;

            case 1:
                callToCenter(presenter.getCenterInfo().getPhone());
                break;

            case 2:
                callToSite(presenter.getCenterInfo().getSite());
                break;
        }
    }


    private void callToSite(String address) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://" + address));
        startActivity(browserIntent);
    }

    private void callToCenter(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phone));
        startActivity(intent);
    }

    @Override
    public void userRefresh() {
        presenter.updateAnaliseList();
    }
    //endregion

    public void updateAnaliseData(List<AnaliseResponse> response) {
        try {
            if (response.get(0).getLinkToPDF() == null) {
                rootEmpty.setVisibility(ViewGroup.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                return;
            }

            rootEmpty.setVisibility(ViewGroup.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            errMessage.setVisibility(View.GONE);
            errLoadBtn.setVisibility(View.GONE);

            if (adapter == null) {
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
                recyclerView.setLayoutManager(layoutManager);
                adapter = new AnaliseAdapter(getContext(), response, this);
                recyclerView.setAdapter(adapter);
            } else {
                adapter.setList(response);
            }
        } catch (Exception e) {
            Log.wtf("fat", e.getMessage());
        }
    }

    private void clearChatNotification() {
//        NotificationManager notificationManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
//        notificationManager.cancel(ShowNotification.ID_ANALIZ_GROUP);
    }

    public void downloadPDF(int position) {
        if (checkPermissions() || android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            adapter.getList().get(position).setHideDownload(false);
            adapter.checkList();
            adapter.notifyItemChanged(position);

            presenter.loadFile(position, adapter.getList());
        } else {
            ((MainActivity) getContext()).requestPermissions();
        }
    }

    public void openPDF(int position) {
        Intent intent = new Intent(getContext(), ShowPDFActivity.class);
        intent.putExtra(ShowPDFActivity.PATH_TO_FILE, adapter.getList().get(position).getPathToFile());
        startActivity(intent);
    }

    public void deletePDFDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Вы действительно хотите удалить файл?")
                .setNegativeButton("нет", (dialog, which) -> dialog.dismiss())
                .setPositiveButton("да", (dialog, which) -> {
                    dialog.dismiss();
                    presenter.deleteFile(position, adapter.getList());
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void showErrorDownload() {
        showError(getResources().getString(R.string.empty_error_message));
    }
}
