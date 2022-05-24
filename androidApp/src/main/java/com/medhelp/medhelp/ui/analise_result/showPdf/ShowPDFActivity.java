package com.medhelp.medhelp.ui.analise_result.showPdf;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import android.os.StrictMode;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.github.barteksc.pdfviewer.PDFView;
import com.medhelp.medhelp.R;
import com.medhelp.medhelp.utils.timber_log.LoggingTree;

import java.io.File;



import timber.log.Timber;

public class ShowPDFActivity extends AppCompatActivity {

    public final static String PATH_TO_FILE ="pathToFile";

    PDFView pdfView;
    Toolbar toolbar;
    CollapsingToolbarLayout toolbarLayout;
    SubsamplingScaleImageView image;

    private String pathToFile;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_pdf);

        context=this;

        Timber.tag("my").v("Открыл результат анализов");

        pdfView=findViewById(R.id.pdfView);
        toolbar=findViewById(R.id.toolbar);
        toolbarLayout=findViewById(R.id.collapsing_toolbar_service);
        image=findViewById(R.id.image);

        initToolbar();

        pathToFile =getIntent().getExtras().getString(PATH_TO_FILE);

        String expansion=getExspansion(pathToFile);


        switch(expansion)
        {
            case "pdf":
                openFile(pathToFile);
                break;
            case "jpg":
            case "jpeg":
                openImage(pathToFile);
                break;
            default:
                showAlertUnknown(expansion);
        }

    }

    private void openImage(String path)
    {
        pdfView.setVisibility(View.GONE);
        image.setVisibility(View.VISIBLE);

        image.setImage(ImageSource.uri(path));
    }


    private void openFile(String path)
    {
        image.setVisibility(View.GONE);
        pdfView.setVisibility(View.VISIBLE);

        File file=new File(path);

        pdfView.fromFile(file)
                .defaultPage(0)
                .onLoad(nbPages -> {
                    // Log.wtf("mLog","loadComplete nbPages "+nbPages);
                })
                .onError(t -> Timber.tag("my").e(LoggingTree.getMessageForError(t,"ShowPDFActivity$openFile ")))
                .load();
    }

    androidx.appcompat.app.AlertDialog dialog;
    private void showAlertUnknown(String expansion)
    {
        String str="Неизвестное расширение файла \""+expansion+"\"";

        LayoutInflater inflater= getLayoutInflater();
        View view=inflater.inflate(R.layout.dialog_2textview_btn,null);

        TextView title=view.findViewById(R.id.title);
        TextView text=view.findViewById(R.id.text);
        Button btnYes =view.findViewById(R.id.btnYes);
        Button btnNo =view.findViewById(R.id.btnNo);

        btnNo.setVisibility(View.GONE);

        title.setText(Html.fromHtml("<u>Ошибка!</u>"));
        text.setText(str);


        btnYes.setOnClickListener(v -> {
            dialog.cancel();

        });

        androidx.appcompat.app.AlertDialog.Builder builder=new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setView(view);
        dialog =builder.create();
        dialog.show();
    }

    private String getExspansion (String path)
    {
        int index=path.lastIndexOf(".");
        return path.substring(index+1).toLowerCase();
    }

    private void initToolbar()
    {
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        if (actionBar != null)
        {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back_white_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_share, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                super.onBackPressed();
                return true;

            case R.id.btnShare:
                //Log.wtf("mLog","btnShare");
                clickShare();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void clickShare()
    {
        File sharingFile = new File(pathToFile);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        Intent toSendMessage=new Intent(Intent.ACTION_SEND);
        toSendMessage.setType("application/pdf");
        //Uri uri=Uri.parse("file://"+pathToFile);
        Uri uri = FileProvider.getUriForFile(context, "com.medhelp.medhelp" + ".provider", sharingFile);
        toSendMessage.putExtra(Intent.EXTRA_STREAM,uri);
        startActivity(Intent.createChooser(toSendMessage,"MedHelper"));

//        val builder = VmPolicy.Builder()
//        StrictMode.setVmPolicy(builder.build())
//
//        val shareIntent = Intent(Intent.ACTION_SEND)
//        shareIntent.type = "application/pdf"
//
//        val outputPdfUri = FileProvider.getUriForFile(requireContext(), "com.medhelp.medhelp" + ".provider", file!!)
//
//        shareIntent.putExtra(Intent.EXTRA_STREAM, outputPdfUri)
//        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
//        shareIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
//
//        startActivity(Intent.createChooser(shareIntent, "MedHelper"))
    }

}
