package com.medhelp.medhelp.ui.profile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.play.core.tasks.Task;
import com.google.gson.Gson;
import com.medhelp.medhelp.Constants;
import com.medhelp.medhelp.R;
import com.medhelp.medhelp.data.model.VisitResponse;
import com.medhelp.medhelp.ui._main_page.MainActivity;
import com.medhelp.medhelp.ui._main_page.MainFragmentHelper;
import com.medhelp.medhelp.ui.base.BaseFragment;
import com.medhelp.medhelp.ui.doctor.service_activity.ServiceActivity;
import com.medhelp.medhelp.ui.profile.allertDialog.AlertForCancel;
import com.medhelp.medhelp.ui.profile.recycler.ItemClickListener;
import com.medhelp.medhelp.ui.profile.recycler.ProfileAdapter;
import com.medhelp.medhelp.ui.profile.recycler.ProfileParentModel;
import com.medhelp.medhelp.ui.profile.recycler.ProfileVisitViewHolder;
import com.medhelp.medhelp.ui.sale.SaleFragment;
import com.medhelp.medhelp.ui.schedule.ScheduleFragment;
import com.medhelp.medhelp.ui.view.ChosenForPurchaseView;
import com.medhelp.medhelp.ui.view.shopping_basket.ShoppingBasketFragment;
import com.medhelp.medhelp.ui.view.shopping_basket.sub.ShoppingBasketFragmentListener;
import com.medhelp.medhelp.utils.workToFile.show_file.ShowFile2;
import com.medhelp.medhelp.utils.main.MainUtils;
import com.medhelp.shared.model.CenterResponse;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionButton;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionHelper;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionLayout;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RFACLabelItem;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RapidFloatingActionContentLabelList;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


import it.sephiroth.android.library.xtooltip.ClosePolicy;
import it.sephiroth.android.library.xtooltip.Tooltip;
import timber.log.Timber;

import static com.medhelp.medhelp.ui.schedule.ScheduleFragment.EXTRA_BACK_PAGE;

public class ProfileFragment extends BaseFragment implements ProfileHelper.View, ItemClickListener, RapidFloatingActionContentLabelList.OnRapidFloatingActionContentLabelListListener {

    Toolbar toolbar;
    CollapsingToolbarLayout toolbarLayout;
    ImageView logoProfile;
    TextView centerBranch;
    TextView centerPhone;
    ImageView icoPhone;
    TextView centerSite;
    ImageView icoSite;
    TextView errMessage;
    Button errLoadBtn;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeProfile;
    ConstraintLayout noPosts;
    RapidFloatingActionLayout rfaLayout;
    RapidFloatingActionButton rfaBtn;
    ChosenForPurchaseView cfpv;

    private RapidFloatingActionHelper rfabHelper;

    private DialogFragment shoppingBasketFragment;

    Context context;
    Activity activity;

    ProfilePresenter presenter;
    MainFragmentHelper mainFragmentHelper;

    SwipeRefreshLayout.OnRefreshListener refreshListener=new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            cfpv.closeView();
            presenter.getVisits();
        }
    };


    public static Fragment newInstance()
    {
        return new SaleFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.tag("my").i("Главная страница");

        context =getContext();
        activity =getActivity();
        presenter=new ProfilePresenter(context,this);
        mainFragmentHelper=(MainFragmentHelper)context;

        clearChatNotification();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.activity_profile,container,false);
        initValue(rootView);

        return rootView;
    }
    private void initValue(View v){
        toolbar= v.findViewById(R.id.toolbar_profile);
        toolbarLayout= v.findViewById(R.id.collapsing_toolbar_profile);
        logoProfile= v.findViewById(R.id.logo_center_profile);
        centerBranch= v.findViewById(R.id.tv_branch_profile);
        centerPhone= v.findViewById(R.id.tv_phone_profile);
        icoPhone= v.findViewById(R.id.icoPhone);
        centerSite= v.findViewById(R.id.tv_site_profile);
        icoSite= v.findViewById(R.id.icoSite);
        errMessage= v.findViewById(R.id.err_tv_message);
        errLoadBtn= v.findViewById(R.id.err_load_btn);
        recyclerView= v.findViewById(R.id.rv_profile);
        swipeProfile= v.findViewById(R.id.swipe_profile);
        noPosts= v.findViewById(R.id.noPosts);
        rfaLayout= v.findViewById(R.id.activity_main_rfal);
        rfaBtn= v.findViewById(R.id.activity_main_rfab);
        cfpv= v.findViewById(R.id.cfpv);
    }

    @Override
    protected void setUp(View view) {
        presenter.updateHeaderInfo();
        setupRefresh();
        initRFAB();
        testToolTip();

        recyclerView.post(() ->
                presenter.getVisits());

        cfpv.setListener(new ChosenForPurchaseView.ChosenForPurchaseViewListener() {
            @Override
            public void isShownView(Boolean boo) {
                if(boo)
                {
                    setBottomPaddingRecy(108);
                    rfaLayout.setVisibility(View.GONE);
                }
                else
                {
                    setBottomPaddingRecy(0);
                    rfaLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onClickBtn(List<VisitResponse> items) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                Fragment prev = getFragmentManager().findFragmentByTag("dialog");
                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);

                shoppingBasketFragment = new ShoppingBasketFragment();

                Bundle bundle=new Bundle();
                bundle.putParcelableArrayList("mList", (ArrayList<? extends Parcelable>) items);


                shoppingBasketFragment.setArguments(bundle);
                ((ShoppingBasketFragment)shoppingBasketFragment).setListener(new ShoppingBasketFragmentListener() {
                    @Override
                    public void closeBasketFragment() {
                        recyclerView.post(() -> presenter.getVisits());
                    }
                });
                shoppingBasketFragment.show(ft, "dialog");
            }

            @Override
            public void onClickCross() {
                recyclerView.post(() ->
                        presenter.getVisits());
            }

            @Override
            public void limitReached() {
                ((ProfileAdapter)recyclerView.getAdapter()).setInaccessibleBtnPay();
            }

            @Override
            public void limitIsOver() {
                ((ProfileAdapter)recyclerView.getAdapter()).setAffordableBtnPay();
            }
        });

        testExistIdRecord();
    }

    private void testExistIdRecord()
    {
//        try
//        {
//            if(getArguments()==null)
//                return;
//
//            int idRec=getArguments().getInt(ReminderAdmNoti.ID_RECORD,-1);
//            int idBranch=getArguments().getInt(ReminderAdmNoti.ID_BRANCH,-1);
//            int idUser=getArguments().getInt(ReminderAdmNoti.ID_USER,-1);
//
//            if(idRec!=-1)
//            {
//                cancelBtnClick(idUser,idRec,idBranch);
//            }
//        }
//        catch (Exception e) {}
    }


    @Override
    public void userRefresh() {
        presenter.getVisits();
    }

    private void clearChatNotification()
    {
//        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        notificationManager.cancel(ShowNotification.ID_REMINDER_GROUP);
    }

//    @Override
//    public void closeBasketFragment() {
//        recyclerView.post(() ->
//                presenter.getVisits());
//    }

    private void setBottomPaddingRecy(int dp)
    {
        if(dp!=0)
            dp= MainUtils.dpToPx(context, dp);

        recyclerView.setPadding(0,0,0,dp);
    }



    private void testToolTip()
    {
        //+
        centerBranch.post(() -> {
            if(presenter.prefManager.getShowTooltipProfile())
            {
                Tooltip builder = new Tooltip.Builder(context)
                        .anchor(centerBranch, 0, 0,false)
                        .closePolicy(
                                new ClosePolicy(300))
                        .activateDelay(10)
                        .text(context.getResources().getString(R.string.searchTooltipProfileBranch))
                        .maxWidth(600)
                        .showDuration(40000)
                        .arrow(true)
                        .styleId(R.style.ToolTipLayoutCustomStyle)
                        .overlay(true)
                        .create();

                builder.show(centerBranch, Tooltip.Gravity.BOTTOM,false);

                presenter.prefManager.setShowTooltipProfile();
            }
        });
    }


    private void callToCenter(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phone));
        startActivity(intent);
    }

    private void callToSite(String address)
    {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://"+address));
        startActivity(browserIntent);
    }



    @Override
    public void updateHeader(CenterResponse response) {

        centerBranch.setText(presenter.getCurrentHospitalBranch());

        if(response.getSite() != null && !response.getSite().equals("") )
        {
            centerSite.setText(response.getSite());
            centerSite.setOnClickListener(click->callToSite(response.getSite()));
            icoSite.setOnClickListener(click -> callToSite(response.getSite()));
        }

        if (response.getPhone() != null) {
            centerPhone.setText(response.getPhone());
            centerPhone.setOnClickListener(v -> {
                        callToCenter(response.getPhone());
                    }
            );
            icoPhone.setOnClickListener(v->callToCenter(response.getPhone()));
        }


        new ShowFile2.BuilderImage(context)
                .setType(ShowFile2.TYPE_ICO)
                .load(response.getLogo())
                .token(presenter.getUserToken())
                .imgError(R.drawable.sh_center)
                .into(logoProfile)
                .setListener(new ShowFile2.ShowListener() {
                    @Override
                    public void complete(File file) {
                    }

                    @Override
                    public void error(String error) {
                    }
                })
                .build();

        setupToolbar(response);
    }

    @SuppressWarnings("unused")
    private void setupToolbar(CenterResponse response) {
        ((MainActivity)context).setSupportActionBar(toolbar);
        ActionBar actionBar = ((MainActivity)context).getSupportActionBar();
        toolbarLayout.setTitleEnabled(false);
        actionBar.setTitle(response.getTitle());

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


    @Override
    public void updateData(List<VisitResponse> response, String today,String time) {

      // new EvaluateTheDoctor(this,response.get(0));

        ArrayList<ProfileParentModel> parentModels = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setVisibility(View.VISIBLE);
        errMessage.setVisibility(View.GONE);
        errLoadBtn.setVisibility(View.GONE);

        if (response.size() > 0 && response.get(0).getNameSotr()!=null && today != null) {
            noPosts.setVisibility(View.GONE);

            List<VisitResponse> actualReceptions = new ArrayList<>();
            List<VisitResponse> latestReceptions = new ArrayList<>();
            long currentTime=System.currentTimeMillis();

            for (VisitResponse visit : response) {
                if (visit.getTimeMills() >= currentTime) {
                    actualReceptions.add(visit);
                } else {
                    latestReceptions.add(visit);
                }
            }

            if (actualReceptions.size() > 0) {
                Collections.sort(actualReceptions);
                parentModels.add(new ProfileParentModel("Предстоящие", actualReceptions));
            }

            if (latestReceptions.size() > 0) {
                Collections.sort(latestReceptions);
                Collections.reverse(latestReceptions);
                parentModels.add(new ProfileParentModel("Прошедшие", latestReceptions));
            }

            ProfileAdapter adapter = new ProfileAdapter(activity, parentModels,today,time,this,presenter.getUserToken());
            recyclerView.setAdapter(adapter);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            adapter.onGroupClick(0);

        } else {
            recyclerView.setVisibility(View.GONE);
            noPosts.setVisibility(View.VISIBLE);
        }
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


    private void setupRefresh() {
        swipeProfile.setOnRefreshListener(refreshListener);
        swipeProfile.setColorSchemeColors(getResources().getColor(android.R.color.holo_red_light));
    }

    @Override
    public void swipeDismiss() {
        if(swipeProfile!=null)
            swipeProfile.setRefreshing(false);
    }


    //region recy item btn click listener
    @Override
    public void cancelBtnClick(int user, int id_record, int idBranch) {
          new AlertForCancel(context, msg -> presenter.cancellationOfVisit(user, id_record, msg, idBranch));
    }

    private AlertDialog alert;
    @Override
    public void confirmBtnClick(int user, int id_record, int idBranch, String str) {

        LayoutInflater inflater= getLayoutInflater();
        View view=inflater.inflate(R.layout.dialog_2textview_btn,null);

        TextView title=view.findViewById(R.id.title);
        TextView text=view.findViewById(R.id.text);
        Button btnYes =view.findViewById(R.id.btnYes);
        Button btnNo =view.findViewById(R.id.btnNo);

        title.setText(Html.fromHtml("<u>Ваш прием подтвержден</u>"));
        if(str.equals(""))
        {
            text.setText("");
        }
        else
        {
            text.setText(Html.fromHtml("<b>Обратите внимание на рекомендации перед приемом:</b><br><br>"+str));
        }

        btnYes.setOnClickListener(v -> {
            alert.dismiss();
            presenter.confirmationOfVisit(user, id_record, idBranch);
        });

        btnNo.setVisibility(View.GONE);
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setView(view);

        alert=builder.create();
        alert.setCanceledOnTouchOutside(false);
        alert.show();
    }

    @Override
    public void enrollAgainBtnClick(VisitResponse viz) {
        if(viz.getWorks().equals("да"))
        {
            Intent intent= ServiceActivity.getStartIntent(context);
            intent.putExtra(ServiceActivity.EXTRA_DATA_ID_DOCTOR,viz.getIdSotr());
            intent.putExtra(ServiceActivity.EXTRA_DATA_SERVICE,0);
            intent.putExtra(ServiceActivity.EXTRA_DATA_ID_BRANCH,viz.getIdBranch());
            intent.putExtra(ServiceActivity.EXTRA_DATA_ID_USER,viz.getIdUser());
            intent.putExtra(EXTRA_BACK_PAGE, Constants.MENU_THE_MAIN);

            startActivity(intent);
        }
    }

    @Override
    public void postponeBtnClick(VisitResponse viz) {
        Gson gson=new Gson();
        String newViz=gson.toJson(viz);

//        Intent intent= ScheduleFragment.getStartIntent(context);
//        intent.putExtra(ScheduleFragment.EXTRA_DATA_ID_VIZIT,newViz);
//        startActivity(intent);

        Bundle bundle=new Bundle();
        bundle.putString(ScheduleFragment.EXTRA_DATA_ID_VIZIT,newViz);
        bundle.putInt(EXTRA_BACK_PAGE,Constants.MENU_THE_MAIN);
        ((MainActivity)context).selectedFragmentSchedule(bundle);
    }

    @Override
    public void payBtnClick(VisitResponse viz, boolean toPay) {
        if (toPay)
            cfpv.addItem(viz);
        else
            cfpv.deleteItem(viz);
    }

    @Override
    public void confirmComing(VisitResponse viz) {
        presenter.sendConfirmComing(viz);
    }

    @Override
    public void responseConfirmComing(VisitResponse viz)
    {
        if(ProfileVisitViewHolder.statusIsPaid(viz.getStatus()))
        {
            showConfirmComing("Вы можете сразу пройти к кабинету  <big><br><b>"+viz.getCabinet() +"</b><br></big> и ожидать приглашения врача");
        }else{
            showConfirmComing("Необходимо подойти к регистратуре для оформления документов");
        }


    }

    AlertDialog alertDialogConfirmComing;
    private void showConfirmComing(String msg)
    {
        String titleS="Рады приветствовать Вас в нашем медицинском центре!";
        //String str=msg;

        LayoutInflater inflater= LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.dialog_2textview_btn,null);

        TextView title=view.findViewById(R.id.title);
        TextView text=view.findViewById(R.id.text);
        Button btnYes =view.findViewById(R.id.btnYes);
        Button btnNo =view.findViewById(R.id.btnNo);
        ImageView img=view.findViewById(R.id.img);

        img.setVisibility(View.GONE);
        title.setVisibility(View.VISIBLE);
        btnNo.setVisibility(View.GONE);

        title.setText(titleS);
        text.setGravity(Gravity.CENTER);
        text.setText(Html.fromHtml(msg));

        btnYes.setOnClickListener(v -> {
            presenter.getVisits();
            alertDialogConfirmComing.cancel();
        });


        androidx.appcompat.app.AlertDialog.Builder builder=new androidx.appcompat.app.AlertDialog.Builder(context);
        builder.setView(view);
        alertDialogConfirmComing =builder.create();
        alertDialogConfirmComing.setCanceledOnTouchOutside(false);
        alertDialogConfirmComing.show();
    }
    //endregion


    //region rfab
    @Override
    public void onRFACItemLabelClick(int position, RFACLabelItem item) {
        //Log.wtf("mLog", "Label "+position);
        rfabHelper.toggleContent();
        rfabClickSubItem(position);

    }

    @Override
    public void onRFACItemIconClick(int position, RFACLabelItem item) {
        //Log.wtf("mLog", "Icon "+position);
        rfabHelper.toggleContent();
        rfabClickSubItem(position);
    }

    private void initRFAB()
    {
        RapidFloatingActionContentLabelList rfaContent = new RapidFloatingActionContentLabelList(context);
        rfaContent.setOnRapidFloatingActionContentLabelListListener(this);
        List<RFACLabelItem> items = new ArrayList<>();

        int corner=MainUtils.dip2px(context, 4);
        ShapeDrawable sd=MainUtils.generateCornerShapeDrawable(0xFF00C853,corner,corner,corner,corner );
        Integer color= Color.WHITE;

        items.add(new RFACLabelItem<Integer>()
                .setLabel("Записаться")
                .setResId(R.drawable.ic_create_white_24dp)
                .setIconNormalColor(0xFF00C853)
                .setIconPressedColor(0xFF00E676)
                .setLabelBackgroundDrawable(sd)
                .setLabelColor(color)
                .setWrapper(0)
        );
//        items.add(new RFACLabelItem<Integer>()
//                .setLabel("Чат")
//                .setResId(R.drawable.ic_message_white_24dp)
//                .setIconNormalColor(0xFF00C853)
//                .setIconPressedColor(0xFF00E676)
//                .setLabelBackgroundDrawable(sd)
//                .setLabelColor(color)
//                .setWrapper(1)
//        );
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
                context,
                rfaLayout,
                rfaBtn,
                rfaContent
        ).build();
    }

    private void rfabClickSubItem(int num)
    {
        switch(num)
        {
            case 0:
                ((MainActivity)context).selectedFragmentItem(Constants.MENU_RECORD,false);
                break;

            case 1:
               // showChatActivity();
                callToCenter(centerPhone.getText().toString());
                break;

            case 2:
               // callToCenter(centerPhone.getText().toString());
                callToSite(centerSite.getText().toString());
                break;
//
//            case 3:
//                callToSite(centerSite.getText().toString());
//                break;
        }
    }


    //endregion
}
