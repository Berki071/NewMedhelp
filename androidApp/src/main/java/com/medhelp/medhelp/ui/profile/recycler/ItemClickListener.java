package com.medhelp.medhelp.ui.profile.recycler;
import com.medhelp.newmedhelp.model.VisitResponseAndroid;

public interface ItemClickListener {
    void cancelBtnClick(int idUser, int id_record, int idBranch);
    void confirmBtnClick(int idUser, int id_record, int idBranch,String str);

    void enrollAgainBtnClick(VisitResponseAndroid viz);

    void postponeBtnClick(VisitResponseAndroid viz);

    void payBtnClick(VisitResponseAndroid viz, boolean toPay);

    void confirmComing(VisitResponseAndroid viz);

}
