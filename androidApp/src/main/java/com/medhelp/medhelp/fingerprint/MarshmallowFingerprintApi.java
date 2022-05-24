package com.medhelp.medhelp.fingerprint;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.KeyguardManager;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


@SuppressLint("StaticFieldLeak")
final class MarshmallowFingerprintApi extends FingerprintApi {
    private static MarshmallowFingerprintApi instance;

    private final Activity activity;
    private CancellationSignal cancellationSignal; // used to cancel authorisation

    static synchronized MarshmallowFingerprintApi getInstance(@NonNull Activity activity) {
        if (instance == null) {
            instance = new MarshmallowFingerprintApi(activity);
        }

        return instance;
    }

    private MarshmallowFingerprintApi(Activity activity) {
        this.activity = activity;
    }

    @Override
    public boolean isFingerprintSupported() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            KeyguardManager keyguardManager = (KeyguardManager) activity.getSystemService(Activity.KEYGUARD_SERVICE);
            FingerprintManager fingerprintManager = null;

            fingerprintManager = (FingerprintManager) activity.getSystemService(Activity.FINGERPRINT_SERVICE);

            boolean hasPermission = ContextCompat.checkSelfPermission(activity, Manifest.permission.USE_FINGERPRINT) == PackageManager.PERMISSION_GRANTED;

            if (!hasPermission) {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.USE_FINGERPRINT}, PERMISSION_FINGERPRINT);
            }

            return hasPermission && keyguardManager != null && fingerprintManager != null &&
                    keyguardManager.isKeyguardSecure() && fingerprintManager.hasEnrolledFingerprints();
        }

        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void start(@NonNull Callback callback) {

        cancellationSignal = new CancellationSignal();
        FingerprintManager fingerprintManager = (FingerprintManager) activity.getSystemService(Activity.FINGERPRINT_SERVICE);

        if (fingerprintManager != null) {
            fingerprintManager.authenticate( null, cancellationSignal, 0, new MarshmallowFingerprintHandler(callback), null);
        }
    }

    @Override
    public void cancel() {
        if (cancellationSignal != null) {
            cancellationSignal.cancel();
            cancellationSignal = null;
        }
    }
}
