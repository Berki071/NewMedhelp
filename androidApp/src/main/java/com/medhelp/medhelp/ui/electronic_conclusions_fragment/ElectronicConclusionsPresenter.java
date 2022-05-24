package com.medhelp.medhelp.ui.electronic_conclusions_fragment;

import android.content.Context;
import android.os.Environment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.androidnetworking.error.ANError;
import com.medhelp.medhelp.R;
import com.medhelp.medhelp.data.model.AnaliseResponse;
import com.medhelp.medhelp.data.model.LoadDataZaklAmbItem;
import com.medhelp.medhelp.data.model.ResultZakl2Item;
import com.medhelp.medhelp.data.model.ResultZaklResponse;
import com.medhelp.medhelp.data.network.NetworkManager;
import com.medhelp.medhelp.data.pref.PreferencesManager;
import com.medhelp.medhelp.ui._main_page.MainActivity;
import com.medhelp.medhelp.ui.electronic_conclusions_fragment.adapters.recy.DataClassForElectronicRecy;
import com.medhelp.medhelp.utils.timber_log.LoggingTree;
import com.medhelp.medhelp.utils.workToFile.show_file.LoadFile;
import com.medhelp.medhelp.utils.workToFile.show_file.ShowFile2;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Headers;
import okhttp3.Response;
import okhttp3.ResponseBody;
import timber.log.Timber;

public class ElectronicConclusionsPresenter {
    Context context;
    ElectronicConclusionsFragment main;
    PreferencesManager prefManager;
    NetworkManager networkManager;

    androidx.appcompat.app.AlertDialog dialog;

    public ElectronicConclusionsPresenter(ElectronicConclusionsFragment main) {
        this.main = main;
        context = main.getContext();

        prefManager = new PreferencesManager(context);
        networkManager = new NetworkManager(prefManager);
    }

    ResultZaklResponse res1 = null;

    public void getData() {
        //запрос данных с сервера
        main.showLoading();
        CompositeDisposable cd = new CompositeDisposable();
        cd.add(networkManager
                .getResultZakl()
                .concatMap(res -> {
                    res1 = res;
                    return networkManager.getResultZakl2();
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(dateList -> {
                    if (main == null) {
                        return;
                    }


                    if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                        main.showErrorScreen();
                        return;
                    }

                    List<DataClassForElectronicRecy> combinedList = new ArrayList<>();
                    if (res1 != null && res1.getResponse().size() >= 1 && res1.getResponse().get(0).getDate() != null)
                        combinedList.addAll(res1.getResponse());
                    if (dateList != null && dateList.getResponse().size() >= 1 && dateList.getResponse().get(0).getDataPriema() != null)
                        combinedList.addAll(dateList.getResponse());

                    if (combinedList.size() == 0)
                        main.initRecy(null);
                    else {
                        testDownloadIn(combinedList);

                        Collections.sort(combinedList);
                        main.initRecy(combinedList);
                    }

                    main.hideLoading();
                    cd.dispose();
                }, throwable -> {
                    Timber.tag("my").e(LoggingTree.getMessageForError(throwable, "ElectronicConclusionsPresenter$getFilesForRecy"));

                    if (main == null) {
                        return;
                    }
                    main.hideLoading();
                    main.showErrorScreen();
                    cd.dispose();
                }));
    }

    private void testDownloadIn(List<DataClassForElectronicRecy> list) {
        try {
            File pathToDownloadFolder = context.getCacheDir();
            File pathToFolder = new File(pathToDownloadFolder, LoadFile.NAME_FOLDER_APP);
            if (!pathToFolder.exists()) {
                clearListPath(list);
                return;
            }

            File pathToFolderAnalise = new File(pathToFolder, LoadFile.NAME_FOLDER_ANALISE);
            File pathToFolderImage = new File(pathToFolder, LoadFile.NAME_FOLDER_CHAT);

            String[] mmm = pathToFolderAnalise.list();
            String[] mmm2 = pathToFolderImage.list();

            String[] nnn = concatArray(mmm, mmm2);

            if (nnn == null || nnn.length <= 0 || (!pathToFolderAnalise.exists() && !pathToFolderImage.exists())) {
                clearListPath(list);
                return;
            }

            File[] fff1 = pathToFolderAnalise.listFiles();
            File[] fff2 = pathToFolderImage.listFiles();
            File[] allFiles = concatArray(fff1, fff2);

            for (int i = 0; i < list.size(); i++) {
                String fileNameList;

                if (list.get(i) instanceof AnaliseResponse)
                    fileNameList = truncatePathToAName(((AnaliseResponse) list.get(i)).getLinkToPDF());
                else
                    fileNameList = ((ResultZakl2Item) list.get(i)).getNameFile(prefManager.getCurrentUserInfo().getName());

                boolean boo = false;

                for (File f : allFiles) {
                    String fileName = f.getName();

                    if (fileNameList.equals(fileName)) {
                        list.get(i).setPathToFile(f.getPath());
                        boo = true;
                        break;
                    }
                }

                if (!boo) {
                    list.get(i).setPathToFile("");
                }
            }
        } catch (Exception e) {
            Log.wtf("fat", e.getMessage());
        }
    }

    private void clearListPath(List<DataClassForElectronicRecy> list) {
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setPathToFile("");
        }
    }

    private String[] concatArray(String[] a, String[] b) {
        if (a == null && b == null)
            return null;
        if (a == null)
            return b;
        if (b == null)
            return a;

        String[] r = new String[a.length + b.length];
        System.arraycopy(a, 0, r, 0, a.length);
        System.arraycopy(b, 0, r, a.length, b.length);
        return r;
    }

    private File[] concatArray(File[] a, File[] b) {
        if (a == null && b == null)
            return null;
        if (a == null)
            return b;
        if (b == null)
            return a;

        File[] r = new File[a.length + b.length];
        System.arraycopy(a, 0, r, 0, a.length);
        System.arraycopy(b, 0, r, a.length, b.length);
        return r;
    }

    private String truncatePathToAName(String name) {
        int startTruncate = name.indexOf("path=") + 5;
        int entTruncate = name.length();
        String s = name.substring(startTruncate, entTruncate);
        return s;
    }

    public void loadFile(AnaliseResponse item) {
        try {
            item.setHideDownload(false);
            String link = item.getLinkToPDF();
            String expansion = getExpansion(link);
            String ddd = prefManager.getCurrentUserInfo().getApiKey();

            if (expansion.equals("pdf")) {
                new ShowFile2.BuilderPdf(context)
                        .load(link)
                        .token(prefManager.getCurrentUserInfo().getApiKey())
                        .setListener(new ShowFile2.ShowListener() {
                            @Override
                            public void complete(File file) {

                                boolean dd = file.exists();

                                testDownloadIn(Arrays.asList(item));
                                item.setPathToFile(file.getPath());
                                item.setHideDownload(true);
                                // main.electronicConclusionsAdapter.updateItemInRecy(item);

                                if (main == null)
                                    return;

                                main.electronicConclusionsAdapter.updateItemInRecy(item);
                            }

                            @Override
                            public void error(String error) {
                                item.setHideDownload(true);
                                main.electronicConclusionsAdapter.updateItemInRecy(item);
                                ((MainActivity) context).runOnUiThread(() -> {
                                    main.showErrorDownload();
                                });
                            }
                        })
                        .build();
            } else if (expansion.equals("jpg") || expansion.equals("jpeg")) {
                new LoadFile(context, ShowFile2.TYPE_IMAGE, truncatePathToAName(link), link, prefManager.getCurrentUserInfo().getApiKey(), null, new LoadFile.LoadFileListener() {
                    @Override
                    public void success(List<File> img) {

                        boolean dd = img.get(0).exists();
                        testDownloadIn(Arrays.asList(item));
                        item.setPathToFile(img.get(0).getAbsolutePath());
                        item.setHideDownload(true);
                        main.electronicConclusionsAdapter.updateItemInRecy(item);

                        if (main == null) {
                            return;
                        }

                        main.electronicConclusionsAdapter.updateItemInRecy(item);
                    }

                    @Override
                    public void error(String err) {
                        item.setHideDownload(true);
                        main.electronicConclusionsAdapter.updateItemInRecy(item);

                        ((MainActivity) context).runOnUiThread(() -> {
                            main.showErrorDownload();
                        });
                    }
                });
            } else {
                showAlertUnknown(expansion);
            }
        } catch (Exception e) {
            Log.wtf("fat", e.getMessage());
        }
    }

    private String getExpansion(String path) {
        int index = path.lastIndexOf(".");
        return path.substring(index + 1).toLowerCase();
    }

    private void showAlertUnknown(String expansion) {
        String str = "Неизвестное расширение файла \"" + expansion + "\"";

        LayoutInflater inflater = main.getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_2textview_btn, null);

        TextView title = view.findViewById(R.id.title);
        TextView text = view.findViewById(R.id.text);
        Button btnYes = view.findViewById(R.id.btnYes);
        Button btnNo = view.findViewById(R.id.btnNo);

        btnNo.setVisibility(View.GONE);

        title.setText(Html.fromHtml("<u>Ошибка!</u>"));
        text.setText(str);


        btnYes.setOnClickListener(v -> {
            dialog.cancel();

        });

        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(view.getContext());
        builder.setView(view);
        dialog = builder.create();
        dialog.show();
    }

    public void deleteFile(DataClassForElectronicRecy item) {
        String path = item.getPathToFile();

        if (!path.equals("")) {
            try {
                File f = new File(path);
                f.delete();

                testDownloadIn(Arrays.asList(item));
                main.electronicConclusionsAdapter.updateItemInRecy(item);

            } catch (Exception e) {
                Log.wtf("fat", e.getMessage());
            }
        } else {
            main.electronicConclusionsAdapter.updateItemInRecy(item);
        }
    }

    public void loadFile2(ResultZakl2Item item) {
        //main.showLoading();
        CompositeDisposable cd = new CompositeDisposable();
        cd.add(networkManager
                .geDataResultZakl2(item)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(dateList -> {
                    //String link = getDataToLink(dateList.getResponse().get(0));
                    String dir = getDir();
                    String fName = item.getNameFile(prefManager.getCurrentUserInfo().getName());
                    downloadPdfFromInternet(dateList.getResponse().get(0), dir, fName, item);

                    //main.hideLoading();
                    cd.dispose();
                }, throwable -> {
                    Timber.tag("my").e(LoggingTree.getMessageForError(throwable, "ElectronicConclusionsPresenter$loadFile2"));

                    if (main == null) {
                        return;
                    }
                    item.setHideDownload(true);
                    main.electronicConclusionsAdapter.updateItemInRecy(item);

                    main.showErrorScreen();
                    cd.dispose();
                }));
    }

    private String getDir() {
        File pathToDownloadFolder = context.getCacheDir();
        File pathToFolder = new File(pathToDownloadFolder, LoadFile.NAME_FOLDER_APP);
        File pathToFolderAnalise = new File(pathToFolder, LoadFile.NAME_FOLDER_ANALISE);
        if (!pathToFolderAnalise.exists()) {
            pathToFolderAnalise.mkdirs();
        }

        return pathToFolderAnalise.getAbsolutePath();
    }

    private void downloadPdfFromInternet(LoadDataZaklAmbItem data, String dirPath, String fileName, ResultZakl2Item item) {
        CompositeDisposable cd = new CompositeDisposable();
        cd.add(networkManager
                .loadFileZakl(data, dirPath, fileName, load2FileListener, item)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    cd.dispose();
                }, throwable -> {
                    item.setHideDownload(true);
                    main.electronicConclusionsAdapter.updateItemInRecy(item);
                    Timber.tag("my").e(LoggingTree.getMessageForError(throwable, "ElectronicConclusionsPresenter/downloadPdfFromInternet"));
                    cd.dispose();
                }));
    }

    NetworkManager.Load2FileListener load2FileListener = new NetworkManager.Load2FileListener() {
        @Override
        public void onResponse(Response response, String dirPath, String fileName, ResultZakl2Item item) {
            byteToFile(response, dirPath, fileName, item);
        }

        @Override
        public void onError(ANError anError, ResultZakl2Item item) {
            item.setHideDownload(true);
            main.electronicConclusionsAdapter.updateItemInRecy(item);
            Timber.tag("my").e(LoggingTree.getMessageForError(anError, "ElectronicConclusionsPresenter/load2FileListener"));
        }
    };

    void byteToFile(Response response, String dirPath, String fileName, ResultZakl2Item item) {
        if(response.code()!=200){
            item.setHideDownload(true);
            updateItemInMainThread(item);
            Timber.tag("my").e( "ElectronicConclusionsPresenter/load2FileListener "+ response.toString());
        }

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                Headers head = response.headers();
                ResponseBody body = response.body();

                File f = new File(dirPath);
                File f2 = new File(f, "/" + fileName);

                if (f2.exists())
                    f2.delete();

                try {
                    f2.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (f2.exists()) {

                    try {
                        InputStream inputStream = null;
                        OutputStream outputStream = null;
                        try {
                            byte[] fileReader = new byte[4096];
                            long fileSize = body.contentLength();
                            long fileSizeDownloaded = 0;
                            inputStream = body.byteStream();
                            outputStream = new FileOutputStream(f2);
                            while (true) {
                                int read = inputStream.read(fileReader);
                                if (read == -1) {
                                    break;
                                }
                                outputStream.write(fileReader, 0, read);
                                fileSizeDownloaded += read;
                            }
                            outputStream.flush();

                            testDownloadIn(Arrays.asList(item));
                            item.setPathToFile(f2.getPath());
                            item.setHideDownload(true);
                            updateItemInMainThread(item);

                        } catch (IOException e) {
                            item.setHideDownload(true);
                            updateItemInMainThread(item);
                            Timber.tag("my").e(LoggingTree.getMessageForError(e, "ElectronicConclusionsPresenter/byteToFile"));
                        } finally {
                            if (inputStream != null) {
                                inputStream.close();
                            }
                            if (outputStream != null) {
                                outputStream.close();
                            }
                        }
                    } catch (IOException e) {
                        item.setHideDownload(true);
                        updateItemInMainThread(item);
                        Timber.tag("my").e(LoggingTree.getMessageForError(e, "ElectronicConclusionsPresenter/byteToFile"));
                    }
                }
                item.setHideDownload(true);
                updateItemInMainThread(item);
            }
        });

        thread.start();
    }

    void updateItemInMainThread(ResultZakl2Item item){
        main.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                main.electronicConclusionsAdapter.updateItemInRecy(item);
            }
        });
    }

}
