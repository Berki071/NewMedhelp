package com.medhelp.medhelp.ui.doctor.alertDoc;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.medhelp.medhelp.Constants;
import com.medhelp.medhelp.R;
import com.medhelp.medhelp.data.model.AllDoctorsResponse;
import com.medhelp.medhelp.data.pref.PreferencesManager;
import com.medhelp.medhelp.ui.doctor.service_activity.ServiceActivity;
import com.medhelp.medhelp.utils.workToFile.show_file.ShowFile2;
import java.io.File;
import java.util.prefs.Preferences;

import static com.medhelp.medhelp.ui.schedule.ScheduleFragment.EXTRA_BACK_PAGE;

public class AlertCardDoctor {
    ImageView doc_info_image;
    TextView doc_info_name;
    TextView doc_info_exp;
    TextView doc_info_spec;
    TextView doc_info_info;
    Button doc_info_btn_close;
    Button doc_info_btn_record;

    private Context context;
    private AllDoctorsResponse doc;
    private AlertDialog alertDialog;

    private int idService;
    private String token;

    private PreferencesManager preferences;

    public AlertCardDoctor(Context context, AllDoctorsResponse doc, int idService,String token)
    {
        this.context=context;
        this.doc=doc;
        this.idService=idService;
        this.token=token;

        preferences=new PreferencesManager(context);
        creteDialog();
    }

    private void creteDialog()
    {


        LayoutInflater layoutInflater=LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.fragment_doctor_details, null);

        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setView(view);
        alertDialog=builder.create();
        alertDialog.show();

        initValue(view);

        doc_info_info.setMovementMethod(new ScrollingMovementMethod());

        doc_info_name.setText(doc.getFio_doctor());
        doc_info_exp.setText(doc.getExperience());
        doc_info_spec.setText(doc.getName_specialties());
        doc_info_info.setText(doc.getDop_info());

        if(doc_info_image!=null) {

            new ShowFile2.BuilderImage(context)
                    .setType(ShowFile2.TYPE_ICO)
                    .load(doc.getImage_url())
                    .token(token)
                    .imgError(R.drawable.sh_doc)
                    .into(doc_info_image)
                    .setListener(new ShowFile2.ShowListener() {
                        @Override
                        public void complete(File file) {
                        }

                        @Override
                        public void error(String error) {
                        }
                    })
                    .build();

        }
    }

    private void initValue(View view){
        doc_info_image= view.findViewById(R.id.doc_info_image);
        doc_info_name= view.findViewById(R.id.doc_info_name);
        doc_info_exp= view.findViewById(R.id.doc_info_exp);
        doc_info_spec= view.findViewById(R.id.doc_info_spec);
        doc_info_info= view.findViewById(R.id.doc_info_info);
        doc_info_btn_close= view.findViewById(R.id.doc_info_btn_close);
        doc_info_btn_record= view.findViewById(R.id.doc_info_btn_record);

        if(preferences.getCenterInfo().getButton_zapis()==0)
            doc_info_btn_record.setVisibility(View.GONE);

        doc_info_btn_close.setOnClickListener(c -> {
            alertDialog.cancel();
        });

        doc_info_btn_record.setOnClickListener(c -> {
            showServiceActivity(doc.getId_doctor());
            alertDialog.cancel();
        });
    }


    public void showServiceActivity(int idDoctor)
    {
        if (idDoctor != 0)
        {
            Intent intent = ServiceActivity.getStartIntent(context);
            intent.putExtra(ServiceActivity.EXTRA_DATA_ID_DOCTOR, idDoctor);
            intent.putExtra(ServiceActivity.EXTRA_DATA_SERVICE,idService);
            intent.putExtra(EXTRA_BACK_PAGE, Constants.MENU_STAFF);
            context.startActivity(intent);
        }
    }

}
