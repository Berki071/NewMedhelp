package com.medhelp.medhelp.ui.doctor.service_activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.medhelp.medhelp.Constants;
import com.medhelp.medhelp.R;
import com.medhelp.medhelp.data.model.ServiceResponse;
import com.medhelp.medhelp.data.pref.PreferencesManager;
import com.medhelp.medhelp.ui._main_page.MainActivity;
import com.medhelp.medhelp.ui.base.BaseViewHolder;
import com.medhelp.medhelp.ui.schedule.ScheduleFragment;

import java.util.ArrayList;
import java.util.List;



import it.sephiroth.android.library.xtooltip.ClosePolicy;
import it.sephiroth.android.library.xtooltip.Tooltip;

import static com.medhelp.medhelp.ui._main_page.MainActivity.POINTER_TO_PAGE;

public class ServiceAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private static final int VIEW_TYPE_NORMAL = 11;
    private int idDoctor;
    private List<ServiceResponse> response;

    private ServiceAdapter.ItemListener listener;

    ServiceAdapter(List<ServiceResponse> response, int idDoc, ServiceAdapter.ItemListener listener) {
        this.response = response;
        this.listener = listener;
        idDoctor = idDoc;

    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_NORMAL) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.item_search, parent, false));
        }
        return new EmptyViewHolder(LayoutInflater.from(parent.getContext()).
                inflate(R.layout.item_error_download, parent, false));
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

    void addItems(List<ServiceResponse> repoList) {
        response.clear();
        response.addAll(repoList);
        notifyDataSetChanged();
    }

    public interface ItemListener {
        void clickFab(ServiceResponse item);

        void clickRecord(ServiceAdapter.ViewHolder holder, int service, int limit);
    }


    public class ViewHolder extends BaseViewHolder {
        TextView tvTitle;
        TextView tvPrice;
        TextView tvRub;
        Button recordButton;
        TextView hint;
        ConstraintLayout rootVisible;
        ImageView tab;

        private PreferencesManager ph;

        Context context;


        ViewHolder(View itemView) {
            super(itemView);

            tvTitle=itemView.findViewById(R.id.tv_search_item_name);
            tvPrice=itemView.findViewById(R.id.tv_search_item_data);
            tvRub=itemView.findViewById(R.id.tv_search_item_rub);
            recordButton=itemView.findViewById(R.id.btn_search_record);
            hint=itemView.findViewById(R.id.hint);
            rootVisible=itemView.findViewById(R.id.rootVisible);
            tab=itemView.findViewById(R.id.tab);

            itemView.setOnClickListener(view ->
            {

                if (rootVisible == null)
                    return;

                if (rootVisible.getVisibility() == View.VISIBLE) {
                    rootVisible.setVisibility(View.GONE);
                    tvTitle.setTypeface(null, Typeface.NORMAL);
                    tvPrice.setTypeface(null, Typeface.NORMAL);
                    tvRub.setTypeface(null, Typeface.NORMAL);
                } else {
                    rootVisible.setVisibility(View.VISIBLE);
                    tvTitle.setTypeface(null, Typeface.BOLD);
                    tvPrice.setTypeface(null, Typeface.BOLD);
                    tvRub.setTypeface(null, Typeface.BOLD);
                    testForShowTooltip(tab, context.getResources().getString(R.string.searchTooltipSearchTab));
                }
            });

            context = this.itemView.getContext();
            ph = new PreferencesManager(context);
        }

        private void testForShowTooltip(View view, String msg) {
            //+
            view.post(()->{
                if (ph.getShowTooltipSearchTab()) {

                    Tooltip builder = new Tooltip.Builder(context)
                            .anchor(view, 0, 0,false)
                            .closePolicy(
                                    new ClosePolicy(300))
                            .activateDelay(10)
                            .showDuration(40000)
                            .text(msg)
                            .maxWidth(600)
                            .arrow(true)
                            .styleId(R.style.ToolTipLayoutCustomStyle)
                            .overlay(true)
                            .create();

                    builder.show(view, Tooltip.Gravity.BOTTOM,false);

                    ph.setShowTooltipSearchTab();
                }
            });
        }

        private void installTab(ServiceResponse repo) {
            if (repo.getFavorites().equals("0")) {
                tab.setImageResource(R.drawable.ic_turned_in_not_grey_500_24dp);
            } else {
                tab.setImageResource(R.drawable.ic_turned_in_indigo_500_24dp);
            }
        }

        protected void clear() {
            tvTitle.setText("");
            tvPrice.setText("");
            hint.setText("");
        }

        ServiceResponse repo;

        public void onBind(int position) {
            super.onBind(position);

            rootVisible.setVisibility(View.GONE);
            tvTitle.setTypeface(null, Typeface.NORMAL);
            tvPrice.setTypeface(null, Typeface.NORMAL);
            tvRub.setTypeface(null, Typeface.NORMAL);

            repo = response.get(position);

            if (repo != null) {
                tvTitle.setText(repo.getTitle());
                tvPrice.setText(repo.getValue());
                hint.setText(repo.getHint());
            }

            if (repo.getPossibilityToEnroll().equals("0"))
                recordButton.setVisibility(View.GONE);
            else {
                recordButton.setOnClickListener(view -> {
                            listener.clickRecord(this, repo.getId(), repo.getMax_zapis());
                        }
                );
            }

            installTab(repo);

            tab.setOnClickListener(v -> {
                repo.setFavorites(repo.getFavorites().equals("0") ? "1" : "0");
                installTab(repo);
                listener.clickFab(repo);
            });


            if (position == 0)
                showTooltip();
        }

        public void jumpToNextPage() {
            if (repo != null)
                startScheduleActivity(repo);
        }

        private void showTooltip() {
            //+
            tvTitle.post(()->{
                if (ph.getShowTooltipASearchItem()) {
                    Tooltip builder = new Tooltip.Builder(context)
                            .anchor(tvTitle, 0, 0,false)
                            .closePolicy(
                                    new ClosePolicy(300))
                            .activateDelay(10)
                            .showDuration(40000)
                            .text(context.getResources().getString(R.string.searchTooltipItem))
                            .maxWidth(600)
                            .arrow(true)
                            .styleId(R.style.ToolTipLayoutCustomStyle)
                            .overlay(true)
                            .create();

                    builder.show(tvTitle, Tooltip.Gravity.BOTTOM,false);

                    ph.setShowTooltipASearchItem();
                }
            });
        }

        private void startScheduleActivity(ServiceResponse repo) {
            if (repo != null) {
//                Intent intent = ScheduleFragment.getStartIntent(recordButton.getContext());
//                intent.putExtra(ScheduleFragment.EXTRA_DATA_ID_SERVICE, repo.getId());
//                intent.putExtra(ScheduleFragment.EXTRA_DATA_ADM, repo.getAdmission());
//                intent.putExtra(ScheduleFragment.EXTRA_DATA_ID_DOCTOR, idDoctor);
//                intent.putExtra(ScheduleFragment.EXTRA_DATA_NAME_SERVICE, repo.getTitle());
//                intent.putExtra(ScheduleFragment.EXTRA_DATA_ID_SPEC, repo.getIdSpec());
//                recordButton.getContext().startActivity(intent);

                Bundle bundle = new Bundle();
                bundle.putInt(ScheduleFragment.EXTRA_DATA_ID_SERVICE, repo.getId());
                bundle.putInt(ScheduleFragment.EXTRA_DATA_ADM, repo.getAdmission());
                bundle.putInt(ScheduleFragment.EXTRA_DATA_ID_DOCTOR, idDoctor);
                bundle.putString(ScheduleFragment.EXTRA_DATA_NAME_SERVICE, repo.getTitle());
                bundle.putInt(ScheduleFragment.EXTRA_DATA_ID_SPEC, repo.getIdSpec());
                bundle.putInt(ScheduleFragment.EXTRA_BACK_PAGE, Constants.ACTIVITY_SERVICE);
                bundle.putInt(ScheduleFragment.EXTRA_BACK_PAGE_SERVICE, ((ServiceActivity) context).getBackPage());
                //((MainActivity)context).selectedFragmentSchedule(bundle);
                Intent intent = new Intent(context, MainActivity.class);
                intent.putExtras(bundle);
                intent.putExtra(POINTER_TO_PAGE, Constants.FRAGMENT_SCHEDULE);

                context.startActivity(intent);
            }
        }
    }


    class EmptyViewHolder extends BaseViewHolder {
        TextView errMessage;

        EmptyViewHolder(View itemView) {
            super(itemView);
            errMessage = itemView.findViewById(R.id.err_tv_message);
        }

        @Override
        protected void clear() {
        }
    }

    void setFilter(List<ServiceResponse> filterService) {
        response = new ArrayList<>();
        response.addAll(filterService);
        notifyDataSetChanged();
    }
}
