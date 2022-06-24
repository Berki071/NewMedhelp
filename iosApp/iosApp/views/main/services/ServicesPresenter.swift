//
//  ServicesPresenter.swift
//  iosApp
//
//  Created by Михаил Хари on 24.06.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation
import shared

class ServicesPresenter : ObservableObject{
    @Published var showDialogLoading: Bool = false
    @Published var isShowAlertRecomend: StandartAlertData? = nil
    @Published var categoryList :[CategoryResponse] = []
    var serviceList : [ServiceResponseIos] = []
    @Published var serviceListForRecy : [ServiceResponseIos] = []
    
    var selectedOption: CategoryResponse?
    
    
    let sdk: NetworkManager
    var sharePreferenses : SharedPreferenses
    let netConnection = NetMonitor.shared

    init(){
        sdk=NetworkManager()
        sharePreferenses = SharedPreferenses()
        netConnection.startMonitoring()
    
        getSpecialtyByCenter()
    }
    
    func getSpecialtyByCenter() {
        
        if(self.sharePreferenses.currentUserInfo == nil || self.sharePreferenses.centerInfo == nil){
            LoggingTree.e("ServicesPresenter/getSpecialtyByCenter sharePreferenses.currentUserInfo == nil || sharePreferenses.centerInfo == nil")
            return
        }
        
        showLoading(true)
        
        let apiKey = String.init(self.sharePreferenses.currentUserInfo!.apiKey!)
        let idUser=String(Int.init(truncating: self.sharePreferenses.currentUserInfo!.idUser!))
        let idBranch=String(Int.init(truncating: self.sharePreferenses.currentUserInfo!.idBranch!))
        let h_dbName = self.sharePreferenses.centerInfo!.db_name!
        
        
        sdk.getCategoryApiCall(h_Auth: apiKey, h_dbName: h_dbName, h_idKl: idUser, h_idFilial:  idBranch,
                                  completionHandler: { response, error in
            if let res : SpecialtyList = response {
                if(res.spec.count > 1 || res.spec[0].id != nil){
                    let catAll = CategoryResponse(title: "Все")
                    catAll.id = -1
                    res.spec.insert(catAll, at: 0)
                    self.categoryList = res.spec
                }
                self.getPrice()
            } else {
                if let t=error{
                    LoggingTree.e("ServicesPresenter/getSpecialtyByCenter", t)
                }
                
                self.showLoading(false)
                self.showErrorScreen()
            }
        })
    }
    
    func getPrice(){
        
        self.showLoading(true)
        
        let apiKey = String.init(self.sharePreferenses.currentUserInfo!.apiKey!)
        let idUser=String(Int.init(truncating: self.sharePreferenses.currentUserInfo!.idUser!))
        let idBranch=String(Int.init(truncating: self.sharePreferenses.currentUserInfo!.idBranch!))
        let h_dbName = self.sharePreferenses.centerInfo!.db_name!
        
        
        sdk.getPriceApiCall(h_Auth: apiKey, h_dbName: h_dbName, h_idKl: idUser, h_idFilial:  idBranch,
                                  completionHandler: { response, error in
            if let res : ServiceList = response {
                if(res.services!.count > 1 || res.services![0].title != nil ){
                    
                    var tmpList :[ServiceResponseIos] = []
                    
                    res.services!.forEach{ i in
                        tmpList.append(ServiceResponseIos(item: i))
                    }
                
                    self.serviceList = tmpList
                    self.serviceListForRecy = tmpList
                }
                self.showLoading(false)
            } else {
                if let t=error{
                    LoggingTree.e("ServicesPresenter/getPrice", t)
                }
                
                self.showLoading(false)
                self.showErrorScreen()
            }
        })
    }
    
    
    
    func selctSpinnerItem(_ selectedOption: CategoryResponse){
        self.selectedOption = selectedOption
        
        if(selectedOption.id == -1){
            serviceListForRecy = serviceList
            return
        }

        var tmpList :[ServiceResponseIos] = []

        serviceList.forEach{i in
            if(i.idSpec != nil && i.idSpec == selectedOption.id) {
                tmpList.append(i)
                return
            }
        }

        serviceListForRecy = tmpList
    }
    
    
    func showLoading(_ isShow : Bool){
        if(isShow){
            self.showDialogLoading = true
        }else{
            self.showDialogLoading = false
        }
    }
    
    func showEmptyScreen(){
        
    }
    
    func showErrorScreen(){
        //todo erroe screen
    }
}
