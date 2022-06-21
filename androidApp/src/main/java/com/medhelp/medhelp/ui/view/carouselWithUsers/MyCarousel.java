package com.medhelp.medhelp.ui.view.carouselWithUsers;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.medhelp.medhelp.R;
import com.medhelp.medhelp.ui.view.carouselWithUsers.user_add.AlertForAddUser;
import com.medhelp.medhelp.utils.timber_log.LoggingTree;
import com.medhelp.shared.model.UserResponse;

import java.util.List;

import me.crosswall.lib.coverflow.CoverFlow;
import me.crosswall.lib.coverflow.core.PagerContainer;
import timber.log.Timber;


public class MyCarousel extends ConstraintLayout implements MyPagerAdapter.MyPagerListener, AlertForAddUser.AlertForInfoUserListener {
    Context context;

    List<UserResponse> dataList;

    PagerContainer pagerContainer;
    TextView title;
    ConstraintLayout main;
    ViewPager pager;
    PagerAdapter pagerAdapter;
    ConstraintLayout mainLoading;

    int coloBackgroundMain;
    int colorTitle;
    int colorLogoText;
    float sizeLogoPx;
    float sizeTitlePx;

    Boolean isLoading=false;

    MyCarouselListener listener;


    public MyCarousel(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.MyCarousel, 0, 0);
        try {
            coloBackgroundMain=a.getColor(R.styleable.MyCarousel_coloBackgroundMain, ContextCompat.getColor(context, R.color.color_primary));
            colorTitle=a.getColor(R.styleable.MyCarousel_colorTitle, Color.BLACK);
            colorLogoText=a.getColor(R.styleable.MyCarousel_colorLogoText, Color.BLACK);

            sizeLogoPx=a.getDimension(R.styleable.MyCarousel_sizeLogoSp,spToPx(14));
            sizeTitlePx=a.getDimension(R.styleable.MyCarousel_sizeTitleSp,spToPx(14));

        } finally {
            a.recycle();
        }

        init(context);

        pager.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
    }

    public void setListener(MyCarouselListener listener)
    {
        this.listener=listener;
    }

    private void init(final Context context)
    {
        inflate(context,R.layout.m_carousel_view,this);

        main=findViewById(R.id.main);
        main.setBackgroundColor(coloBackgroundMain);


        title=findViewById(R.id.title);
        title.setTextSize(sizeTitlePx);
        title.setTextColor(colorTitle);

        pagerContainer=findViewById(R.id.pager_container);
        pager= pagerContainer.getViewPager();
        pager.setOffscreenPageLimit(15);

        mainLoading=findViewById(R.id.mainLoading);
        setVisibleLoading(false);


        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) { }

            @Override
            public void onPageSelected(int i) {
                title.setText(dataList.get(i).getName()+" "+dataList.get(i).getPatronymic());
                if(listener!=null)
                    listener.selectedUser(dataList.get(i));
            }

            @Override
            public void onPageScrollStateChanged(int i) { }
        });
    }

    public void setVisibleLoading(Boolean boo)
    {
        if(boo)
        {
            mainLoading.setVisibility(View.VISIBLE);
            isLoading=true;
        }
        else
        {
            mainLoading.setVisibility(View.GONE);
            isLoading=false;
        }
    }

    public boolean loadingIsVisible()
    {
        return isLoading;
    }


    private String getPhoneOfTheMainUser()
    {
        for(UserResponse tmp : dataList)
        {
            if(tmp.getPhone()!=null && !tmp.getPhone().equals(""))
            {
                return tmp.getPhone();
            }
        }

        return "";
    }

    public void setList(final List<UserResponse> dataList, UserResponse currentUser)
    {
        this.dataList=dataList;
        //addItemPlus(this.dataList);

        int currentNum=getCurrentItem(dataList,currentUser.getIdUser());

        if(currentNum==-1)
            return;

        pager.post(new Runnable() {
            @Override
            public void run() {
                int width= pager.getWidth();
                int height=pager.getHeight();

                pagerAdapter=new MyPagerAdapter(context,dataList,MyCarousel.this,colorLogoText,sizeLogoPx,width,height);
                pager.setAdapter(pagerAdapter);
                pager.setClipChildren(false);
                pager.setCurrentItem(currentNum);
            }
        });



        new CoverFlow.Builder()
                .with(pager)
                .scale(0.3f)
                .spaceSize(0f)
                .pagerMargin(getResources().getDimensionPixelSize(R.dimen.pager_margin))
                .rotationY(0f)
                .build();

        title.setText(currentUser.getName()+" "+currentUser.getPatronymic());
    }

    private int getCurrentItem(List<UserResponse> dataList, int currentUserId)
    {
        for(int i=0;i<dataList.size();i++)
        {
            if(dataList.get(i).getIdUser()==currentUserId)
                return i;
        }

        Timber.tag("my").e(LoggingTree.getMessageForError(null,"MyCarousel/getCurrentItem  currentUserId нет в списке связанных контактов"));
        return -1;
    }


    private boolean testToRootCheck()
    {
        UserResponse ur= dataList.get(pager.getCurrentItem());
        return ur.getLogin().equals(dataList.get(0).getLogin());
    }



    private int spToPx(float sp) {
        return  (int)(sp * context.getResources().getDisplayMetrics().scaledDensity);
    }


    @Override
    public void cratedNewUser( List<UserResponse> dataList, UserResponse currentUser) {
        setList(dataList,currentUser);
    }


    public interface MyCarouselListener {
        void selectedUser(UserResponse user);
    }
}
