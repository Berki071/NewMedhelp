package com.medhelp.medhelp.ui.doctor

import com.medhelp.medhelp.ui._main_page.MainFragmentHelper
import com.medhelp.medhelp.ui.doctor.adapters.DoctorsAdapter
import com.google.android.material.appbar.CollapsingToolbarLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.constraintlayout.widget.ConstraintLayout
import com.medhelp.medhelp.data.model.AllDoctorsResponse
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.*
import com.medhelp.medhelp.ui.doctor.DoctorsPresenter
import com.medhelp.medhelp.R
import androidx.recyclerview.widget.LinearLayoutManager
import com.medhelp.medhelp.utils.view.ItemListDecorator
import androidx.recyclerview.widget.DefaultItemAnimator
import com.medhelp.medhelp.utils.view.RecyclerViewTouchListener
import com.medhelp.medhelp.utils.view.RecyclerViewClickListener
import com.medhelp.medhelp.ui.doctor.adapters.DocSpinnerAdapter
import com.medhelp.medhelp.data.model.CategoryResponse
import android.widget.*
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.MenuItemCompat
import androidx.fragment.app.Fragment
import com.medhelp.medhelp.ui._main_page.MainActivity
import com.google.android.material.appbar.AppBarLayout
import com.medhelp.medhelp.ui.base.BaseFragment
import com.medhelp.medhelp.ui.doctor.DoctorsFragment
import com.medhelp.medhelp.ui.doctor.alertDoc.AlertCardDoctor
import timber.log.Timber
import java.util.*

class DoctorsFragment : BaseFragment(), AdapterView.OnItemSelectedListener {
    var presenter: DoctorsPresenter? = null
    var mainFragmentHelper: MainFragmentHelper? = null
    var adapter: DoctorsAdapter? = null
    var toolbar: Toolbar? = null
    var toolbarLayout: CollapsingToolbarLayout? = null
    var spinner: Spinner? = null
    var errMessage: TextView? = null
    var errLoadBtn: Button? = null
    var recyclerView: RecyclerView? = null
    var rootErr: LinearLayout? = null
    var rootEmpty: ConstraintLayout? = null
    private var resList: List<AllDoctorsResponse>? = null
    private var activity: Activity? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.tag("my").i("Специалисты")
        activity = getActivity()
        mainFragmentHelper = context as MainFragmentHelper?
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        presenter = DoctorsPresenter(this)
        val rootView = inflater.inflate(R.layout.activity_doctors, container, false)
        initValue(rootView)
        setHasOptionsMenu(true)
        return rootView
    }

    private fun initValue(v: View) {
        toolbar = v.findViewById(R.id.toolbar_doctors)
        toolbarLayout = v.findViewById(R.id.collapsing_toolbar_doctors)
        spinner = v.findViewById(R.id.spinner_doctors)
        errMessage = v.findViewById(R.id.err_tv_message)
        errLoadBtn = v.findViewById(R.id.err_load_btn)
        recyclerView = v.findViewById(R.id.rv_doctors)
        rootErr = v.findViewById(R.id.rootErr)
        rootEmpty = v.findViewById(R.id.rootEmpty)
    }

    override fun setUp(view: View) {
        presenter = DoctorsPresenter(this)
        setupToolbar()
        resList = ArrayList()
        presenter!!.getSpecialtyByCenter()
        val layoutManager = LinearLayoutManager(context)
        recyclerView!!.layoutManager = layoutManager
    }

    fun updateView(response: List<AllDoctorsResponse>?) {
        errMessage!!.visibility = View.GONE
        errLoadBtn!!.visibility = View.GONE
        rootErr!!.visibility = View.GONE
        recyclerView!!.visibility = View.VISIBLE
        if (response == null || response.size == 0) {
            recyclerView!!.visibility = View.GONE
            rootEmpty!!.visibility = View.VISIBLE
            return
        }
        resList = response
        adapter = DoctorsAdapter(response)
        recyclerView!!.addItemDecoration(ItemListDecorator(context))
        recyclerView!!.itemAnimator = DefaultItemAnimator()
        recyclerView!!.adapter = adapter
        recyclerView!!.addOnItemTouchListener(
            RecyclerViewTouchListener(
                context,
                recyclerView,
                object : RecyclerViewClickListener {
                    override fun onClick(view: View, position: Int) {
                        val doc = adapter!!.getItem(position)
                        val idSpec = (spinner!!.adapter as DocSpinnerAdapter).getIdSpec(
                            spinner!!.selectedItemPosition
                        )
                        AlertCardDoctor(context, doc, idSpec, presenter!!.getUserToken())
                    }

                    override fun onLongClick(view: View, position: Int) {}
                })
        )
    }

    var spinnerAdapter: DocSpinnerAdapter? = null
    fun updateSpecialty(response: List<CategoryResponse>) {
        spinnerAdapter = DocSpinnerAdapter(context, response)
        spinner!!.adapter = spinnerAdapter
        spinner!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                adapterView: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                if (position == 0) {
                    presenter!!.getDoctorList(-1)
                } else {
                    presenter!!.getDoctorList(response[position - 1].id)
                }
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
        }
    }

    var searchViewItem: MenuItem? = null
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        //inflater.inflate(R.menu.menu_search, menu);
        inflater.inflate(R.menu.menu_search, menu)
        searchViewItem = menu.findItem(R.id.action_search)
        val searchViewAndroidActionBar = MenuItemCompat.getActionView(searchViewItem) as SearchView
            ?: return
        searchViewAndroidActionBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                searchViewAndroidActionBar.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                spinner!!.setSelection(0)
                val filteredModelList = filterDoctor(resList, newText)
                adapter!!.setFilter(filteredModelList)
                return true
            }
        })
    }

    override fun onItemSelected(adapterView: AdapterView<*>?, view: View, i: Int, l: Long) {}
    override fun onNothingSelected(adapterView: AdapterView<*>?) {}
    private fun filterDoctor(
        models: List<AllDoctorsResponse>?,
        query: String
    ): List<AllDoctorsResponse> {
        var query = query
        query = query.lowercase(Locale.getDefault())
        val filteredModelList: MutableList<AllDoctorsResponse> = ArrayList()
        for (model in models!!) {
            if (model.fio_doctor != null) {
                val text = model.fio_doctor.lowercase(Locale.getDefault())
                if (text.contains(query)) {
                    filteredModelList.add(model)
                }
            }
        }
        return filteredModelList
    }

    private fun setupToolbar() {
        (context as MainActivity?)!!.setSupportActionBar(toolbar)
        val actionBar = (context as MainActivity?)!!.supportActionBar
        toolbarLayout!!.isTitleEnabled = false
        actionBar!!.title = "Поиск по сотрудникам"
        toolbar!!.setOnClickListener { v: View? -> searchViewItem!!.expandActionView() }
        val appBarParams = toolbarLayout!!.layoutParams as AppBarLayout.LayoutParams
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp)
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setDisplayShowHomeEnabled(true)
        }
        toolbar!!.setNavigationOnClickListener { v: View? -> mainFragmentHelper!!.showNavigationMenu() }
    }

    fun showErrorScreen() {
        recyclerView!!.visibility = View.GONE
        errMessage!!.visibility = View.VISIBLE
        errLoadBtn!!.visibility = View.VISIBLE
        rootErr!!.visibility = View.VISIBLE
        errLoadBtn!!.visibility = View.VISIBLE
        errLoadBtn!!.setOnClickListener { v: View? -> presenter!!.getSpecialtyByCenter() }
    }

    override fun userRefresh() {
        presenter!!.getSpecialtyByCenter()
    }

    companion object {
        fun newInstance(): Fragment {
            return DoctorsFragment()
        }
    }
}