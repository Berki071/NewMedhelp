package com.medhelp.medhelp.ui.analysis_price_list;

import com.medhelp.medhelp.data.model.analise_price.AnalisePriceResponse;
import com.medhelp.medhelp.ui.base.MvpView;
import com.medhelp.shared.model.UserResponse;

import java.util.List;

public interface AnalisePriceListHelper {

    interface View extends MvpView {

        void updateView(List<String> categories, List<AnalisePriceResponse> analise);

        void showErrorScreen();

        void showLoading();

        void hideLoading();
    }

    interface Presenter{
        void getAnalisePrice();

        void removePassword();

        String getUserToken();

        UserResponse getCurrentUser();
        void setCurrentUser(UserResponse user);
    }
}
