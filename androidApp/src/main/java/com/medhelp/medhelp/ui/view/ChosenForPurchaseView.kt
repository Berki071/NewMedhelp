package com.medhelp.medhelp.ui.view

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import com.medhelp.newmedhelp.model.VisitResponseAndroid
import android.widget.TextView
import android.widget.ImageButton
import android.widget.Toast
import com.medhelp.medhelp.R
import java.util.ArrayList

class ChosenForPurchaseView : ConstraintLayout {
    private var items: MutableList<VisitResponseAndroid> = ArrayList()
    var listener: ChosenForPurchaseViewListener? = null
    var stateRootVisibility = true
    var root: ConstraintLayout? = null
    var amountServices: TextView? = null
    var amountCash: TextView? = null
    var btnCross: ImageButton? = null
    private val limitItems = 5

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context)
    }

    fun newListener(listener: ChosenForPurchaseViewListener) {
        this.listener = listener
    }

    fun addItem(item: VisitResponseAndroid) {
        items.add(item)
        testShowRootView()
        refreshDataAmount()
        if (items.size >= limitItems) {
            listener!!.limitReached()
            Toast.makeText(
                context,
                "Достигнуто ограничение на добавление в корзину",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    fun deleteItem(item: VisitResponseAndroid) {
        if (items.size >= limitItems) {
            listener!!.limitIsOver()
        }
        items.remove(item)
        testShowRootView()
        refreshDataAmount()
    }

    fun closeView() {
        items = ArrayList()
        stateRootVisibility = false
        root!!.visibility = GONE
    }

    private fun init(context: Context) {
        val v = inflate(context, R.layout.view_chosen_for_purchase, this)
        root = v.findViewById(R.id.root)
        amountServices = v.findViewById(R.id.amountServices)
        amountCash = v.findViewById(R.id.amountCash)
        btnCross = v.findViewById(R.id.btnCross)
        root!!.setOnClickListener(OnClickListener {
            root!!.setVisibility(GONE)
            listener!!.isShownView(false)
            stateRootVisibility = false
            listener!!.onClickBtn(items)
            items = ArrayList()
        })
        btnCross!!.setOnClickListener(OnClickListener {
            closeView()
            listener!!.isShownView(false)
            listener!!.onClickCross()
        })
        testShowRootView()
    }

    private fun testShowRootView() {
        if (items.size == 0) {
            if (!stateRootVisibility) {
                return
            }
            stateRootVisibility = false
            root!!.visibility = GONE
            if (listener != null) listener!!.isShownView(false)
        } else {
            if (stateRootVisibility == true) {
                return
            }
            stateRootVisibility = true
            root!!.visibility = VISIBLE
            if (listener != null) listener!!.isShownView(true)
        }
    }

    private fun refreshDataAmount() {
        if (items.size == 0) return
        val services: Int
        var cash = 0
        services = items.size
        for (tmp in items) {
            cash += tmp.price
        }
        amountServices!!.text = services.toString() + ""
        amountCash!!.text = "$cash руб."
    }

    interface ChosenForPurchaseViewListener {
        fun isShownView(boo: Boolean)
        fun onClickBtn(items: List<VisitResponseAndroid>)
        fun onClickCross()
        fun limitReached()
        fun limitIsOver()
    }

    private fun wordDeclension(number: Int): String {
        var number = number
        if (number > 20) {
            number %= 10
        }
        return when (number) {
            1 -> " позицию"
            2 -> " позиции"
            3 -> " позиции"
            4 -> " позиции"
            5 -> " позиций"
            6 -> " позиций"
            7 -> " позиций"
            8 -> " позиций"
            9 -> " позиций"
            10 -> " позиций"
            11 -> " позиций"
            12 -> " позиций"
            13 -> " позиций"
            14 -> " позиций"
            15 -> " позиций"
            16 -> " позиций"
            17 -> " позиций"
            18 -> " позиций"
            19 -> " позиций"
            20 -> " позиций"
            else -> " позиций"
        }
    }
}