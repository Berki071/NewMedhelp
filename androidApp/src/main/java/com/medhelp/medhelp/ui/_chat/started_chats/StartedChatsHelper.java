//package com.medhelp.medhelp.ui._chat.started_chats;
//
//import com.medhelp.medhelp.data.model.chat.Room;
//import com.medhelp.medhelp.ui.base.MvpView;
//import com.medhelp.shared.model.UserResponse;
//
//import java.util.List;
//
//public interface StartedChatsHelper {
//    interface View extends MvpView {
//        void showError();
//        void updateRecy(List<Room> list);
//        void showLoading();
//        void hideLoading();
//        boolean isLoading();
//    }
//
//    interface Presenter{
//
//        void removePassword();
//
//        void getListRoom();
//
//        void onDestroy();
//
//        String getUserToken();
//
//        UserResponse getCurrentUser();
//        void setCurrentUser(UserResponse user);
//    }
//}
