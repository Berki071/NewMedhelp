package com.medhelp.medhelp.ui.analise_result;

import android.content.Context;
import android.os.Environment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.medhelp.medhelp.R;
import com.medhelp.medhelp.data.model.AnaliseResponse;
import com.medhelp.medhelp.data.network.NetworkManager;
import com.medhelp.medhelp.data.pref.PreferencesManager;
import com.medhelp.medhelp.ui._main_page.MainActivity;
import com.medhelp.medhelp.utils.workToFile.show_file.LoadFile;
import com.medhelp.medhelp.utils.workToFile.show_file.ShowFile2;
import com.medhelp.medhelp.utils.timber_log.LoggingTree;
import com.medhelp.shared.model.CenterResponse;
import com.medhelp.shared.model.UserResponse;

import java.io.File;
import java.util.Collections;
import java.util.List;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

@SuppressWarnings("unchecked")
public class AnaliseResPresenter {
    Context context;
    AnaliseResFragment view;
    CenterResponse centerInfo;

    PreferencesManager prefManager;
    NetworkManager networkManager ;

    public AnaliseResPresenter(Context context, AnaliseResFragment view) {
        this.context = context;
        this.view = view;

        prefManager = new PreferencesManager(context);
        networkManager = new NetworkManager(prefManager);

        centerInfo = prefManager.getCenterInfo();
    }


    public CenterResponse getCenterInfo() {
        return centerInfo;
    }

    public void updateAnaliseList() {
        //запрос данных с сервера
        view.showLoading();
        CompositeDisposable cd = new CompositeDisposable();
        cd.add(networkManager
                .getResultAnalysis()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(dateList -> {
                    if (view == null) {
                        return;
                    }

                    Collections.sort(dateList.getResponse());

                    if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                        view.showErrorScreen();
                        return;
                    }

                    testDownloadIn(dateList.getResponse());

                    view.updateAnaliseData(dateList.getResponse());
                    view.hideLoading();
                    cd.dispose();
                }, throwable -> {
                    Timber.tag("my").e(LoggingTree.getMessageForError(throwable, "AnaliseResPresenter$updateAnaliseList "));

                    if (view == null) {
                        return;
                    }
                    view.hideLoading();
                    view.showErrorScreen();
                    cd.dispose();
                }));
    }


    private void clearListPath(List<AnaliseResponse> list) {
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setPathToFile("");
        }
    }


    private void testDownloadIn(List<AnaliseResponse> list) {
        try {
            if (list.size() == 1 && list.get(0).getDate() == null)
                return;

           //File pathToDownloadFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            File pathToDownloadFolder= context.getCacheDir();

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

            if (nnn == null) {
                clearListPath(list);
                return;
            }

            if ((!pathToFolderAnalise.exists() && !pathToFolderImage.exists()) || nnn.length <= 0) {
                clearListPath(list);
                return;
            }


            File[] fff1 = pathToFolderAnalise.listFiles();
            File[] fff2 = pathToFolderImage.listFiles();
            File[] allFiles = concatArray(fff1, fff2);

            for (int i = 0; i < list.size(); i++) {
                String linkToFile = truncatePathToAName(list.get(i).getLinkToPDF());

                boolean boo = false;

                for (File f : allFiles) {
                    String fileName = f.getName();

                    if (linkToFile.equals(fileName)) {
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

    public void unSubscribe() {
        view.hideLoading();
    }

    public void deleteFile(int num, List<AnaliseResponse> list) {
        String path = list.get(num).getPathToFile();

        if (!path.equals("")) {
            try {
                File f = new File(path);
                f.delete();

                testDownloadIn(list);
                view.updateAnaliseData(list);
                //((MainActivity) context).runOnUiThread(() -> );
            } catch (Exception e) {
                Log.wtf("fat", e.getMessage());
            }
        } else {
            view.updateAnaliseData(list);
        }
    }

    public String getUserToken() {
        return prefManager.getCurrentUserInfo().getApiKey();
    }

    public void removePassword() {
        prefManager.setCurrentPassword("");
        prefManager.setUsersLogin(null);
        prefManager.setCurrentUserInfo(null);
    }

    private String getExpansion(String path) {
        int index = path.lastIndexOf(".");
        return path.substring(index + 1).toLowerCase();
    }

    public void loadFile(int num, List<AnaliseResponse> list) {
        try {
            list.get(num).setHideDownload(false);
            String link = list.get(num).getLinkToPDF();
            String expansion = getExpansion(link);

            if (expansion.equals("pdf")) {
                new ShowFile2.BuilderPdf(context)
                        .load(link)
                        .token(prefManager.getCurrentUserInfo().getApiKey())
                        .setListener(new ShowFile2.ShowListener() {
                            @Override
                            public void complete(File file) {

                                boolean dd = file.exists();

                                testDownloadIn(list);
                                list.get(num).setPathToFile(file.getPath());
                                list.get(num).setHideDownload(true);

                                if (view == null)
                                    return;

                                view.updateAnaliseData(list);

                            }

                            @Override
                            public void error(String error) {
                                list.get(num).setHideDownload(true);
                                ((MainActivity) context).runOnUiThread(() -> {
                                    view.updateAnaliseData(list);
                                    view.showErrorDownload();
                                });
                            }
                        })
                        .build();
            } else if (expansion.equals("jpg") || expansion.equals("jpeg")) {
                new LoadFile(context, ShowFile2.TYPE_IMAGE, truncatePathToAName(link), link, prefManager.getCurrentUserInfo().getApiKey(), null, new LoadFile.LoadFileListener() {
                    @Override
                    public void success(List<File> img) {

                        boolean dd = img.get(0).exists();
                        testDownloadIn(list);
                        list.get(num).setPathToFile(img.get(0).getAbsolutePath());
                        list.get(num).setHideDownload(true);

                        if (view == null) {
                            return;
                        }

                        view.updateAnaliseData(list);

                        //((MainActivity) context).runOnUiThread(() -> view.updateAnaliseData(list));
                    }

                    @Override
                    public void error(String err) {
                        list.get(num).setHideDownload(true);

                        ((MainActivity) context).runOnUiThread(() -> {
                            view.updateAnaliseData(list);
                            view.showErrorDownload();
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

    private String truncatePathToAName(String name) {
        int startTruncate = name.indexOf("path=") + 5;
        int entTruncate = name.length();
        String s = name.substring(startTruncate, entTruncate);
        return s;
    }

    androidx.appcompat.app.AlertDialog dialog;

    private void showAlertUnknown(String expansion) {
        String str = "Неизвестное расширение файла \"" + expansion + "\"";

        LayoutInflater inflater = view.getActivity().getLayoutInflater();
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

    public UserResponse getCurrentUser() {
        return prefManager.getCurrentUserInfo();
    }

    public void setCurrentUser(UserResponse user) {
        prefManager.setCurrentUserInfo(user);
    }
}
