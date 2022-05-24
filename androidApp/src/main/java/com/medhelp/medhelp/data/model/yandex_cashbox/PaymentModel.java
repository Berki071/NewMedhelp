package com.medhelp.medhelp.data.model.yandex_cashbox;

import com.google.gson.annotations.SerializedName;
import com.medhelp.medhelp.data.model.yandex_cashbox.sub.AmountModel;
import com.medhelp.medhelp.data.model.yandex_cashbox.sub.ConfirmationModel;
import com.medhelp.medhelp.data.model.yandex_cashbox.sub.PaymentMethodModel;
import com.medhelp.medhelp.data.model.yandex_cashbox.sub.RecipientModel;

public class PaymentModel {
    @SerializedName("id") private String id;
    @SerializedName("status") private String status;
    @SerializedName("paid") private String paid;
    @SerializedName("amount") private AmountModel amount;
    @SerializedName("confirmation") private ConfirmationModel confirmation;
    @SerializedName("created_at") private String created_at;
    @SerializedName("description") private String description;
    //@SerializedName("metadata") String metadata; на данный момент приходит пустое поле
    @SerializedName("payment_method") private PaymentMethodModel payment_method;
    @SerializedName("recipient") private RecipientModel recipient;
    @SerializedName("test") private String test;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPaid() {
        return paid;
    }

    public void setPaid(String paid) {
        this.paid = paid;
    }

    public AmountModel getAmount() {
        return amount;
    }

    public void setAmount(AmountModel amount) {
        this.amount = amount;
    }

    public ConfirmationModel getConfirmation() {
        return confirmation;
    }

    public void setConfirmation(ConfirmationModel confirmation) {
        this.confirmation = confirmation;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public PaymentMethodModel getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(PaymentMethodModel payment_method) {
        this.payment_method = payment_method;
    }

    public RecipientModel getRecipient() {
        return recipient;
    }

    public void setRecipient(RecipientModel recipient) {
        this.recipient = recipient;
    }

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }
}
