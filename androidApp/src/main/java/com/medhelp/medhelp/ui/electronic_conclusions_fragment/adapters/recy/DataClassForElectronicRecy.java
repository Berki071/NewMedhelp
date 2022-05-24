package com.medhelp.medhelp.ui.electronic_conclusions_fragment.adapters.recy;

import com.medhelp.medhelp.data.model.AnaliseResponse;
import com.medhelp.medhelp.data.model.ResultZakl2Item;
import com.medhelp.medhelp.utils.main.TimesUtils;

public class DataClassForElectronicRecy implements Comparable<DataClassForElectronicRecy>{
    // сортировка перенесена с AnaliseResponse а должна работать без проблем в анализах

    private boolean hideDownload =true;
    private String pathToFile="";
    private boolean showTooltip;
    private boolean showHideBox=false;

    public boolean isHideDownload() {
        return hideDownload;
    }
    public void setHideDownload(boolean hideDownload) {
        this.hideDownload = hideDownload;
    }

    public String getPathToFile() {
        return pathToFile;
    }
    public void setPathToFile(String pathToFile) {
        this.pathToFile = pathToFile;
    }

    public boolean isShowTooltip() {
        return showTooltip;
    }
    public void setShowTooltip(boolean showTooltip) {
        this.showTooltip = showTooltip;
    }

    public boolean isShowHideBox() {
        return showHideBox;
    }
    public void setShowHideBox(boolean showHideBox) {
        this.showHideBox = showHideBox;
    }

    public boolean isDownloadIn() {
        return !pathToFile.equals("");
    }

    String getDate(){
        if(this instanceof AnaliseResponse)
            return ((AnaliseResponse)this).getDateForZakl();
        else
            return ((ResultZakl2Item)this).getDataPriema();
    }

    String getTitle(){
        if(this instanceof AnaliseResponse)
            return ((AnaliseResponse)this).getTitle();
        else
            return ((ResultZakl2Item)this).getTitle();
    }

    @Override
    public int compareTo(DataClassForElectronicRecy o) {
        String name1=getDate();
        String name2=o.getDate();

        if(name1==null || name2==null || name1.equals("") || name2.equals("") || name1.equals(name2))
            return 0;

        long d1= TimesUtils.stringToLong(name1,TimesUtils.DATE_FORMAT_ddMMyyyy);
        long d2= TimesUtils.stringToLong(name2,TimesUtils.DATE_FORMAT_ddMMyyyy);

        if(d2>d1)
            return 1;
        else if(d2<d1)
            return -1;
        else
            return 0;
    }
}
