package com.medhelp.medhelp.ui.splash.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.appcompat.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.medhelp.medhelp.R;
import com.medhelp.medhelp.fingerprint.FingerprintApi;


/**
 * Created by Evgeny Eliseyev on 12/01/2018.
 */

public class FingerprintDialog extends DialogFragment implements FingerprintApi.Callback {
    public interface Callback extends DialogInterface.OnClickListener {
        void onSuccess();
    }

    private Callback listener;
    private ImageView iconView;
    private TextView messageView;

    public static FingerprintDialog getInstance(@NonNull Callback listener) {
        FingerprintDialog dialog = new FingerprintDialog();
        dialog.listener = listener;
        return dialog;
    }

    @NonNull
    @Override
    @SuppressLint("InflateParams")
    @SuppressWarnings("ConstantConditions")
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Activity activity = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_fingerprint, null, false);

        iconView = view.findViewById(R.id.dialog_icon);
        messageView = view.findViewById(R.id.dialog_message);

        builder.setView(view)
            .setTitle(R.string.app_name)
            .setNegativeButton(android.R.string.cancel, listener);

        setCancelable(false);
        return builder.create();
    }

    @Override
    public void onSuccess(String publicKey) {
        if (!isActive()) {
            return;
        }

        iconView.setImageResource(R.drawable.ic_fingerprint_success);
        messageView.setText(R.string.auth_success);
        listener.onSuccess();
        dismissWithDelay();
    }

    @Override
    public void onFailure() {
        if (!isActive()) {
            return;
        }

        iconView.setImageResource(R.drawable.ic_fingerprint_failure);
        messageView.setText(R.string.auth_failure);
        clearFailure();
    }

    @Override
    public void onError(int errorCode) {
        if (!isActive()) {
            return;
        }

        iconView.setImageResource(R.drawable.ic_fingerprint_failure);
        messageView.setText(getString(R.string.auth_error, errorCode));

        dismissWithDelay();
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private boolean isActive() {
        return getActivity() != null && !isRemoving() && isAdded();
    }

    private void dismissWithDelay() {
        new Handler().postDelayed(() -> {
            FragmentActivity ddd=getActivity();
            if(ddd !=null)
                dismiss();
        }, 1000);
    }

    private void clearFailure()
    {
        new Handler().postDelayed(() -> {
            iconView.setImageResource(R.drawable.ic_fingerprint);
            messageView.setText(R.string.dialog_start_scanning_hint);
        },500);
    }


}
