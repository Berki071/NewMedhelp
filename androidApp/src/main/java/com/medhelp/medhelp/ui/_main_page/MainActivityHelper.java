package com.medhelp.medhelp.ui._main_page;

import com.medhelp.medhelp.data.model.news.NewsResponse;
import com.medhelp.shared.model.UserResponse;

import java.util.List;

public interface MainActivityHelper {
    interface View{
        void openNewInTheApplication(List<NewsResponse> dataList);
        void ErrorMessage(String str);
        void setItemSaveNavMenu(int num);
        void showLoading();
        void hideLoading();
        void initBonuses(String str);
    }

    interface Presenter{
        UserResponse getCurrentUser();
        void setCurrentUser(UserResponse user);
        void removePassword();
    }
}
