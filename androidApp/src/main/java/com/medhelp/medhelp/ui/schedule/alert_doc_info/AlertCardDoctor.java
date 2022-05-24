package com.medhelp.medhelp.ui.schedule.alert_doc_info;

import android.app.AlertDialog;
import android.content.Context;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.medhelp.medhelp.R;
import com.medhelp.medhelp.data.model.Doctor;
import com.medhelp.medhelp.utils.workToFile.show_file.ShowFile2;
import java.io.File;

public class AlertCardDoctor {
    ImageView doc_info_image;
    TextView doc_info_name;
    TextView doc_info_exp;
    TextView doc_info_spec;
    TextView doc_info_info;
    TextView doc_info_btn_close;
    TextView doc_info_btn_record;
    TextView btn_ok;

    private Context context;
    private Doctor doc;
    private AlertDialog alertDialog;
    private String token;


    public AlertCardDoctor(Context context, Doctor doc, String token)
    {
        this.context=context;
        this.doc=doc;

        this.token=token;

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

        doc_info_btn_close.setVisibility(View.GONE);
        doc_info_btn_record.setVisibility(View.GONE);
        btn_ok.setVisibility(View.VISIBLE);


        doc_info_info.setMovementMethod(new ScrollingMovementMethod());

        doc_info_name.setText(doc.getFullName());
        doc_info_exp.setText(doc.getExperience());
        doc_info_spec.setText(doc.getSpecialty());
        doc_info_info.setText(doc.getDop_info());

        if(doc_info_image!=null) {

            new ShowFile2.BuilderImage(context)
                    .setType(ShowFile2.TYPE_ICO)
                    .load(doc.getPhoto())
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
    private void initValue(View v){
        doc_info_image=v.findViewById(R.id.doc_info_image);
        doc_info_name=v.findViewById(R.id.doc_info_name);
        doc_info_exp=v.findViewById(R.id.doc_info_exp);
        doc_info_spec=v.findViewById(R.id.doc_info_spec);
        doc_info_info=v.findViewById(R.id.doc_info_info);
        doc_info_btn_close=v.findViewById(R.id.doc_info_btn_close);
        doc_info_btn_record=v.findViewById(R.id.doc_info_btn_record);
        btn_ok=v.findViewById(R.id.btn_ok);

        btn_ok.setOnClickListener(c -> {
            if (c.getId() == R.id.btn_ok) {
                alertDialog.cancel();
            }
        });
    }
}
