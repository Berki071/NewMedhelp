package com.medhelp.medhelp.ui.finances_and_services;

import com.medhelp.medhelp.data.model.VisitResponse;
import com.medhelp.medhelp.ui.base.MvpView;
import com.medhelp.shared.model.UserResponse;

import java.util.List;

public interface FinancesAndServicesHelper {

    interface View extends MvpView {
        void showLoading();
        void hideLoading();
        void  showErrorScreen();
        void updateData(List<VisitResponse> data, String today, String time);
    }

    interface Presenter {
        void removePassword();
        void getVisits();
        String getUserToken();
        UserResponse getCurrentUser();
        void setCurrentUser(UserResponse user);
    }
}
