package com.medhelp.medhelp.ui.profile.recycler;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.medhelp.medhelp.R;
import com.medhelp.medhelp.data.pref.PreferencesManager;
import com.medhelp.newmedhelp.model.VisitResponse;
import com.medhelp.newmedhelp.model.VisitResponseAndroid;
import com.thoughtbot.expandablerecyclerview.MultiTypeExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class ProfileAdapter extends MultiTypeExpandableRecyclerViewAdapter<ProfileTitleViewHolder, ProfileVisitViewHolder>
{
    private static final int BUTTON_MODE = 3;
    private static final int NO_BUTTON_MODE = 4;
    private static final int ERROR_MODE = 5;
    private Context context;
    private String today;
    private String time;
    private ItemClickListener itemClickListener;

    private String token;

    private boolean blockBasket=false;
    private boolean yandexStoreIsWork;

    public ProfileAdapter(Activity context, List<? extends ExpandableGroup> groups, String today, String time, ItemClickListener itemClickListener, String token) {
        super(groups);
        this.context = context;
        this.today=today;
        this.time=time;
        this.itemClickListener=itemClickListener;
        this.token=token;

        PreferencesManager preferencesHelper=new PreferencesManager(context);
        yandexStoreIsWork=preferencesHelper.getYandexStoreIsWorks();
    }

    @Override
    public ProfileTitleViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        View view = null;

        if (inflater != null) {
            view = inflater.inflate(R.layout.item_groupe, parent, false);
        }

        return new ProfileTitleViewHolder(view);
    }

    @Override
    public ProfileVisitViewHolder onCreateChildViewHolder(ViewGroup parent, final int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);

        switch (viewType) {
            case BUTTON_MODE:

                View viewBtn = null;

                if (inflater != null) {
                    viewBtn = inflater.inflate(R.layout.item_profile_btn, parent, false);
                }

                return new ProfileVisitViewHolder(viewBtn,today,time,itemClickListener,token);

            case NO_BUTTON_MODE:

                View viewNoBtn = null;

                if (inflater != null) {
                    viewNoBtn = inflater.inflate(R.layout.item_profile_no_btn, parent, false);
                }

                return new ProfileVisitViewHolder(viewNoBtn,today,time,itemClickListener,token);

            default:
                throw new IllegalArgumentException("Invalid viewType");
        }
    }

    @Override
    public void onBindGroupViewHolder(ProfileTitleViewHolder holder, int flatPosition, ExpandableGroup group) {
        holder.setGroupName(group);
    }

    @Override
    public void onBindChildViewHolder(ProfileVisitViewHolder holder, int flatPosition, ExpandableGroup group, int childIndex) {
        int viewType = getItemViewType(flatPosition);
        VisitResponseAndroid visit = ((ProfileParentModel) group).getItems().get(childIndex);
        switch (viewType) {
            case BUTTON_MODE:
                holder.onBindButton(visit,blockBasket,yandexStoreIsWork);
                break;
            case NO_BUTTON_MODE:
                holder.onBindNoButton(visit,blockBasket,yandexStoreIsWork);
                break;
        }
    }

    @Override
    public int getChildViewType(int position, ExpandableGroup group, int childIndex) {
        switch (group.getTitle()) {
            case "Прошедшие":
                return NO_BUTTON_MODE;
            case "Предстоящие":
                return BUTTON_MODE;
            default:
                return ERROR_MODE;
        }
    }

    @Override
    public boolean isChild(int viewType) {
        return viewType == BUTTON_MODE || viewType == NO_BUTTON_MODE;
    }

    public void setInaccessibleBtnPay()
    {
        blockBasket=true;
        notifyDataSetChanged();
    }
    public void setAffordableBtnPay()
    {
        blockBasket=false;
        notifyDataSetChanged();
    }

}
