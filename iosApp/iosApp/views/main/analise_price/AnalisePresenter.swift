//
//  AnalisePresenter.swift
//  iosApp
//
//  Created by Михаил Хари on 24.06.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation
import shared

class AnalisePresenter : ObservableObject  {
    @Published var showDialogLoading: Bool = false
    @Published var showDialogErrorScreen: Bool = false
    @Published var isShowAlertRecomend: StandartAlertData? = nil
    @Published var categoryList :[String] = []
    var analisList : [AnalisePriceResponseIos] = []
    @Published var analisListForRecy : [AnalisePriceResponseIos] = []
    
    var selectedOption: String?
    var textSearch: String = "" {
         willSet(newValue) {
             //print(">>> " + textSearch)
             filterList()
         }
    }
    
    
    let sdk: NetworkManager
    var sharePreferenses : SharedPreferenses
    let netConnection = NetMonitor.shared

    init(){
        sdk=NetworkManager()
        sharePreferenses = SharedPreferenses()
        netConnection.startMonitoring()
    
        getAnalisePrice()
    }
    
    func getAnalisePrice() {
        if(self.sharePreferenses.currentUserInfo == nil || self.sharePreferenses.centerInfo == nil){
            LoggingTree.e("AnalisePresenter/getSpecialtyByCenter sharePreferenses.currentUserInfo == nil || sharePreferenses.centerInfo == nil")
            return
        }
        
        showLoading(true)
        
        let apiKey = String.init(self.sharePreferenses.currentUserInfo!.apiKey!)
        let idUser=String(Int.init(truncating: self.sharePreferenses.currentUserInfo!.idUser!))
        let idBranch=String(Int.init(truncating: self.sharePreferenses.currentUserInfo!.idBranch!))
        let h_dbName = self.sharePreferenses.centerInfo!.db_name!
        
        
        sdk.getAnalisePrice(h_Auth: apiKey, h_dbName: h_dbName, h_idKl: idUser, h_idFilial:  idBranch,
                                  completionHandler: { response, error in
            if let res : AnalisePriceList = response {
                if(res.response.count > 1 || res.response[0].title != nil){
                    
                    var tmpList :[AnalisePriceResponseIos] = []
                    
                    res.response.forEach{ i in
                        tmpList.append(AnalisePriceResponseIos(item: i))
                    }
                    
                    self.categoryList = self.separateGroup(tmpList)
                    self.analisList = tmpList
                    self.analisListForRecy = tmpList
                }
                self.showLoading(false)
                
            
                
            } else {
                if let t=error{
                    LoggingTree.e("AnalisePresenter/getAnalisePrice", t)
                }
                
                self.showLoading(false)
                self.showErrorScreen(true)
            }
        })
    }
    
    func separateGroup(_ list : [AnalisePriceResponseIos]) -> [String]{
        var tmpL : Set<String> = []
        
        list.forEach{ i in
            tmpL.insert(i.group!)
        }
        
        var tmp2 : [String] = Array(tmpL)
        tmp2.sort()
        tmp2.insert("Все", at: 0)
        
        return tmp2
    }
    
    func filterList(){
        
        var tmpList :[AnalisePriceResponseIos] = []
        
        if(self.selectedOption == nil || self.selectedOption == "Все"){
            tmpList = analisList
        }else{
            analisList.forEach{i in
                if(i.group != nil && i.group == selectedOption) {
                    tmpList.append(i)
                    return
                }
            }
        }
        
        if(!textSearch.isEmpty && tmpList.count>0){
            
            var tmpList2 :[AnalisePriceResponseIos] = []
            tmpList.forEach{ i in
                if(i.title!.lowercased().contains(textSearch.lowercased())){
                    tmpList2.append(i)
                }
            }
            
            tmpList = tmpList2
        }
        

        analisListForRecy = tmpList
    }
    
    func showLoading(_ isShow : Bool){
        if(isShow){
            self.showDialogLoading = true
        }else{
            self.showDialogLoading = false
        }
    }
    
    
    func showErrorScreen(_ isShow : Bool){
        if isShow {
            showDialogErrorScreen = true
            analisListForRecy = []
        }else{
            showDialogErrorScreen = false
        }
    }

}
