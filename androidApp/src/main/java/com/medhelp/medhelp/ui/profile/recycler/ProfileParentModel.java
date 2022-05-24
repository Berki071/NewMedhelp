package com.medhelp.medhelp.ui.profile.recycler;

import android.annotation.SuppressLint;

import com.medhelp.medhelp.data.model.VisitResponse;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

@SuppressLint("ParcelCreator")
public class ProfileParentModel extends ExpandableGroup<VisitResponse> {

    public ProfileParentModel(String title, List<VisitResponse> items) {
        super(title, items);
    }
}