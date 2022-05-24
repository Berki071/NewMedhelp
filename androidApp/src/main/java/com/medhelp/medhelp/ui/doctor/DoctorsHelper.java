package com.medhelp.medhelp.ui.doctor;

import com.medhelp.medhelp.data.model.AllDoctorsResponse;
import com.medhelp.medhelp.data.model.CategoryResponse;
import com.medhelp.medhelp.ui.base.MvpView;
import com.medhelp.shared.model.UserResponse;

import java.util.List;

public interface DoctorsHelper {

    interface View extends MvpView {

        void showErrorScreen();

        void updateView(List<AllDoctorsResponse> response);

        void updateSpecialty(List<CategoryResponse> response);
    }


    interface Presenter {

        void getDoctorList(int idSpec);

        void getSpecialtyByCenter();

        void removePassword();

        void unSubscribe();

        String getUserToken();

        UserResponse getCurrentUser();
        void setCurrentUser(UserResponse user);
    }
}
