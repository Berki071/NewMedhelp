package com.medhelp.medhelp.ui.profile.recycler;

import android.annotation.SuppressLint;

import com.medhelp.newmedhelp.model.VisitResponseAndroid;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;


@SuppressLint("ParcelCreator")
public class ProfileParentModel extends ExpandableGroup<VisitResponseAndroid> {

    public ProfileParentModel(String title, List<VisitResponseAndroid> items) {
        super(title, items);
    }
}