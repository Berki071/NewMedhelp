package com.medhelp.medhelp.ui.view.shopping_basket.recy;

import com.medhelp.newmedhelp.model.VisitResponseAndroid;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

public class ShoppingBasketParentModel extends ExpandableGroup<VisitResponseAndroid> {
    public ShoppingBasketParentModel(String amount, List<VisitResponseAndroid> items) {
        super(amount, items);
    }
}
