package com.medhelp.medhelp.ui.analise_result.recycler;

public interface AnaliseRecyItemListener {
    void downloadPDF (int position);
    void openPDF(int position);
    void deletePDFDialog(int position);
}
