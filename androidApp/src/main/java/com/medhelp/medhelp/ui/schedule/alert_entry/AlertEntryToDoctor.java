package com.medhelp.medhelp.ui.schedule.alert_entry;

import android.content.Context;
import androidx.appcompat.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.medhelp.medhelp.R;
import com.medhelp.medhelp.data.network.NetworkManager;
import com.medhelp.medhelp.data.pref.PreferencesManager;
import com.medhelp.medhelp.ui.view.carouselWithUsers.MyCarousel;
import com.medhelp.medhelp.utils.timber_log.LoggingTree;
import com.medhelp.shared.model.UserResponse;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class AlertEntryToDoctor {
    AlertDialog alertDialog;

    Context context;
    AlertEntryToDoctor.mOnClickListener listener;

    TextView tvDoctor;
    TextView tvYsl;
    TextView tvDate;
    TextView tvTime;
    Button btnYes;
    Button btnNo;

    MyCarousel myCarousel;

    PreferencesManager preferencesHelper;
    NetworkManager networkHelper;

    int idSelectBranch;
    String idSelectUser;


    public AlertEntryToDoctor(Context context, AlertEntryToDoctor.mOnClickListener listener, String nameDoctor, String ysl, String date, String time,  String idSelectUser, int idSelectBranch){
        this.context = context;
        this.listener=listener;
        this.idSelectBranch=idSelectBranch;
        this.idSelectUser=idSelectUser;

        preferencesHelper=new PreferencesManager(this.context);
        networkHelper=new NetworkManager(preferencesHelper);

        LayoutInflater li= LayoutInflater.from(context);
        View view=li.inflate(R.layout.dialog_entry_to_doctor,null);

        myCarousel=view.findViewById(R.id.mView);
        myCarousel.setListener(new MyCarousel.MyCarouselListener() {
            @Override
            public void selectedUser(UserResponse user) {
               // Log.wtf("mLog","selectedUser "+user.getSurname());
                //Toast.makeText(context,"selectedUser "+user.getSurname() ,Toast.LENGTH_SHORT).show();
                if(user!=null)
                {
                    testSelectedUser(user);
                }
            }

        });
        myCarousel.setList(preferencesHelper.getUsersLogin(),preferencesHelper.getCurrentUserInfo());


        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setView(view);

        alertDialog=builder.create();
        alertDialog.show();

        initValue(view);

        tvDoctor.setText(nameDoctor);
        tvYsl.setText(ysl);
        tvDate.setText(date);
        tvTime.setText(time);
    }
    private void initValue(View v){
        tvDoctor=v.findViewById(R.id.doctorName);
        tvYsl=v.findViewById(R.id.ysl);
        tvDate=v.findViewById(R.id.recordingDate);
        tvTime=v.findViewById(R.id.recordingTime);
        btnYes=v.findViewById(R.id.btnYes);
        btnNo=v.findViewById(R.id.btnNo);

        btnYes.setOnClickListener(c -> {
            alertDialog.cancel();
            clickBtnYes();
        });

        btnNo.setOnClickListener(c -> {
            alertDialog.cancel();
        });
    }

    private void testSelectedUser(UserResponse use)
    {
        UserResponse currentUser=preferencesHelper.getCurrentUserInfo();

        if(idSelectBranch!=use.getIdBranch())
        {
            selectedNewHospitalBranch(use,idSelectBranch);
        }
        else
        {
            idSelectUser=String.valueOf(use.getIdUser());
        }
    }

    private void clickBtnYes()
    {
        listener.positiveClickButton(idSelectUser,idSelectBranch);
    }


    private void lockButtons(boolean boo)
    {
        if(boo)
        {
            btnNo.setClickable(false);
            btnYes.setClickable(false);
        }
        else
        {
            btnNo.setClickable(true);
            btnYes.setClickable(true);
        }
    }


    public void selectedNewHospitalBranch(UserResponse use, int newBranch) {
        myCarousel.setVisibleLoading(true);
        lockButtons(true);

        int oUser=use.getIdUser();
        int oBranch=use.getIdBranch();

        CompositeDisposable cd = new CompositeDisposable();
        cd.add(networkHelper
                .sendNewFavoriteHospitalBranch(oUser, oBranch, newBranch)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {

                    if(idSelectUser!=null && myCarousel!=null) {
                        idSelectUser = response.getResponse();
                        myCarousel.setVisibleLoading(false);
                    }

                    if(btnNo!=null)
                    {
                        lockButtons(false);
                    }

                    cd.dispose();
                }, throwable -> {
                    Timber.tag("my").e(LoggingTree.getMessageForError(throwable,"AlertEntryToDoctor/selectedNewHospitalBranch "));
                    Toast.makeText(context,R.string.api_default_error,Toast.LENGTH_LONG);
                    myCarousel.setVisibleLoading(false);
                    if(btnNo!=null)
                    {
                        lockButtons(false);
                    }
                    cd.dispose();
                }));
    }


    public interface mOnClickListener{
        void positiveClickButton(String idUser, int idBranch);
    }
}
