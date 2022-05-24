package com.medhelp.medhelp.ui.news_about_the_application.pager;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.medhelp.medhelp.R;
import com.medhelp.medhelp.data.model.news.NewsResponse;
import com.medhelp.medhelp.data.pref.PreferencesManager;
import com.medhelp.medhelp.utils.workToFile.show_file.ShowFile2;

import java.io.File;
import java.util.Random;

public class PageFragment extends Fragment {

    static final String ARGUMENT_PAGE_NUMBER = "arg_page_number";
    int backColor;
    NewsResponse item;
    PreferencesManager preferencesHelper;
    Context context;
    ImageView image;

    static PageFragment newInstance(NewsResponse item) {
        PageFragment pageFragment = new PageFragment();
        Bundle arguments = new Bundle();

        arguments.putParcelable(ARGUMENT_PAGE_NUMBER, item);
        pageFragment.setArguments(arguments);
        return pageFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        item = getArguments().getParcelable(ARGUMENT_PAGE_NUMBER);

        Random rnd = new Random();
        backColor = Color.argb(40, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_view_fragment, null);

        TextView title = (TextView) view.findViewById(R.id.title);
        TextView text = (TextView) view.findViewById(R.id.text);
        ImageView image = (ImageView) view.findViewById(R.id.image);
        ConstraintLayout rootProgress= view.findViewById(R.id.rootProgress);

        context=image.getContext();
        preferencesHelper=new PreferencesManager(context);

        title.setText(item.getTitle());
        text.setText(item.getText());
        image.setVisibility(View.VISIBLE);
        //image.setImageResource(R.drawable.cat6);

        if(item.getImage()!=null)
        {
            //image.setBackgroundResource(item.getImage());
            new ShowFile2.BuilderImage(image.getContext())
                    .setType(ShowFile2.TYPE_IMAGE)
                    .load(item.getImage())
                    .token(preferencesHelper.getCurrentUserInfo().getApiKey())
                    .imgError(R.drawable.sh_doc)
                    .into(image)
                    .setListener(new ShowFile2.ShowListener() {
                        @Override
                        public void complete(File file) {
                            //Log.wtf("","");
                            rootProgress.setVisibility(View.GONE);
                            image.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void error(String error) {
                            Log.wtf("","");
                        }
                    })
                    .build();
        }
        else
        {
            image.setVisibility(View.GONE);
        }

        return view;
    }


}
