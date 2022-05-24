package com.medhelp.medhelp.ui.rate_app;

import android.content.Context;

import com.medhelp.medhelp.R;
import com.medhelp.medhelp.data.model.DateResponse;
import com.medhelp.medhelp.data.network.NetworkManager;
import com.medhelp.medhelp.data.pref.PreferencesManager;
import com.medhelp.medhelp.utils.timber_log.LoggingTree;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class RatePresenter implements RateHelper.Presenter {
    private boolean isRatingSecondaryActionShown = false;

    Context context;
    RateHelper.View viewHelper;

    PreferencesManager prefManager;
    NetworkManager networkManager;

    public RatePresenter (Context context, RateHelper.View viewHelper)
    {
        this.context=context;

        this.viewHelper=viewHelper;

        prefManager=new PreferencesManager(context);
        networkManager=new NetworkManager(prefManager);
    }

    @Override
    public void onRatingSubmitted(final float rating, String message) {

        if (rating == 0) {
            viewHelper.showMessage(R.string.rating_not_provided_error);
            return;
        }

        String msg=testMessage(message);

        sendRating(rating, msg);
        viewHelper.showMessage(R.string.rating_thanks);
        viewHelper.dismissDialog();
    }

    private String testMessage(String msg)
    {
        String s= "+";

        if(msg.length()==0)
        {
            return s;
        }

        try {
            s= URLEncoder.encode(msg,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            Timber.tag("my").e(LoggingTree.getMessageForError(e,"RatePresenter$testMessage "));
        }

        return s;
    }


    @Override
    public void onCancelClicked() {
        viewHelper.dismissDialog();
    }

    @Override
    public void onLaterClicked() {
        viewHelper.dismissDialog();
    }

    @Override
    public void onPlayStoreRatingClicked(final float rating) {
        sendRating(rating,"+");
        viewHelper.openPlayStoreForRating();
        viewHelper.dismissDialog();
    }

    private void sendRating(float rating, String message) {

        if (prefManager.getCurrentRatingApp() == rating)
            return;
        else
            prefManager.setCurrentRatingApp(rating);


        CompositeDisposable compositeDisposable = new CompositeDisposable();

        compositeDisposable.add(networkManager
                .getCurrentDateApiCall()
                .concatMap(resp -> {
                    DateResponse date = resp.getResponse();
                    return networkManager.sendRating(date.getToday(), rating, message);
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response ->
                        {
                            String s = "d";
                            compositeDisposable.dispose();
                        },

                        throwable -> {
                            Timber.tag("my").e(LoggingTree.getMessageForError(throwable, "RatePresenter$sendRating "));
                            viewHelper.showError("Error send rating");
                            compositeDisposable.dispose();
                        }));
    }
}
