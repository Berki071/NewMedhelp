package com.medhelp.medhelp.ui.analise_list.recy_folder;

import androidx.annotation.NonNull;

import com.medhelp.medhelp.data.model.AnaliseListData;
import com.medhelp.medhelp.utils.TimesUtils;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

public class DataForAnaliseList extends ExpandableGroup<AnaliseListData> implements Comparable {
    private String title;

    public DataForAnaliseList(String title, List<AnaliseListData> items) {
        super(title, items);
        this.title=title;
    }


    @Override
    public int compareTo(@NonNull Object o) {
        long d1= TimesUtils.stringToLong(title,TimesUtils.DATE_FORMAT_ddMMyyyy);
        long d2= TimesUtils.stringToLong(((DataForAnaliseList)o).title,TimesUtils.DATE_FORMAT_ddMMyyyy);

        long res=d2-d1;

        if(res==0)
            return 0;
        else if(res<0)
            return -1;
        else
            return 1;
    }
}
