package com.medhelp.medhelp.ui.view.shopping_basket.recy;

import com.medhelp.medhelp.data.model.VisitResponse;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

public class ShoppingBasketParentModel extends ExpandableGroup<VisitResponse> {
    public ShoppingBasketParentModel(String amount, List<VisitResponse> items) {
        super(amount, items);
    }
}
