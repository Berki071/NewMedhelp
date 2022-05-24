package com.medhelp.medhelp.ui.view.shopping_basket.recy;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.medhelp.medhelp.R;
import com.medhelp.medhelp.data.model.VisitResponse;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;


public class ShoppingBasketItemViewHolder extends ChildViewHolder {

    private TextView title;
    private TextView price;
    private ImageView delete;

    private VisitResponse response;

    ChildListener listener;

    public ShoppingBasketItemViewHolder(View itemView,ChildListener listener) {
        super(itemView);

        this.listener=listener;

        title=itemView.findViewById(R.id.title);
        price=itemView.findViewById(R.id.price);
        delete=itemView.findViewById(R.id.delete);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.deleteItem(response);
            }
        });
    }

    void onBind(VisitResponse response) {
        this.response=response;
        title.setText(response.getNameServices());
        price.setText(response.getPrice() +"р.");

        if(response.getStatus().equals("p"))
        {
            delete.setVisibility(View.GONE);
        }
        else
        {
            delete.setVisibility(View.VISIBLE);
        }
    }


    public interface ChildListener{
        void deleteItem(VisitResponse itm);
    }
}
