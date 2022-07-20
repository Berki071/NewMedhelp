package com.medhelp.medhelp.ui.electronic_conclusions_fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.medhelp.medhelp.R;
import com.medhelp.medhelp.data.model.AnaliseResponse;
import com.medhelp.medhelp.data.model.ResultZakl2Item;
import com.medhelp.medhelp.ui._main_page.MainActivity;
import com.medhelp.medhelp.ui._main_page.MainFragmentHelper;
import com.medhelp.medhelp.ui.analise_result.showPdf.ShowPDFActivity;
import com.medhelp.medhelp.ui.base.BaseFragment;
import com.medhelp.medhelp.ui.doctor.adapters.DocSpinnerAdapter;
import com.medhelp.medhelp.ui.electronic_conclusions_fragment.adapters.MySpinnerAdapter;
import com.medhelp.medhelp.ui.electronic_conclusions_fragment.adapters.recy.DataClassForElectronicRecy;
import com.medhelp.medhelp.ui.electronic_conclusions_fragment.adapters.recy.ElectronicConclusionsAdapter;
import com.medhelp.medhelp.ui.electronic_conclusions_fragment.adapters.recy.ElectronicConclusionsHolder;

import java.util.List;


import kotlin.Triple;

public class ElectronicConclusionsFragment extends BaseFragment {

    Toolbar toolbar;
    Spinner spinner;
    RecyclerView recy;
    ConstraintLayout rootEmpty;
    TextView errMessage;
    TextView errLoadBtn;

    MainFragmentHelper mainFragmentHelper;
    ElectronicConclusionsPresenter presenter;

    MySpinnerAdapter spinnerAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_electronic_conclusions,container, false);

        initValue(v);
        setHasOptionsMenu(true);
        initClick();

        return v;
    }
    private void initValue(View v){
        toolbar=v.findViewById(R.id.toolbar);
        spinner=v.findViewById(R.id.spinner);
        recy=v.findViewById(R.id.recy);
        rootEmpty=v.findViewById(R.id.rootEmpty);
        errMessage=v.findViewById(R.id.err_tv_message);
        errLoadBtn=v.findViewById(R.id.err_load_btn);

        ((MainActivity)getActivity()).setSupportActionBar(toolbar);
        mainFragmentHelper=(MainFragmentHelper)getContext();

        presenter=new ElectronicConclusionsPresenter(this);
    }
    private void initClick(){

        toolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainFragmentHelper.showNavigationMenu();
            }
        });
    }

    @Override
    protected void setUp(View view) {
        presenter.getData();
    }

    public void initSpinner(List<String> list){
        spinnerAdapter = new MySpinnerAdapter(getContext(), list);

        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
//                if(position==0)
//                {
//                    presenter.getDoctorList(-1);
//                }
//                else
//                {
//                    presenter.getDoctorList(response.get(position-1).getId());
//                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
    }

    ElectronicConclusionsAdapter electronicConclusionsAdapter;
    public void initRecy(List<DataClassForElectronicRecy> list){
        errMessage.setVisibility(View.GONE);
        errLoadBtn.setVisibility(View.GONE);

        if(list==null){
            showEmpty(true);
            return;
        }else
            showEmpty(false);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        electronicConclusionsAdapter=new ElectronicConclusionsAdapter(getContext(), list, new ElectronicConclusionsHolder.ElectronicConclusionsHolderListener() {
            @Override
            public void openHideBox(DataClassForElectronicRecy data) {
                electronicConclusionsAdapter.processingShowHideBox(data);
            }

            @Override
            public void showDoc(DataClassForElectronicRecy data) {
                if(data.isDownloadIn())
                    openPDF(data);
                else
                    downloadPDF(data);
            }

            @Override
            public void deleteDoc(DataClassForElectronicRecy data) {
                deletePDFDialog(data);
            }
        });

        recy.setLayoutManager(linearLayoutManager);
        recy.setAdapter(electronicConclusionsAdapter);
    }

    public void openPDF(DataClassForElectronicRecy data) {
        Intent intent = new Intent(getContext(), ShowPDFActivity.class);
        intent.putExtra(ShowPDFActivity.PATH_TO_FILE, data.getPathToFile());
        startActivity(intent);
    }

    public void downloadPDF(DataClassForElectronicRecy data) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R || checkPermissions()) {
            data.setHideDownload(false);

            electronicConclusionsAdapter.updateItemInRecy(data);

            if(data instanceof AnaliseResponse)
                presenter.loadFile((AnaliseResponse)data);
            else
                presenter.loadFile2((ResultZakl2Item)data);
        } else {
           ((MainActivity) getContext()).requestPermissions();
            //((MainActivity) getContext()).requestPermissionsRead();
        }
    }

    public void deletePDFDialog(DataClassForElectronicRecy data) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Вы действительно хотите удалить файл?")
                .setNegativeButton("нет", (dialog, which) -> dialog.dismiss())
                .setPositiveButton("да", (dialog, which) -> {
                    dialog.dismiss();
                    presenter.deleteFile(data);
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showEmpty (Boolean boo){
        if(boo){
            rootEmpty.setVisibility(View.VISIBLE);
            recy.setVisibility(View.GONE);
        }else{
            rootEmpty.setVisibility(View.GONE);
            recy.setVisibility(View.VISIBLE);
        }
    }

    public void showErrorScreen() {
        errMessage.setVisibility(View.VISIBLE);
        errLoadBtn.setVisibility(View.VISIBLE);
        recy.setVisibility(View.GONE);
        errLoadBtn.setOnClickListener(v -> presenter.getData());
    }

    @Override
    public void userRefresh() {
        presenter.getData();
    }

    public void showErrorDownload() {
        showError(getResources().getString(R.string.empty_error_message));
    }
}
