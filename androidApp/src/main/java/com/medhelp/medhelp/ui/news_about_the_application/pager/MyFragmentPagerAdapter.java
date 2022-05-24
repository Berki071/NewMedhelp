package com.medhelp.medhelp.ui.news_about_the_application.pager;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.medhelp.medhelp.data.model.news.NewsResponse;

import java.util.List;

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
    private List<NewsResponse> dataList;

    public MyFragmentPagerAdapter(FragmentManager fm, List<NewsResponse> dataList) {
        super(fm);
        this.dataList=dataList;

    }

    @Override
    public Fragment getItem(int position) {
        return PageFragment.newInstance(dataList.get(position));
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

}