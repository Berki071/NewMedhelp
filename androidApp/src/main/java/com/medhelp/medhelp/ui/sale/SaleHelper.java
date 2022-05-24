package com.medhelp.medhelp.ui.sale;

import com.medhelp.medhelp.data.model.SaleResponse;
import com.medhelp.medhelp.ui.base.MvpView;
import com.medhelp.shared.model.CenterResponse;
import com.medhelp.shared.model.UserResponse;

import java.util.List;

public interface SaleHelper {

    interface View extends MvpView {
        void showErrorScreen();

        void updateSaleData(List<SaleResponse> response);
    }

    interface Presenter {
        CenterResponse getCenterInfo();

        void updateSaleList();

        void removePassword();

        void unSubscribe();

        String getUserToken();

        UserResponse getCurrentUser();
        void setCurrentUser(UserResponse user);
    }

}
