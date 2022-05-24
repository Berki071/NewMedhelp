package com.medhelp.medhelp.ui.view.shopping_basket.recy;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.medhelp.medhelp.R;
import com.medhelp.medhelp.data.model.VisitResponse;
import com.medhelp.medhelp.data.model.yandex_cashbox.YandexKey;
import com.medhelp.medhelp.utils.timber_log.LoggingTree;
import com.thoughtbot.expandablerecyclerview.MultiTypeExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import timber.log.Timber;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class ShoppingBasketAdapter  extends MultiTypeExpandableRecyclerViewAdapter<ShoppingBasketTitleViewHolder, ShoppingBasketItemViewHolder> {

    Context context;
    ShoppingBasketItemViewHolder.ChildListener itemListener;
    ShoppingBasketTitleViewHolder.TitleListener titleListener;
    List<YandexKey> keys;



    public ShoppingBasketAdapter(Context context, List<YandexKey> keys,  List<? extends ExpandableGroup> groups,
                                 ShoppingBasketItemViewHolder.ChildListener listenerItem,ShoppingBasketTitleViewHolder.TitleListener titleListener) {
        super(groups);
        this.context=context;
        this.itemListener =listenerItem;
        this.titleListener=titleListener;
        this.keys=keys;

    }



    @Override
    public ShoppingBasketTitleViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        View view = null;

        if (inflater != null) {
            view = inflater.inflate(R.layout.shopping_basket_title_item, parent, false);
        }

        return new ShoppingBasketTitleViewHolder(view,titleListener);
    }

    @Override
    public ShoppingBasketItemViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);

        View viewBtn = null;

        if (inflater != null) {
            viewBtn = inflater.inflate(R.layout.shopping_basket_item_item, parent, false);
        }

        return new ShoppingBasketItemViewHolder(viewBtn, itemListener);
    }

    @Override
    public void onBindChildViewHolder(ShoppingBasketItemViewHolder holder, int flatPosition, ExpandableGroup group, int childIndex) {
        VisitResponse visit = ((ShoppingBasketParentModel) group).getItems().get(childIndex);
        holder.onBind(visit);
    }

    //в Title лежит idBranch
    @Override
    public void onBindGroupViewHolder(ShoppingBasketTitleViewHolder holder, int flatPosition, ExpandableGroup group) {
        holder.setData(group.getItems(),getYKey(group.getTitle()),group.getTitle());
    }

    private YandexKey getYKey(String idBranch)
    {
       for(YandexKey tmp : keys)
       {
           if(String.valueOf(tmp.getIdBranch()).equals(idBranch))
           {
               return tmp;
           }
       }

       Timber.tag("my").e(LoggingTree.getMessageForError(null,"ShoppingBasketAdapter/getYKey не найден YandexKey для idBranch="+idBranch));
       return null;
    }
}
