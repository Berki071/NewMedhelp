package com.medhelp.medhelp.ui.tax_certifacate

import com.androidnetworking.error.ANError
import com.downloader.PRDownloader
import com.medhelp.medhelp.R
import com.medhelp.medhelp.data.model.SendTaxCertificateResponse
import com.medhelp.medhelp.data.model.SendTaxCertificateResponseList
import com.medhelp.medhelp.data.network.NetworkManager
import com.medhelp.medhelp.data.pref.PreferencesManager
import com.medhelp.medhelp.utils.Different
import com.medhelp.medhelp.utils.main.MainUtils
import com.medhelp.medhelp.utils.timber_log.LoggingTree
import com.medhelp.medhelp.utils.workToFile.show_file.LoadFile
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.Response
import timber.log.Timber
import java.io.*

class TaxCertificatePresenter(val mainView : TaxCertificateFragment) {
    val preferencesManager  = PreferencesManager(mainView.requireContext())
    val networkManager = NetworkManager(preferencesManager)

    init {
        PRDownloader.initialize(mainView.requireContext())
    }

    fun getFilesForRecy(){
        try {

            val pathToDownloadFolder: File = mainView.requireContext().cacheDir
            val pathToFolder = File(pathToDownloadFolder, LoadFile.NAME_FOLDER_APP)
            if (!pathToFolder.exists()) {
                mainView.intiRecy(mutableListOf<File>())
                return
            }

            val pathToFolderTax = File(pathToFolder, LoadFile.NAME_FOLDER_TAX_CERTIFICATE)
            if (!pathToFolderTax.exists()) {
                mainView.intiRecy(mutableListOf<File>())
                return
            }

            val mmm = pathToFolderTax.list()
            if (mmm == null) {
                mainView.intiRecy(mutableListOf<File>())
                return
            }

            val fff1 = pathToFolderTax.listFiles()
            mainView.intiRecy(fff1!!.toMutableList())

        } catch (e: Exception) {
            Timber.tag("my").e(LoggingTree.getMessageForError(e, "TaxCertificatePresenter\$getFilesForRecy"))
        }

    }


    var counterListData=0
    fun sendDataForTaxCertificate (data: DataForTaxCertificate){
        MainUtils.showLoading(mainView.requireContext())

        val cd = CompositeDisposable()
        cd.add(networkManager
            .sendDataForTaxCertificate(data)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ response: SendTaxCertificateResponseList ->
                if (response.response[0].kto_vidal == null) {
                    MainUtils.hideLoading()
                    Different.showAlertInfo(mainView.requireActivity(),"","Мы не нашли оплат за выбранный период")
                } else{
                    Timber.tag("my").v("полшучено справок в налоговую: "+response.response.size)

                    counterListData=response.response.size

                    var counter=0
                    for(i in response.response){
                        counter++

                        //val link=getDataToLink(i)
                        val dir=getDir()
                        val fName=getNameFile(data, counter, i.ooo)

                        downloadPdfFromInternetTax(i,dir, fName, data, response.response.size)
                    }
                }

                cd.dispose()
            }, { throwable: Throwable? ->
                MainUtils.hideLoading()
                Timber.tag("my").e(LoggingTree.getMessageForError(throwable, "TaxCertificatePresenter/sendDataForTaxCertificate"))
                Different.showAlertInfo(mainView.requireActivity(),"Ошибка",mainView.requireContext().getString(R.string.api_default_error))
            })
        )
    }

    private fun getNameFile(data: DataForTaxCertificate,counter : Int, ooo : String) : String {
        //return "Tax_"+data.dateFrom+"_"+data.dateTo+"_"+counter+".pdf"
        val ooo1=ooo.replace(" ","_")
        return "Tax_"+ooo1+"_"+data.dateFrom+"_"+data.dateTo+".pdf"
       //return "Tax_"+".pdf"
    }

//    private fun getDataToLink(data : SendTaxCertificateResponse) : String{
//        if(data.nom_amb==null || data.nom_amb.isEmpty()){
//            data.nom_amb="-"
//        }
//
//        val link ="https://oneclick.tmweb.ru/medhelp_client/fpdf/report_spravka_nalog.php?nom_doc="+data.nom_doc+"&fio_nalogoplat="+data.fio_nalogoplat+
//                "&inn="+data.inn+"&fio_pac="+data.fio_pac+"&nom_amb="+data.nom_amb+"&itogo="+data.itogo+"&itogo_propis="+data.itogo_propis+"&dati_oplat="+
//                data.dati_oplat+"&OOO="+data.ooo+"&rekviziti="+data.rekviziti+"&licenziya="+data.licenziya+"&min_data="+data.min_data+"&kto_vidal="+data.kto_vidal+"&telefon="+data.telefon+
//                "&dbname="+data.dbname
//        return link
//    }

    private fun getDir(): String {
        val pathToFolderCache: File =mainView.requireContext().cacheDir
        val pathToFolderApp: File = File(pathToFolderCache, LoadFile.NAME_FOLDER_APP)
        val pathToFolder = File(pathToFolderApp, LoadFile.NAME_FOLDER_TAX_CERTIFICATE)
        if (!pathToFolder.exists()) {
            pathToFolder.mkdirs()
        }
        return pathToFolder.absolutePath
    }

//    private fun downloadPdfFromInternet(url: String, dirPath: String, fileName: String, counter : Int) {
//        PRDownloader.download(url, dirPath, fileName).build()
//            .start(object : OnDownloadListener {
//                override fun onDownloadComplete() {
//                    counterListData--
//
//                    val downloadedFile = File(dirPath, fileName)
//                    val file_size = (downloadedFile.length() / 1024).toString().toInt()
//
//                    if(counter==1) {
//                        getFilesForRecy()
//                        mainView.showPdfInView(downloadedFile)
//                    }
//
//                    if(counterListData<=0) {
//                        MainUtils.hideLoading()
//                        if(counter>1){
//                            getFilesForRecy()
//                            Different.showAlertInfo(mainView.requireActivity(),"","Файлы успешно загружены, проверьте список")
//                        }
//                    }
//                }
//
//                override fun onError(error: com.downloader.Error?) {
//                    Timber.tag("my").e("TaxCertificatePresenter\$downloadPdfFromInternet "+error.toString())
//                    Different.showAlertInfo(mainView.requireActivity(),"Ошибка",mainView.requireContext().getString(R.string.api_default_error))
//                    MainUtils.hideLoading()
//                }
//            })
//    }

    private fun downloadPdfFromInternetTax(data: SendTaxCertificateResponse, dirPath: String, fileName: String, item: DataForTaxCertificate, num : Int) {
        val cd = CompositeDisposable()
        cd.add(networkManager
            .loadFileTax(data, dirPath, fileName, load2FileListener, item)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { cd.dispose() }
            ) { throwable: Throwable? ->
                Timber.tag("my").e("TaxCertificatePresenter\$downloadPdfFromInternet " + throwable.toString())
                Different.showAlertInfo(mainView.requireActivity(), "Ошибка", mainView.requireContext().getString(R.string.api_default_error))
                MainUtils.hideLoading()
                cd.dispose()
            })
    }

    var load2FileListener: NetworkManager.Load2FileListenerTax = object : NetworkManager.Load2FileListenerTax {
        override fun onResponse(response: Response, dirPath: String, fileName: String, item: DataForTaxCertificate) {
            byteToFile(response, dirPath, fileName, item)
        }

        override fun onError(anError: ANError, item: DataForTaxCertificate) {
            Timber.tag("my").e("TaxCertificatePresenter\$load2FileListener " + anError.toString())
            Different.showAlertInfo(mainView.requireActivity(), "Ошибка", mainView.requireContext().getString(R.string.api_default_error))
            MainUtils.hideLoading()
            getFilesForRecy()
        }
    }

    fun byteToFile(response: Response, dirPath: String?, fileName: String, item: DataForTaxCertificate) {
        if (response.code != 200) {
            MainUtils.hideLoading()
            updateItemInMainThread()
            Timber.tag("my").e("ElectronicConclusionsPresenter/load2FileListener $response")
        }

        val thread = Thread {
            val head = response.headers
            val body = response.body
            val f = File(dirPath)
            val f2 = File(f, "/$fileName")
            if (f2.exists()) f2.delete()
            try {
                f2.createNewFile()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            if (f2.exists()) {
                try {
                    var inputStream: InputStream? = null
                    var outputStream: OutputStream? = null
                    try {
                        val fileReader = ByteArray(4096)
                        val fileSize = body!!.contentLength()
                        var fileSizeDownloaded: Long = 0
                        inputStream = body.byteStream()
                        outputStream = FileOutputStream(f2)
                        while (true) {
                            val read = inputStream.read(fileReader)
                            if (read == -1) {
                                break
                            }
                            outputStream.write(fileReader, 0, read)
                            fileSizeDownloaded += read.toLong()
                        }
                        outputStream.flush()
                        //item.pathToFile = f2.path
                        MainUtils.hideLoading()
                        updateItemInMainThread()
                    } catch (e: IOException) {
                        updateItemInMainThread()
                        updateItemInMainThread()
                        Timber.tag("my").e(
                            LoggingTree.getMessageForError(
                                e,
                                "ElectronicConclusionsPresenter/byteToFile"
                            )
                        )
                    } finally {
                        inputStream?.close()
                        outputStream?.close()
                    }
                } catch (e: IOException) {
                    MainUtils.hideLoading()
                    updateItemInMainThread()
                    Timber.tag("my").e(
                        LoggingTree.getMessageForError(
                            e,
                            "ElectronicConclusionsPresenter/byteToFile"
                        )
                    )
                }
            }
            MainUtils.hideLoading()
            updateItemInMainThread()
        }
        thread.start()
    }

    fun updateItemInMainThread() {
        mainView.getActivity()?.runOnUiThread(Runnable { getFilesForRecy()})
    }
}