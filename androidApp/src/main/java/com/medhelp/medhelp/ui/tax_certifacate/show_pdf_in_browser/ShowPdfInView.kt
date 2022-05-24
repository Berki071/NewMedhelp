package com.medhelp.medhelp.ui.tax_certifacate.show_pdf_in_browser

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.content.FileProvider
import androidx.fragment.app.DialogFragment
import com.github.barteksc.pdfviewer.PDFView
import com.medhelp.medhelp.R
import java.io.File
import java.util.*


class ShowPdfInView : DialogFragment() {
    var file: File? = null
    fun setData(file: File) {
        //val file_size = (file.length() / 1024).toString().toInt()
        this.file=file

    }

    lateinit var pdfView : PDFView
    lateinit var toolbar : Toolbar

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setHasOptionsMenu(true)
//    }


    override fun onStart() {
        super.onStart()
        setHasOptionsMenu(true)

        //костыль, по умолчанию окно показывается не во весь размер
        val dialog = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            Objects.requireNonNull(dialog.window)?.setLayout(width, height)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.df_show_pdf_in_browser, null)
        pdfView=view.findViewById(R.id.pdfView)
        toolbar=view.findViewById(R.id.toolbar)

        toolbar.inflateMenu(R.menu.menu_share)

        val menu = toolbar.menu
        val btnShare = menu.findItem(R.id.btnShare)
        btnShare.setOnMenuItemClickListener (object  : MenuItem.OnMenuItemClickListener{
            override fun onMenuItemClick(item: MenuItem?): Boolean {
                clickShare()
                return true
            }

        })

        return view
    }

//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        inflater.inflate(R.menu.menu_share, menu)
//
//        val btnShare = menu.findItem(R.id.btnShare)
//        btnShare.setOnMenuItemClickListener (object  : MenuItem.OnMenuItemClickListener{
//            override fun onMenuItemClick(item: MenuItem?): Boolean {
//                clickShare()
//                return true
//            }
//
//        })
//
//        //super.onCreateOptionsMenu(menu, inflater)
//    }

//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when (item.itemId) {
////            android.R.id.home -> {
////                super.onBackPressed()
////                return true
////            }
//            R.id.btnShare -> {
//                //Log.wtf("mLog","btnShare");
//                clickShare()
//                return true
//            }
//        }
//        return super.onOptionsItemSelected(item)
//    }

    private fun clickShare() {
        val builder = VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())

        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "application/pdf"

        val outputPdfUri = FileProvider.getUriForFile(requireContext(), "com.medhelp.medhelp" + ".provider", file!!)

        shareIntent.putExtra(Intent.EXTRA_STREAM, outputPdfUri)
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        shareIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)

        startActivity(Intent.createChooser(shareIntent, "MedHelper"))
    }

    override fun onResume() {
        super.onResume()
        showPdfFromFile(file!!)
        toolbar.setNavigationOnClickListener{
            dismiss()
        }

    }

    private fun showPdfFromFile(file: File) {
        pdfView.fromFile(file)
            .password(null)
            .defaultPage(0)
            .enableSwipe(true)
            .swipeHorizontal(false)
            .enableDoubletap(true)
            .onPageError { page, _ ->
                Toast.makeText(this@ShowPdfInView.requireContext(), "Error at page: $page", Toast.LENGTH_LONG).show()
            }
            .load()
    }
}