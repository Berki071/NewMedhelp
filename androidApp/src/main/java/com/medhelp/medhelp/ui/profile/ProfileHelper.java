/*
package com.medhelp.medhelp.ui.profile;

import com.medhelp.medhelp.ui.base.MvpView;
import com.medhelp.newmedhelp.model.VisitResponse;
import com.medhelp.shared.model.CenterResponse;
import com.medhelp.shared.model.UserResponse;

import java.util.List;

public interface ProfileHelper {

    interface View extends MvpView {
        void showErrorScreen();

        void swipeDismiss();

       // void onRefresh();

        void updateHeader(CenterResponse response);

        void updateData(List<VisitResponse> response, String today, String time);

        void responseConfirmComing(VisitResponse viz);

    }


    interface Presenter {

        void updateHeaderInfo();

      // String getVersionCode();

        void getVisits();

        void unSubscribe();

        void cancellationOfVisit(int idUser, int id_record, String cause, int idBranch);

        void confirmationOfVisit(int idUser, int id_record, int idBranch);

        String getCurrentHospitalBranch();

        String getUserToken();

        UserResponse getCurrentUser();
        void setCurrentUser(UserResponse user);
        void sendConfirmComing(VisitResponse viz);
    }
}
*/
