//package com.medhelp.medhelp.data.db;
//
//import com.medhelp.medhelp.data.model.chat.InfoAboutDoc;
//import com.medhelp.medhelp.data.model.chat.Message;
//import com.medhelp.medhelp.data.model.chat.MessageFromServer;
//import com.medhelp.medhelp.data.model.chat.ResponseFromSaveOurMessage;
//import com.medhelp.medhelp.data.model.chat.Room;
//import com.medhelp.medhelp.data.model.news.NewsResponse;
//import com.medhelp.medhelp.ui.view.shopping_basket.sub.DataPaymentForRealm;
//
//import java.util.List;
//
//import io.reactivex.Completable;
//import io.reactivex.Single;
//
//public interface RealmHelper {
//
//    Single<List<Message>> getAllMessageRoom(long idRoom, long idDoc );
//
//    Single<ResponseFromSaveOurMessage> saveOurMsg(long idRoom , String msg, int type);
//    Completable updateOurMsgIsRead(MessageFromServer msgO);
//    Completable updateOurMsgIsNoRead(MessageFromServer msgO);
//
//    Completable saveExternalMsg(List<MessageFromServer> list);
//
//    Completable saveInfoAboutDoc(List<InfoAboutDoc> list);
//
//    Single<List<Room>> getAllActiveRooms();
//
//    Single<List<MessageFromServer>> getAllNoReadMsg();
//
//    Single<Boolean> testExistAllRoom(List<MessageFromServer> list);
//
//    Single<InfoAboutDoc> getInfoAboutOneDoc(long idRoom);
//
//    Single<List<NewsResponse>> testNewsForShow(List<NewsResponse> list, int restrictionTime);
//
//    void savePaymentData(DataPaymentForRealm data);
//    Single<List<DataPaymentForRealm>> getPaymentData();
//    void deletePaymentData(String idPayment);
//
//
//}
