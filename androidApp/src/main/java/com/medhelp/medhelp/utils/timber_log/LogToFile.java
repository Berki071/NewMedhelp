package com.medhelp.medhelp.utils.timber_log;

import android.content.Context;

import com.medhelp.medhelp.utils.TimesUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class LogToFile {

    private final String name="Logging.txt";

    private File fWrite;

    public LogToFile(Context context)
    {
        fWrite=getFile(context);
    }

    public void write(String message)
    {
        String newString=getCurrentTime()+" "+message+"\n";

        FileOutputStream stream;
        try {
            stream = new FileOutputStream(fWrite,true);
            stream.write(newString.getBytes());
            stream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File getFile(Context context)
    {
        //File f= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File pathToDownloadFolder= context.getCacheDir();

        File file=new File(pathToDownloadFolder,name);

        if(!file.exists())
        {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return file;
    }

    private String getCurrentTime()
    {
        return TimesUtils.longToString(System.currentTimeMillis(),TimesUtils.DATE_FORMAT_HHmmss_ddMMyyyy);
    }
}
