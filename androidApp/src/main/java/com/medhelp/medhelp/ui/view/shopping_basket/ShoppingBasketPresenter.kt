package com.medhelp.medhelp.ui.view.shopping_basket

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.widget.Toast
import com.androidnetworking.error.ANError
import com.google.gson.Gson
import com.medhelp.medhelp.R
import com.medhelp.medhelp.data.model.chat.SimpleResBoolean
import com.medhelp.medhelp.data.model.yandex_cashbox.PaymentInformationModel
import com.medhelp.medhelp.data.model.yandex_cashbox.PaymentModel
import com.medhelp.medhelp.data.model.DataPaymentForRealm
import com.medhelp.medhelp.ui.view.shopping_basket.sub.PaymentData
import com.medhelp.medhelp.data.model.YandexKey
import com.medhelp.medhelp.data.pref.PreferencesManager
import com.medhelp.medhelp.utils.timber_log.LoggingTree
import com.medhelp.shared.model.CenterResponse
import com.medhelp.shared.network.NetworkManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject
import ru.yoomoney.sdk.kassa.payments.TokenizationResult
import timber.log.Timber
import java.util.*
import com.medhelp.medhelp.data.network.NetworkManager as NM

class ShoppingBasketPresenter  {
    private var context: Context? = null
    private var helper: ShoppingBasketFragment? = null
    private var uuidM: String? = null
    private var pm: PaymentModel? = null
    val networkManager2 = NetworkManager()

    private val mainScope = MainScope()     //+

    var prefManager: PreferencesManager? = null
    var networkManager: NM? = null

    fun setData(context: Context, thisCont: ShoppingBasketFragment) {
        this.context = context
        helper = thisCont
        prefManager = PreferencesManager(context)
        networkManager = NM(prefManager!!)
    }

    fun tokenReceived(result: TokenizationResult, data: PaymentData) {
        val json = createPayJson(result.paymentToken, data.description, data.sum)
        makingPayment(json, data.keys.uuid, data.keys)
    }

    private fun createPayJson(tok: String, descriptions: String, at: String): JSONObject? {
        var mainBody: JSONObject? = null
        val amount: JSONObject
        val confirmation: JSONObject
        try {
            mainBody = JSONObject()
            amount = JSONObject()
            confirmation = JSONObject()
            amount.put("value", at)
            amount.put("currency", "RUB")
            confirmation.put("type", "redirect") //оплачивается банковской картойв
            confirmation.put("enforce", false)
            // confirmation.put("return_url", M_URL_FOR_EXIT);  //сайт куда перейти после оплаты
            confirmation.put("return_url", "")
            mainBody.put("payment_token", tok)
            mainBody.put("amount", amount)
            mainBody.put("confirmation", confirmation)
            mainBody.put(
                "capture",
                true
            ) //false платеж будет проходить через подтверждение/отмена, в противно случае делать только возврат
            mainBody.put("description", descriptions)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return mainBody
    }

    private fun makingPayment(json: JSONObject?, keyIdempotence: String, yKey: YandexKey) {
        uuidM = keyIdempotence
        helper!!.showLoading()

//        Log.wtf("dddfdfdfd", "json "+json.toString()+"/n" +
//                "keyIdempotence "+ keyIdempotence+"/n" +
//                "yKey "+yKey.getUuid()+" "+yKey.getIdShop()+" "+yKey.getKeyAppYandex()+" "+yKey.getKeyShop()+yKey.getIdBranch()
//                );
        val cd = CompositeDisposable()
        cd.add(networkManager!!
            .sendPayment(json!!, keyIdempotence, yKey.idShop, yKey.keyShop)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ response: PaymentModel ->
                when (response.status) {
                    "pending" -> {
                        pm = response //созранение текущей информации о платеже в PaymentModel
                        helper!!.setIdPaid(response.id) //сохранение  id в начальном обюъекте(не обязательно, перестраховка)
                        helper!!.secure3D(response)
                    }
                    "waiting_for_capture" -> {
                        pm = response //созранение текущей информации о платеже в PaymentModel
                        helper!!.setIdPaid(response.id)
                        helper!!.purchaseMade()
                    }
                    "succeeded" -> {
                        helper!!.setIdPaid(response.id)
                        pm = response //созранение текущей информации о платеже в PaymentModel
                        helper!!.purchaseMade()
                    }
                    else -> {
                        Toast.makeText(
                            context,
                            context!!.resources.getString(R.string.some_error),
                            Toast.LENGTH_LONG
                        ).show()
                        Timber.tag("my").e(
                            LoggingTree.getMessageForError(
                                null,
                                "ShoppingBasketPresenter/makingPayment1 /n response.getStatus() " + response.status
                                        + ";/n response.getId()(idPayment) 1 " + response.id
                                        + ";/n response.getPaid() " + response.paid
                            )
                        )
                        //                                    + ";/n response "+response.getCreated_at()+" "+response.getDescription()+" "
//                                    +response.getTest()+response.getConfirmation().getType()+" "+response.getRecipient().getGateway_id()
//                                    + " "+ response.getPayment_method().getTitle()+" "+response.getPayment_method().getType()
                        helper!!.hideLoading()
                    }
                }
                cd.dispose()
            }
            ) { throwable: Throwable? ->
                if (throwable is ANError) {
                    val msg = throwable.errorBody
                    if (msg != null && msg.contains("Idempotence key duplicated")) {
                        Timber.tag("my").e(
                            LoggingTree.getMessageForError(
                                throwable,
                                "ShoppingBasketPresenter/makingPayment2: Idempotence key duplicated"
                            )
                        )
                        val builder = AlertDialog.Builder(context)
                        builder.setMessage("Произошла ошибка при проведении платежа, для уточнения статуса обратитесь к получателю платежа с информацией о сумме и дате проведения платежа")
                            .setPositiveButton(
                                "Ok",
                                DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })
                        val alertDialog = builder.create()
                        alertDialog.show()
                        helper!!.hideLoading()
                        cd.dispose()
                        return@subscribe
                    }
                }
                Timber.tag("my").e(
                    LoggingTree.getMessageForError(
                        throwable,
                        "ShoppingBasketPresenter/makingPayment3"
                    )
                )
                Toast.makeText(
                    context,
                    context!!.resources.getString(R.string.some_error),
                    Toast.LENGTH_LONG
                ).show()
                helper!!.hideLoading()
                cd.dispose()
            }
        )
    }

    fun getYandexKeyData() {
        helper!!.showLoading()
        val cd = CompositeDisposable()

        mainScope.launch {
            kotlin.runCatching {
                networkManager2.getAllHospitalBranch(prefManager!!.currentUserInfo!!.idCenter.toString())
            }
                .onSuccess {
                    val list: MutableList<YandexKey> = ArrayList()
                    for (tmp in it.response) {
                        val ya = YandexKey()
                        ya.idBranch = tmp.idBranch
                        ya.idShop = tmp.idShop
                        ya.keyShop = tmp.keyShope
                        ya.keyAppYandex = tmp.keyAppYandex
                        ya.uuid = uUID
                        list.add(ya)
                    }
                    helper!!.processingDataWithKey(list)
                    helper!!.hideLoading()
                }.onFailure {
                    Toast.makeText(context, context!!.resources.getString(R.string.some_error), Toast.LENGTH_LONG).show()
                    Timber.tag("my").e(LoggingTree.getMessageForError(it, "ShoppingBasketPresenter/getYandexKeyData "))
                    helper!!.hideLoading()
                }
        }
    }

    private val uUID: String
        private get() {
            val uuid = UUID.randomUUID()
            return uuid.toString()
        }

    fun testKeyOnEqual(listKey: List<YandexKey>): Boolean {
        for (i in listKey.indices) {
            for (j in i + 1 until listKey.size) {
                if (listKey[i].idShop != listKey[j].idShop || listKey[i].keyAppYandex != listKey[j].keyAppYandex
                    || listKey[i].keyShop != listKey[j].keyShop
                ) {
                    return false
                }
            }
        }
        return true
    }

    fun getCenterInfo(): CenterResponse {
        return prefManager!!.centerInfo!!
    }

    private fun getCountItems(element: String): Int {
        return element.length - element.replace("&", "").length
    }

    fun sendPaymentToServer(data: DataPaymentForRealm) {
        Timber.tag("my")
            .v("услуги оплачены, Sum: " + data.price + "; IdZapisi: " + data.idZapisi + "; IdPayment: " + data.idPayment + "; IdYsl: " + data.idYsl)
        val cd = CompositeDisposable()
        cd.add(networkManager!!
            .sendToServerPaymentData(
                data.idUser,
                data.idBranch,
                data.idZapisi,
                data.idYsl,
                data.price,
                getCountItems(data.idUser),
                data.idPayment
            )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { response: SimpleResBoolean ->
                    if (response.response) {
                    } else {
                        val gson = Gson()
                        Timber.tag("my").e(
                            LoggingTree.getMessageForError(
                                null,
                                "ShoppingBasketPresenter/sendPaymentToServer, совпал pay_id " + gson.toJson(
                                    data
                                )
                            )
                        )
                    }
                    cd.dispose()
                }
            ) { throwable: Throwable? ->
                data.yandexInformation = false
                prefManager!!.savePaymentData(data)
                val gson = Gson()
                Timber.tag("my").e(
                    LoggingTree.getMessageForError(
                        throwable,
                        "ShoppingBasketPresenter/sendPaymentToServer, сохранено в Realm: " + gson.toJson(
                            data
                        )
                    )
                )
                cd.dispose()
            }
        )
    }

    fun getPaymentInformation(data: DataPaymentForRealm) {
        val cd = CompositeDisposable()
        cd.add(networkManager!!
            .getPaymentInformation(data.idPayment, data.yKeyObt.idShop, data.yKeyObt.keyShop)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { response: PaymentInformationModel ->
                    when (response.status) {
                        "waiting_for_capture" -> helper!!.purchaseMade()
                        "succeeded" -> helper!!.purchaseMade()
                        "canceled" -> Toast.makeText(
                            context,
                            "Платеж отменен! Вы отменили платеж самостоятельно, истекло время на принятие платежа или платеж был отклонен Яндекс.Кассой или платежным провайдером.",
                            Toast.LENGTH_LONG
                        ).show()
                        else -> Timber.tag("my").e(
                            LoggingTree.getMessageForError(
                                null,
                                "ShoppingBasketPresenter/makingPayment4 default  response.getStatus()" + response.status + "; response.getId()(idPayment) 2 "
                                        + response.id + "; response.getPaid() " + response.paid
                            )
                        )
                    }
                    helper!!.hideLoading()
                    cd.dispose()
                }
            ) { throwable: Throwable? ->
                if (throwable is ANError) {
                    val msg = throwable.errorBody
                    if (msg != null && msg.contains("not found")) {
                        cd.dispose()
                        return@subscribe
                    }
                }
                data.yandexInformation = true
                prefManager!!.savePaymentData(data)
                Toast.makeText(
                    context,
                    context!!.resources.getString(R.string.some_error),
                    Toast.LENGTH_LONG
                ).show()
                val gson = Gson()
                Timber.tag("my").e(
                    LoggingTree.getMessageForError(
                        throwable,
                        "ShoppingBasketPresenter/getPaymentInformation, сохранено в Realm: " + gson.toJson(
                            data
                        )
                    )
                )
                helper!!.hideLoading()
                cd.dispose()
            }
        )
    }

    fun onDestroyView() {
        mainScope.cancel()
    }

    companion object {
        @JvmStatic
        val instance = ShoppingBasketPresenter()
        const val M_URL_FOR_EXIT = "http://www.gotohome.ru"
    }
}