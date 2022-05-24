package com.medhelp.medhelp.ui.analise_list;

import com.medhelp.medhelp.ui.analise_list.recy_folder.DataForAnaliseList;
import com.medhelp.medhelp.ui.base.MvpView;
import com.medhelp.shared.model.UserResponse;

import java.util.List;

public interface AnaliseListHelper {
    interface View extends MvpView {
        void showErrorScreen();
        void updateAnaliseData(List<DataForAnaliseList> response);
    }

    interface Presenter {
        void updateAnaliseList();
        void removePassword();
        void unSubscribe();

        String getUserToken();

        UserResponse getCurrentUser();
        void setCurrentUser(UserResponse user);
    }
}
