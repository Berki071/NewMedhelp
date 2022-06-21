//package com.medhelp.medhelp.ui.rate_app;
//
//import android.graphics.PorterDuff;
//import android.graphics.drawable.LayerDrawable;
//import android.os.Bundle;
//import androidx.annotation.NonNull;
//import androidx.fragment.app.FragmentManager;
//import androidx.core.content.ContextCompat;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.RatingBar;
//import com.medhelp.medhelp.R;
//import com.medhelp.medhelp.ui.base.BaseDialog;
//import com.medhelp.medhelp.utils.main.PlayStoreUtils;
//
//public class RateFragment extends BaseDialog implements RateHelper.View {
//
//    private static final String TAG = "RateFragment";
//
//    RatePresenter presenter;
//
//    RatingBar ratingBar;
//    EditText message;
//    View ratingMessageView;
//    View playStoreRatingView;
//    Button submitButton;
//    Button btn_later;
//    Button btn_rate_on_play_store;
//
//
//    public static RateFragment newInstance() {
//        RateFragment fragment = new RateFragment();
//        Bundle bundle = new Bundle();
//        fragment.setArguments(bundle);
//        return fragment;
//    }
//
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_rate, container, false);
//        initValue(view);
//
//        presenter=new RatePresenter(getContext(),this);
//        return view;
//    }
//    private void initValue(View v){
//        ratingBar=v.findViewById(R.id.rating_bar_feedback);
//        message=v.findViewById(R.id.et_message);
//        ratingMessageView=v.findViewById(R.id.view_rating_message);
//        playStoreRatingView=v.findViewById(R.id.view_play_store_rating);
//        submitButton=v.findViewById(R.id.btn_submit);
//        btn_later=v.findViewById(R.id.btn_later);
//        btn_rate_on_play_store=v.findViewById(R.id.btn_rate_on_play_store);
//
//        btn_later.setOnClickListener(c -> {
//            presenter.onLaterClicked();
//        });
//        btn_rate_on_play_store.setOnClickListener(c -> {
//            presenter.onPlayStoreRatingClicked(ratingBar.getRating());
//        });
//    }
//
//    public void show(FragmentManager fragmentManager) {
//        super.show(fragmentManager, TAG);
//    }
//
//
//    public void openPlayStoreForRating() {
//        PlayStoreUtils.openPlayStoreForApp(getContext());
//    }
//
//
//    public void showPlayStoreRatingView(boolean show) {
//        if(show)
//          playStoreRatingView.setVisibility(View.VISIBLE);
//        else
//            playStoreRatingView.setVisibility(View.GONE);
//    }
//
//    @Override
//    public void showRatingMessageView(boolean show) {
//        if(show)
//            ratingMessageView.setVisibility(View.VISIBLE);
//        else
//            ratingMessageView.setVisibility(View.GONE);
//    }
//
//    @Override
//    protected void setUp(View view) {
//        ratingMessageView.setVisibility(View.GONE);
//        playStoreRatingView.setVisibility(View.GONE);
//
//        LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
//        stars.getDrawable(0).setColorFilter(ContextCompat.getColor(getContext(), R.color.light_gray), PorterDuff.Mode.SRC_ATOP);
//        stars.getDrawable(1)
//                .setColorFilter(ContextCompat.getColor(getContext(), R.color.light_gray), PorterDuff.Mode.SRC_ATOP);
//        stars.getDrawable(2)
//                .setColorFilter(ContextCompat.getColor(getContext(), R.color.yellow), PorterDuff.Mode.SRC_ATOP);
//
//        ratingBar.setRating(presenter.prefManager.getCurrentRatingApp());
//
//
//        submitButton.setOnClickListener(v ->
//                presenter.onRatingSubmitted(ratingBar.getRating(), message.getText().toString()));
//
//        ratingBar.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
//            int r = ((int)rating);
//
//            if((float)r!=rating) {
//                //Log.wtf("mLog", "rating " + rating + " r " + r);
//                ratingBar.setRating((float) r+1);
//            }
//            else
//            {
//                if (rating == 5) {
//                    showPlayStoreRatingView(true);
//                    showRatingMessageView(false);
//                } else if(rating!=0) {
//                    showPlayStoreRatingView(false);
//                    showRatingMessageView(true);
//                }else
//                {
//                    showPlayStoreRatingView(false);
//                    showRatingMessageView(false);
//                }
//            }
//        });
//    }
//
//
//    @Override
//    public void dismissDialog() {
//        super.dismissDialog(TAG);
//    }
//
//    @Override
//    public void userRefresh() {
//    }
//}