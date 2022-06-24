package com.medhelp.medhelp.ui.analysis_price_list.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.medhelp.medhelp.R;
import com.medhelp.medhelp.data.pref.PreferencesManager;
import com.medhelp.medhelp.ui.base.BaseViewHolder;
import com.medhelp.newmedhelp.model.AnalisePriceResponse;
import java.util.ArrayList;
import java.util.List;



public class AnalisePriceAdapter extends RecyclerView.Adapter<BaseViewHolder> {

   //SearchPresenter presenter;

    private static final int VIEW_TYPE_NORMAL = 11;
    private List<AnalisePriceResponse> response;

    private Context context;

    private PreferencesManager ph;

    public AnalisePriceAdapter(List<AnalisePriceResponse> response, Context context) {
        this.response = response;
        this.context=context;

        ph=new PreferencesManager(this.context);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.onBind(position);
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_NORMAL) {
            return new ViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_analise_price, parent, false));
        }
        return new EmptyViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_error_download, parent, false));
    }

    @Override
    public int getItemViewType(int position) {
        if (response != null && response.size() > 0) {
            return VIEW_TYPE_NORMAL;
        }
        return 1;
    }

    @Override
    public int getItemCount() {
        return response.size();
    }

    public void addItems(List<AnalisePriceResponse> repoList) {
        response.clear();
        response.addAll(repoList);
        notifyDataSetChanged();
    }

    public interface ItemListener{
        void clickFab(AnalisePriceResponse item);

        void clickRecord(ViewHolder holder, int service, int limit);
    }







    public class ViewHolder extends BaseViewHolder {
        TextView tvTitle;
        TextView tvPrice;
        TextView tvRub;
        TextView hint;

        AnalisePriceResponse repo;

        ViewHolder(View itemView) {
            super(itemView);
            tvTitle=itemView.findViewById(R.id.tv_search_item_name);
            tvPrice=itemView.findViewById(R.id.tv_search_item_data);
            tvRub=itemView.findViewById(R.id.tv_search_item_rub);
            hint=itemView.findViewById(R.id.hint);
        }

        protected void clear() {
            tvTitle.setText("");
            tvPrice.setText("");
            hint.setText("");
        }

        public void onBind(int position) {
            super.onBind(position);

            repo = response.get(position);
            if (repo != null) {
                tvTitle.setText(repo.getTitle());
                tvPrice.setText(repo.getPrice()+"");
                hint.setText("Срок выполнения (дней): "+repo.getTime());
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

    public void setFilter(List<AnalisePriceResponse> filterService) {
        response = new ArrayList<>();
        response.addAll(filterService);
        notifyDataSetChanged();
    }
}
