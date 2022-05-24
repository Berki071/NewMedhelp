//package com.medhelp.medhelp.ui._chat.info;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.Parcelable;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.widget.Toolbar;
//import android.view.MenuItem;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.medhelp.medhelp.R;
//import com.medhelp.medhelp.data.model.chat.InfoAboutDoc;
//import com.medhelp.medhelp.data.pref.PreferencesManager;
//import com.medhelp.medhelp.ui._chat.room.RoomActivity;
//import com.medhelp.medhelp.ui._chat.show.ShowImage;
//import com.medhelp.medhelp.utils.workToFile.show_file.ShowFile2;
//
//import java.io.File;
//
//
//
//
//public class InformationActivity extends AppCompatActivity {
//
//    ImageView ico;
//    TextView fio;
//    TextView experience;
//    TextView specialty;
//    TextView additionally;
//
//    InfoAboutDoc info;
//
//    PreferencesManager preferencesHelper;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_information);
//
//        ico= findViewById(R.id.ico);
//        fio= findViewById(R.id.fio);
//        experience= findViewById(R.id.experience);
//        specialty= findViewById(R.id.service);
//        additionally= findViewById(R.id.additionally);
//
//        preferencesHelper=new PreferencesManager(this);
//
//        getInputData();
//        initToolBar();
//        init();
//    }
//
//    private void getInputData()
//    {
//        info=getIntent().getParcelableExtra(InfoAboutDoc.class.getCanonicalName());
//    }
//
//    private void init()
//    {
//        if (info==null)
//            return;
//
//        fio.setText(info.getName());
//        experience.setText(info.getExperience());
//        specialty.setText(info.getSpeciality());
//        additionally.setText(info.getAdditionally());
//
//        new ShowFile2.BuilderImage(this)
//                .setType(ShowFile2.TYPE_ICO)
//                .load(info.getImgLink())
//                .token(preferencesHelper.getCurrentUserInfo().getApiKey())
//                .imgError(R.drawable.sh_doc)
//                .into(ico)
//                .setListener(new ShowFile2.ShowListener() {
//                    @Override
//                    public void complete(File file) {
//                    }
//
//                    @Override
//                    public void error(String error) {
//                    }
//                })
//                .build();
//    }
//
//    private void initToolBar()
//    {
//        Toolbar toolbar=findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_white_24dp);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//    }
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
//        Parcelable tmp=getIntent().getExtras().getParcelable(ShowImage.KEY_SCROLL);
//
//        Intent intent=new Intent(this, RoomActivity.class);
//        intent.putExtra(InfoAboutDoc.class.getCanonicalName(),info);
//        intent.putExtra(ShowImage.KEY_SCROLL,tmp);
//        startActivity(intent);
//        finish();
//    }
//}
