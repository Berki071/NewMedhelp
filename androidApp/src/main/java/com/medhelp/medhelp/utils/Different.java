package com.medhelp.medhelp.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.util.PatternsCompat;
import com.medhelp.medhelp.R;

import java.util.Calendar;
import java.util.regex.Pattern;

public class Different {

    public static boolean isValidEmail(String target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            Pattern pp=android.util.Patterns.EMAIL_ADDRESS;
            return PatternsCompat.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    //region phone number
    public static String getClearSmallFormatMobilePhone(String ph) {
        if(ph.length()<3)
            throw  new IllegalArgumentException ("Different.getClearSmallFormatMobilePhone размер менее 3 символов " + ph);

        ph = ph.replaceAll(" ", "");

        if(ph.startsWith("+7("))
            ph = ph.substring(3);
        else if(ph.startsWith("+7"))
            ph = ph.substring(2);
        else if(ph.length()>10 && ph.startsWith("8"))
            ph = ph.substring(1);

        ph = ph.replaceAll("\\+", "");
        ph = ph.replaceAll("\\(", "");
        ph = ph.replaceAll("\\)", "");
        ph = ph.replaceAll("-", "");
        ph = ph.replaceAll("_", "");

        return ph;
    }
    public static String getLongFormatMobilePhoneWithPlusFromShort(String phone)
    {
        //9889998877->+7(988)888-88-88
        return "+7("+phone.substring(0,3)+") "+phone.substring(3,6)+"-"+phone.substring(6,8)+"-"+phone.substring(8);
    }
    public static String formatPhone(String ph){
        //9889998877->+7(988)888-88-88
        return "+7("+ph.substring(0,3)+")"+ph.substring(3);
    }
    public static String queryPhone(String number, Activity activity) {
        String name = "";
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));
        Cursor cursor = activity.getContentResolver().query(uri, new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME}, null, null, null);
        if (cursor.moveToFirst()) {
            name = cursor.getString(0);
        }
        cursor.close();
        return name;
    }
    //endregion

    //region converting dp and sp
    public static float convertPixelsToDp(float px, Context context) {
        return px / ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }
    public static float dpToPixel(float dp, Context context) {
        return dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }
    public static int spToPx(float sp, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
    }
    //endregion dp

    //region progress alert

    static AlertDialog progressDialog;
    static Long timeStartProgressDialog;

    public static boolean showLoadingDialog(Context context) {
        if ((progressDialog == null || (progressDialog != null && !progressDialog.isShowing())) && !((Activity) context).isFinishing()) {
            timeStartProgressDialog = Calendar.getInstance().getTimeInMillis();

            View view = ((Activity) context).getLayoutInflater().inflate(R.layout.dialog_progress, null);
            AlertDialog.Builder builder = new AlertDialog.Builder(context/*, R.style.DialogTheme*/);
            builder.setView(view);
            progressDialog = builder.create();
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            return true;
        }
        return false;
    }

    static Handler handler;
    static Runnable runnable;
    public static void hideLoadingDialog(Context context) {
        try {
            long tmp = Calendar.getInstance().getTimeInMillis();
            long tmp2 = tmp - timeStartProgressDialog;
            if (tmp2 < 800 && tmp2 > 80) {
                handler = new Handler();
                runnable = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            progressDialog.dismiss();
                            progressDialog = null;
                        } catch (Exception e) {

                        }
                    }
                };
                handler.postDelayed(runnable, tmp2);
            } else {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                    progressDialog = null;
                }
            }
        }catch (Exception e){}
    }


    public static void hideLoadingDialog(int deley) {
        long tmp = Calendar.getInstance().getTimeInMillis();
        long tmp2 = tmp - timeStartProgressDialog;
        if (tmp2 < deley) {
            handler = new Handler();
            runnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        progressDialog.dismiss();
                        progressDialog=null;
                    } catch (Exception e) {
                    }
                }
            };
            handler.postDelayed(runnable, tmp2);
        } else {
            {
                if(progressDialog!=null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                     progressDialog=null;
                }
            }
        }
    }
    //endregion

    //region info alert
    static AlertDialog alertInfo;
    public static void showAlertInfo(Activity activity, String titleToolText, String msgText)
    {
        showAlertInfo(activity,titleToolText,msgText,null,false);
    }

    public static void showAlertInfo(Activity activity, String titleToolText, String msgText,AlertInfoListener listener, boolean showCloseBtn) {
        showAlertInfo(activity,titleToolText,msgText,listener,showCloseBtn,"Закрыть","Ok");
    }

    public static void showAlertInfo(Activity activity, String titleToolText, String msgText,AlertInfoListener listener, boolean showCloseBtn,
                                     String btnNoName, String btnYesName) {
        if(alertInfo!=null)
        {
            alertInfo.dismiss();
            alertInfo=null;
        }

        LayoutInflater inflater= activity.getLayoutInflater();
        View view=inflater.inflate(R.layout.dialog_2textview_btn2,null);

        Toolbar toolbar=view.findViewById(R.id.toolbarD);
        TextView titleTool=view.findViewById(R.id.titleToolbar);

        ConstraintLayout mainBox=view.findViewById(R.id.mainBox);
        TextView text=view.findViewById(R.id.text);
        Button btnYes =view.findViewById(R.id.btnYes);
        Button btnNo =view.findViewById(R.id.btnNo);

        if(showCloseBtn) {
            btnNo.setVisibility(View.VISIBLE);
            btnNo.setOnClickListener(v -> {
                if(listener!=null)
                    listener.clickNo();
                alertInfo.dismiss();
            });
        }else{
            btnNo.setVisibility(View.GONE);
        }

        //btnYes.setText("Ok");
        btnYes.setText(btnYesName);
        btnNo.setText(btnNoName);

        if(titleToolText==null)
            titleTool.setVisibility(View.GONE);
        else {
            titleTool.setVisibility(View.VISIBLE);
            titleTool.setText(titleToolText);
        }

        if(msgText==null)
            text.setVisibility(View.GONE);
        else {
            text.setVisibility(View.VISIBLE);
            text.setText(msgText);
        }

        btnYes.setOnClickListener(v -> {
            alertInfo.dismiss();
            if(listener!=null)
                listener.clickYes();
        });

        AlertDialog.Builder builder=new AlertDialog.Builder(activity);
        builder.setView(view);

        alertInfo=builder.create();
        alertInfo.getWindow().setBackgroundDrawableResource(R.color.transparent);

        int forWidthPix= activity.getWindow().getDecorView().getWidth() -(((int) Different.dpToPixel(16,activity)) * 2);
        alertInfo.show();
        if (activity.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE)
            alertInfo.getWindow().setLayout(forWidthPix, ViewGroup.LayoutParams.WRAP_CONTENT);
        else
            alertInfo.getWindow().setLayout((int) Different.dpToPixel(400,activity), ViewGroup.LayoutParams.WRAP_CONTENT);

        alertInfo.getWindow().setBackgroundDrawableResource(R.color.transparent);

        alertInfo.setCanceledOnTouchOutside(true);
        alertInfo.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if(listener!=null)
                    listener.clickNo();
            }
        });
    }

    public interface AlertInfoListener{
        void clickYes();
        void clickNo();
    }
    //endregion

}
