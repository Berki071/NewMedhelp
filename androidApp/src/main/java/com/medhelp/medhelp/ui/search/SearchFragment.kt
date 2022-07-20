package com.medhelp.medhelp.ui.search

import android.widget.AdapterView
import com.medhelp.medhelp.ui.search.SearchPresenter
import com.medhelp.medhelp.ui._main_page.MainFragmentHelper
import androidx.recyclerview.widget.RecyclerView
import android.widget.Spinner
import com.google.android.material.appbar.CollapsingToolbarLayout
import android.widget.TextView
import com.medhelp.medhelp.ui.search.recy_spinner.SearchAdapter
import com.medhelp.newmedhelp.model.CategoryResponse
import android.app.Activity
import android.content.Context
import android.os.Bundle
import com.medhelp.medhelp.R
import androidx.recyclerview.widget.LinearLayoutManager
import com.medhelp.medhelp.ui._main_page.MainActivity
import com.google.android.material.appbar.AppBarLayout
import androidx.core.view.MenuItemCompat
import com.medhelp.medhelp.utils.view.ItemListDecorator
import androidx.recyclerview.widget.DefaultItemAnimator
import com.medhelp.medhelp.ui.search.recy_spinner.SearchSpinnerAdapter
import it.sephiroth.android.library.xtooltip.Tooltip
import it.sephiroth.android.library.xtooltip.ClosePolicy
import android.content.Intent
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import com.medhelp.medhelp.data.model._heritable.ServiceResponseAndroid
import com.medhelp.medhelp.ui.base.BaseFragment
import com.medhelp.medhelp.ui.search.SearchFragment
import com.medhelp.newmedhelp.model.ServiceResponse
import kotlinx.coroutines.cancel
import timber.log.Timber
import java.util.*

class SearchFragment : BaseFragment(), AdapterView.OnItemSelectedListener {
    var presenter: SearchPresenter? = null
    var mainFragmentHelper: MainFragmentHelper? = null
    var recyclerView: RecyclerView? = null
    var spinner: Spinner? = null
    var toolbar: Toolbar? = null
    var toolbarLayout: CollapsingToolbarLayout? = null
    var errMessage: TextView? = null
    var errLoadBtn: TextView? = null
    private var adapter: SearchAdapter? = null
    private var filterList: MutableList<CategoryResponse>? = null
    private var serviceCash: List<ServiceResponseAndroid>? = null
    var activity: Activity? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.tag("my").i("Прейскурант услуг")
        activity = getActivity()
        mainFragmentHelper = context as MainFragmentHelper?
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        presenter = SearchPresenter( this)
        val rootView = inflater.inflate(R.layout.activity_search, container, false)
        initValue(rootView)
        setHasOptionsMenu(true)
        return rootView
    }

    private fun initValue(v: View) {
        recyclerView = v.findViewById(R.id.rv_search)
        spinner = v.findViewById(R.id.spinner_search)
        toolbar = v.findViewById(R.id.toolbar_search)
        toolbarLayout = v.findViewById(R.id.collapsing_toolbar_search)
        errMessage = v.findViewById(R.id.err_tv_message)
        errLoadBtn = v.findViewById(R.id.err_load_btn)
    }

    override fun setUp(view: View) {
        spinner!!.visibility = View.GONE
        setupToolbar()
        serviceCash = ArrayList()
        val layoutManager = LinearLayoutManager(context)
        recyclerView!!.layoutManager = layoutManager
    }

    override fun onResume() {
        super.onResume()
        if (searchViewAndroidActionBar == null) {
            presenter!!.getData()
        }
    }

    private fun setupToolbar() {
        (context as MainActivity?)!!.setSupportActionBar(toolbar)
        toolbar!!.setOnClickListener { v: View? -> searchViewItem!!.expandActionView() }
        showTooltip(toolbar!!)
        val actionBar = (context as MainActivity?)!!.supportActionBar
        val appBarParams = toolbarLayout!!.layoutParams as AppBarLayout.LayoutParams
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp)
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setDisplayShowHomeEnabled(true)
        }
        toolbar!!.setNavigationOnClickListener { mainFragmentHelper!!.showNavigationMenu() }
    }

    var searchViewItem: MenuItem? = null
    var searchViewAndroidActionBar: SearchView? = null
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_search, menu)
        searchViewItem = menu.findItem(R.id.action_search)
        searchViewAndroidActionBar = MenuItemCompat.getActionView(searchViewItem) as SearchView
        if (searchViewAndroidActionBar == null) return
        searchViewAndroidActionBar!!.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                searchViewAndroidActionBar!!.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                spinner!!.setSelection(0)
                val filteredModelList = filterService(serviceCash, newText)
                adapter!!.setFilter(filteredModelList)
                return true
            }
        })
    }

    override fun onItemSelected(adapterView: AdapterView<*>?, view: View, i: Int, l: Long) {}
    override fun onNothingSelected(adapterView: AdapterView<*>?) {}
    fun showErrorScreen() {
        recyclerView!!.visibility = View.GONE
        spinner!!.visibility = View.GONE
        errMessage!!.visibility = View.VISIBLE
        errLoadBtn!!.visibility = View.VISIBLE
        errLoadBtn!!.setOnClickListener { v: View? -> presenter!!.getData() }
    }

    fun updateView(categories: List<CategoryResponse>, services: List<ServiceResponseAndroid>) {
        spinner!!.visibility = View.VISIBLE
        recyclerView!!.visibility = View.VISIBLE
        errMessage!!.visibility = View.GONE
        errLoadBtn!!.visibility = View.GONE
        adapter = SearchAdapter(serviceCash, context, object : SearchAdapter.ItemListener {
            override fun clickFab(item: ServiceResponse) {
                presenter!!.changeFabFavorites(item)
            }

            override fun clickRecord(holder: SearchAdapter.ViewHolder, service: Int, limit: Int) {
                presenter!!.testToSpam(holder, service, limit)
            }
        })
        recyclerView!!.addItemDecoration(ItemListDecorator(context))
        recyclerView!!.itemAnimator = DefaultItemAnimator()
        recyclerView!!.adapter = adapter
        filterList = ArrayList()
        filterList?.add(0, CategoryResponse("Все специальности"))
        filterList?.add(1, CategoryResponse("Избранные услуги"))
        filterList?.addAll(categories)
        val spinnerAdapter = SearchSpinnerAdapter(context, filterList)
        spinner!!.adapter = spinnerAdapter
        spinner!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                adapterView: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                if (position == 0) {
                    //adapter.addItems(sortByWight(services));
                    Collections.sort(services)
                    adapter!!.addItems(services)
                } else if (position == 1) {
                    adapter!!.addItems(sortByWight(selectItemWithTab(services)))
                } else {
                    val serviceList: MutableList<ServiceResponseAndroid> = ArrayList()
                    for (serviceResponse in services) {
                        if (serviceResponse.idSpec == filterList!!.get(position).id) {
                            serviceList.add(serviceResponse)
                        }
                    }
                    adapter!!.addItems(sortByWight(serviceList))
                }
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
        }
    }

    private fun sortByWight(list: MutableList<ServiceResponseAndroid>): List<ServiceResponseAndroid> {
        var tmp: ServiceResponseAndroid
        for (j in 0..list.size - 2) {
            var i = list.size - 1
            while (j < i) {
                val wight1 = if (list[i].poryadok == 0) 0 else 11 - list[i].poryadok!!
                val wight2 = if (list[i - 1].poryadok == 0) 0 else 11 - list[i - 1].poryadok!!
                if (wight1 > wight2) {
                    tmp = list[i - 1]
                    list[i - 1] = list[i]
                    list[i] = tmp
                }
                i--
            }
        }
        return list
    }

    private fun selectItemWithTab(services: List<ServiceResponseAndroid>): MutableList<ServiceResponseAndroid> {
        val newList: MutableList<ServiceResponseAndroid> = ArrayList()
        for (item in services) {
            if (item.favorites == "1") {
                newList.add(item)
            }
        }
        return newList
    }

    fun refreshRecy() {
        adapter!!.notifyDataSetChanged()
    }

    private fun filterService(
        models: List<ServiceResponseAndroid>?,
        query: String
    ): List<ServiceResponseAndroid> {
        var query = query
        query = query.lowercase(Locale.getDefault())
        val filteredModelList: MutableList<ServiceResponseAndroid> = ArrayList()
        for (model in models!!) {
            val text = model.title!!.lowercase(Locale.getDefault())
            if (text.contains(query)) {
                filteredModelList.add(model)
            }
        }
        return filteredModelList
    }

    private fun showTooltip(view: View) {
        //+
        view.post {
            if (presenter!!.prefManager.showTooltipSearchLoupe) {
                SearchFragmentHint.show(view)
                presenter!!.prefManager.setShowTooltipSearchLoupe()
            }
        }
    }

    override fun userRefresh() {
        presenter!!.getData()
    }

    override fun onDestroy() {
        presenter?.mainScope?.cancel()
        super.onDestroy()
    }

    companion object {
        fun getStartIntent(context: Context?): Intent {
            return Intent(context, SearchFragment::class.java)
        }
    }
}