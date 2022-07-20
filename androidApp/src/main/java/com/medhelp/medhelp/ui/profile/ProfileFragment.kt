package com.medhelp.medhelp.ui.profile

import com.medhelp.medhelp.ui.profile.recycler.ItemClickListener
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RapidFloatingActionContentLabelList.OnRapidFloatingActionContentLabelListListener
import com.google.android.material.appbar.CollapsingToolbarLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionLayout
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionButton
import com.medhelp.medhelp.ui.view.ChosenForPurchaseView
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionHelper
import android.app.Activity
import com.medhelp.medhelp.ui._main_page.MainFragmentHelper
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.medhelp.medhelp.R
import com.medhelp.medhelp.ui.view.ChosenForPurchaseView.ChosenForPurchaseViewListener
import com.medhelp.newmedhelp.model.VisitResponseAndroid
import com.medhelp.medhelp.ui.view.shopping_basket.ShoppingBasketFragment
import android.os.Parcelable
import com.medhelp.medhelp.ui.profile.recycler.ProfileAdapter
import com.medhelp.medhelp.utils.main.MainUtils
import android.content.Intent
import android.graphics.Color
import com.medhelp.shared.model.CenterResponse
import com.medhelp.medhelp.utils.workToFile.show_file.ShowFile2.BuilderImage
import com.medhelp.medhelp.utils.workToFile.show_file.ShowFile2
import com.medhelp.medhelp.utils.workToFile.show_file.ShowFile2.ShowListener
import com.medhelp.medhelp.ui._main_page.MainActivity
import com.google.android.material.appbar.AppBarLayout
import com.medhelp.medhelp.ui.profile.recycler.ProfileParentModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.DefaultItemAnimator
import com.medhelp.medhelp.ui.profile.allertDialog.AlertForCancel
import android.text.Html
import com.medhelp.medhelp.ui.doctor.service_activity.ServiceActivity
import com.medhelp.medhelp.ui.schedule.ScheduleFragment
import com.medhelp.medhelp.ui.profile.recycler.ProfileVisitViewHolder
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RFACLabelItem
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RapidFloatingActionContentLabelList
import android.net.Uri
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.medhelp.medhelp.Constants
import com.medhelp.medhelp.ui.base.BaseFragment
import kotlinx.coroutines.cancel
import timber.log.Timber
import java.io.File
import java.util.*

class ProfileFragment : BaseFragment(), ItemClickListener, OnRapidFloatingActionContentLabelListListener<Any?> {
    companion object {
        fun newInstance(): Fragment {
            return ProfileFragment()
        }
    }

    var toolbar: Toolbar? = null
    var toolbarLayout: CollapsingToolbarLayout? = null
    var logoProfile: ImageView? = null
    var centerBranch: TextView? = null
    var centerPhone: TextView? = null
    var icoPhone: ImageView? = null
    var centerSite: TextView? = null
    var icoSite: ImageView? = null
    var errMessage: TextView? = null
    var errLoadBtn: Button? = null
    var recyclerView: RecyclerView? = null
    var swipeProfile: SwipeRefreshLayout? = null
    var noPosts: ConstraintLayout? = null
    var rfaLayout: RapidFloatingActionLayout? = null
    var rfaBtn: RapidFloatingActionButton? = null
    var cfpv: ChosenForPurchaseView? = null
    private var rfabHelper: RapidFloatingActionHelper? = null
    private var shoppingBasketFragment: DialogFragment? = null
    var activity: Activity? = null
    var presenter: ProfilePresenter? = null
    var mainFragmentHelper: MainFragmentHelper? = null
    var refreshListener = OnRefreshListener {
        cfpv!!.closeView()
        presenter!!.getVisits1()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.tag("my").i("Главная страница")
        activity = getActivity()
        presenter = ProfilePresenter(requireContext(), this)
        mainFragmentHelper = context as MainFragmentHelper?
        clearChatNotification()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.activity_profile, container, false)
        initValue(rootView)
        return rootView
    }

    private fun initValue(v: View) {
        toolbar = v.findViewById(R.id.toolbar_profile)
        toolbarLayout = v.findViewById(R.id.collapsing_toolbar_profile)
        logoProfile = v.findViewById(R.id.logo_center_profile)
        centerBranch = v.findViewById(R.id.tv_branch_profile)
        centerPhone = v.findViewById(R.id.tv_phone_profile)
        icoPhone = v.findViewById(R.id.icoPhone)
        centerSite = v.findViewById(R.id.tv_site_profile)
        icoSite = v.findViewById(R.id.icoSite)
        errMessage = v.findViewById(R.id.err_tv_message)
        errLoadBtn = v.findViewById(R.id.err_load_btn)
        recyclerView = v.findViewById(R.id.rv_profile)
        swipeProfile = v.findViewById(R.id.swipe_profile)
        noPosts = v.findViewById(R.id.noPosts)
        rfaLayout = v.findViewById(R.id.activity_main_rfal)
        rfaBtn = v.findViewById(R.id.activity_main_rfab)
        cfpv = v.findViewById(R.id.cfpv)
    }

    override fun setUp(view: View) {
        presenter!!.updateHeaderInfo()
        setupRefresh()
        initRFAB()
        testToolTip()
        recyclerView!!.post { presenter!!.getVisits1() }
        cfpv!!.newListener(object : ChosenForPurchaseViewListener {
            override fun isShownView(boo: Boolean) {
                if (boo) {
                    setBottomPaddingRecy(108)
                    rfaLayout!!.visibility = View.GONE
                } else {
                    setBottomPaddingRecy(0)
                    rfaLayout!!.visibility = View.VISIBLE
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
                bundle.putParcelableArrayList("mList", items as ArrayList<out Parcelable?>?)
                shoppingBasketFragment!!.setArguments(bundle)
                (shoppingBasketFragment as ShoppingBasketFragment).setListener { recyclerView!!.post { presenter!!.getVisits1() } }
                shoppingBasketFragment!!.show(ft, "dialog")
            }


            override fun onClickCross() {
                recyclerView!!.post { presenter!!.getVisits1() }
            }

            override fun limitReached() {
                (recyclerView!!.adapter as ProfileAdapter?)!!.setInaccessibleBtnPay()
            }

            override fun limitIsOver() {
                (recyclerView!!.adapter as ProfileAdapter?)!!.setAffordableBtnPay()
            }
        })
        testExistIdRecord()
    }

    private fun testExistIdRecord() {
//        try
//        {
//            if(getArguments()==null)
//                return;
//
//            int idRec=getArguments().getInt(ReminderAdmNoti.ID_RECORD,-1);
//            int idBranch=getArguments().getInt(ReminderAdmNoti.ID_BRANCH,-1);
//            int idUser=getArguments().getInt(ReminderAdmNoti.ID_USER,-1);
//
//            if(idRec!=-1)
//            {
//                cancelBtnClick(idUser,idRec,idBranch);
//            }
//        }
//        catch (Exception e) {}
    }

    override fun userRefresh() {
        presenter!!.getVisits1()
    }

    private fun clearChatNotification() {
//        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        notificationManager.cancel(ShowNotification.ID_REMINDER_GROUP);
    }

    //    @Override
    //    public void closeBasketFragment() {
    //        recyclerView.post(() ->
    //                presenter.getVisits());
    //    }
    private fun setBottomPaddingRecy(dp: Int) {
        var dp = dp
        if (dp != 0) dp = MainUtils.dpToPx(context, dp)
        recyclerView!!.setPadding(0, 0, 0, dp)
    }

    private fun testToolTip() {
        centerBranch!!.post {
            if (presenter!!.prefManager.showTooltipProfile) {
                ProfileShowTooltip().show1(centerBranch!!)
                presenter!!.prefManager.setShowTooltipProfile()
            }
        }
    }

    private fun callToCenter(phone: String?) {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:$phone")
        startActivity(intent)
    }

    private fun callToSite(address: String?) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("http://$address"))
        startActivity(browserIntent)
    }

    fun updateHeader(response: CenterResponse) {
        centerBranch!!.text = presenter!!.getCurrentHospitalBranch()
        if (response.site != null && response.site != "") {
            centerSite!!.text = response.site
            centerSite!!.setOnClickListener { click: View? -> callToSite(response.site) }
            icoSite!!.setOnClickListener { click: View? -> callToSite(response.site) }
        }
        if (response.phone != null) {
            centerPhone!!.text = response.phone
            centerPhone!!.setOnClickListener { v: View? -> callToCenter(response.phone) }
            icoPhone!!.setOnClickListener { v: View? -> callToCenter(response.phone) }
        }
        BuilderImage(context)
            .setType(ShowFile2.TYPE_ICO)
            .load(response.logo)
            .token(presenter!!.getUserToken())
            .imgError(R.drawable.sh_center)
            .into(logoProfile)
            .setListener(object : ShowListener {
                override fun complete(file: File) {}
                override fun error(error: String) {}
            })
            .build()
        setupToolbar(response)
    }

    private fun setupToolbar(response: CenterResponse) {
        (context as MainActivity?)!!.setSupportActionBar(toolbar)
        val actionBar = (context as MainActivity?)!!.supportActionBar
        toolbarLayout!!.isTitleEnabled = false
        actionBar!!.title = response.title
        val appBarParams = toolbarLayout!!.layoutParams as AppBarLayout.LayoutParams
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp)
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setDisplayShowHomeEnabled(true)
        }
        toolbar!!.setNavigationOnClickListener { mainFragmentHelper!!.showNavigationMenu() }
    }

    fun updateData(response: List<VisitResponseAndroid>, today: String, time: String) {

        // new EvaluateTheDoctor(this,response.get(0));
        val parentModels = ArrayList<ProfileParentModel>()
        val layoutManager = LinearLayoutManager(context)
        recyclerView!!.layoutManager = layoutManager
        recyclerView!!.visibility = View.VISIBLE
        errMessage!!.visibility = View.GONE
        errLoadBtn!!.visibility = View.GONE
        if (response.size > 0 && response[0].nameSotr != null && today != null) {
            noPosts!!.visibility = View.GONE
            val actualReceptions: MutableList<VisitResponseAndroid> = ArrayList()
            val latestReceptions: MutableList<VisitResponseAndroid> = ArrayList()
            val currentTime = System.currentTimeMillis()
            for (visit in response) {
                if (visit.getTimeMills()!! >= currentTime) {
                    actualReceptions.add(visit)
                } else {
                    latestReceptions.add(visit)
                }
            }
            if (actualReceptions.size > 0) {
                Collections.sort(actualReceptions)
                parentModels.add(ProfileParentModel("Предстоящие", actualReceptions))
            }
            if (latestReceptions.size > 0) {
                Collections.sort(latestReceptions)
                Collections.reverse(latestReceptions)
                parentModels.add(ProfileParentModel("Прошедшие", latestReceptions))
            }
            val adapter =
                ProfileAdapter(activity, parentModels, today, time, this, presenter!!.getUserToken())
            recyclerView!!.adapter = adapter
            recyclerView!!.itemAnimator = DefaultItemAnimator()
            adapter.onGroupClick(0)
        } else {
            recyclerView!!.visibility = View.GONE
            noPosts!!.visibility = View.VISIBLE
        }
    }

    fun showErrorScreen() {
        if (recyclerView != null) recyclerView!!.visibility = View.GONE
        if (errMessage != null) errMessage!!.visibility = View.VISIBLE
        if (errLoadBtn != null) {
            errLoadBtn!!.visibility = View.VISIBLE
            errLoadBtn!!.setOnClickListener { v: View? -> presenter!!.getVisits1() }
        }
    }

    private fun setupRefresh() {
        swipeProfile!!.setOnRefreshListener(refreshListener)
        swipeProfile!!.setColorSchemeColors(resources.getColor(android.R.color.holo_red_light))
    }

    fun swipeDismiss() {
        if (swipeProfile != null) swipeProfile!!.isRefreshing = false
    }

    //region recy item btn click listener
    override fun cancelBtnClick(user: Int, id_record: Int, idBranch: Int) {
        AlertForCancel(context) { msg: String? ->
            presenter!!.cancellationOfVisit(
                user,
                id_record,
                msg!!,
                idBranch
            )
        }
    }

    private var alert: AlertDialog? = null
    override fun confirmBtnClick(user: Int, id_record: Int, idBranch: Int, str: String) {
        val inflater = layoutInflater
        val view = inflater.inflate(R.layout.dialog_2textview_btn, null)
        val title = view.findViewById<TextView>(R.id.title)
        val text = view.findViewById<TextView>(R.id.text)
        val btnYes = view.findViewById<Button>(R.id.btnYes)
        val btnNo = view.findViewById<Button>(R.id.btnNo)
        title.text = Html.fromHtml("<u>Ваш прием подтвержден</u>")
        if (str == "") {
            text.text = ""
        } else {
            text.text =
                Html.fromHtml("<b>Обратите внимание на рекомендации перед приемом:</b><br><br>$str")
        }
        btnYes.setOnClickListener { v: View? ->
            alert!!.dismiss()
            presenter!!.confirmationOfVisit(user, id_record, idBranch)
        }
        btnNo.visibility = View.GONE
        val builder = AlertDialog.Builder(requireContext())
        builder.setView(view)
        alert = builder.create()
        alert!!.setCanceledOnTouchOutside(false)
        alert!!.show()
    }

    override fun enrollAgainBtnClick(viz: VisitResponseAndroid) {
        if (viz.works == "да") {
            val intent = ServiceActivity.getStartIntent(context)
            intent.putExtra(ServiceActivity.EXTRA_DATA_ID_DOCTOR, viz.idSotr!!)
            intent.putExtra(ServiceActivity.EXTRA_DATA_SERVICE, 0)
            intent.putExtra(ServiceActivity.EXTRA_DATA_ID_BRANCH, viz.idBranch!!)
            intent.putExtra(ServiceActivity.EXTRA_DATA_ID_USER, viz.idUser!!)
            intent.putExtra(ScheduleFragment.EXTRA_BACK_PAGE, Constants.MENU_THE_MAIN)
            startActivity(intent)
        }
    }

    override fun postponeBtnClick(viz: VisitResponseAndroid?) {
        val gson = Gson()
        val newViz = gson.toJson(viz)

//        Intent intent= ScheduleFragment.getStartIntent(context);
//        intent.putExtra(ScheduleFragment.EXTRA_DATA_ID_VIZIT,newViz);
//        startActivity(intent);
        val bundle = Bundle()
        bundle.putString(ScheduleFragment.EXTRA_DATA_ID_VIZIT, newViz)
        bundle.putInt(ScheduleFragment.EXTRA_BACK_PAGE, Constants.MENU_THE_MAIN)
        (context as MainActivity?)!!.selectedFragmentSchedule(bundle)
    }

    override fun payBtnClick(viz: VisitResponseAndroid, toPay: Boolean) {
        if (toPay) cfpv!!.addItem(viz) else cfpv!!.deleteItem(viz)
    }

    override fun confirmComing(viz: VisitResponseAndroid?) {
        presenter!!.sendConfirmComing(viz!!)
    }

    fun responseConfirmComing(viz: VisitResponseAndroid) {
        if (ProfileVisitViewHolder.statusIsPaid(viz.status!!)) {
            showConfirmComing("Вы можете сразу пройти к кабинету  <big><br><b>" + viz.cabinet + "</b><br></big> и ожидать приглашения врача")
        } else {
            showConfirmComing("Необходимо подойти к регистратуре для оформления документов")
        }
    }

    var alertDialogConfirmComing: AlertDialog? = null
    private fun showConfirmComing(msg: String) {
        val titleS = "Рады приветствовать Вас в нашем медицинском центре!"
        //String str=msg;
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.dialog_2textview_btn, null)
        val title = view.findViewById<TextView>(R.id.title)
        val text = view.findViewById<TextView>(R.id.text)
        val btnYes = view.findViewById<Button>(R.id.btnYes)
        val btnNo = view.findViewById<Button>(R.id.btnNo)
        val img = view.findViewById<ImageView>(R.id.img)
        img.visibility = View.GONE
        title.visibility = View.VISIBLE
        btnNo.visibility = View.GONE
        title.text = titleS
        text.gravity = Gravity.CENTER
        text.text = Html.fromHtml(msg)
        btnYes.setOnClickListener { v: View? ->
            presenter!!.getVisits1()
            alertDialogConfirmComing!!.cancel()
        }
        val builder = AlertDialog.Builder(requireContext())

        builder.setView(view)
        alertDialogConfirmComing = builder.create()
        alertDialogConfirmComing!!.setCanceledOnTouchOutside(false)
        alertDialogConfirmComing!!.show()
    }

    //endregion
    //region rfab
    override fun onRFACItemLabelClick(position: Int, item: RFACLabelItem<Any?>?) {
        rfabHelper!!.toggleContent()
        rfabClickSubItem(position)
    }

    override fun onRFACItemIconClick(position: Int, item: RFACLabelItem<Any?>?) {
        rfabHelper!!.toggleContent()
        rfabClickSubItem(position)
    }

    private fun initRFAB() {
        val rfaContent = RapidFloatingActionContentLabelList(context)
        rfaContent.setOnRapidFloatingActionContentLabelListListener(this)
        val items: MutableList<RFACLabelItem<*>> = ArrayList()
        val corner = MainUtils.dip2px(context, 4f)
        val sd = MainUtils.generateCornerShapeDrawable(-0xff37ad, corner, corner, corner, corner)
        val color = Color.WHITE
        items.add(
            RFACLabelItem<Int>()
                .setLabel("Записаться")
                .setResId(R.drawable.ic_create_white_24dp)
                .setIconNormalColor(-0xff37ad)
                .setIconPressedColor(-0xff198a)
                .setLabelBackgroundDrawable(sd)
                .setLabelColor(color)
                .setWrapper(0)
        )
        //        items.add(new RFACLabelItem<Integer>()
//                .setLabel("Чат")
//                .setResId(R.drawable.ic_message_white_24dp)
//                .setIconNormalColor(0xFF00C853)
//                .setIconPressedColor(0xFF00E676)
//                .setLabelBackgroundDrawable(sd)
//                .setLabelColor(color)
//                .setWrapper(1)
//        );
        items.add(
            RFACLabelItem<Int>()
                .setLabel("Позвонить ")
                .setResId(R.drawable.ic_call_white_24dp)
                .setIconNormalColor(-0xff37ad)
                .setIconPressedColor(-0xff198a)
                .setLabelBackgroundDrawable(sd)
                .setLabelColor(color)
                .setWrapper(2)
        )
        items.add(
            RFACLabelItem<Int>()
                .setLabel("Перейти на сайт")
                .setResId(R.drawable.ic_web_white_24dp)
                .setIconNormalColor(-0xff37ad)
                .setIconPressedColor(-0xff198a)
                .setLabelBackgroundDrawable(sd)
                .setLabelColor(color)
                .setWrapper(3)
        )
        rfaContent
            .setItems(items)
            .setIconShadowRadius(2)
            .setIconShadowColor(-0x777778)
            .setIconShadowDy(2)
        rfabHelper = RapidFloatingActionHelper(
            context,
            rfaLayout,
            rfaBtn,
            rfaContent
        ).build()
    }

    private fun rfabClickSubItem(num: Int) {
        when (num) {
            0 -> (context as MainActivity?)!!.selectedFragmentItem(Constants.MENU_RECORD, false)
            1 ->                // showChatActivity();
                callToCenter(centerPhone!!.text.toString())
            2 ->                // callToCenter(centerPhone.getText().toString());
                callToSite(centerSite!!.text.toString())
        }
    } //endregion

    override fun onDestroy() {
        presenter?.mainScope?.cancel()
        super.onDestroy()
    }
}