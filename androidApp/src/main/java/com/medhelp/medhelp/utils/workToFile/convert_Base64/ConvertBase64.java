package com.medhelp.medhelp.utils.workToFile.convert_Base64;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Base64OutputStream;

import com.medhelp.medhelp.utils.workToFile.show_file.LoadFile;
import com.medhelp.medhelp.utils.timber_log.LoggingTree;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import io.reactivex.Single;
import timber.log.Timber;

public class ConvertBase64 {

    public String base64ToFile(String b64,Context context)
    {
        File file=null;
        try {

        byte[] decodedString = Base64.decode(b64, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        file = new File(getFile(context));
        OutputStream os;
        os = new BufferedOutputStream(new FileOutputStream(file));

        decodedByte.compress(Bitmap.CompressFormat.JPEG, 100, os);
        os.close();

        } catch (FileNotFoundException e) {
            Timber.tag("my").e(LoggingTree.getMessageForError(null,"ConvertBase64$base64ToFile1 "+e.getMessage()));
        } catch (IOException e) {
            Timber.tag("my").e(LoggingTree.getMessageForError(null,"ConvertBase64$base64ToFile2 "+e.getMessage()));
        }

        if(file!=null)
            return file.getAbsolutePath();
        else
            return null;
    }

    private String getFile(Context context)
    {
        //File pathToDownloadFolder= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File pathToDownloadFolder= context.getCacheDir();

        File pathToFolderApp=new File(pathToDownloadFolder, LoadFile.NAME_FOLDER_APP);
        if(!pathToFolderApp.exists())
        {
            pathToFolderApp.mkdirs();
        }

        File pathToFolder = new File(pathToFolderApp, LoadFile.NAME_FOLDER_CHAT);
        if (!pathToFolder.exists()) {
            pathToFolder.mkdirs();
        }


        File file=new File(pathToFolder,LoadFile.NAME_FOLDER_CHAT+System.currentTimeMillis()+".JPEG");
        try {
            file.createNewFile();
        } catch (IOException e) {
            Timber.tag("my").e(LoggingTree.getMessageForError(null,"ConvertBase64$getFile "+e.getMessage()));
        }

        if (file.exists())
            return file.getAbsolutePath();
        else
            return null;
    }



    public Single<String> fileToBase64 (File img) {

        if(img.length() > 1024*1024*9)
            return Single.error(new Throwable("Ограничение на размер изображения 9 мегабайт"));

        InputStream inputStream = null;//You can get an inputStream using any IO API
        try {
            inputStream = new FileInputStream(img.getAbsolutePath());
        } catch (FileNotFoundException e) {
            Timber.tag("my").e(LoggingTree.getMessageForError(null,"ConvertBase64$fileToBase64$1 "+e.getMessage()));
        }

        byte[] buffer = new byte[1024];
        int bytesRead;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Base64OutputStream output64 = new Base64OutputStream(output, Base64.DEFAULT);
        try {
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                output64.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            Timber.tag("my").e(LoggingTree.getMessageForError(null,"ConvertBase64$fileToBase64$2 "+e.getMessage()));
        }
        try {
            output64.close();
        } catch (IOException e) {
            Timber.tag("my").e(LoggingTree.getMessageForError(null,"ConvertBase64$fileToBase64$3 "+e.getMessage()));
        }

        String attachedFile = output.toString();
        return Single.just(attachedFile);

    }



}
