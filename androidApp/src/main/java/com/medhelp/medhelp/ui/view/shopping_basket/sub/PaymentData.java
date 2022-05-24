package com.medhelp.medhelp.ui.view.shopping_basket.sub;

import com.medhelp.medhelp.data.model.VisitResponse;
import com.medhelp.medhelp.data.model.yandex_cashbox.YandexKey;

import java.util.List;

public class PaymentData {
    String idBranch;
    String sum;
    String description;
    YandexKey keys;
    List<VisitResponse> visitList;
    String idPayment;


    public String getIdPayment() {
        return idPayment;
    }

    public void setIdPayment(String idPayment) {
        this.idPayment = idPayment;
    }

    public List<VisitResponse> getVisitList() {
        return visitList;
    }

    public void setVisitList(List<VisitResponse> visitList) {
        this.visitList = visitList;
    }

    public String getIdBranch() {
        return idBranch;
    }

    public void setIdBranch(String idBranch) {
        this.idBranch = idBranch;
    }

    public String getSum() {
        return sum;
    }

    public void setSum(String sum) {
        this.sum = sum;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public YandexKey getKeys() {
        return keys;
    }

    public void setKeys(YandexKey keys) {
        this.keys = keys;
    }
}
