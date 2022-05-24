//package com.medhelp.medhelp.ui._chat.room;
//
//import android.content.Context;
//import com.medhelp.medhelp.R;
//import com.medhelp.medhelp.data.db.RealmHelper;
//import com.medhelp.medhelp.data.db.RealmManager;
//import com.medhelp.medhelp.data.network.NetworkManager;
//import com.medhelp.medhelp.data.pref.PreferencesManager;
//import com.medhelp.medhelp.utils.workToFile.show_file.LoadFile;
//import com.medhelp.medhelp.utils.rx.AppSchedulerProvider;
//import com.medhelp.medhelp.utils.rx.SchedulerProvider;
//import com.medhelp.medhelp.utils.timber_log.LoggingTree;
//import java.io.BufferedInputStream;
//import java.io.BufferedOutputStream;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.util.Random;
//
//import io.reactivex.disposables.CompositeDisposable;
//import timber.log.Timber;
//
//public class RoomPresenter implements RoomHelper.Presenter {
//    private SchedulerProvider schedulerProvider;
//
//    private RoomHelper.View activityHelper;
//    private Context context;
//
//    PreferencesManager prefManager;
//    NetworkManager networkManager;
//    RealmHelper realmManager;
//
//    public RoomPresenter(Context context) {
//        this.context=context;
//        prefManager=new PreferencesManager(context);
//        networkManager=new NetworkManager(prefManager);
//        realmManager=new RealmManager(context);
//
//        schedulerProvider=new AppSchedulerProvider();
//
//        activityHelper=(RoomHelper.View)context;
//    }
//
//
//    @Override
//    public void getAllMessage(long idRoom, long idDoc ) {
//
//        activityHelper.showLoading();
//
//        CompositeDisposable cd = new CompositeDisposable();
//        cd.add(realmManager
//                .getAllMessageRoom(idRoom,idDoc)
//                .subscribeOn(schedulerProvider.io())
//                .observeOn(schedulerProvider.ui())
//                .subscribe(response -> {
//                    if (activityHelper == null) {
//                        return;
//                    }
//                    activityHelper.updateRecy(response);
//
//                    activityHelper.hideLoading();
//                    cd.dispose();
//                }, throwable ->
//                {
//                    Timber.tag("my").e(LoggingTree.getMessageForError(throwable,"RoomPresenter$getAllMessage "));
//
//                    if (activityHelper == null) {
//                        return;
//                    }
//
//                    if (throwable.getMessage().equals(RealmManager.ERROR_SEVERAL_ROOM)) {
//                        activityHelper.showError(throwable.getMessage());
//                    }
//
//                    activityHelper.showError(context.getResources().getString(R.string.api_default_error));
//
//                    activityHelper.hideLoading();
//                    cd.dispose();
//                }));
//
//    }
//
//
//
//    //region send message
//    private boolean testMessage(String msg) {
//        return !msg.equals("") && msg.trim().length() > 0;
//
//    }
//
//    @Override
//    public void sendOurMsgSaveToRealm(long idRoom , String msg, int type) {
//
//        if(!testMessage(msg))
//            return;
//
//        CompositeDisposable cd = new CompositeDisposable();
//        cd.add(realmManager
//                .saveOurMsg(idRoom,msg,type)
//                .subscribeOn(schedulerProvider.io())
//                .observeOn(schedulerProvider.ui())
//                .subscribe(response-> {
//                    if (activityHelper == null) {
//                        return;
//                    }
//
//                    activityHelper.updateItemRecy(response.getListMsg());
//                  //  sendOurMsgToServer(idRoom , msg , type , response.getIdSaveMsg());
//                    cd.dispose();
//                }, throwable ->
//                {
//                    Timber.tag("my").e(LoggingTree.getMessageForError(throwable,"RoomPresenter$sendOurMsgSaveToRealm "));
//
//                    if (activityHelper == null) {
//                        return;
//                    }
//
//                    activityHelper.showError(context.getResources().getString(R.string.api_default_error));
//                    cd.dispose();
//                }));
//    }
//
//    //отправка на сервер идет в бэкграунде в данный момент по таймеру
//
//    //endregion
//
//    //region work with files
//
//    @Override
//    public File generateFileCamera() {
//        //File pathToDownloadFolder= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
//        File pathToDownloadFolder= context.getCacheDir();
//
//        File pathToFolderPDF=new File(pathToDownloadFolder, LoadFile.NAME_FOLDER_APP);
//        if(!pathToFolderPDF.exists())
//        {
//            pathToFolderPDF.mkdirs();
//        }
//
//        File pathToFolderCamera=new File(pathToFolderPDF, LoadFile.NAME_FOLDER_CAMERA);
//        if(!pathToFolderCamera.exists())
//        {
//            pathToFolderCamera.mkdirs();
//        }
//
//        String str="/camera_"+System.currentTimeMillis()+generateTwo_digitNumber()+".jpg";
//        File newFile=new File(pathToFolderCamera,str);
//        try {
//            newFile.createNewFile();
//            return newFile;
//        } catch (IOException e) {
//            Timber.tag("my").e(LoggingTree.getMessageForError(e,"RoomPresenter$generateFileCamera"));
//        }
//
//        return null;
//    }
//
//    @Override
//    public File generateFilePhoto(File file) {
//        //File pathToDownloadFolder= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
//        File pathToDownloadFolder= context.getCacheDir();
//
//        File pathToFolderPDF=new File(pathToDownloadFolder, LoadFile.NAME_FOLDER_APP);
//        if(!pathToFolderPDF.exists())
//        {
//            pathToFolderPDF.mkdirs();
//        }
//
//        File pathToFolderCamera=new File(pathToFolderPDF, LoadFile.NAME_FOLDER_CHAT);
//        if(!pathToFolderCamera.exists())
//        {
//            pathToFolderCamera.mkdirs();
//        }
//
//        String str="/image_"+System.currentTimeMillis()+generateTwo_digitNumber()+".jpg";
//        File newFile=new File(pathToFolderCamera,str);
//
//        try {
//            newFile.createNewFile();
//        } catch (IOException e) {
//            Timber.tag("my").e(LoggingTree.getMessageForError(e,"RoomPresenter$generateFileFoto$1 "));
//        }
//
//        try (
//                InputStream in = new BufferedInputStream(
//                        new FileInputStream(file));
//                OutputStream out = new BufferedOutputStream(
//                        new FileOutputStream(newFile))) {
//
//            byte[] buffer = new byte[1024];
//            int lengthRead;
//            while ((lengthRead = in.read(buffer)) > 0) {
//                out.write(buffer, 0, lengthRead);
//                out.flush();
//            }
//        } catch (FileNotFoundException e) {
//            Timber.tag("my").e(LoggingTree.getMessageForError(e,"RoomPresenter$generateFileFoto$2"));
//        } catch (IOException e) {
//            Timber.tag("my").e(LoggingTree.getMessageForError(e,"RoomPresenter$generateFileFoto$3"));
//        }
//
//        return newFile;
//    }
//
//
//    private int generateTwo_digitNumber()
//    {
//        Random rand=new Random();
//        return rand.nextInt(100);
//    }
//
//    @Override
//    public void clearEmptyFiles() {
//        Thread dr=new Thread(
//                () -> {
//                    //File pathToDownloadFolder= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
//                    File pathToDownloadFolder= context.getCacheDir();
//
//                    File pathToFolderPDF=new File(pathToDownloadFolder, LoadFile.NAME_FOLDER_APP);
//                    if(!pathToFolderPDF.exists())
//                    {
//                        return;
//                    }
//
//                    File pathToFolderCamera=new File(pathToFolderPDF,LoadFile.NAME_FOLDER_CAMERA);
//                    if(!pathToFolderCamera.exists())
//                    {
//                        return;
//                    }
//
//                    File [] masF=pathToFolderCamera.listFiles();
//
//                    for(File f : masF)
//                    {
//                        if(f.length()==0)
//                            f.delete();
//                    }
//                }
//        );
//
//        dr.start();
//    }
//
//    @Override
//    public String getUserToken() {
//        return prefManager.getCurrentUserInfo().getApiKey();
//    }
//    //endregion
//}
