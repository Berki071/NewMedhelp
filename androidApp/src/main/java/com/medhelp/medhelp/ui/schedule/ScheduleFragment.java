package com.medhelp.medhelp.ui.schedule;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
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
import com.medhelp.medhelp.data.model.DateResponse;
import com.medhelp.medhelp.data.model.Doctor;
import com.medhelp.medhelp.data.model.ScheduleResponse;
import com.medhelp.medhelp.data.model.VisitResponse;
import com.medhelp.medhelp.ui._main_page.MainActivity;
import com.medhelp.medhelp.ui.base.BaseFragment;
import com.medhelp.medhelp.ui.schedule.alert_doc_info.AlertCardDoctor;
import com.medhelp.medhelp.ui.schedule.alert_entry.AlertEntryToDoctor;
import com.medhelp.medhelp.ui.schedule.data.ItemCounterFreePlaces;
import com.medhelp.medhelp.ui.schedule.decorators.DayDecorator;
import com.medhelp.medhelp.ui.schedule.decorators.SelectDecorator;
import com.medhelp.medhelp.ui.schedule.recy_branch.MyBranchSection;
import com.medhelp.medhelp.ui.schedule.recycler.DateState;
import com.medhelp.medhelp.ui.schedule.recycler.HeaderViewHolder;
import com.medhelp.medhelp.ui.schedule.recycler.ItemViewHolder;
import com.medhelp.medhelp.utils.workToFile.show_file.ShowFile2;
import com.medhelp.medhelp.utils.main.TimesUtils;
import com.medhelp.medhelp.utils.timber_log.LoggingTree;
import com.medhelp.shared.model.SettingsAllBranchHospitalResponse;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;
import com.prolificinteractive.materialcalendarview.format.ArrayWeekDayFormatter;
import com.prolificinteractive.materialcalendarview.format.MonthArrayTitleFormatter;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;



import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;
import timber.log.Timber;

public class ScheduleFragment extends BaseFragment implements OnDateSelectedListener, OnMonthChangedListener {

    public static final String EXTRA_DATA_ID_DOCTOR = "EXTRA_DATA_ID_DOCTOR";
    public static final String EXTRA_DATA_ID_SERVICE = "EXTRA_DATA_ID_SERVICE";
    public static final String EXTRA_DATA_ADM = "EXTRA_DATA_ADM";
    public static final String EXTRA_DATA_NAME_SERVICE="EXTRA_DATA_NAME_SERVICE";
    public static final String EXTRA_DATA_ID_SPEC="EXTRA_DATA_ID_SPEC";
    public static final String EXTRA_DATA_ID_VIZIT = "EXTRA_DATA_ID_VIZIT";

    public static final String EXTRA_BACK_PAGE = "EXTRA_BACK_PAGE";
    public static final String EXTRA_BACK_PAGE_SERVICE = "EXTRA_BACK_PAGE_SERVICE";

    SchedulePresenter presenter;

    RecyclerView recyclerView;
    Toolbar toolbar;
    CollapsingToolbarLayout toolbarLayout;
    MaterialCalendarView calendarView;
    TextView errorSchedule;
    TextView errMessage;
    TextView errLoadBtn;
    TextView holSchedule;
    TextView holScheduleDay;
    TextView holScheduleTime;
    ConstraintLayout rootEmpty;
    RecyclerView allBranchRecy;

    MyBranchSection myBranchSection;

    private int selectedBranch=-1;

    private String todayString;
    private int idDoctor;
    private int idService;
    private int adm;
    private String nameService;
    private int idSpec;

    private SectionedRecyclerViewAdapter sectionAdapter;
    private List<ScheduleResponse> timeList = new ArrayList<>();
    private List<DateState> listSelected = new ArrayList<>();

    private List<ItemCounterFreePlaces> listCalendarPlaces;

    VisitResponse visit;

    Context context;
    Activity activity;

    private int backPage=0;
    private int backpageService=0;

    public static Fragment newInstance()
    {
        return new ScheduleFragment();
    }

    public int getBackPage()
    {
        return backPage;
    }

    public int getBackPageService()
    {
        return backpageService;
    }

    public int getIdDoctor()
    {
        return idDoctor;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Timber.tag("my").i("Запись на прием");

        context =getContext();
        activity =getActivity();

        listSelected = new ArrayList<>();

        presenter=new SchedulePresenter(context,this);

        try
        {
            if(getArguments()==null)
                return;

            backpageService=getArguments().getInt(EXTRA_BACK_PAGE_SERVICE);
            backPage=getArguments().getInt(EXTRA_BACK_PAGE);
            String str=getArguments().getString(EXTRA_DATA_ID_VIZIT);
            if(str!=null  && !str.equals(""))
            {
                Gson gson=new Gson();
                visit=gson.fromJson(str, VisitResponse.class);

                idDoctor = 0;
                idService = visit.getIdServices();
                adm = visit.getDurationService();
                nameService=visit.getNameServices();
                idSpec=getArguments().getInt(EXTRA_DATA_ID_SPEC,-1);
            }
            else
            {
                idDoctor = getArguments().getInt(EXTRA_DATA_ID_DOCTOR, 0);
                idService = getArguments().getInt(EXTRA_DATA_ID_SERVICE, 0);
                adm = getArguments().getInt(EXTRA_DATA_ADM, 0);
                nameService=getArguments().getString(EXTRA_DATA_NAME_SERVICE);
                idSpec=getArguments().getInt(EXTRA_DATA_ID_SPEC,-1);
            }
        }
        catch (Exception e)
        {
            Timber.tag("my").e(LoggingTree.getMessageForError(e,"ProfileActivity$testExistIdRecord "));
        }


       // testShowTooltips();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.activity_schedule,container,false);
        initValue(rootView);
        return rootView;
    }
    private void initValue(View v){
        recyclerView=v.findViewById(R.id.rv_schedule);
        toolbar=v.findViewById(R.id.toolbar_schedule);
        toolbarLayout=v.findViewById(R.id.collapsing_toolbar_schedule);
        calendarView=v.findViewById(R.id.calendar_schedule);
        errorSchedule=v.findViewById(R.id.err_schedule);
        errMessage=v.findViewById(R.id.err_tv_message);
        errLoadBtn=v.findViewById(R.id.err_load_btn);
        holSchedule=v.findViewById(R.id.hol_schedule);
        holScheduleDay=v.findViewById(R.id.holder_schedule_day);
        holScheduleTime=v.findViewById(R.id.holder_schedule_time);
        rootEmpty=v.findViewById(R.id.rootEmpty);
        allBranchRecy=v.findViewById(R.id.allBranch);
    }


    @Override
    protected void setUp(View view) {

        if(visit==null) {
            selectedBranch = presenter.prefManager.getCurrentUserInfo().getIdBranch();
            presenter.setNewIdUserFavouriteBranch(String.valueOf(presenter.prefManager.getCurrentUserInfo().getIdUser()));
        }
        else
        {
            selectedBranch=visit.getIdBranch();
            presenter.setNewIdUserFavouriteBranch(String.valueOf(visit.getIdUser()));
        }

        holScheduleDay.setVisibility(View.GONE);
        holScheduleTime.setVisibility(View.GONE);

        setupToolbar();
        setupCalendarView();
    }


    @Override
    public void onResume() {
        super.onResume();

        if (idDoctor != 0) {
            presenter.getBranchByIdServiceIdDoc( idService, idDoctor);
        } else {
            presenter.getBranchByIdService(idService);
        }
    }

    public void setHospitalBranch(List<SettingsAllBranchHospitalResponse> list) {

        if(list.get(0).getNameBranch()!=null && list.size()>0)
            initRecyBranch(list);
        else
            showCancelAlert();
    }

    private void showCancelAlert()
    {
        LayoutInflater inflater= getLayoutInflater();
        View view=inflater.inflate(R.layout.dialog_2textview_btn,null);

        TextView title=view.findViewById(R.id.title);
        TextView text=view.findViewById(R.id.text);
        Button btnYes =view.findViewById(R.id.btnYes);
        Button btnNo =view.findViewById(R.id.btnNo);

        btnNo.setVisibility(View.GONE);

        title.setText(Html.fromHtml("<u>Возможность записаться отсутствует</u>"));
        text.setText(Html.fromHtml("Выберите другую услугу или врача"));

        btnYes.setOnClickListener(v -> {
            alert.dismiss();
            ((MainActivity)context).selectedFragmentItem(Constants.MENU_THE_MAIN,false);
        });


        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setView(view);

        alert=builder.create();
        alert.setCanceledOnTouchOutside(false);
        alert.show();
    }


    private void initRecyBranch(List<SettingsAllBranchHospitalResponse> list)
    {
        int branch;
        if (selectedBranch == -1)
            branch = presenter.getCurrentHospitalBranchId();
        else {
            branch = selectedBranch;

        }

        int selectedBranchh=-1;

        for(int i=0;i<list.size();i++)
        {
            if(list.get(i).getIdBranch()==branch)
            {
                list.get(i).setFavorite(true);
                selectedBranchh=list.get(i).getIdBranch();
                selectedBranch=list.get(i).getIdBranch();
                break;
            }
        }

        if(selectedBranchh==-1)
        {
            list.get(0).setFavorite(true);
            selectedBranch=list.get(0).getIdBranch();

            presenter.selectedNewHospitalBranch(selectedBranch);
        }
        else
        {
            getDataFrom();
        }



        Collections.sort(list);

        SectionedRecyclerViewAdapter sectionAdapterBranch = new SectionedRecyclerViewAdapter();
        myBranchSection=new MyBranchSection(context,list, new MyBranchSection.ItemBranchListener() {
            @Override
            public void onClick(SettingsAllBranchHospitalResponse branch) {
                myBranchSection.selectNewItem(branch);
                allBranchRecy.getAdapter().notifyDataSetChanged();

                if(branch.getIdBranch()==selectedBranch)
                    return;

               // int oldBranch=selectedBranch;
                selectedBranch=branch.getIdBranch();
                presenter.selectedNewHospitalBranch(selectedBranch);

               // getDataFrom();
            }
        });

        sectionAdapterBranch.addSection(myBranchSection);


        GridLayoutManager glm = new GridLayoutManager(context, 2);
        glm.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (sectionAdapterBranch.getSectionItemViewType(position) == SectionedRecyclerViewAdapter.VIEW_TYPE_HEADER) {
                    return 2;
                }
                return 1;
            }
        });

        allBranchRecy.setLayoutManager(glm);
        allBranchRecy.setAdapter(sectionAdapterBranch);


    }

    public void getDataFrom()
    {
        if (idDoctor != 0) {
            presenter.getDateFromDoctor(idDoctor, idService, adm,selectedBranch);
        } else {
            presenter.getDateFromService(idService, adm,selectedBranch);
        }
    }

//    private void testShowTooltips()
//    {
//        if(presenter.getShowTooltipForShedule())
//        {
//            showInfoAlert();
//            presenter.setShowTooltipForShedule();
//        }
//
//    }

    private void setupCalendarView() {
        calendarView.state().edit()
                .setFirstDayOfWeek(Calendar.MONDAY)
                .setCalendarDisplayMode(CalendarMode.WEEKS)
                .commit();
        calendarView.setTitleFormatter(new MonthArrayTitleFormatter(getResources().getStringArray(R.array.calendar_months_array)));
        calendarView.setWeekDayFormatter(new ArrayWeekDayFormatter(getResources().getStringArray(R.array.calendar_days_array)));
        calendarView.setVisibility(View.GONE);
    }


    public void setupCalendar(DateResponse today, List<ScheduleResponse> response) {
        todayString = today.getToday();
        String lastMondayString = today.getLastMonday();

        CalendarDay min = CalendarDay.from(TimesUtils.stringToDate(todayString));

        Calendar max = Calendar.getInstance();
        max.setTime(TimesUtils.stringToDate(todayString));
        max.add(Calendar.DAY_OF_MONTH, 28);

        calendarView.state().edit()
                .setMinimumDate(min)
                .setMaximumDate(max)
                .commit();

        calendarView.setVisibility(View.VISIBLE);
        calendarView.addDecorators(new SelectDecorator(activity));
        calendarView.setOnDateChangedListener(this);
        calendarView.setOnMonthChangedListener(this);
        updateCalendar(lastMondayString, response);
    }


    public void updateCalendar(String day, List<ScheduleResponse> response) {
        listCalendarPlaces=new ArrayList<>();

        recyclerView.setVisibility(View.GONE);
        errorSchedule.setVisibility(View.GONE);
        errMessage.setVisibility(View.GONE);
        errLoadBtn.setVisibility(View.GONE);
        //emptySchedule.setVisibility(View.VISIBLE);
        holScheduleDay.setVisibility(View.VISIBLE);
        allBranchRecy.setVisibility(View.VISIBLE);
        holScheduleTime.setVisibility(View.GONE);
        //scheduleDescription.setVisibility(View.VISIBLE);

        timeList.clear();
        timeList.addAll(response);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(TimesUtils.stringToDate(todayString));
        calendar.add(Calendar.MONTH, 0);

        CalendarDay calD=calendarView.getSelectedDate();

        calendarView.removeDecorators();
        calendarView.addDecorators(new SelectDecorator(activity));

        Boolean isShowStubEmpty =true;

        for (ScheduleResponse scheduleResponse : response) {
            String currentDay = scheduleResponse.getAdmDay();
            Date date = TimesUtils.stringToDate(currentDay);
            CalendarDay calendarDay = CalendarDay.from(date);

            if (scheduleResponse.isWork() ) {

                int yardstick= testCounterFreePlaces(scheduleResponse);
                int t=TimesUtils.compareWithCurrentTimeByDate(scheduleResponse.getAdmDay());
                switch (yardstick)
                {
                    case 0:
                        calendarView.addDecorator(new DayDecorator(context, calendarDay, DayDecorator.DAY_MODE_NO));
                        calendar.add(Calendar.DATE, 1);

                        if(t==0  ||  t==1)
                            isShowStubEmpty =false;
                        break;

                    case 1:
                        calendarView.addDecorator(new DayDecorator(context, calendarDay, DayDecorator.DAY_MODE_FEW));
                        calendar.add(Calendar.DATE, 1);

                        if(t==0  ||  t==1)
                            isShowStubEmpty =false;
                        break;

                    case 2:
                        calendarView.addDecorator(new DayDecorator(context, calendarDay, DayDecorator.DAY_MODE_MANY));
                        calendar.add(Calendar.DATE, 1);

                        if(t==0  ||  t==1)
                            isShowStubEmpty =false;
                        break;
                }
            } else {
                calendarView.addDecorator(new DayDecorator(context, calendarDay, DayDecorator.DAY_MODE_NOT));
                calendar.add(Calendar.DATE, 1);
            }
        }

        showAndHideRootEmpty(isShowStubEmpty);

        if(calD!=null)
        {
            onDateSelected(calendarView,calD,true);
        }
        else
        {
            CalendarDay cd=calendarView.getSelectedDate();
            if(cd!=null)
            {
                onDateSelected(calendarView,cd,true);
            }
        }
    }

    private void showAndHideRootEmpty(Boolean boo)
    {
        if(boo)
        {
            rootEmpty.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            holSchedule.setText(R.string.docNotWork2);
        }
        else
        {
            rootEmpty.setVisibility(View.GONE);
            holSchedule.setText(R.string.docNotWork);
        }
    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        errorSchedule.setVisibility(View.GONE);
        holSchedule.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        holScheduleTime.setVisibility(View.VISIBLE);

        listSelected = new ArrayList<>();

        String sDate="";

        String isSchedule="0";

        for (ScheduleResponse dateResponse : timeList) {
            Date selectedDate = TimesUtils.stringToDate(dateResponse.getAdmDay());
            CalendarDay selectedDay = CalendarDay.from(selectedDate);

            if (selectedDay != null && selectedDay.equals(date)) {
                if (dateResponse.isWork()) {
                    if (dateResponse.getAdmTime() != null  &&  dateResponse.getAdmTime().size() > 0) {
                        errorSchedule.setVisibility(View.GONE);
                        holSchedule.setVisibility(View.GONE);

                        List<String> listTime = new ArrayList<>(dateResponse.getAdmTime());
                        DateState dateState = new DateState(dateResponse.getIdDoctor(),dateResponse.getId_spec() ,dateResponse.getFullName(), listTime,dateResponse.getPhoto());
                        listSelected.add(dateState);

                        sDate=dateResponse.getAdmDay();
                        isSchedule="1";

                    } else {
                        holSchedule.setVisibility(View.GONE);
                        if(!isSchedule.equals("1"))
                            isSchedule="-1";
                    }
                }
            }
        }

        if(holSchedule!=null && holSchedule.getVisibility()== View.VISIBLE)
        {
            holScheduleTime.setVisibility(View.GONE);
        }

        if(isSchedule.equals("-1")) {
            errorSchedule.setVisibility(View.VISIBLE);
            holScheduleTime.setVisibility(View.GONE);
        }


        if(timeList.size()==0  || sDate.equals(""))
            return;


        recyclerView.setVisibility(View.VISIBLE);
        holScheduleTime.setVisibility(View.VISIBLE);
        updateRecyclerView(listSelected,sDate);
    }

    private int testCounterFreePlaces(ScheduleResponse scheduleResponse)
    {
        Date date = new Date();
        String dateDay=TimesUtils.dateToString(date, TimesUtils.DATE_FORMAT_HHmmss_ddMMyyyy);
        Boolean currentD=comparingDay(dateDay,scheduleResponse.getAdmDay());

        if(currentD)
        {
            if(scheduleResponse.getAdmTime()!=null) {
                List<String> newIemList = comparingTime(dateDay, scheduleResponse.getAdmTime());
                if (newIemList != null) {
                    scheduleResponse.setAdmTime(newIemList);
                }
            }

        }

        int ext =existToListPlaces(scheduleResponse.getAdmDay());
        if(ext ==-1)
        {
            int count;
            if(scheduleResponse.getAdmTime()==null) {
                count=0;
            }
            else {
                count = scheduleResponse.getAdmTime().size();
            }

                ItemCounterFreePlaces itm = new ItemCounterFreePlaces(scheduleResponse.getAdmDay(), count);
                listCalendarPlaces.add(itm);
                ext = listCalendarPlaces.size() - 1;

        }
        else
        {
            int count;
            if(scheduleResponse.getAdmTime()==null) {
                count=0;
            }
            else {
                count = scheduleResponse.getAdmTime().size();
            }
            listCalendarPlaces.get(ext).addPlaces(count);
        }


        if(listCalendarPlaces.get(ext).getCounterPlaces()>5)
            return 2;
        else if(listCalendarPlaces.get(ext).getCounterPlaces()>0)
            return 1;
        else
            return 0;
    }


    private Boolean comparingDay(String tekDay,String compareDay)
    {
        long tek= TimesUtils.getMillisFromDateString(tekDay);
        long comp=TimesUtils.stringToLong(compareDay,TimesUtils.DATE_FORMAT_ddMMyyyy);

        return tek == comp;
    }

    private List<String> comparingTime(String tecTime, List<String> compareTime)
    {
        List<String> result=new ArrayList<>();
        Long tekT=TimesUtils.stringToLong(tecTime, TimesUtils.DATE_FORMAT_HHmm);

        for(int i=0;i<compareTime.size();i++)
        {
            Long compT=TimesUtils.stringToLong(compareTime.get(i), TimesUtils.DATE_FORMAT_HHmm);

            if(((1000*60)+tekT)<compT)
                result.add(compareTime.get(i));
        }

        return result;
    }

    private int existToListPlaces(String date)
    {
        if(listCalendarPlaces.size()<=0)
            return -1;

        for(int i=0;i<listCalendarPlaces.size();i++)
        {
            if(listCalendarPlaces.get(i).getDate().equals(date))
                return i;
        }

        return -1;
    }


    AlertDialog alert;
    public void showAlertRecordedSuccessfully(String msg) {
        LayoutInflater inflater= ((MainActivity)context).getLayoutInflater();
        View view=inflater.inflate(R.layout.dialog_2textview_btn,null);

        TextView title=view.findViewById(R.id.title);
        TextView text=view.findViewById(R.id.text);
        Button btnYes =view.findViewById(R.id.btnYes);
        Button btnNo =view.findViewById(R.id.btnNo);
        ImageView img=view.findViewById(R.id.img);

        title.setText("Запись прошла успешно");
        text.setText(msg);
        btnYes.setOnClickListener(v -> {

            alert.dismiss();
            ((MainActivity)context).selectedFragmentItem(Constants.MENU_THE_MAIN,true);
        });

        btnNo.setVisibility(View.GONE);
        if(msg.equals(""))
        {
            img.setVisibility(View.GONE);
            text.setVisibility(View.GONE);
        }

        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setView(view);
        builder.setCancelable(false);

        alert=builder.create();
        alert.show();
    }


    private void showInfoAlert()
    {
        LayoutInflater inflater=getLayoutInflater();
        View view=inflater.inflate(R.layout.dialog_schedule_info,null);

        Button btnOk=view.findViewById(R.id.btnYes);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });

        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setView(view);
        //builder.setCancelable(false);

        alert=builder.create();
        alert.show();
    }

    public void showAlertRecordingNotPossible(String reason) {
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setMessage(reason)
                .setPositiveButton("Ok", (dialog, which) -> {
                    dialog.cancel();

                    if (idDoctor != 0) {
                        presenter.getDateFromDoctor(idDoctor, idService, adm,selectedBranch);
                    } else {
                        presenter.getDateFromService(idService, adm,selectedBranch);
                    }
                });

        AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }


    @Override
    public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
        calendarView.clearSelection();
        //emptySchedule.setVisibility(View.VISIBLE);
        errorSchedule.setVisibility(View.GONE);
        holSchedule.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        holScheduleTime.setVisibility(View.GONE);

        Date selectedDate = date.getDate();
        String nextDate = TimesUtils.dateToString(selectedDate,TimesUtils.DATE_FORMAT_ddMMyyyy);

        if (idDoctor != 0) {
            presenter.getScheduleByDoctor(idDoctor, nextDate, adm,selectedBranch);
        } else {
            presenter.getScheduleByService(idService, nextDate, adm,selectedBranch);
        }
    }


    public void updateRecyclerView(List<DateState> responses, String admDay) {
        sectionAdapter = new SectionedRecyclerViewAdapter();

        Date date = new Date();
        String dateDay=TimesUtils.dateToString(date, TimesUtils.DATE_FORMAT_HHmmss_ddMMyyyy);
        boolean latchDay=comparingDay(dateDay,admDay);


        for (DateState state : responses) {
            List<String> category2=null;

            if(latchDay)
            {
                category2= comparingTime(dateDay,state.getCategory());
                if(category2==null)
                {
                    break;
                }
                else
                {
                    sectionAdapter.addSection(new TimeSection(state.getDoctor(), category2,state.getIdDoctor(),((idSpec==-1)?(state.getIdSpec()):idSpec),admDay,state.getFoto()));
                }
            }
            else
            {
                sectionAdapter.addSection(new TimeSection(state.getDoctor(), state.getCategory(),state.getIdDoctor(),((idSpec==-1)?(state.getIdSpec()):idSpec),admDay,state.getFoto()));
            }
        }

        GridLayoutManager glm = new GridLayoutManager(context,4,GridLayoutManager.VERTICAL,false);

        glm.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (sectionAdapter.getSectionItemViewType(position) == SectionedRecyclerViewAdapter.VIEW_TYPE_HEADER) {
                    return 4;
                }
                return 1;
            }
        });
        recyclerView.setLayoutManager(glm);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(sectionAdapter);
    }


    @SuppressWarnings("unused")
    private void setupToolbar() {
        ((MainActivity)context).setSupportActionBar(toolbar);
        ActionBar actionBar = ((MainActivity)context).getSupportActionBar();

        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back_white_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)context).onBackPressed();
            }
        });
    }

    public void showInfoAboutDoctor(Doctor doc) {
        new AlertCardDoctor(context, doc, presenter.getUserToken());
    }


    @Override
    public void userRefresh() {

    }


    private class TimeSection extends StatelessSection {
        boolean expanded = true;
        String title;
        List<String> list;
        int idDoctor;
        int idSpec;
        String date;
        String pathIco;

        TimeSection(String title, List<String> list, int idDoctor,int idSpec,String date, String pathIco) {
            super(new SectionParameters.Builder(R.layout.item_date)
                    .headerResourceId(R.layout.item_groupe)
                    .build());

            this.title = title;
            this.list = list;
            this.idDoctor= idDoctor;
            this.idSpec=idSpec;
            this.date=date;
            this.pathIco=pathIco;
        }

        @Override
        public int getContentItemsTotal() {
            return expanded ? list.size() : 0;
        }



        @Override
        public RecyclerView.ViewHolder getItemViewHolder(View view) {
            return new ItemViewHolder(view);
        }

        @Override
        public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
            final ItemViewHolder itemHolder = (ItemViewHolder) holder;

            itemHolder.tvItem.setText(list.get(position));
            itemHolder.tvItem.setOnClickListener(v1 ->
                    {
                        //нажатие на кнопку со временем
                        String time = itemHolder.tvItem.getText().toString();

                        //new AlertEntryToDoctor(context, () -> presenter.makeAnAppointment(idDoctor, date, time, idSpec, idService, adm, selectedBranch), title, nameService, date, time);
                        new AlertEntryToDoctor(context, new AlertEntryToDoctor.mOnClickListener() {
                            @Override
                            public void positiveClickButton(String idUser, int idBranch) {
                                if(visit==null)
                                {
                                    presenter.makeAnAppointment(idDoctor, date, time, idSpec, idService, adm,idUser, idBranch);
                                } else {
                                    presenter.rewriteAppointment_cancellation(visit.getIdUser(), visit.getIdRecord(), visit.getIdBranch(), idDoctor,
                                            date, time, idSpec, idService, adm,idUser, idBranch);
                                }
                            }

                        }, title, nameService, date, time,presenter.getIdUserFavouriteBranch(),selectedBranch);

                    }
            );
        }

        @Override
        public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
            return new HeaderViewHolder(view);
        }

        @Override
        public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
            HeaderViewHolder headerHolder = (HeaderViewHolder) holder;
            headerHolder.tvTitle.setText(title);

            if(pathIco!=null  && !pathIco.equals(""))

                new ShowFile2.BuilderImage(headerHolder.ico.getContext())
                        .setType(ShowFile2.TYPE_ICO)
                        .load(pathIco)
                        .token(presenter.getUserToken())
                        .imgError(R.drawable.sh_doc)
                        .into(headerHolder.ico)
                        .setListener(new ShowFile2.ShowListener() {
                            @Override
                            public void complete(File file) {
                            }

                            @Override
                            public void error(String error) {
                            }
                        })
                        .build();

            headerHolder.rootView.setOnClickListener(v ->
                    presenter.getInfoAboutDoc(idDoctor));
        }

    }

    public void showErrorScreen() {
        recyclerView.setVisibility(View.GONE);
        holScheduleDay.setVisibility(View.GONE);
        holScheduleTime.setVisibility(View.GONE);
        //scheduleDescription.setVisibility(View.GONE);
        allBranchRecy.setVisibility(View.GONE);

        errMessage.setVisibility(View.VISIBLE);
        errLoadBtn.setVisibility(View.VISIBLE);
        //errLoadBtn.setVisibility(View.VISIBLE);
        errLoadBtn.setOnClickListener(v ->
        {
            if (idDoctor != 0) {
                presenter.getDateFromDoctor(idDoctor, idService, adm,selectedBranch);
            } else {
                presenter.getDateFromService(idService, adm,selectedBranch);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }
}

