package com.medhelp.medhelp.ui.analysis_price_list

import android.widget.AdapterView
import androidx.recyclerview.widget.RecyclerView
import android.widget.Spinner
import com.google.android.material.appbar.CollapsingToolbarLayout
import android.widget.TextView
import com.medhelp.medhelp.ui.analysis_price_list.adapters.AnalisePriceAdapter
import com.medhelp.medhelp.ui._main_page.MainFragmentHelper
import android.app.Activity
import android.os.Bundle
import android.view.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.appcompat.app.AppCompatActivity
import com.medhelp.medhelp.R
import com.medhelp.medhelp.ui._main_page.MainActivity
import com.google.android.material.appbar.AppBarLayout
import com.medhelp.medhelp.utils.view.ItemListDecorator
import androidx.recyclerview.widget.DefaultItemAnimator
import com.medhelp.medhelp.ui.analysis_price_list.adapters.AnalisePriceSpinnerAdapter
import androidx.core.view.MenuItemCompat
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.medhelp.medhelp.ui.base.BaseFragment
import com.medhelp.newmedhelp.model.AnalisePriceResponse
import timber.log.Timber
import java.lang.Exception
import java.util.*

class AnalysisPriceListFragment : BaseFragment(), AdapterView.OnItemSelectedListener {
    lateinit var recyclerView: RecyclerView
    lateinit var spinner: Spinner
    lateinit var toolbar: Toolbar
    lateinit var toolbarLayout: CollapsingToolbarLayout
    lateinit var errMessage: TextView
    lateinit var errLoadBtn: TextView
    lateinit var presenter: AnalysisPriceListPresenter

    private var adapter: AnalisePriceAdapter? = null
    private var filterList: MutableList<String>? = null
    private var serviceCash: List<AnalisePriceResponse>? = null
    var mainFragmentHelper: MainFragmentHelper? = null
    var searchViewAndroidActionBar: SearchView? = null
    var searchViewItem: MenuItem? = null

    var activity: Activity? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.tag("my").i("Прейскурант анализов")
        activity = getActivity()
        mainFragmentHelper = context as MainFragmentHelper?
    }

    override fun setUp(view: View) {
        spinner!!.visibility = View.GONE
        setupToolbar()
        serviceCash = ArrayList()
        val layoutManager = LinearLayoutManager(context)
        recyclerView!!.layoutManager = layoutManager
        recyclerView!!.post { presenter.getAnalisePrice() }
        recyclerView!!.setOnTouchListener { v: View?, event: MotionEvent? ->
            try {
                val inputMethodManager =
                    context!!.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                if ((context as AppCompatActivity?)!!.currentFocus != null && (context as AppCompatActivity?)!!.currentFocus!!
                        .windowToken != null
                ) inputMethodManager.hideSoftInputFromWindow(
                    (context as AppCompatActivity?)!!.currentFocus!!.windowToken, 0
                )
            } catch (e: Exception) {
            }
            false
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        presenter = AnalysisPriceListPresenter( this)
        val rootView = inflater.inflate(R.layout.activity_analises_price, container, false)
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

    fun showErrorScreen() {
        recyclerView!!.visibility = View.GONE
        spinner!!.visibility = View.GONE
        errMessage!!.visibility = View.VISIBLE
        errLoadBtn!!.visibility = View.VISIBLE
        errLoadBtn!!.setOnClickListener { v: View? -> presenter!!.getAnalisePrice() }
    }

    private fun setupToolbar() {
        (context as MainActivity?)!!.setSupportActionBar(toolbar)
        val actionBar = (context as MainActivity?)!!.supportActionBar
        val appBarParams = toolbarLayout!!.layoutParams as AppBarLayout.LayoutParams
        toolbar!!.setOnClickListener { searchViewItem!!.expandActionView() }
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp)
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setDisplayShowHomeEnabled(true)
        }
        toolbar!!.setNavigationOnClickListener { mainFragmentHelper!!.showNavigationMenu() }
    }

    fun updateView(categories: List<String>, analise: List<AnalisePriceResponse>) {
        spinner!!.visibility = View.VISIBLE
        recyclerView!!.visibility = View.VISIBLE
        errMessage!!.visibility = View.GONE
        errLoadBtn!!.visibility = View.GONE
        adapter = AnalisePriceAdapter(serviceCash, context)
        recyclerView!!.addItemDecoration(ItemListDecorator(context))
        recyclerView!!.itemAnimator = DefaultItemAnimator()
        recyclerView!!.adapter = adapter
        filterList = mutableListOf()
        filterList!!.add(0, "Все анализы ")
        filterList!!.addAll(categories)
        val spinnerAdapter = AnalisePriceSpinnerAdapter(context, filterList)
        spinner!!.adapter = spinnerAdapter
        spinner!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                adapterView: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                if (searchViewAndroidActionBar != null && searchViewAndroidActionBar!!.query.toString() != "") searchViewAndroidActionBar!!.setQuery(
                    "",
                    false
                )
                if (position == 0) {
                    adapter!!.addItems(analise)
                } else {
                    val serviceList: MutableList<AnalisePriceResponse> = ArrayList()
                    for (serviceResponse in analise) {
                        if (serviceResponse.group == filterList!!.get(position)) {
                            serviceList.add(serviceResponse)
                        }
                    }
                    adapter!!.addItems(serviceList)
                }
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {
                searchViewAndroidActionBar!!.setQuery("", false)
            }
        }
        spinner!!.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                if (searchViewAndroidActionBar != null) searchViewAndroidActionBar!!.clearFocus()
                val inputMethodManager =
                    context!!.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                if ((context as AppCompatActivity?)!!.currentFocus != null && (context as AppCompatActivity?)!!.currentFocus!!
                        .windowToken != null
                ) inputMethodManager.hideSoftInputFromWindow(
                    (context as AppCompatActivity?)!!.currentFocus!!.windowToken, 0
                )
            }
            false
        }
    }

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
                //if(!newText.equals("")) {
                val filteredModelList = filterService(serviceCash, newText)
                adapter!!.setFilter(filteredModelList)
                //}
                return true
            }
        })
        searchViewAndroidActionBar!!.setOnQueryTextFocusChangeListener { v, hasFocus ->
            if (hasFocus) spinner!!.setSelection(
                0
            )
        }
    }

    private fun filterService(
        models: List<AnalisePriceResponse>?,
        query: String
    ): List<AnalisePriceResponse> {
        var query = query
        query = query.lowercase(Locale.getDefault())
        val filteredModelList: MutableList<AnalisePriceResponse> = ArrayList()
        for (model in models!!) {
            val text = model.title!!.lowercase(Locale.getDefault())
            if (text.contains(query)) {
                filteredModelList.add(model)
            }
        }
        return filteredModelList
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {}
    override fun onNothingSelected(parent: AdapterView<*>?) {}
    override fun userRefresh() {
        presenter!!.getAnalisePrice()
    }

    companion object {
        fun newInstance(): Fragment {
            return AnalysisPriceListFragment()
        }
    }
}