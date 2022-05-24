package com.medhelp.medhelp.ui._main_page;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.play.core.tasks.Task;
import com.medhelp.medhelp.Constants;
import com.medhelp.medhelp.R;
import com.medhelp.medhelp.data.model.news.NewsResponse;
import com.medhelp.medhelp.ui._main_page.bonuses_alert.BonusesAlert;
import com.medhelp.medhelp.ui.analise_list.AnaliseListFragment;
import com.medhelp.medhelp.ui.analise_result.AnaliseResFragment;
import com.medhelp.medhelp.ui.analysis_price_list.AnalysisPriceListFragment;
import com.medhelp.medhelp.ui.base.MvpView;
import com.medhelp.medhelp.ui.doctor.DoctorsFragment;
import com.medhelp.medhelp.ui.doctor.service_activity.ServiceActivity;
import com.medhelp.medhelp.ui.electronic_conclusions_fragment.ElectronicConclusionsFragment;
import com.medhelp.medhelp.ui.finances_and_services.FinancesAndServicesFragment;
import com.medhelp.medhelp.ui.login.LoginActivity;
import com.medhelp.medhelp.ui.news_about_the_application.NewsAboutTheApplicationActivity;
import com.medhelp.medhelp.ui.profile.ProfileFragment;
import com.medhelp.medhelp.ui.sale.SaleFragment;
import com.medhelp.medhelp.ui.schedule.ScheduleFragment;
import com.medhelp.medhelp.ui.schedule.alert_question_at_exit.AlertQuestionAtExit;
import com.medhelp.medhelp.ui.search.SearchFragment;
import com.medhelp.medhelp.ui.settings.SettingsFragment;
import com.medhelp.medhelp.ui.support_fragment.SupportDf;
import com.medhelp.medhelp.ui.tax_certifacate.TaxCertificateFragment;
import com.medhelp.medhelp.ui.view.carouselWithUsers.MyCarousel;
import com.medhelp.medhelp.utils.Different;
import com.medhelp.shared.model.UserResponse;

import java.util.ArrayList;
import java.util.List;


import timber.log.Timber;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import static com.medhelp.medhelp.ui.schedule.ScheduleFragment.EXTRA_BACK_PAGE;

public class MainActivity extends AppCompatActivity implements MainActivityHelper.View, MainFragmentHelper {

    NavigationView navView;
    DrawerLayout drawer;
    LinearLayout fragmentRootView;

    public static final String POINTER_TO_PAGE = "POINTER_TO_PAGE";
    public static final String ANALISE_LIST_BACK = "ANALISE_LIST_BACK";

    CheckQueue checkQueue;
    private MainActivityPresenter presenter;
    List<NewsResponse> dataList;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context=this;

        navView=findViewById(R.id.nav_view_analise);
        drawer=findViewById(R.id.drawer_main);
        fragmentRootView=findViewById(R.id.rootFragment);

        presenter = new MainActivityPresenter(this);

        //обработка нотификации Fcm
        Intent intent = getIntent();
        if (intent != null) {
            String type_message = intent.getStringExtra("type_message");
            String id_kl = intent.getStringExtra("id_kl");
            String id_filial = intent.getStringExtra("id_filial");

            Log.wtf("logFCM","mainActivity type_message= "+type_message+", id_kl= "+id_kl+", id_filial= "+id_filial);

            if(type_message!=null && id_kl!=null && id_filial!=null) {
                if(type_message.equals("zapis")){
                    intent.putExtra(POINTER_TO_PAGE, Constants.MENU_THE_MAIN);
                }else if(type_message.equals("analiz")){
                    intent.putExtra(POINTER_TO_PAGE, Constants.MENU_RESULT_ANALISES);
                }
            }
        }

        setupNavigationView();
    }


    @Override
    protected void onResume() {
        super.onResume();

        checkQueue= new CheckQueue(this);  //запуск очереди для проверки автозапуска, батареи и версии приложения
        checkQueue.startTest();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_CODE_FOR_UPDATE_APP) {
            if (resultCode != RESULT_OK) {
                Timber.tag("my").e("MainActivity onActivityResult UPDATE_APP Update flow failed! Result code: " + resultCode);
                // If the update is cancelled or fails,
                // you can request to start the update again.
            } else {
                Timber.tag("my").v("UPDATE_APP successfully");
            }
        }
    }

    private void setupNavigationView() {
        setVisibleItemMenu();
        navView.setNavigationItemSelectedListener(navigationItemSelectedListener);
        intiCarousel();
        testWhichFragmentToShow();

        View root = navView.getHeaderView(0);
        myCarousel = root.findViewById(R.id.mView);
        bonuses=root.findViewById(R.id.bonuses);
        bonuses.setVisibility(View.GONE);
        presenter.getAllBonuses();

        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View view, float v) {

            }

            @Override
            public void onDrawerOpened(@NonNull View view) {
                //для корректной работы карусели в боковом меню, в самой карусели еще один слушатель
                myCarousel.requestDisallowInterceptTouchEvent(false);
            }

            @Override
            public void onDrawerClosed(@NonNull View view) {

            }

            @Override
            public void onDrawerStateChanged(int i) {

            }
        });
    }

    MyCarousel myCarousel;
    TextView bonuses;
    private void intiCarousel() {
        View root = navView.getHeaderView(0);
        myCarousel = root.findViewById(R.id.mView);

        myCarousel.setListener(new MyCarousel.MyCarouselListener() {
            @Override
            public void selectedUser(UserResponse user) {
                if (user != null) {
                    testSelectedUser(user);
                }
            }
        });
        myCarousel.setList(presenter.prefManager.getUsersLogin(), presenter.prefManager.getCurrentUserInfo());
    }
    public void initBonuses(String str){
        if(str==null) {
            bonuses.setVisibility(View.INVISIBLE);
            return;
        }

        bonuses.setText(Html.fromHtml("<u>Бонусная карта: "+ str+"\u20BD</u>") );
        bonuses.setVisibility(View.VISIBLE);

        bonuses.setOnClickListener(v -> {
            drawer.closeDrawer(GravityCompat.START);

            BonusesAlert bonusesAlert=new BonusesAlert();
            bonusesAlert.setList(presenter.getBonusesItemList());
            bonusesAlert.show(getSupportFragmentManager(),"BonusesAlert");
        });
    }

    private void testSelectedUser(UserResponse use) {
        UserResponse currentUser = presenter.getCurrentUser();

        if (currentUser.getIdUser() != use.getIdUser()) {
            presenter.setCurrentUser(use);

            if (fragment != null) {
                ((MvpView) fragment).userRefresh();
            }
        }
    }


    private void testWhichFragmentToShow() {
        int pointer = 0;

        try {
            pointer = getIntent().getExtras().getInt(POINTER_TO_PAGE);
        } catch (Exception e) {
            Log.wtf("mLog", "CallPageActivity information for the pointer is not present, everything of fine");
        }

        if (pointer != Constants.FRAGMENT_SCHEDULE)
            selectedFragmentItem(pointer,false);
        else {
            Bundle bundle = getIntent().getExtras();
            selectedFragmentSchedule(bundle);
        }
    }

    public void selectedFragmentItem(int item, boolean isShowReviews) {
        if(navView!=null && navView.getMenu()!=null) {
            navigationItemSelectedListener.onNavigationItemSelected(navView.getMenu().findItem(item));
        }

        if(isShowReviews)
            showInAppReviews();
    }
    private void showInAppReviews(){
        ReviewManager manager = ReviewManagerFactory.create(context);
        Task<ReviewInfo> request = manager.requestReviewFlow();
        request.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // We can get the ReviewInfo object
                ReviewInfo reviewInfo = task.getResult();
                Task<Void> flow = manager.launchReviewFlow(this, reviewInfo);
                flow.addOnCompleteListener(task2 -> {
                    Timber.tag("my").v("Показано окно с оценкой внутри приложения");
                    // The flow has finished. The API does not indicate whether the user
                    // reviewed or not, or even whether the review dialog was shown. Thus, no
                    // matter the result, we continue our app flow.
                });

            } else {
                // There was some problem, continue regardless of the result.
            }
        });
    }

    public void selectedFragmentSchedule(Bundle bundle) {
        navView.setTag(Constants.FRAGMENT_SCHEDULE);

        fragment =  ScheduleFragment.newInstance();

        if (bundle != null) {
            fragment.setArguments(bundle);
        }
        // Вставить фрагмент, заменяя любой существующий
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.rootFragment, fragment).commit();
    }

    Fragment fragment = null;
    NavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            if(menuItem.getItemId() != Constants.MENU_RATE &&  menuItem.getItemId() != Constants.MENU_LOGOUT
                    && menuItem.getItemId() != Constants.MENU_CONTACTING_SUPPORT) {
                menuItem.setChecked(true);
                menuItem.setCheckable(true);
            }


            int tmp = menuItem.getItemId();
            if (tmp == Constants.MENU_PRICE)
                tmp = Constants.MENU_RECORD;

            if (navView.getTag() != null && (int) navView.getTag() == tmp) {
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }

            Class fragmentClass;

            navView.setTag(tmp);

            drawer.closeDrawer(GravityCompat.START);
            switch (tmp) {
                case Constants.MENU_THE_MAIN:
                    fragmentClass = ProfileFragment.class;
                    break;

                case Constants.MENU_FINANCES_SERVICES:
                    fragmentClass = FinancesAndServicesFragment.class;
                    break;

                case Constants.MENU_STAFF:
                    fragmentClass = DoctorsFragment.class;
                    break;

                case Constants.MENU_RECORD:
                    fragmentClass = SearchFragment.class;
                    break;

                case Constants.MENU_PRICE:
                    fragmentClass = SearchFragment.class;
                    break;

                case Constants.MENU_ELECTRONIC_CONCLUSIONS:
                    fragmentClass = ElectronicConclusionsFragment.class;
                    break;

                case Constants.MENU_ANALISE_PRICE:
                    fragmentClass = AnalysisPriceListFragment.class;
                    break;

                case Constants.MENU_LIST_ANALISES:
                    fragmentClass = AnaliseListFragment.class;
                    break;

                case Constants.MENU_RESULT_ANALISES:
                    fragmentClass = AnaliseResFragment.class;
                    break;

                case Constants.MENU_TAX_CERTIFICATE:
                    fragmentClass = TaxCertificateFragment.class;
                    break;

//                case Constants.MENU_ONLINE_CONSULTATION:
//                    fragmentClass = OnlineConsultationFragment.class;
//                    break;

                //                case Constants.MENU_SALE:
//                    fragmentClass = SaleFragment.class;
//                    break;

//                case Constants.MENU_CHAT:
//                    fragmentClass = StartedChatsFragment.class;
//                    break;

                case Constants.MENU_CONTACTING_SUPPORT:
                    //fragmentClass = SupportDf.class;
                    goToSupport();
                    return true;

                case Constants.MENU_SETTINGS:
                    fragmentClass = SettingsFragment.class;
                    break;

//                case Constants.MENU_RATE:
//                    //RateFragment.newInstance().show(getSupportFragmentManager());
//                    Timber.tag("my").v("Переход на страницу приложения в маркете ");
//                    PlayStoreUtils.openPlayStoreForApp(MainActivity.this);
//                    navView.setTag(-1);
//                    return true;

//                case Constants.MENU_NEWS:
//                    presenter.createListNews();
//                    return true;

                case Constants.MENU_LOGOUT:
                    Timber.tag("my").v("showLoginActivity NavigationItemSelectedListener");
                    showLoginActivity();
                    navView.setTag(-1);
                    return true;

                default:
                    fragmentClass = SaleFragment.class;
                    break;
            }

            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }


            // Вставить фрагмент, заменяя любой существующий
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.rootFragment, fragment).commit();

            // Выделение существующего элемента выполнено с помощью
            // NavigationView
            menuItem.setChecked(true);

            return true;
        }
    };

    private void goToSupport(){
        drawer.closeDrawer(GravityCompat.START);
        navView.setTag(-1);

        SupportDf supportDf= new SupportDf();
        supportDf.setData(presenter.getLogin(),null);
        supportDf.show(getSupportFragmentManager(),SupportDf.class.getCanonicalName());
    }

    public void setItemSaveNavMenu(int num){
        navView.setTag(num);
    }

//    public void openNews()
//    {
//        Intent intent = new Intent(context, NewsAboutTheApplicationActivity.class);
//        intent.putExtra("data", (ArrayList<? extends Parcelable>) dataList);
//        startActivity(intent);
//    }

    @Override
    public void openNewInTheApplication(List<NewsResponse> dataList) {
        this.dataList=dataList;
        if(!checkPermissions() && android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.R)
        {
            requestPermissions();
            return;
        }

        Intent intent = new Intent(this,NewsAboutTheApplicationActivity.class);
        intent.putExtra("data", (ArrayList<? extends Parcelable>) this.dataList);
        startActivity(intent);
        finish();
    }

    //region permission
    public final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE=132;
    protected boolean checkPermissions() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    public void requestPermissions()
    {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
    }

//    // Request code for creating a PDF document.
//    private static final int CREATE_FILE = 1;
//    public void createFile(String path) {
//
//        Uri uri= Uri.fromFile(new File(path));
//        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
//        intent.addCategory(Intent.CATEGORY_OPENABLE);
//        intent.setType("application/pdf");
//        intent.putExtra(Intent.EXTRA_TITLE, "invoice.pdf");
//
//        // Optionally, specify a URI for the directory that should be opened in
//        // the system file picker when your app creates the document.
//        intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, uri);
//
//        startActivityForResult(intent, CREATE_FILE);
//    }



    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Timber.tag("my").v("Пользователь дал разрешение на использование памяти телефона");

                if (listenerMainActivity != null) {
                    listenerMainActivity.permissionGranted();
                }
//                else
//                {
//                    openNewInTheApplication(dataList);
//                }
            } else {
                Timber.tag("my").v("Пользователь не дал разрешение на использование памяти телефона");
                setItemSaveNavMenu(-1);

                if (listenerMainActivity != null) {
                    listenerMainActivity.permissionDenied();
                }
            }
        }
    }
        //endregion


    private void showLoginActivity() {
        Different.showAlertInfo(this, null, "Вы действительно хотите выйти из учетной записи?", new Different.AlertInfoListener() {
            @Override
            public void clickYes() {
                Timber.tag("my").v("showLoginActivity");
                presenter.removePassword();

                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                fileList();
            }
            @Override
            public void clickNo() {}
        },true);
    }


    private void setVisibleItemMenu() {

        Menu menu = navView.getMenu();

        menu.add(0, Constants.MENU_THE_MAIN, Menu.NONE, getResources().getString(R.string.app_profile)).setIcon(R.drawable.ic_account_circle_white_24dp).setChecked(true);

        if(presenter.prefManager.getCenterInfo().getButton_zapis()==1)
            menu.add(0, Constants.MENU_RECORD, Menu.NONE, getResources().getString(R.string.nav_record)).setIcon(R.drawable.ic_assignment_turned_in_white_24dp).setChecked(true);
        if(presenter.prefManager.getCenterInfo().getButton_doctors()==1)
            menu.add(0, Constants.MENU_STAFF, Menu.NONE, getResources().getString(R.string.nav_staff)).setIcon(R.drawable.ic_person_pin_white_24dp).setChecked(true);
        if(presenter.prefManager.getCenterInfo().getButton_price_ysl()==1)
            menu.add(0, Constants.MENU_PRICE, Menu.NONE, getResources().getString(R.string.nav_price)).setIcon(R.drawable.ic_payment_white_24dp).setChecked(true);
        if(presenter.prefManager.getCenterInfo().getButton_price_anal()==1)
            menu.add(0, Constants.MENU_ANALISE_PRICE, Menu.NONE, getResources().getString(R.string.analisePrice)).setIcon(R.drawable.ic_list_pink_800_24dp).setChecked(true);
        if(presenter.prefManager.getCenterInfo().getButton_result_zakl()==1)
            menu.add(0, Constants.MENU_ELECTRONIC_CONCLUSIONS, Menu.NONE, "Электронные заключения ").setIcon(R.drawable.ic_baseline_book_online_white_24).setChecked(true);
        if(presenter.prefManager.getCenterInfo().getButton_sdan_anal()==1)
            menu.add(0, Constants.MENU_LIST_ANALISES, Menu.NONE, getResources().getString(R.string.nav_list_analyzes)).setIcon(R.drawable.ic_opacity_red_700_24dp).setChecked(true);
        if(presenter.prefManager.getCenterInfo().getButton_result_anal()==1)
            menu.add(0, Constants.MENU_RESULT_ANALISES, Menu.NONE, getResources().getString(R.string.nav_result)).setIcon(R.drawable.ic_create_new_folder_red_700_24dp).setChecked(true);
        if(presenter.prefManager.getCenterInfo().getButton_nalog()==1)
            menu.add(0, Constants.MENU_TAX_CERTIFICATE, Menu.NONE, getResources().getString(R.string.app_tax_certificate)).setIcon(R.drawable.ic_baseline_history_edu_24_white).setChecked(true);

//        if (false)
//            menu.add(0, Constants.MENU_ONLINE_CONSULTATION, Menu.NONE, getResources().getString(R.string.onlineConsultation)).setIcon(R.drawable.ic_video_cam_blue_24dp).setChecked(true);
//        if (false)
//            menu.add(0, Constants.MENU_CHAT, Menu.NONE, getResources().getString(R.string.nav_chat)).setIcon(R.drawable.ic_chat_pink_a200_24dp).setChecked(true);
        if (presenter.prefManager.getCenterInfo().getButton_fin()==1/*presenter.prefManager.getYandexStoreIsWorks()*/)
            menu.add(0, Constants.MENU_FINANCES_SERVICES, Menu.NONE, getResources().getString(R.string.financeAndServices)).setIcon(R.drawable.ic_account_balance_wallet_pink_700_18dp).setChecked(true);


        menu.add(0, Constants.MENU_SETTINGS, Menu.NONE, getResources().getString(R.string.nav_settings)).setIcon(R.drawable.ic_settings_applications_red_700_18dp).setChecked(true);

        menu.add(0, Constants.MENU_CONTACTING_SUPPORT, Menu.NONE, getResources().getString(R.string.nav_contacting_support)).setIcon(R.drawable.ic_baseline_record_voice_over_24).setChecked(true);
        menu.add(0, Constants.MENU_LOGOUT, Menu.NONE, getResources().getString(R.string.nav_logout)).setIcon(R.drawable.ic_exit_to_app_white_24dp).setChecked(true);
    }





    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    @Override
    public void showNavigationMenu() {
        drawer.openDrawer(Gravity.LEFT);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            testOnBackPresses();
        }
    }

    private void testOnBackPresses() {

        int itemId = (int) navView.getTag();

        if (itemId == Constants.MENU_THE_MAIN) {
            Timber.tag("my").v("onBackPressed");
            super.onBackPressed();
            return;
        }

        if (itemId == Constants.MENU_RESULT_ANALISES) {
            try {
                Boolean backAnaliseList = getIntent().getExtras().getBoolean(ANALISE_LIST_BACK);
                getIntent().getExtras().clear();

                if (backAnaliseList) {
                    selectedFragmentItem(Constants.MENU_LIST_ANALISES,false);
                    return;
                }
            } catch (Exception e) {
            }
        }

        if (itemId == Constants.FRAGMENT_SCHEDULE) {
            if (!presenter.prefManager.getNotShowAlertQuestionAtExit()) {
                new AlertQuestionAtExit(this, new AlertQuestionAtExit.ExitListener() {
                    @Override
                    public void alertClose() {
                        scheduleBack();
                    }
                });
            } else {
                scheduleBack();
            }

            return;
        }

        selectedFragmentItem(0,false);
    }

    private void scheduleBack() {
        int backPage = ((ScheduleFragment) fragment).getBackPage();
        if (Constants.ACTIVITY_SERVICE == backPage) {
            Intent intent = ServiceActivity.getStartIntent(this);
            intent.putExtra(ServiceActivity.EXTRA_DATA_ID_DOCTOR, ((ScheduleFragment) fragment).getIdDoctor());
            intent.putExtra(ServiceActivity.EXTRA_DATA_SERVICE, 0);
            intent.putExtra(EXTRA_BACK_PAGE, ((ScheduleFragment) fragment).getBackPageService());

            startActivity(intent);
        } else {
            selectedFragmentItem(backPage,false);
        }
    }

    public void ErrorMessage(String str)
    {
        Toast.makeText(context,str,Toast.LENGTH_LONG).show();
    }

    public void showLoading()
    {
        if(fragment!=null)
        {
            ((MvpView)fragment).showLoading();
        }
    }

    public void hideLoading(){
        if(fragment!=null)
        {
            ((MvpView)fragment).hideLoading();
        }
    }

    public void setListener(ListenerMainActivity listenerMainActivity)
    {
        this.listenerMainActivity=listenerMainActivity;
    }

    ListenerMainActivity listenerMainActivity;
    public interface ListenerMainActivity{
        void permissionGranted();
        void permissionDenied();
    }
}
