package com.medhelp.medhelp.ui.doctor.service_activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.MenuItemCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.medhelp.medhelp.R
import com.medhelp.medhelp.data.model._heritable.ServiceResponseAndroid
import com.medhelp.medhelp.ui._main_page.MainActivity
import com.medhelp.medhelp.ui.base.BaseActivity
import com.medhelp.medhelp.ui.schedule.ScheduleFragment
import com.medhelp.medhelp.utils.view.ItemListDecorator
import com.medhelp.newmedhelp.model.CategoryResponse
import kotlinx.coroutines.cancel
import timber.log.Timber
import java.lang.Exception
import java.util.*

class ServiceActivity : BaseActivity(), AdapterView.OnItemSelectedListener {
    lateinit var presenter: ServicePresenter
    lateinit var recyclerView: RecyclerView
    lateinit var errMessage: TextView
    lateinit var errLoadBtn: Button
    lateinit var spinner: Spinner
    lateinit var toolbar: Toolbar
    lateinit var toolbarLayout: CollapsingToolbarLayout
    lateinit var noServices: ConstraintLayout

    private var adapter: ServiceAdapter? = null
    private var filterList: MutableList<CategoryResponse>? = null
    private var serviceCash: List<ServiceResponseAndroid>? = null
    private var idDoctor = 0
    private var idService = 0
    var idBranch = 0
    var idUser = 0
    var backPage = 0
    protected override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service)
        Timber.tag("my").i("Прейскурант услуг для конкретного доктора")
        initValue()
        setUp()
    }

    private fun initValue() {
        recyclerView = findViewById<RecyclerView>(R.id.rv_service)
        errMessage = findViewById<TextView>(R.id.err_tv_message)
        errLoadBtn = findViewById<Button>(R.id.err_load_btn)
        spinner = findViewById<Spinner>(R.id.spinner_service)
        toolbar = findViewById<Toolbar>(R.id.toolbar_service)
        toolbarLayout = findViewById<CollapsingToolbarLayout>(R.id.collapsing_toolbar_service)
        noServices = findViewById<ConstraintLayout>(R.id.noServices)
    }

    override fun setUp() {
        presenter = ServicePresenter( this)
       // idService = getIntent().getExtras()?.getInt(EXTRA_DATA_SERVICE)
        spinner.setVisibility(View.GONE)
        setupToolbar()
        serviceCash = ArrayList<ServiceResponseAndroid>()
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        recyclerView.setLayoutManager(layoutManager)
        val bundle: Bundle? = getIntent().getExtras()
        if (bundle != null) {
            backPage = bundle.getInt(ScheduleFragment.EXTRA_BACK_PAGE)
            idDoctor = bundle.getInt(EXTRA_DATA_ID_DOCTOR, 0)
            try {
                idBranch = bundle.getInt(EXTRA_DATA_ID_BRANCH)
                idUser = bundle.getInt(EXTRA_DATA_ID_USER)
            } catch (e: Exception) {
                // all right
            } finally {
                if (idBranch == 0 || idUser == 0) {
                    idBranch = presenter!!.prefManager.currentUserInfo!!.idBranch!!
                    idUser = presenter!!.prefManager.currentUserInfo!!.idUser!!
                }
            }

            // presenter.getFilesForRecy(idDoctor,idBranch,idUser);
        }
    }

    protected override fun onResume() {
        super.onResume()
        if (searchViewAndroidActionBar != null) {
            searchViewAndroidActionBar!!.setQuery("", false)
            searchViewItem!!.collapseActionView()
        }
        presenter!!.getData(idDoctor, idBranch, idUser)
    }

    override fun onDestroy() {
        presenter.mainScope.cancel()
        super.onDestroy()
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        val actionBar: ActionBar? = getSupportActionBar()
        val appBarParams: AppBarLayout.LayoutParams =
            toolbarLayout.getLayoutParams() as AppBarLayout.LayoutParams
        toolbar!!.setOnClickListener { v: View? -> searchViewItem!!.expandActionView() }
        toolbar!!.setNavigationOnClickListener { onBackPressed() }
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back_white_24dp)
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setDisplayShowHomeEnabled(true)
        }
    }

    var searchViewItem: MenuItem? = null
    var searchViewAndroidActionBar: SearchView? = null
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = getMenuInflater()
        inflater.inflate(R.menu.menu_search, menu)
        searchViewItem = menu.findItem(R.id.action_search)
        searchViewAndroidActionBar = MenuItemCompat.getActionView(searchViewItem) as SearchView
        if (searchViewAndroidActionBar == null) return false
        searchViewAndroidActionBar!!.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                searchViewAndroidActionBar!!.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                spinner.setSelection(0)
                val filteredModelList: List<ServiceResponseAndroid> = filterService(serviceCash!!, newText)
                if (filteredModelList != null && filteredModelList.size > 0) {
                    adapter!!.setFilter(filteredModelList)
                }
                return true
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    override fun onItemSelected(adapterView: AdapterView<*>?, view: View, i: Int, l: Long) {}
    override fun onNothingSelected(adapterView: AdapterView<*>?) {}
    var spinnerAdapter: ServiceSpinnerAdapter? = null
    var latchFirstChoiceSpinner = false
    fun updateView(categories: List<CategoryResponse>?, services: MutableList<ServiceResponseAndroid>) {
        errMessage.setVisibility(View.GONE)
        errLoadBtn!!.visibility = View.GONE
        spinner.setVisibility(View.VISIBLE)
        recyclerView.setVisibility(View.VISIBLE)
        if (services[0].title == null) {
            if (noServices != null) noServices.setVisibility(View.VISIBLE)
            return
        }
        adapter = ServiceAdapter(serviceCash, idDoctor, object : ServiceAdapter.ItemListener {
            override fun clickFab(item: ServiceResponseAndroid) {
                presenter!!.changeFabFavorites(item)
            }

            override fun clickRecord(holder: ServiceAdapter.ViewHolder, service: Int, limit: Int) {
                presenter!!.testToSpam(holder, service, limit)
            }
        })
        recyclerView.addItemDecoration(ItemListDecorator(this))
        recyclerView.setItemAnimator(DefaultItemAnimator())
        recyclerView.setAdapter(adapter)
        filterList = ArrayList<CategoryResponse>()
        filterList!!.add(0, CategoryResponse("Все"))
        filterList!!.add(1, CategoryResponse("Избранные услуги"))
        filterList!!.addAll(categories!!)
        spinnerAdapter = ServiceSpinnerAdapter(this, filterList)
        spinner.setAdapter(spinnerAdapter)
        if (!latchFirstChoiceSpinner) {
            setSelectedItemForSpinner()
            latchFirstChoiceSpinner = true
        }
        spinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                adapterView: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                if (position == 0) {
                    adapter!!.addItems(sortByWight(services))
                } else if (position == 1) {
                    adapter!!.addItems(selectItemWithTab(sortByWight(services)))
                } else {
                    val serviceList: MutableList<ServiceResponseAndroid> = ArrayList<ServiceResponseAndroid>()
                    for (serviceResponse in services) {
                        if (serviceResponse.idSpec == filterList!![position].id) {
                            serviceList.add(serviceResponse)
                        }
                    }
                    adapter!!.addItems(sortByWight(serviceList))
                }
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
        })
    }

    private fun sortByWight(list: MutableList<ServiceResponseAndroid>): List<ServiceResponseAndroid> {
        var tmp: ServiceResponseAndroid
        for (j in 0..list.size - 2) {
            var i = list.size - 1
            while (j < i) {
                val wight1 = if (list[i].poryadok == 0) 0 else 11 - list[i].poryadok!!
                val wight2 =
                    if (list[i - 1].poryadok == 0) 0 else 11 - list[i - 1].poryadok!!
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

    private fun selectItemWithTab(services: List<ServiceResponseAndroid>): List<ServiceResponseAndroid> {
        val newList: MutableList<ServiceResponseAndroid> = ArrayList<ServiceResponseAndroid>()
        for (item in services) {
            if (item.favorites == "1") {
                newList.add(item)
            }
        }
        return newList
    }

    private fun setSelectedItemForSpinner() {
        var i = 0
        while (i < filterList!!.size) {
            if (filterList!![i].id === idService) {
                break
            }
            i++
        }
        if (i == filterList!!.size) {
            i = 0
        }
        spinner.setSelection(i)
    }

    fun showErrorScreen() {
        recyclerView.setVisibility(View.GONE)
        spinner.setVisibility(View.GONE)
        errMessage.setVisibility(View.VISIBLE)
        errLoadBtn!!.visibility = View.VISIBLE
        errLoadBtn!!.setOnClickListener { v: View? ->
            presenter!!.getData(
                idDoctor,
                idBranch,
                idUser
            )
        }
    }

    fun refreshRecy() {
        adapter!!.notifyDataSetChanged()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.home) {
            super.onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
//            presenter.unSubscribe();
//            super.onBackPressed();
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra(MainActivity.POINTER_TO_PAGE, backPage)
        startActivity(intent)
    }

    private fun filterService(models: List<ServiceResponseAndroid>, query: String): List<ServiceResponseAndroid> {
        var query = query
        query = query.lowercase(Locale.getDefault())
        val filteredModelList: MutableList<ServiceResponseAndroid> = ArrayList<ServiceResponseAndroid>()
        for (model in models) {
            val text: String = model.title!!.lowercase(Locale.getDefault())
            if (text.contains(query)) {
                filteredModelList.add(model)
            }
        }
        return filteredModelList
    }

    override fun userRefresh() {}

    companion object {
        const val EXTRA_DATA_ID_DOCTOR = "EXTRA_DATA_ID_DOCTOR"
        const val EXTRA_DATA_SERVICE = "EXTRA_DATA_SERVICE"
        const val EXTRA_DATA_ID_BRANCH = "EXTRA_DATA_ID_BRANCH"
        const val EXTRA_DATA_ID_USER = "EXTRA_DATA_ID_USER"
        @JvmStatic
        fun getStartIntent(context: Context?): Intent {
            return Intent(context, ServiceActivity::class.java)
        }
    }
}