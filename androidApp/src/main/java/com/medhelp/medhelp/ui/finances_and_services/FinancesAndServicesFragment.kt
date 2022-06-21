package com.medhelp.medhelp.ui.finances_and_services

import com.medhelp.medhelp.ui._main_page.MainFragmentHelper
import androidx.recyclerview.widget.RecyclerView
import android.widget.TextView
import com.google.android.material.appbar.CollapsingToolbarLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.medhelp.medhelp.ui.view.ChosenForPurchaseView
import android.app.Activity
import android.os.Bundle
import com.medhelp.medhelp.ui.view.ChosenForPurchaseView.ChosenForPurchaseViewListener
import com.medhelp.newmedhelp.model.VisitResponseAndroid
import com.medhelp.medhelp.ui.view.shopping_basket.ShoppingBasketFragment
import android.os.Parcelable
import com.medhelp.medhelp.ui.finances_and_services.recy.FinancesAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.medhelp.medhelp.R
import com.medhelp.medhelp.ui._main_page.MainActivity
import com.google.android.material.appbar.AppBarLayout
import com.medhelp.medhelp.utils.main.MainUtils
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.DefaultItemAnimator
import com.medhelp.medhelp.ui.base.BaseFragment
import kotlinx.coroutines.cancel
import timber.log.Timber
import java.util.*

class FinancesAndServicesFragment : BaseFragment(){
    companion object {
        fun newInstance(): Fragment {
            return FinancesAndServicesFragment()
        }
    }

    var presenter: FinancesAndServicesPresenter? = null
    var mainFragmentHelper: MainFragmentHelper? = null
    var recyclerView: RecyclerView? = null
    var toolbar: Toolbar? = null
    var errMessage: TextView? = null
    var errLoadBtn: TextView? = null
    var toolbarLayout: CollapsingToolbarLayout? = null
    var rootEmpty: ConstraintLayout? = null
    var cfpv: ChosenForPurchaseView? = null
    private var shoppingBasketFragment: DialogFragment? = null
    var activity: Activity? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.tag("my").i("Финансы и услуги")
        activity = getActivity()
        mainFragmentHelper = context as MainFragmentHelper?
    }

    override fun setUp(view: View) {
        cfpv!!.listener = object : ChosenForPurchaseViewListener {
            override fun isShownView(boo: Boolean) {
                if (boo) {
                    setBottomPaddingRecy(108)
                } else {
                    setBottomPaddingRecy(0)
                }
            }

            override fun onClickBtn(items: List<VisitResponseAndroid>) {
                val ft = fragmentManager!!.beginTransaction()
                val prev = fragmentManager!!.findFragmentByTag("dialog")
                if (prev != null) {
                    ft.remove(prev)
                }
                ft.addToBackStack(null)
                shoppingBasketFragment = ShoppingBasketFragment()
                val bundle = Bundle()
                bundle.putParcelableArrayList("mList", items as ArrayList<out Parcelable?>)
                (shoppingBasketFragment as ShoppingBasketFragment).setListener { recyclerView!!.post { presenter!!.getVisits1() } }
                shoppingBasketFragment!!.setArguments(bundle)
                shoppingBasketFragment!!.show(ft, "dialog")
            }

            override fun onClickCross() {
                recyclerView!!.post { presenter!!.getVisits1() }
            }

            override fun limitReached() {
                (recyclerView!!.adapter as FinancesAdapter?)!!.setInaccessibleBtnPay()
            }

            override fun limitIsOver() {
                (recyclerView!!.adapter as FinancesAdapter?)!!.setAffordableBtnPay()
            }
        }
        presenter!!.getVisits1()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        presenter = FinancesAndServicesPresenter(this)
        val rootView = inflater.inflate(R.layout.activity_finances_and_services, container, false)
        recyclerView = rootView.findViewById(R.id.rv_sale)
        toolbar = rootView.findViewById(R.id.toolbar_sale)
        errMessage = rootView.findViewById(R.id.err_tv_message)
        errLoadBtn = rootView.findViewById(R.id.err_load_btn)
        toolbarLayout = rootView.findViewById(R.id.collapsing_toolbar_sale)
        rootEmpty = rootView.findViewById(R.id.rootEmpty)
        cfpv = rootView.findViewById(R.id.cfpv)
        setupToolbar()
        return rootView
    }

    private fun setupToolbar() {
        (context as MainActivity?)!!.setSupportActionBar(toolbar)
        val actionBar = (context as MainActivity?)!!.supportActionBar
        val appBarParams = toolbarLayout!!.layoutParams as AppBarLayout.LayoutParams
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp)
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setDisplayShowHomeEnabled(true)
        }
        toolbar!!.setNavigationOnClickListener { mainFragmentHelper!!.showNavigationMenu() }
    }

    private fun setBottomPaddingRecy(dp: Int) {
        var dp = dp
        if (dp != 0) dp = MainUtils.dpToPx(context, dp)
        recyclerView!!.setPadding(0, 0, 0, dp)
    }

    fun showErrorScreen() {
        if (recyclerView != null) recyclerView!!.visibility = View.GONE
        if (errMessage != null) errMessage!!.visibility = View.VISIBLE
        if (errLoadBtn != null) {
            errLoadBtn!!.visibility = View.VISIBLE
            errLoadBtn!!.setOnClickListener { v: View? -> presenter!!.getVisits1() }
        }
    }

    fun updateData(list: List<VisitResponseAndroid>, today: String, time: String) {
        if (list.size == 1 && list[0].nameSotr == null) {
            rootEmpty!!.visibility = View.VISIBLE
            recyclerView!!.visibility = View.GONE
            return
        }
        rootEmpty!!.visibility = View.GONE
        recyclerView!!.visibility = View.VISIBLE
        Collections.sort(list)
        Collections.reverse(list)
        val adapter = FinancesAdapter(
            context,
            list,
            { item, toPay -> if (toPay) cfpv!!.addItem(item) else cfpv!!.deleteItem(item) },
            time,
            today
        )
        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView!!.layoutManager = layoutManager
        recyclerView!!.itemAnimator = DefaultItemAnimator()
        recyclerView!!.adapter = adapter
    }

    /*    @Override
    public void closeBasketFragment() {
        recyclerView.post(() ->
                presenter.getVisits());
    }*/
    override fun userRefresh() {
        presenter!!.getVisits1()
    }

    override fun onDestroy() {
        presenter?.mainScope?.cancel()
        super.onDestroy()
    }
}