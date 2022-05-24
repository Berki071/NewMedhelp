//package com.medhelp.medhelp.ui._chat.room;
//
//import android.net.Uri;
//
//import com.medhelp.medhelp.data.model.chat.Message;
//
//import java.io.File;
//import java.util.List;
//
//public interface RoomHelper {
//    interface View{
//        void updateRecy(List<Message> listMsg);
//        void updateItemRecy(List<Message> response);
//
//        void showError(String msg);
//        void showLoading();
//        void hideLoading();
//
//        void setMessageToRealm(Message msg);
//
//        void refreshRecyItems();
//    }
//
//    interface Presenter{
//        void getAllMessage(long idRoom, long idDoc );
//
//        void sendOurMsgSaveToRealm(long idRoom , String msg, int type);
//
//        File generateFileCamera();
//        File generateFilePhoto(File file);
//
//        void clearEmptyFiles();
//
//        String getUserToken();
//    }
//}
