package com.medhelp.medhelp.ui.sale.recy;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.medhelp.medhelp.R;
import com.medhelp.medhelp.data.model.SaleResponse;
import com.medhelp.medhelp.data.pref.PreferencesManager;
import com.medhelp.medhelp.ui.base.BaseViewHolder;
import com.medhelp.medhelp.utils.workToFile.show_file.ShowFile2;

import java.io.File;
import java.util.List;




public class SaleAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private static final int VIEW_TYPE_NORMAL = 11;
    private List<SaleResponse> response;

    public SaleAdapter(List<SaleResponse> response) {
        this.response = response;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_NORMAL) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sale, parent, false));
        }
        return new EmptyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_error_download, parent, false));
    }

    @Override
    public int getItemViewType(int position) {
        if (response != null && response.size() > 0) {
            return VIEW_TYPE_NORMAL;
        } else {
            return 0;
        }
    }

    @Override
    public int getItemCount() {
        if (response != null && response.size() > 0) {
            return response.size();
        } else {
            return 0;
        }
    }

    public void addItems(List<SaleResponse> repoList) {
        response.clear();
        response.addAll(repoList);
        notifyDataSetChanged();
    }

    class ViewHolder extends BaseViewHolder {
        ImageView saleImage;
        TextView saleDescription;

        String token;

        ViewHolder(View itemView) {
            super(itemView);

            saleDescription=itemView.findViewById(R.id.sale_item_description);
            saleImage=itemView.findViewById(R.id.sale_item_image);

            Context context=itemView.getContext();
            PreferencesManager ph=new PreferencesManager(context);
            token=ph.getCurrentUserInfo().getApiKey();
        }

        protected void clear() {
            saleImage.setImageDrawable(null);
            saleDescription.setText("");
        }

        public void onBind(int position) {
            super.onBind(position);
            final SaleResponse repo = response.get(position);

            if (repo != null) {

                new ShowFile2.BuilderImage(saleImage.getContext())
                        .setType(ShowFile2.TYPE_ICO)
                        .load(repo.getSaleImage())
                        .token(token)
                        .into(saleImage)
                        .setListener(new ShowFile2.ShowListener() {
                            @Override
                            public void complete(File file) {
                            }

                            @Override
                            public void error(String error) {
                            }
                        })
                        .build();

                if (repo.getSaleDescription() != null) {
                    saleDescription.setText(repo.getSaleDescription());
                }
            }
        }
    }

    class EmptyViewHolder extends BaseViewHolder {
        TextView errMessage;

        EmptyViewHolder(View itemView) {
            super(itemView);
            errMessage=itemView.findViewById(R.id.err_tv_message);
        }

        @Override
        protected void clear() {
        }
    }
}
