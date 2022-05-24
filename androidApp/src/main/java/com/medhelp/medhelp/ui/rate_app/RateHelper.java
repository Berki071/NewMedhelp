package com.medhelp.medhelp.ui.rate_app;


public interface RateHelper {

    interface View  {
        void openPlayStoreForRating();

        void showPlayStoreRatingView(boolean show);

        void showRatingMessageView(boolean show);

        void dismissDialog();

        void dismissDialog(String tag);

        void showMessage(String message);
        void showMessage(int resId);

        void showError(String message);

    }

    interface Presenter {
        void onRatingSubmitted(float rating, String message);

        void onCancelClicked();

        void onLaterClicked();

        void onPlayStoreRatingClicked(final float rating);
    }
}
