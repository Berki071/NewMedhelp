//package com.medhelp.medhelp.ui._chat.all_rooms;
//
//import android.content.Intent;
//import android.os.Bundle;
//import androidx.recyclerview.widget.LinearLayoutManager;
//
//import androidx.recyclerview.widget.RecyclerView;
//import androidx.appcompat.widget.Toolbar;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.Button;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.medhelp.medhelp.Constants;
//import com.medhelp.medhelp.R;
//import com.medhelp.medhelp.data.model.chat.InfoAboutDoc;
//import com.medhelp.medhelp.ui._chat.all_rooms.recycler.AllRoomAdapter;
//import com.medhelp.medhelp.ui._main_page.MainActivity;
//import com.medhelp.medhelp.ui.base.BaseActivity;
//
//import java.util.List;
//
//
//
//
//import static com.medhelp.medhelp.ui._main_page.MainActivity.POINTER_TO_PAGE;
//
//public class AllRoomsActivity extends BaseActivity implements AllRoomsHelper.View {
//
//    AllRoomsHelper.Presenter presenter;
//
//    TextView titleCounter;
//    RecyclerView recy;
//    LinearLayout rootErr;
//    TextView errMessage;
//    Button errLoadBtn;
//
//    AllRoomAdapter adapter;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_all_rooms);
//        initValue();
//        initToolBar();
//        setUp();
//    }
//    private void initValue(){
//        titleCounter=findViewById(R.id.titleCounter);
//        recy=findViewById(R.id.recy);
//        rootErr=findViewById(R.id.rootErr);
//        errMessage=findViewById(R.id.err_tv_message);
//        errLoadBtn=findViewById(R.id.err_load_btn);
//    }
//
//    @Override
//    protected void setUp() {
//        presenter = new AllRoomsPresenter(this);
//        presenter.getAllRooms();
//    }
//
//    private void initToolBar()
//    {
//        Toolbar toolbar=findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_white_24dp);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//    }
//
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == android.R.id.home) {
//            onBackPressed();
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    @Override
//    public void onBackPressed() {
//        Intent intent=new Intent(this, MainActivity.class);
//        intent.putExtra(POINTER_TO_PAGE, Constants.MENU_CHAT);
//        startActivity(intent);
//        finish();
//    }
//
//    @Override
//    public void updateRecy(List<InfoAboutDoc> inf) {
//        hideErrorScreen();
//
//        if(inf==null  ||   inf.get(0).getName()==null)
//        {
//            if(titleCounter==null)
//                return;
//
//            titleCounter.setText("Всего: "+0);
//            return;
//        }
//
//        titleCounter.setText("Всего: "+inf.size());
//
//        if(adapter==null)
//        {
//            adapter=new AllRoomAdapter(this,inf,presenter.getUserToken());
//
//            recy.setLayoutManager(new LinearLayoutManager(this));
//            recy.setAdapter(adapter);
//        }
//        else
//        {
//            adapter.setList(inf);
//            adapter.notifyDataSetChanged();
//        }
//    }
//
//    @Override
//    public void showLoading(Boolean boo) {
//        if(boo)
//        {
//            showLoading();
//        }
//        else
//        {
//            hideLoading();
//        }
//    }
//
//    @Override
//    public void showErrorScreen() {
//        errMessage.setVisibility(View.VISIBLE);
//        errLoadBtn.setVisibility(View.VISIBLE);
//        recy.setVisibility(View.GONE);
//        errLoadBtn.setOnClickListener(v ->
//                presenter.getAllRooms());
//    }
//
//    private void hideErrorScreen()
//    {
//        errMessage.setVisibility(View.GONE);
//        errLoadBtn.setVisibility(View.GONE);
//        recy.setVisibility(View.VISIBLE);
//    }
//
//    @Override
//    public void userRefresh() {
//
//    }
//}
