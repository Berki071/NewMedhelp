package com.medhelp.medhelp.fingerprint;

import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.M)
@SuppressWarnings("unused")
class MarshmallowFingerprintHandler extends FingerprintManager.AuthenticationCallback {
    private final FingerprintApi.Callback callback;

    MarshmallowFingerprintHandler(FingerprintApi.Callback callback) {
        this.callback = callback;
    }

    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        callback.onSuccess(/*CryptoManager.getInstance().getPublicKey()*/ null);
    }

    @Override
    public void onAuthenticationFailed() {
        callback.onFailure();
    }

    @Override
    public void onAuthenticationError(int errorCode, CharSequence errorString) {
        if (errorCode != FingerprintManager.FINGERPRINT_ERROR_USER_CANCELED) {
            callback.onError(errorCode);
        }
    }
}
