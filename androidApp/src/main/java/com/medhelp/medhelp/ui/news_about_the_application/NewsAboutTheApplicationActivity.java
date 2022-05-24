package com.medhelp.medhelp.ui.news_about_the_application;

import android.content.Context;
import android.content.Intent;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.os.Bundle;

import com.medhelp.medhelp.Constants;
import com.medhelp.medhelp.R;
import com.medhelp.medhelp.data.model.news.NewsResponse;
import com.medhelp.medhelp.ui._main_page.MainActivity;
import com.medhelp.medhelp.ui.news_about_the_application.pager.MyFragmentPagerAdapter;
import com.medhelp.medhelp.ui.view.BottomBar;

import java.util.List;

import static com.medhelp.medhelp.ui._main_page.MainActivity.POINTER_TO_PAGE;

public class NewsAboutTheApplicationActivity extends FragmentActivity {
    List<NewsResponse> dataList;

    ViewPager pager;
    PagerAdapter pagerAdapter;

    BottomBar botBar;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_in_the_application);
        context=this;

        dataList=getIntent().getExtras().getParcelableArrayList("data");

        pager = findViewById(R.id.pager);
        botBar= findViewById(R.id.botBar);

        botBar.setItemData(dataList.size(), new BottomBar.BottomBarListener() {
            @Override
            public void clickNext(int current) {
                pager.setCurrentItem(current);
            }

            @Override
            public void clickSkip() {
                nextActivity();
            }
        });


        pagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(),dataList);
        pager.setAdapter(pagerAdapter);

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override

            public void onPageScrolled(int i, float v, int i1) {
                if((currentItem==(dataList.size()-1))  &&  (v!=0.0 || i1!=0))
                {
                    isSkip=true;
                }

                //Log.wtf("mTag","onPageScrolled i= "+i+" v= "+v+" i1 "+i1);
            }

            @Override
            public void onPageSelected(int i) {
                //Log.wtf("mTag","onPageSelected i= "+i);
                currentItem=i;
                botBar.arrangeDot(dataList.size(),i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {
                if(currentItem==(dataList.size()-1)) {

                    if (i == 1) {
                        isSkip = false;
                    }

                    if (i == 0 && !isSkip) {
                        //Log.wtf("mTag", "onPageScrollStateChanged i= " + i);
                        nextActivity();
                    }
                }
            }
        });
    }



    private void nextActivity()
    {
        Intent intent=new Intent(context,MainActivity.class);
        intent.putExtra(POINTER_TO_PAGE ,Constants.MENU_THE_MAIN);
        checkIntentOnData(intent);
        startActivity(intent);
    }
    private void checkIntentOnData(Intent intent){
        Intent curIntent= getIntent();
        if(curIntent!=null) {
            String type_message = curIntent.getStringExtra("type_message");
            String id_kl = curIntent.getStringExtra("id_kl");
            String id_filial = curIntent.getStringExtra("id_filial");

            if(type_message!=null && id_kl!=null && id_filial!=null){
                intent.putExtra("type_message",type_message);
                intent.putExtra("id_kl",id_kl);
                intent.putExtra("id_filial",id_filial);
            }
        }
    }


    int currentItem=0;
    boolean isSkip=false;
}
