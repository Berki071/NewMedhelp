package com.medhelp.medhelp.ui.view.shopping_basket;

import android.app.Dialog;



import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.medhelp.medhelp.R;
import com.medhelp.medhelp.data.model.VisitResponse;
import com.medhelp.medhelp.data.model.yandex_cashbox.PaymentModel;
import com.medhelp.medhelp.data.model.yandex_cashbox.YandexKey;
import com.medhelp.medhelp.ui.view.shopping_basket.recy.ShoppingBasketAdapter;
import com.medhelp.medhelp.ui.view.shopping_basket.recy.ShoppingBasketItemViewHolder;
import com.medhelp.medhelp.ui.view.shopping_basket.recy.ShoppingBasketParentModel;
import com.medhelp.medhelp.ui.view.shopping_basket.recy.ShoppingBasketTitleViewHolder;
import com.medhelp.medhelp.ui.view.shopping_basket.sub.DataPaymentForRealm;
import com.medhelp.medhelp.ui.view.shopping_basket.sub.ShoppingBasketFragmentListener;
import com.medhelp.medhelp.ui.view.shopping_basket.sub.PaymentData;
import com.medhelp.medhelp.utils.timber_log.LoggingTree;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Currency;
import java.util.HashSet;
import java.util.List;



import ru.yoomoney.sdk.kassa.payments.Checkout;
import ru.yoomoney.sdk.kassa.payments.TokenizationResult;
import ru.yoomoney.sdk.kassa.payments.checkoutParameters.Amount;
import ru.yoomoney.sdk.kassa.payments.checkoutParameters.PaymentMethodType;
import ru.yoomoney.sdk.kassa.payments.checkoutParameters.PaymentParameters;
import ru.yoomoney.sdk.kassa.payments.checkoutParameters.SavePaymentMethod;
import ru.yoomoney.sdk.kassa.payments.checkoutParameters.TestParameters;
import timber.log.Timber;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;


public class ShoppingBasketFragment extends DialogFragment {

    public static final int REQUEST_CODE_TOKENIZE=83;
    public static final int REQUEST_CODE_3D_SECURE=84;

    View rootView;
    List<VisitResponse> list;

    RecyclerView recy;
    Button cancel;
    TextView hint;
    ConstraintLayout mainLoading;
    ConstraintLayout container;

    ShoppingBasketFragmentListener closedListener;

    ShoppingBasketPresenter presenter;

    List<YandexKey> listKey;

    static PaymentData lastPayment;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView =inflater.inflate(R.layout.shopping_basket_fragment,null);

        initValue(rootView);

        //яндекс
        FragmentActivity myContext=(FragmentActivity)getActivity();
        FragmentManager fragManager =myContext.getSupportFragmentManager();
        //Checkout.attach(fragManager);

        init();

        return rootView;
    }
    private void initValue(View v){
        recy=v.findViewById(R.id.recy);
        cancel=v.findViewById(R.id.cancel);
        hint=v.findViewById(R.id.hint);
        mainLoading=v.findViewById(R.id.loadMain);
        container=v.findViewById(R.id.container);
    }

    private void init()
    {
        presenter=ShoppingBasketPresenter.getInstance();
        presenter.setData(getActivity(),this);

       // closedListener=(ShoppingBasketFragmentListener)getActivity();

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        Bundle bundle= getArguments();
        if(bundle!=null)
        {
            list= bundle.getParcelableArrayList("mList");
            presenter.getYandexKeyData();
        }
    }

    public void setListener(ShoppingBasketFragmentListener closedListener)
    {
        this.closedListener=closedListener;
    }


    public void processingDataWithKey(List<YandexKey> listKey)
    {
        if(list.size()==0)
        {
            getDialog().dismiss();
            return;
        }

        this.listKey=listKey;

        ArrayList<ShoppingBasketParentModel> parentModels = new ArrayList<>();
        if(presenter.testKeyOnEqual(this.listKey))
        {
            parentModels.add(new ShoppingBasketParentModel(String.valueOf(list.get(0).getIdBranch()),list));
        }
        else
        {
            List<List> sortTmp=new ArrayList<>();

            for(VisitResponse tmp : list)
            {
                int contains=-1;
                for( int i=0; i<sortTmp.size();i++)
                {
                    VisitResponse q1=(VisitResponse)sortTmp.get(i).get(0);
                    if(q1.getIdBranch()==tmp.getIdBranch())
                    {
                        contains=i;
                        break;
                    }
                }

                if(contains==-1)
                {
                    List<VisitResponse> item=new ArrayList<>();
                    item.add(tmp);
                    sortTmp.add(item);
                    continue;
                }
                else
                {
                    sortTmp.get(contains).add(tmp);
                }
            }

            for(List<VisitResponse> tmp : sortTmp)
            {
                parentModels.add(new ShoppingBasketParentModel(String.valueOf(tmp.get(0).getIdBranch()),tmp));
            }
        }

        initRecy(parentModels);
    }

    private void initRecy(ArrayList<ShoppingBasketParentModel> list)
    {
        ShoppingBasketItemViewHolder.ChildListener listenerItem=new ShoppingBasketItemViewHolder.ChildListener() {
            @Override
            public void deleteItem(VisitResponse itm) {
                deleteItm(itm);
            }
        };

        ShoppingBasketTitleViewHolder.TitleListener titleListener=new ShoppingBasketTitleViewHolder.TitleListener() {
            @Override
            public void actionPay( PaymentData data) {

                //test payment
//                data.getKeys().setIdShop("768648");
////                data.getKeys().setKeyAppYandex("test_lhGkyfmJjolBBxAb5r_c_a_hM6FqIrZw73miV73zXjc");
////                data.getKeys().setKeyShop("test_NzY4NjQ4AvWtzHK02DH9p5RpAIykG4rOOvf6KmYdQoQ");

//                data.getKeys().setIdShop("531799");
//                data.getKeys().setKeyAppYandex("test_2mcY5S3-Arg28kAq9fvrJeJSaTdmA2KW6XEIkFEwF_I");
//                data.getKeys().setKeyShop("test_NTMxNzk58JhgOphUSgKJEW9S1XLYhrb2YIS1Occ_VSo");

                lastPayment=data;
                payForThePurchase(getActivity(),data);

            }
        };

        boolean isPaid=true;
        for(ShoppingBasketParentModel tmp : list)
        {
            if(!tmp.getItems().get(0).getStatus().equals("p"))
            {
                isPaid=false;
                break;
            }
        }

        if(isPaid)
            showAlertAllRight(list.size() <= 1);

        ShoppingBasketAdapter adapter = new ShoppingBasketAdapter(getActivity(),listKey, list,listenerItem,titleListener);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recy.setLayoutManager(layoutManager);
        recy.setAdapter(adapter);
        recy.setItemAnimator(new DefaultItemAnimator());

        if(list.size()==1) {
            adapter.onGroupClick(0);
            hint.setVisibility(View.GONE);
        }
        else
        {
            hint.setVisibility(View.VISIBLE);
        }
    }

    androidx.appcompat.app.AlertDialog dialog;
    private void showAlertAllRight(boolean isOnePayment)
    {
        cancel.setText("ok");
        container.setVisibility(View.GONE);

        String str;
        if(isOnePayment)
        {
            str="Ваш платеж проведен успешно";
        }
        else
        {
            str="Ваши платежи проведены успешно";
        }


        LayoutInflater inflater= getActivity().getLayoutInflater();
        View view=inflater.inflate(R.layout.dialog_payment_all_right,null);

        TextView text=view.findViewById(R.id.text);
        Button btnYes =view.findViewById(R.id.btnYes);

        text.setText(str);

        btnYes.setOnClickListener(v -> {
            dialog.cancel();
            getDialog().dismiss();
        });

        androidx.appcompat.app.AlertDialog.Builder builder=new androidx.appcompat.app.AlertDialog.Builder(getActivity());
        builder.setView(view);
        dialog =builder.create();
        dialog.show();

    }

    private void deleteItm(VisitResponse itm)
    {
        list.remove(itm);
        processingDataWithKey(listKey);
    }


    @Override
    public void onStart()
    {
        super.onStart();

        //костыль, по умолчанию окно показывается не во весь размер
        Dialog dialog = getDialog();
        if (dialog != null)
        {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    @Override
    public void onDestroyView() {
        if(closedListener!=null)
            closedListener.closeBasketFragment();
        super.onDestroyView();
        presenter.onDestroyView();
    }

    public void closedFragment() {
        getDialog().dismiss();
    }

    public void showLoading() {
       //closedListener.showLoading();
        mainLoading.setVisibility(View.VISIBLE);
        cancel.setEnabled(false);
    }

    public void hideLoading() {
       // closedListener.hideLoading();
        mainLoading.setVisibility(View.GONE);
        cancel.setEnabled(true);
    }

   private void payForThePurchase(Context context,  PaymentData data) {
       showLoading();

        String nameShop=presenter.getCenterInfo().getTitle();
        HashSet<PaymentMethodType> countryHashType = new HashSet<>();
        countryHashType.add(PaymentMethodType.GOOGLE_PAY);
        countryHashType.add(PaymentMethodType.BANK_CARD);

        PaymentParameters paymentParameters = new PaymentParameters(
                new Amount(new BigDecimal(data.getSum()), Currency.getInstance("RUB")),
                nameShop,
                data.getDescription(),
                data.getKeys().getKeyAppYandex(),
                data.getKeys().getIdShop(),
                SavePaymentMethod.OFF,
                //Collections.singleton(PaymentMethodType.GOOGLE_PAY)
                countryHashType
        );

       TestParameters testParameters = new TestParameters(true, true);  //не удалять включает логи "Yandex.Checkout.SDK"

        Intent intent = Checkout.createTokenizeIntent(context, paymentParameters ,testParameters);
        startActivityForResult(intent, REQUEST_CODE_TOKENIZE);
    }


    @Override
     public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_TOKENIZE) {
            switch (resultCode) {
                case RESULT_OK:
                    // successful tokenization
                    TokenizationResult result = Checkout.createTokenizationResult(data);
                    presenter.tokenReceived(result,lastPayment);
                    Timber.tag("my").v("токен получен");
                    break;
                case RESULT_CANCELED:
                    hideLoading();
                    // user canceled tokenization
                    Timber.tag("my").v("user canceled tokenization");
                    break;
            }
        }

        if (requestCode == REQUEST_CODE_3D_SECURE) {

            switch (resultCode) {
                case RESULT_OK:
                    // Аутентификация по 3-D_Secure прошла успешно
                    Timber.tag("my").v("Аутентификация по 3-D_Secure прошла успешно");
                    //purchaseMade();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            presenter.getPaymentInformation(createDataServer());
                        }
                    },1500); //проверка через 1,5 сек

                    break;
                case RESULT_CANCELED:
                    // Экран 3-D_Secure был закрыт
                    Timber.tag("my").v("Экран 3-D_Secure был закрыт");
                    presenter.getPaymentInformation(createDataServer());
                    break;
                case Checkout.RESULT_ERROR:

                    presenter.getPaymentInformation(createDataServer());
                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.some_error), Toast.LENGTH_LONG).show();
                    Timber.tag("my").e(LoggingTree.getMessageForError(null,"ShoppingBasketFragment/onActivityResult REQUEST_CODE_3D_SECURE, код ошибки из WebViewClient.ERROR_* или Checkout.ERROR_NOT_HTTPS_URL"
                            +data.getIntExtra(Checkout.EXTRA_ERROR_CODE,007)));
                    // Во время 3-D&nbsp;Secure произошла какая-то ошибка
                    //(например, нет соединения),
                    // более подробную информацию можно посмотреть в data:
                    // data.getIntExtra(Checkout.EXTRA_ERROR_CODE) — код ошибки из WebViewClient.ERROR_* или Checkout.ERROR_NOT_HTTPS_URL
                    // data.getStringExtra(Checkout.EXTRA_ERROR_DESCRIPTION) — описание ошибки (может отсутствовать)
                    // data.getStringExtra(Checkout.EXTRA_ERROR_FAILING_URL) — URL, по которому произошла ошибка (может отсутствовать)
                    break;
            }
        }
    }

    public void secure3D(PaymentModel payOoject)
    {
        if(payOoject.getConfirmation().getType().equals("redirect")  &&  !payOoject.getConfirmation().getConfirmation_url().equals(""))
        {
            Intent intent = Checkout.create3dsIntent(
                    getActivity(),
                    payOoject.getConfirmation().getConfirmation_url()
            );
            startActivityForResult(intent, REQUEST_CODE_3D_SECURE);
        }
        else
        {
            Timber.tag("my").e(LoggingTree.getMessageForError(null,"ShoppingBasketFragment/secure3D payOoject not correct (!equals(redirect) || getConfirmation_url()==null) "));
        }
    }

    public void setIdPaid(String id) {
        lastPayment.setIdPayment(id);
    }

    public void purchaseMade()
    {
        hideLoading();

        presenter.sendPaymentToServer(createDataServer());

        ShoppingBasketAdapter adapter=(ShoppingBasketAdapter)recy.getAdapter();
        ArrayList<ShoppingBasketParentModel> parentModels=(ArrayList<ShoppingBasketParentModel> )adapter.getGroups();

        for(ShoppingBasketParentModel tmp : parentModels)
        {
            String brch=tmp.getTitle();

            if(brch.equals(lastPayment.getIdBranch()))
            {
               List<VisitResponse> listV=tmp.getItems();
                for(VisitResponse tmp2 : listV)
                {
                    tmp2.setStatus("p");
                }
            }
        }

        processingDataWithKey(listKey);
    }

    private DataPaymentForRealm createDataServer()
    {
        String idUser="";
        String idBranch="";
        String idZapisi="";
        String idYsl="";
        String price="";

        for(VisitResponse tmp : lastPayment.getVisitList()) {
            idUser+=tmp.getIdUser();
            idUser+="&";

            idBranch+=tmp.getIdBranch();
            idBranch+="&";

            idZapisi+=tmp.getIdRecord();
            idZapisi+="&";

            idYsl+=tmp.getIdServices();
            idYsl+="&";

            price+=tmp.getPrice();
            price+="&";
        }

        DataPaymentForRealm ddd=new DataPaymentForRealm();
        ddd.setIdUser(idUser);
        ddd.setIdBranch(idBranch);
        ddd.setIdZapisi(idZapisi);
        ddd.setIdYsl(idYsl);
        ddd.setPrice(price);
        ddd.setIdPayment(lastPayment.getIdPayment());
        ddd.setYKeyObt(lastPayment.getKeys());

        return ddd;
    }

    public PaymentData getLastpayment() {
        return lastPayment;
    }

}
