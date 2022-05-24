package com.medhelp.medhelp.utils.main;

import com.medhelp.medhelp.utils.timber_log.LoggingTree;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import timber.log.Timber;

public class TimesUtils {

    public static final String DATE_FORMAT_HHmmss_ddMMyyyy ="HH:mm:ss dd.MM.yyyy";
    public static final String DATE_FORMAT_HHmm="HH:mm";
    public static final String DATE_FORMAT_HHmmss="HH:mm:ss";
    public static final String DATE_FORMAT_HHmm_ddMMyyyy ="HH:mm dd.MM.yyyy";
    public static final String DATE_FORMAT_ddMMyyyy ="dd.MM.yyyy";
    public static final String DATE_FORMAT_dd_MMMM_yyyy ="dd MMMM yyyy";
    public static final String DATE_FORMAT_ddMMyy ="dd.MM.yy";

    private TimesUtils() { }

    public static String UtcLongToStrLocal (Long date)
    {
        TimeZone loc=TimeZone.getDefault();
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT_HHmmss_ddMMyyyy);

        Date lDate=new Date(date+loc.getRawOffset());

        return formatter.format(lDate);
    }

    public static long localLongToUtcLong(Long local)
    {
        TimeZone loc=TimeZone.getDefault();
        Date lData=new Date(local-loc.getRawOffset());
        return lData.getTime();
    }

    public static Long stringToLong(String date, String format)
    {
        SimpleDateFormat sdf=new SimpleDateFormat(format);

        try {
            Date data=sdf.parse(date);
            return data.getTime();
        } catch (ParseException e) {
            Timber.tag("my").e(LoggingTree.getMessageForError(e,"TimesUtils stringToLong"));
        }

        return null;
    }

    public static String getNewFormatString(String data, String oldFormat, String newFormat) {
        DateFormat format = new SimpleDateFormat(oldFormat);
        Date date = null;
        try {
            date = format.parse(data);
        } catch (ParseException e) {
            Timber.tag("my").e(LoggingTree.getMessageForError(e,"getNewFormatString"));
        }
        assert date != null;
        long dataLong = date.getTime();

        SimpleDateFormat dateFormat = new SimpleDateFormat(newFormat);
        return dateFormat.format(new Date(dataLong));
    }

    public static long longToNewFormatLong(long date, String format)
    {
        DateFormat dateFormat=new SimpleDateFormat(format);

        Date d=new Date(date);

        String newD=dateFormat.format(d);
        try {
            d=dateFormat.parse(newD);
            return d.getTime();
        } catch (ParseException e) {
            Timber.tag("my").e(LoggingTree.getMessageForError(e,"            Timber.e(e,);\n"));
        }
        return 0;
    }

    public static String longToString(long date, String format)
    {
        DateFormat df=new SimpleDateFormat(format);

        Date d=new Date(date);

        return df.format(d);
    }


    public static Long getMillisFromDateString(String time) {
        SimpleDateFormat f = new SimpleDateFormat("HH:mm:ss dd.MM.yyyy", Locale.getDefault());
        SimpleDateFormat f2 = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        try {
            Date d = f.parse(time);
            String s=f2.format(d);
            Date d2=f2.parse(s);

            return d2.getTime();
        } catch (ParseException e) {
            Timber.tag("my").e(LoggingTree.getMessageForError(e,"getMillisFromDateString"));
            return 0L;
        }
    }

    public static Date stringToDate(String data) {
        DateFormat format = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        try {
            return format.parse(data);
        } catch (ParseException e) {
            Timber.tag("my").e(LoggingTree.getMessageForError(e,"stringToDate"));
        }
        return null;
    }

    public static String dateToString(Date data,String newFormat) {
        DateFormat format = new SimpleDateFormat(newFormat, Locale.getDefault());
        return format.format(data);
    }

    public static String getCurrentDate(String format)
    {
        long mill=System.currentTimeMillis();
        SimpleDateFormat dateFormat=new SimpleDateFormat(format, Locale.getDefault());
        Date date=new Date(mill);
        return dateFormat.format(date);
    }

    public static int compareWithCurrentTimeByDate(String data2)
    {
        long mill=System.currentTimeMillis();
        long dateClear=longToNewFormatLong(mill,DATE_FORMAT_ddMMyyyy);

        long date2Clear=stringToLong(data2,DATE_FORMAT_ddMMyyyy);

        if(dateClear>date2Clear)
            return -1;
        else if(dateClear<date2Clear)
            return 1;
        else
            return 0;
    }
}