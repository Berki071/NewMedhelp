package com.medhelp.medhelp.ui.tax_certifacate


import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.medhelp.medhelp.R
import com.medhelp.medhelp.ui._main_page.MainFragmentHelper
import com.medhelp.medhelp.ui.base.BaseFragment
import com.medhelp.medhelp.ui.tax_certifacate.recy.TaxCertificateAdapter
import com.medhelp.medhelp.ui.tax_certifacate.recy.TaxCertificateHolder
import com.medhelp.medhelp.ui.tax_certifacate.show_pdf_in_browser.ShowPdfInView
import com.medhelp.medhelp.ui.tax_certifacate.to_orter_certificate_df.ToOrderCertificateDf
import com.medhelp.medhelp.utils.timber_log.LoggingTree
import timber.log.Timber
import java.io.File

class TaxCertificateFragment : BaseFragment()  {
    fun newInstance(): Fragment? {
        return TaxCertificateFragment()
    }

    var presenter : TaxCertificatePresenter? = null
    var mainFragmentHelper: MainFragmentHelper? = null

    var toolbar : Toolbar? = null
    var recy : RecyclerView? = null
    var btnToOrder : Button? = null
    var rootEmpty : ConstraintLayout? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Timber.tag("my").i("Справка в налоговую")
        val rootView: View = inflater.inflate(R.layout.fragment_tax_certificate, container, false)
        init(rootView)
        return rootView
    }

    private fun init(v : View) {
        toolbar = v.findViewById(R.id.toolbar)
        recy = v.findViewById(R.id.recy)
        btnToOrder = v.findViewById(R.id.btnToOrder)
        rootEmpty= v.findViewById(R.id.rootEmpty)

        mainFragmentHelper= context as MainFragmentHelper?
        presenter   =   TaxCertificatePresenter(this)

        toolbar?.setNavigationOnClickListener{
            mainFragmentHelper?.showNavigationMenu()
        }

        btnToOrder?.setOnClickListener{
            showOrderAlert()
        }
    }

    override fun setUp(view: View?) {
        presenter?.getFilesForRecy()
    }

//    override fun onResume() {
//        super.onResume()
//        presenter?.getFilesForRecy()
//    }

    fun showOrderAlert(){
        val alert=ToOrderCertificateDf()
        alert.setData(object : ToOrderCertificateDf.ToOrderCertificateListener {

            override fun sendOrderCertificate(data: DataForTaxCertificate) {
                presenter?.sendDataForTaxCertificate(data)
            }
        })
        alert.show(childFragmentManager, ToOrderCertificateDf :: class.java.canonicalName)
    }
    fun showPdfInView(file: File){
        val showPdfInView = ShowPdfInView()
        showPdfInView.setData(file)
        showPdfInView.show(childFragmentManager, ShowPdfInView :: class.java.canonicalName)
    }

    var adapter : TaxCertificateAdapter?=null
    fun intiRecy(list : MutableList<File>){
        if(list.size==0) {
            isVisibleRootEmpty(true)
            return
        }else
            isVisibleRootEmpty(false)

//        if(adapter==null) {
            var layM = LinearLayoutManager(requireContext())
            adapter = TaxCertificateAdapter(requireContext(), list, object :
                TaxCertificateHolder.TaxCertificateHolderListener {
                override fun onClick(file: File) {
                    showPdfInView(file)
                }

                override fun delete(titleRecy : String, file: File) {
                    deletePDFDialog(titleRecy,file)

                }
            })
            recy?.layoutManager = layM
            recy?.adapter = adapter
//        }else{
//            adapter?.setNewLisetData(list)
//        }
    }

    fun deletePDFDialog(titleRecy : String, file: File) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage("Вы действительно хотите удалить файл?")
            .setNegativeButton("нет") { dialog: DialogInterface, which: Int -> dialog.dismiss() }
            .setPositiveButton("да") { dialog: DialogInterface, which: Int ->
                dialog.dismiss()
                try{
                    file.delete()
                } catch(e : Exception){
                    Timber.tag("my").e(LoggingTree.getMessageForError(e, "TaxCertificateFragment\$intiRecy_delete"))
                }
                adapter?.deleteItem(file)

                if(adapter?.list?.size==0){
                    presenter?.getFilesForRecy()
                }
            }
        val alertDialog = builder.create()
        alertDialog.show()
    }


    fun isVisibleRootEmpty (boo : Boolean){
        if(boo){
            rootEmpty?.visibility = View.VISIBLE
            recy?.visibility = View.GONE
        }else{
            rootEmpty?.visibility = View.GONE
            recy?.visibility = View.VISIBLE
        }
    }

    override fun userRefresh() {}


}