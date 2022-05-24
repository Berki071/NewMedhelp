//package com.medhelp.medhelp.ui.video_consultation;
//
//import android.Manifest;
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.os.Bundle;
//import android.os.Handler;
//import androidx.annotation.Nullable;
//import androidx.constraintlayout.widget.ConstraintLayout;
//import androidx.fragment.app.Fragment;
//import androidx.core.content.ContextCompat;
//import androidx.appcompat.app.ActionBar;
//import androidx.appcompat.app.AlertDialog;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//import androidx.appcompat.widget.Toolbar;
//import android.text.Html;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.TextView;
//
//import com.medhelp.medhelp.R;
//import com.medhelp.medhelp.data.model.VisitResponse;
//import com.medhelp.medhelp.ui._main_page.MainActivity;
//import com.medhelp.medhelp.ui._main_page.MainFragmentHelper;
//import com.medhelp.medhelp.ui.base.BaseFragment;
//import com.medhelp.medhelp.ui.video_consultation.recy.ConsultationAdapter;
//import com.medhelp.medhelp.ui.video_consultation.recy.ConsultationHolder;
//import com.medhelp.medhelp.ui.video_consultation.video_chat.VideoChatActivity;
//
//import java.util.List;
//
//
//
//import timber.log.Timber;
//
//public class OnlineConsultationFragment extends BaseFragment implements ConsultationAdapter.ConsultationListener {
//    RecyclerView recy;
//    Toolbar toolbar;
//    ConstraintLayout rootEmpty;
//
//    OnlineConsultationPresenter presenter;
//    MainFragmentHelper mainFragmentHelper;
//    ConsultationAdapter adapter;
//
//    final int MY_PERMISSIONS_REQUEST_READ_CONTACTS=122;
//
//    private AlertDialog alertPermission;
//
//    Context context;
//    Activity activity;
//
//    public static Fragment newInstance()
//    {
//        OnlineConsultationFragment myFragment = new OnlineConsultationFragment();
//        return myFragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        Timber.tag("my").i("Вход в онлайн-консультацию");
//        context =getContext();
//        activity =getActivity();
//        mainFragmentHelper=(MainFragmentHelper)context;
//    }
//
//    @Override
//    protected void setUp(View view) {
//        setupToolbar();
//
//        presenter.getConsultationList();
//
//        if(!testPermission())
//        {
//            requestPermission();
//        }
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
//
//        presenter=new OnlineConsultationPresenter(context,this);
//        View rootView=inflater.inflate(R.layout.activity_online_consultation,container,false);
//
//        recy= rootView.findViewById(R.id.recy);
//        toolbar= rootView.findViewById(R.id.toolbar);
//        rootEmpty= rootView.findViewById(R.id.rootEmpty);
//
//        return rootView;
//    }
//
//
//    private void setupToolbar() {
//        ((MainActivity)context).setSupportActionBar(toolbar);
//        ActionBar actionBar = ((MainActivity)context).getSupportActionBar();
//
//        if (actionBar != null) {
//            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
//            actionBar.setDisplayHomeAsUpEnabled(true);
//            actionBar.setDisplayShowHomeEnabled(true);
//        }
//
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mainFragmentHelper.showNavigationMenu();
//            }
//        });
//    }
//
//
//
//    public void showErrorScreen() {
//        rootEmpty.setVisibility(View.VISIBLE);
//    }
//
//
//    public void updateRecy(List<VisitResponse> data) {
//        if(data==null  ||  data.size()==0 || (data.size()==1 && data.get(0).getDateOfReceipt()==null))
//        {
//            recy.setVisibility(View.GONE);
//            rootEmpty.setVisibility(View.VISIBLE);
//        }
//        else
//        {
//            recy.setVisibility(View.VISIBLE);
//            rootEmpty.setVisibility(View.GONE);
//            adapter=new ConsultationAdapter(context,data,presenter.getUserToken(),this);
//            adapter.setHasStableIds(true);
//            recy.setLayoutManager(new LinearLayoutManager(context));
//            recy.setAdapter(adapter);
//            startRefreshRecy();
//        }
//    }
//
//    Handler handler;
//    private void startRefreshRecy()
//    {
//        Handler handler=new Handler();
//
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                adapter.notifyDataSetChanged();
//                startRefreshRecy();
//            }
//        },1000*60);
//    }
//
//
//    @Override
//    public void onDestroy() {
//
//        int count=recy.getChildCount();
//
//        for (int i=0; i<count;i++)
//        {
//            ConsultationHolder holder =(ConsultationHolder)recy.getChildViewHolder(recy.getChildAt(i));
//            //holder.onDestroy();
//        }
//
//        if(handler!=null)
//            handler.removeMessages(0);
//        super.onDestroy();
//    }
//
//    @Override
//    public void onClickItemRecy(VisitResponse itm) {
//        nextPage(itm);
//    }
//
//    private void nextPage(VisitResponse itm)
//    {
//        if(testPermission()) {
//            Intent intent = new Intent(context, VideoChatActivity.class);
//            intent.putExtra(VisitResponse.class.getCanonicalName(), itm);
//            startActivity(intent);
//            activity.finish();
//        }
//        else
//        {
//            alertAboutPermission();
//        }
//    }
//
//
//    private void alertAboutPermission()
//    {
//        String str="Для продолжения работы необходим доступ к камере и микрофону телефона";
//
//        LayoutInflater inflater= getLayoutInflater();
//        View view=inflater.inflate(R.layout.dialog_2textview_btn,null);
//
//        TextView title=view.findViewById(R.id.title);
//        TextView text=view.findViewById(R.id.text);
//        Button btnYes =view.findViewById(R.id.btnYes);
//        Button btnNo =view.findViewById(R.id.btnNo);
//
//        title.setText(Html.fromHtml("<u>Необходимо разрешение от пользователя</u>"));
//        text.setText(str);
//
//        btnYes.setOnClickListener(v -> {
//            requestPermission();
//            alertPermission.cancel();
//        });
//
//        btnNo.setVisibility(View.VISIBLE);
//        btnNo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                alertPermission.cancel();
//            }
//        });
//
//        AlertDialog.Builder builder=new AlertDialog.Builder(context);
//        builder.setView(view);
//        alertPermission =builder.create();
//        alertPermission.setCanceledOnTouchOutside(false);
//        alertPermission.show();
//    }
//
//    private boolean testPermission()
//    {
//        // Here, thisActivity is the current activity
//        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED  ||
//                ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED ||
//                ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
//            return false;
//        } else {
//            return true;
//        }
//    }
//
//    private void requestPermission()
//    {
//        requestPermissions(new String[]{Manifest.permission.CAMERA,
//                Manifest.permission.RECORD_AUDIO,
//                Manifest.permission.READ_PHONE_STATE}, MY_PERMISSIONS_REQUEST_READ_CONTACTS);
//    }
//
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        if (requestCode == MY_PERMISSIONS_REQUEST_READ_CONTACTS) {
//            if (grantResults.length > 0) {
//                Boolean isGranted = true;
//                for (int res : grantResults) {
//                    if (res != PackageManager.PERMISSION_GRANTED) {
//                        isGranted = false;
//                    }
//                }
//            }
//            return;
//        }
//    }
//
//    @Override
//    public void userRefresh() {
//        presenter.getConsultationList();
//    }
//}
