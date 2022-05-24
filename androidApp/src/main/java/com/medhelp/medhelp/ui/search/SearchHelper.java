package com.medhelp.medhelp.ui.search;

import com.medhelp.medhelp.data.model.CategoryResponse;
import com.medhelp.medhelp.data.model.ServiceResponse;
import com.medhelp.medhelp.ui.base.MvpView;
import com.medhelp.medhelp.ui.search.recy_spinner.SearchAdapter;
import com.medhelp.shared.model.UserResponse;

import java.util.List;

public interface SearchHelper {
    interface View extends MvpView {
        void showErrorScreen();

        void showError(String msg);

        void updateView(List<CategoryResponse> categories, List<ServiceResponse> services);

        void refreshRecy();
    }


    interface Presenter {

        void getData();

        void unSubscribe();

        String getUserToken();

        void changeFabFavorites(ServiceResponse item);

        void testToSpam(SearchAdapter.ViewHolder view, int service, int idLimit);

        UserResponse getCurrentUser();

        void setCurrentUser(UserResponse user);
    }
}
