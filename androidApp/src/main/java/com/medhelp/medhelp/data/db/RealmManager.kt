//package com.medhelp.medhelp.data.db
//
//import android.content.Context
//import com.medhelp.medhelp.data.model.DataPaymentForRealm
//import io.reactivex.Single
//import java.util.ArrayList
//
//class RealmManager(private val context: Context) {
//    private val config: RealmConfiguration = RealmConfig().config
//    private val preferencesManager: PreferencesManager
//
//    //region payment
//    fun savePaymentData(data: DataPaymentForRealm) {
//        Realm.init(context)
//        val realm: Realm = Realm.getInstance(config)
//        realm.beginTransaction()
//        val n: Number = realm.where(DataPaymentForRealm::class.java).max("id")
//        val id = if (n == null) 1 else n.toLong() + 1
//        data.setId(id)
//        realm.insertOrUpdate(data)
//        realm.commitTransaction()
//        realm.close()
//        realm.compactRealm(config)
//    }
//
//    //DataPaymentForRealm r=realm.copyFromRealm(rRes);
//    val paymentData: Single<List<Any>>
//        get() {
//            Realm.init(context)
//            val realm: Realm = Realm.getInstance(config)
//            val roomsResult: RealmResults<DataPaymentForRealm> =
//                realm.where(DataPaymentForRealm::class.java).findAll()
//            val dd: MutableList<DataPaymentForRealm> = ArrayList<DataPaymentForRealm>()
//            for (rRes in roomsResult) {
//                //DataPaymentForRealm r=realm.copyFromRealm(rRes);
//                dd.add(realm.copyFromRealm(rRes))
//            }
//            realm.close()
//            return Single.just<List<DataPaymentForRealm>>(dd)
//        }
//
//    fun deletePaymentData(idPayment: String?) {
//        Realm.init(context)
//        val realm: Realm = Realm.getInstance(config)
//        realm.beginTransaction()
//        val result: DataPaymentForRealm =
//            realm.where(DataPaymentForRealm::class.java).equalTo("idPayment", idPayment).findFirst()
//        if (result != null) result.deleteFromRealm()
//        realm.commitTransaction()
//        realm.close()
//        realm.compactRealm(config)
//    } //endregion
//
//    //    @Override
//    //    public Completable saveInfoAboutDoc(List<InfoAboutDoc> list) {
//    //        return Completable.fromAction(() ->
//    //                {
//    //                    long idUser=preferencesManager.getCurrentUserInfo().getIdUser();
//    //
//    //                    Realm.init(context);
//    //                    Realm realm= Realm.getInstance(config);
//    //                    realm.beginTransaction();
//    //
//    //                    for(int i=0;i<list.size();i++)
//    //                    {
//    //                        list.get(i).setForIdUser(idUser);
//    //
//    //                        realm.insertOrUpdate(list.get(i));
//    //                    }
//    //
//    //                    realm.commitTransaction();
//    //
//    //                    realm.close();
//    //                    Realm.compactRealm(config);
//    //                }
//    //        );
//    //    }
//    //
//    //    @Override
//    //    public Single<InfoAboutDoc> getInfoAboutOneDoc(long idRoom)
//    //    {
//    //        long idUser=preferencesManager.getCurrentUserInfo().getIdUser();
//    //
//    //        Realm.init(context);
//    //        Realm realm=Realm.getInstance(config);
//    //
//    //        InfoAboutDoc responseInfoAboutDoc = realm.where(InfoAboutDoc.class).equalTo("room",idRoom).equalTo("forIdUser",idUser).findFirst();
//    //        responseInfoAboutDoc=realm.copyFromRealm(responseInfoAboutDoc);
//    //
//    //        realm.close();
//    //
//    //        return Single.just(responseInfoAboutDoc);
//    //    }
//    //
//    //    @Override
//    //    public Single<List<Room>> getAllActiveRooms() {
//    //
//    //        long idUser=preferencesManager.getCurrentUserInfo().getIdUser();
//    //
//    //        Realm.init(context);
//    //        Realm realm=Realm.getInstance(config);
//    //
//    //        RealmResults<Room>responseRoom=realm
//    //                .where(Room.class)
//    //               //.equalTo("forIdUser",idUser)
//    //                .findAll();
//    //
//    //        List<Room> listRoom=realm.copyFromRealm(responseRoom);
//    //
//    //        List<Room> newList=new ArrayList<>();
//    //
//    //        for(int i=0;i<listRoom.size();i++)
//    //        {
//    //            if(listRoom.get(i).getInfoAboutDoc().getForIdUser()!=idUser)
//    //                continue;
//    //
//    //            if(listRoom.get(i).getListMsg().size()>0)
//    //            {
//    //                newList.add(listRoom.get(i));
//    //            }
//    //        }
//    //
//    //        realm.close();
//    //
//    //        return Single.just(newList);
//    //    }
//    //
//    //
//    //    @Override
//    //    public Single<List<Message>> getAllMessageRoom(long idRoom, long idDoc) {
//    //        List<Message> tmp=getAllMessageRoomList(idRoom,idDoc);
//    //
//    //        if(tmp!=null)
//    //            return Single.just(tmp);
//    //        else
//    //             return Single.error(new Throwable(ERROR_SEVERAL_ROOM));
//    //    }
//    //
//    //    public List<Message> getAllMessageRoomList(long idRoom, long idDoc)  {
//    //        long idUser=preferencesManager.getCurrentUserInfo().getIdUser();
//    //
//    //       Realm.init(context);
//    //       Realm realm=Realm.getInstance(config);
//    //
//    //       RealmResults<Room> responseR =realm.where(Room.class).equalTo("IdRoom",idRoom).findAll();
//    //
//    //        if(responseR.size()>1)
//    //        {
//    //            realm.close();
//    //            return null;
//    //        }
//    //
//    //        if(responseR.size()==0) {
//    //            InfoAboutDoc responseI=realm.where(InfoAboutDoc.class).equalTo("idDoc", idDoc).equalTo("forIdUser",idUser).findFirst();
//    //
//    //            assert responseI!=null;
//    //
//    //            Room roo=new Room();
//    //            roo.setRoom(idRoom);
//    //            roo.setInfoAboutDoc(responseI);
//    //            roo.setListMsg(new RealmList<>());
//    //
//    //            realm.beginTransaction();
//    //            realm.copyToRealm(roo);
//    //            realm.commitTransaction();
//    //
//    //            realm.close();
//    //            return new RealmList<>();
//    //        }
//    //
//    //        Room r=realm.copyFromRealm(responseR.get(0));
//    //
//    //
//    //        clearingDates(r.getListMsg());
//    //        Collections.sort(r.getListMsg());
//    //        addDateItem(r.getListMsg(),realm);
//    //
//    //        realm.close();
//    //
//    //        return r.getListMsg();
//    //    }
//    //
//    //    @Override
//    //    public Single<ResponseFromSaveOurMessage> saveOurMsg(long idRoom, String msg , int type) {
//    //        long idUser=preferencesManager.getCurrentUserInfo().getIdUser();
//    //
//    //        Realm.init(context);
//    //        Realm realm=Realm.getInstance(config);
//    //        realm.beginTransaction();
//    //
//    //        Room r= realm.where(Room.class).equalTo("IdRoom", idRoom).findFirst();
//    //
//    //        assert r !=null;
//    //
//    //        Number n=realm.where(Message.class).max("id");
//    //        long id=n==null?1:n.longValue()+1;
//    //
//    //        Message message=new Message();
//    //        message.setIdUser(idUser);
//    //        message.setId(id);
//    //        message.setMsg(msg);
//    //        message.setMyMsg(true);
//    //        message.setRead(false);
//    //        message.setTimeUtc(getCurrentUtcDate());
//    //        message.setType(type);
//    //
//    //        realm.insert(message);
//    //        r.getListMsg().add(message);
//    //
//    //        realm.commitTransaction();
//    //        realm.close();
//    //        Realm.compactRealm(config);
//    //
//    //        return Single.just(new ResponseFromSaveOurMessage(message.getId(),getAllMessageRoomList(idRoom,0L)));
//    //    }
//    //
//    //    @Override
//    //    public Completable saveExternalMsg(List<MessageFromServer> list) {
//    //        return Completable.fromAction(()->{
//    //            long idUser=preferencesManager.getCurrentUserInfo().getIdUser();
//    //            Realm.init(context);
//    //            Realm realm=Realm.getInstance(config);
//    //            realm.beginTransaction();
//    //
//    //            for(MessageFromServer m : list)
//    //            {
//    //                if(m.getType().equals(MainUtils.IMAGE))
//    //                {
//    //                    m.setMsg(m.getMsg());
//    //                }
//    //
//    //                Number n=realm.where(Message.class).max("id");
//    //                long newId=n==null ? 1 : n.longValue()+1;
//    //
//    //                Message msg=new Message();
//    //                msg.setIdUser(idUser);
//    //                msg.setId(newId);
//    //                msg.setTimeUtc(getCurrentUtcDate());
//    //                msg.setType(MainUtils.convertTypeStringToInt(m.getType()));
//    //                msg.setMsg(m.getMsg());
//    //                msg.setMyMsg(false);
//    //
//    //                realm.insert(msg);
//    //
//    //                Room requestRoom =realm.where(Room.class).equalTo("IdRoom" , m.getIdRoom()) .findFirst();
//    //                requestRoom.getListMsg().add(msg);
//    //            }
//    //
//    //            realm.commitTransaction();
//    //            realm.close();
//    //            Realm.compactRealm(config);
//    //        });
//    //    }
//    //
//    //
//    //    private long getCurrentUtcDate()
//    //    {
//    //        return TimesUtils.localLongToUtcLong(System.currentTimeMillis());
//    //    }
//    //
//    //    // разделяющие даты в списке сообщений
//    //    private void addDateItem(RealmList<Message> msg,Realm realm)
//    //    {
//    //        long korValue = 0;
//    //
//    //        if(msg==null || msg.size()==0)
//    //            return;
//    //
//    //        String date=getDateForMsgTitle(msg.get(0));
//    //        insertMessageItemWithDate(msg,0,date,realm, korValue);
//    //        korValue++;
//    //
//    //        for(int i=1;i<msg.size()-1;i++)
//    //        {
//    //           long d1 = TimesUtils.longToNewFormatLong(msg.get(i).getTimeUtc(),TimesUtils.DATE_FORMAT_ddMMyyyy);
//    //           long d2 = TimesUtils.longToNewFormatLong(msg.get(i+1).getTimeUtc(),TimesUtils.DATE_FORMAT_ddMMyyyy);
//    //
//    //           if(d1!=d2)
//    //           {
//    //               date=getDateForMsgTitle(msg.get(i+1));
//    //               insertMessageItemWithDate(msg,i+1,date,realm, korValue);
//    //               korValue++;
//    //               i++;
//    //           }
//    //        }
//    //    }
//    //
//    //    private void insertMessageItemWithDate(RealmList<Message> msg, int position, String value, Realm realm,long korValue)
//    //    {
//    //        long idUser=preferencesManager.getCurrentUserInfo().getIdUser();
//    //
//    //        Number n=realm.where(Message.class).max("id");
//    //        long id=n==null?1:n.longValue()+1+korValue;
//    //
//    //        Message msgNew=new Message();
//    //        msgNew.setIdUser(idUser);
//    //        msgNew.setType(Message.DATE);
//    //        msgNew.setMsg(value);
//    //        msgNew.setId(id);
//    //        msg.add(position,msgNew);
//    //    }
//    //
//    //    private String getDateForMsgTitle(Message msg)
//    //    {
//    //        return TimesUtils.longToString(msg.getTimeUtc(),TimesUtils.DATE_FORMAT_dd_MMMM_yyyy);
//    //    }
//    //
//    //    private void clearingDates(RealmList<Message> msg)
//    //    {
//    //        for(int i=0;i<msg.size();i++)
//    //        {
//    //            if(msg.get(i).getType()==Message.DATE)
//    //            {
//    //                msg.remove(i);
//    //                i--;
//    //            }
//    //        }
//    //    }
//    //
//    //    @Override
//    //    public Single<List<MessageFromServer>> getAllNoReadMsg()
//    //    {
//    //        Realm.init(context);
//    //        Realm realm=Realm.getInstance(config);
//    //
//    //        RealmResults<Room> roomsResult=realm.where(Room.class).findAll();
//    //        List<MessageFromServer> dd =new ArrayList<>();
//    //
//    //        for(Room rRes : roomsResult)
//    //        {
//    //            Room r=realm.copyFromRealm(rRes);
//    //            for (int j=0;j<r.getListMsg().size();j++)
//    //            {
//    //                if( r.getListMsg().get(j).isMyMsg()  &&  !r.getListMsg().get(j).getRead())
//    //                {
//    //                    dd.add(new MessageFromServer(r.getRoom() , MainUtils.convertTypeIntToString(r.getListMsg().get(j).getType()) , r.getListMsg().get(j).getMsg()  , r.getListMsg().get(j).getId()));
//    //                }
//    //            }
//    //        }
//    //        realm.close();
//    //
//    //        return Single.just(dd);
//    //    }
//    //
//    //    @Override
//    //    public Single<Boolean> testExistAllRoom(List<MessageFromServer> list) {
//    //        long idUser=preferencesManager.getCurrentUserInfo().getIdUser();
//    //
//    //        Realm.init(context);
//    //        Realm realm=Realm.getInstance(config);
//    //
//    //        for(MessageFromServer msg  :  list)
//    //        {
//    //            InfoAboutDoc iad=realm.where(InfoAboutDoc.class).equalTo("room",msg.getIdRoom()).equalTo("forIdUser",idUser).findFirst();
//    //
//    //            if(iad==null || iad.getName()==null) {
//    //                return Single.just(false);
//    //            }
//    //
//    //            Room r=realm.where(Room.class).equalTo("IdRoom",msg.getIdRoom()).findFirst();
//    //            if(r==null)
//    //            {
//    //                InfoAboutDoc tmp2=realm.copyFromRealm(iad);
//    //                List<InfoAboutDoc> tmp= new ArrayList<>();
//    //                tmp.add(tmp2);
//    //
//    //                Single<List<Message>> slm= getAllMessageRoom(iad.getRoom(), iad.getIdDoc());
//    //                slm.subscribe();
//    //            }
//    //           // r=realm.where(Room.class).equalTo("IdRoom",msg.getIdRoom()).findFirst();
//    //        }
//    //
//    //
//    //        realm.close();
//    //        return Single.just(true);
//    //    }
//    //
//    //    @Override
//    //    public Completable updateOurMsgIsRead(MessageFromServer msgO) {
//    //        return Completable.fromAction(()->{
//    //            Realm.init(context);
//    //            Realm realm=Realm.getInstance(config);
//    //            realm.beginTransaction();
//    //
//    //            Message msgRes = realm.where(Message.class).equalTo("id", msgO.getId()).findFirst();
//    //            Message msg = realm.copyFromRealm(msgRes);
//    //            msg.setRead(true);
//    //
//    //            realm.insertOrUpdate(msg);
//    //
//    //            realm.commitTransaction();
//    //            realm.close();
//    //        });
//    //    }
//    //
//    //    @Override
//    //    public Completable updateOurMsgIsNoRead(MessageFromServer msgO) {
//    //        return Completable.fromAction(()->{
//    //            Realm.init(context);
//    //            Realm realm=Realm.getInstance(config);
//    //            realm.beginTransaction();
//    //
//    //            Message msgRes = realm.where(Message.class).equalTo("id", msgO.getId()).findFirst();
//    //            Message msg = realm.copyFromRealm(msgRes);
//    //            msg.setRead(false);
//    //
//    //            realm.insertOrUpdate(msg);
//    //
//    //            realm.commitTransaction();
//    //            realm.close();
//    //        });
//    //    }
//    //
//    //    //region news
//    //
//    //    private void saveNewsId(List<NewsResponse> list) {
//    //
//    //        Realm.init(context);
//    //        Realm realm=Realm.getInstance(config);
//    //        realm.beginTransaction();
//    //
//    //        for(NewsResponse tmp  :  list)
//    //        {
//    //            NewsRealm item=new NewsRealm();
//    //            item.setId(tmp.getId());
//    //            Long l=TimesUtils.stringToLong(tmp.getDate(),TimesUtils.DATE_FORMAT_ddMMyyyy);
//    //            item.setTime(l);
//    //
//    //            realm.insertOrUpdate(item);
//    //        }
//    //
//    //        realm.commitTransaction();
//    //        realm.close();
//    //        realm.compactRealm(config);
//    //    }
//    //
//    //    @Override
//    //    public Single<List<NewsResponse>> testNewsForShow(List<NewsResponse> list, int restrictionTimeDays) {
//    //
//    //        Realm.init(context);
//    //        Realm realm=Realm.getInstance(config);
//    //
//    //        Long currentTime=System.currentTimeMillis();
//    //        Long restrictionTime=currentTime-(restrictionTimeDays*(1000*60*60*24));
//    //
//    //        for(int i=0;i<list.size();i++)
//    //        {
//    //            Long dateL=TimesUtils.stringToLong(list.get(i).getDate(), TimesUtils.DATE_FORMAT_ddMMyyyy);
//    //            if(dateL<restrictionTime)
//    //            {
//    //                list.remove(list.get(i));
//    //                --i;
//    //            }
//    //        }
//    //
//    //        if(list==null || list.size()==0)
//    //        {
//    //            realm.close();
//    //            return Single.just(new ArrayList<>());
//    //        }
//    //
//    //
//    //        RealmResults<NewsRealm> res1=realm.where(NewsRealm.class).greaterThan("time",restrictionTime).findAll();
//    //
//    //        if(res1==null || res1.size()==0)
//    //        {
//    //            realm.close();
//    //            saveNewsId(list);
//    //            return Single.just(list);
//    //        }
//    //
//    //        for(NewsRealm tmp2 : res1)
//    //        {
//    //            NewsRealm tmp=realm.copyFromRealm(tmp2);
//    //
//    //            for(int i=0;i<list.size();i++)
//    //            {
//    //                if(tmp.getId()==list.get(i).getId())
//    //                {
//    //                    list.remove(list.get(i));
//    //                    break;
//    //                }
//    //            }
//    //        }
//    //
//    //        if(list==null || list.size()==0)
//    //        {
//    //            realm.close();
//    //            return Single.just(new ArrayList<>());
//    //        }
//    //
//    //        realm.close();
//    //        saveNewsId(list);
//    //        return Single.just(list);
//    //    }
//    //
//    //    //endregion
//    companion object {
//        const val ERROR_SEVERAL_ROOM = "ERROR several rooms with one id"
//    }
//
//    init {
//        preferencesManager = PreferencesManager(context)
//        //convertBase64=new ConvertBase64();
//    }
//}