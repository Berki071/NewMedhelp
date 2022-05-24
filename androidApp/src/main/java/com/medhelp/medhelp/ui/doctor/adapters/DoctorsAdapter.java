package com.medhelp.medhelp.ui.doctor.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.medhelp.medhelp.R;
import com.medhelp.medhelp.data.model.AllDoctorsResponse;
import com.medhelp.medhelp.data.pref.PreferencesManager;
import com.medhelp.medhelp.ui.base.BaseViewHolder;
import com.medhelp.medhelp.utils.workToFile.show_file.ShowFile2;

import java.io.File;
import java.util.ArrayList;
import java.util.List;




public class DoctorsAdapter extends RecyclerView.Adapter<BaseViewHolder>
{
    private static final int VIEW_TYPE_NORMAL = 11;
    private List<AllDoctorsResponse> response;

    public DoctorsAdapter(List<AllDoctorsResponse> response)
    {
        this.response = response;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position)
    {
        holder.onBind(position);
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        if (viewType == VIEW_TYPE_NORMAL) {
            return new ViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_doctor, parent, false));
        }
        return new EmptyViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_error_download, parent, false));
    }

    @Override
    public int getItemViewType(int position)
    {
        if (response != null && response.size() > 0)
        {
            return VIEW_TYPE_NORMAL;
        }
        return 0;
    }

    @Override
    public int getItemCount()
    {
        if (response != null && response.size() > 0)
        {
            return response.size();
        } else
        {
            return 0;
        }
    }

    @SuppressWarnings("unused")
    void addItems(List<AllDoctorsResponse> repoList)
    {
        response.clear();
        response.addAll(repoList);
        notifyDataSetChanged();
    }

    public AllDoctorsResponse getItem(int num)
    {
        return response.get(num);
    }




    class ViewHolder extends BaseViewHolder
    {
        TextView docName;
        TextView docExp;
        ImageView docImage;

        String token;

        ViewHolder(View itemView)
        {
            super(itemView);

            docName= itemView.findViewById(R.id.doc_name_tv);
            docExp= itemView.findViewById(R.id.doc_spec_tv);
            docImage= itemView.findViewById(R.id.doc_img);

            Context context= itemView.getContext();
            PreferencesManager ph=new PreferencesManager(context);
            token=ph.getCurrentUserInfo().getApiKey();
        }

        protected void clear()
        {
            docImage.setImageDrawable(null);
            docName.setText("");
            docExp.setText("");
        }

        @SuppressLint("StringFormatMatches")
        public void onBind(int position)
        {
            super.onBind(position);
            final AllDoctorsResponse repo = response.get(position);
            if (repo != null)
            {
                docName.setText(repo.getFio_doctor());

                new ShowFile2.BuilderImage(docImage.getContext())
                        .setType(ShowFile2.TYPE_ICO)
                        .load(repo.getImage_url())
                        .token(token)
                        .imgError(R.drawable.sh_doc)
                        .into(docImage)
                        .setListener(new ShowFile2.ShowListener() {
                            @Override
                            public void complete(File file) {
                                Log.wtf("","");
                            }

                            @Override
                            public void error(String error) {
                                Log.wtf("","");
                            }
                        })
                        .build();




                docExp.setText(repo.getName_specialties());
            }
        }
    }



    class EmptyViewHolder extends BaseViewHolder
    {
        EmptyViewHolder(View itemView)
        {
            super(itemView);
        }

        @Override
        protected void clear()
        {
        }
    }

    public void setFilter(List<AllDoctorsResponse> filterService)
    {
        response = new ArrayList<>();
        response.addAll(filterService);
        notifyDataSetChanged();
    }
}
