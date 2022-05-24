package com.medhelp.medhelp.utils.main;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.CursorLoader;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.graphics.drawable.shapes.Shape;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Html;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.medhelp.medhelp.R;
import com.medhelp.medhelp.data.model.BonusesItem;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.List;
import java.util.Random;


public final class MainUtils {
    public static final String IMAGE="image";
    public static final String FILE="file";
    public static final String TEXT="text";
    private static AlertDialog alertDialogConfirmComing;

    private static ProgressDialog dialog;


    private MainUtils() {
    }


    public static void showLoading(Context context) {
        hideLoading();
        if(context!=null)
            dialog = showLoadingDialog(context);
    }
    public static boolean isLoading() {
        return dialog != null && dialog.isShowing();
    }
    public static void hideLoading() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    public static ProgressDialog showLoadingDialog(Context context) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.show();
        if (progressDialog.getWindow() != null) {
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        progressDialog.setContentView(R.layout.dialog_progress);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        return progressDialog;
    }

    @SuppressLint("all")
    public static String getDeviceId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }


    public static String loadJsonFromAsset(Context context, String jsonFileName) throws IOException {
        AssetManager manager = context.getAssets();
        InputStream is = manager.open(jsonFileName);

        int size = is.available();
        byte[] buffer = new byte[size];
        is.read(buffer);
        is.close();

        return new String(buffer, StandardCharsets.UTF_8);
    }

    public static int dpToPx(Context context ,int num)
    {
        Resources r = context.getResources();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, num, r.getDisplayMetrics());
    }

    public static int dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dpValue * scale + 0.5F);
    }

    public static ShapeDrawable generateCornerShapeDrawable(int color, int topLeftCorner, int topRightCorner, int bottomRightCorner, int bottomLeftCorner) {
        Shape shape = new RoundRectShape(new float[]{(float)topLeftCorner, (float)topLeftCorner, (float)topRightCorner, (float)topRightCorner, (float)bottomRightCorner, (float)bottomRightCorner, (float)bottomLeftCorner, (float)bottomLeftCorner}, null, null);
        ShapeDrawable sd = new ShapeDrawable(shape);
        sd.getPaint().setColor(color);
        sd.getPaint().setStyle(Paint.Style.FILL);
        return sd;
    }

//    public static int convertTypeStringToInt(String t)
//    {
//        switch(t)
//        {
//            case TEXT:
//                return Message.MSG;
//
//            case IMAGE:
//                return Message.Img;
//
//            case FILE:
//                return Message.FILE;
//
//            default:
//                return Message.MSG;
//        }
//    }

//    public static String convertTypeIntToString(int t)
//    {
//        switch(t)
//        {
//            case Message.MSG:
//                return TEXT;
//
//            case Message.Img:
//                return IMAGE;
//
//            case Message.FILE:
//                return FILE;
//
//            default:
//                return TEXT;
//        }
//    }

    public static String getEncodeKey(String word){
        String tmp="";
        Random generator = new Random();

        for(int i=0; i<word.length(); i++){
            if(word.charAt(i)>255)
                tmp+=(char)(generator.nextInt(4095));
            else
                tmp+=(char)(generator.nextInt(255));
        }

        return tmp;
    }
    public static String encodeDecodeWord(String word, String key){
        String encodeStr="";
        for (int i = 0; i < word.length(); i++)
            encodeStr += (char)(word.charAt(i) ^ key.charAt(i));

        return encodeStr;
    }

    public static String getRealPathFromURI(Uri contentUri, Context context) {
        String[] proj = { MediaStore.Images.Media.DATA };

        CursorLoader cursorLoader = new CursorLoader(context, contentUri, proj, null, null, null);
        Cursor cursor = cursorLoader.loadInBackground();

        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }


    public static void showAlertInfo(Context context,String titleMsg, String msg){


        LayoutInflater inflater= LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.dialog_2textview_btn,null);

        TextView title=view.findViewById(R.id.title);
        TextView text=view.findViewById(R.id.text);
        Button btnYes =view.findViewById(R.id.btnYes);
        Button btnNo =view.findViewById(R.id.btnNo);
        ImageView img=view.findViewById(R.id.img);

        //img.setVisibility(View.GONE);
        title.setVisibility(View.VISIBLE);
        btnNo.setVisibility(View.GONE);

        title.setText(titleMsg);
        text.setGravity(Gravity.CENTER);
        text.setText(Html.fromHtml(msg));

        btnYes.setOnClickListener(v -> {
            alertDialogConfirmComing.dismiss();
        });

        androidx.appcompat.app.AlertDialog.Builder builder=new androidx.appcompat.app.AlertDialog.Builder(context);
        builder.setView(view);
        alertDialogConfirmComing =builder.create();
        //alertDialogConfirmComing.setCanceledOnTouchOutside(false);
        alertDialogConfirmComing.show();
    }

    public static boolean isValidEmail(String target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    static AlertDialog progressDialog;
    static Long timeStartProgressDialog;

    public static boolean showLoadingDialog2(Context context) {
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

    public static void hideLoadingDialog2(Context context) {
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


    public static void hideLoadingDialog2(int deley) {
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

    public static String getSumBonuses(List<BonusesItem> list) {
        int sum = 0;

        for (BonusesItem item : list) {
            if (item.getStatus().equals("popoln"))
                sum += item.getValue();
            else if (item.getStatus().equals("snyatie"))
                sum -= item.getValue();
        }

        return String.valueOf(sum);
    }

}
