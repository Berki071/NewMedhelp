package com.medhelp.medhelp.utils.main;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.medhelp.medhelp.R;
import com.medhelp.medhelp.utils.timber_log.LoggingTree;

import timber.log.Timber;

public final class PlayStoreUtils {

    private PlayStoreUtils() {
    }

    public static void openPlayStoreForApp(Context context) {
        final String appPackageName = context.getPackageName();
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse(context
                            .getResources()
                            .getString(R.string.google_market_link) + appPackageName)));
        } catch (android.content.ActivityNotFoundException e) {
            Timber.tag("my").e(LoggingTree.getMessageForError(e,"PlayStoreUtils"));
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(context
                            .getResources()
                            .getString(R.string.google_play_store_link) + appPackageName)));
        }
    }
}
