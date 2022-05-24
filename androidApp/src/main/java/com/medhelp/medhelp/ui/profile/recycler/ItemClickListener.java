package com.medhelp.medhelp.ui.profile.recycler;


import com.medhelp.medhelp.data.model.VisitResponse;

public interface ItemClickListener {
    void cancelBtnClick(int idUser, int id_record, int idBranch);
    void confirmBtnClick(int idUser, int id_record, int idBranch,String str);

    void enrollAgainBtnClick(VisitResponse viz);

    void postponeBtnClick(VisitResponse viz);

    void payBtnClick(VisitResponse viz, boolean toPay);

    void confirmComing(VisitResponse viz);

}
