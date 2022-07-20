//
//  ServicesSelctForDoctorPresenter.swift
//  iosApp
//
//  Created by Михаил Хари on 18.07.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation
import shared

class ServicesSelctForDoctorPresenter : ObservableObject{
    @Published var showDialogLoading: Bool = false
    @Published var showDialogErrorScreen: Bool = false
    @Published var showEmptyScreen: Bool = false
    @Published var isShowStandartAlert: StandartAlertData? = nil
    @Published var categoryList :[CategoryResponse] = []
    var serviceList : [ServiceResponseIos] = []
    @Published var serviceListForRecy : [ServiceResponseIos] = []
    
    var selectedOption: CategoryResponse?
    var textSearch: String = "" {
         willSet(newValue) {
             self.filterList()
         }
    }
    
    let sdk: NetworkManager
    var sharePreferenses : SharedPreferenses
    let netConnection = NetMonitor.shared
    
    var selectDocttorForRecord: AllDoctorsResponseIos
    
    init(selectDocttorForRecord: AllDoctorsResponseIos){
        self.selectDocttorForRecord = selectDocttorForRecord
        
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
        
        
        sdk.getCategoryApiCall(idDoctor: selectDocttorForRecord.id,
            h_Auth: apiKey, h_dbName: h_dbName, h_idKl: idUser, h_idFilial:  idBranch,
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
                self.showErrorScreen(true)
            }
        })
    }
    
    func getPrice(){
        
        self.showLoading(true)
        
        let apiKey = String.init(self.sharePreferenses.currentUserInfo!.apiKey!)
        let idUser=String(Int.init(truncating: self.sharePreferenses.currentUserInfo!.idUser!))
        let idBranch=String(Int.init(truncating: self.sharePreferenses.currentUserInfo!.idBranch!))
        let h_dbName = self.sharePreferenses.centerInfo!.db_name!
        
        let idDoc = String(Int(selectDocttorForRecord.id))
        
        sdk.getPriceApiCall(idDoctor: idDoc, idBranch: idBranch, idUser: idUser,
            h_Auth: apiKey, h_dbName: h_dbName, h_idKl: idUser, h_idFilial:  idBranch,
                                  completionHandler: { response, error in
            if let res : ServiceList = response {
                if(res.services!.count > 1 || res.services![0].title != nil ){
                    
                    self.showEmptyScreen = false
                    
                    var tmpList :[ServiceResponseIos] = []
                    
                    res.services!.forEach{ i in
                        tmpList.append(ServiceResponseIos(item: i))
                    }
                
                    self.serviceList = tmpList
                    self.serviceListForRecy = tmpList
                }else{
                    self.showEmptyScreen = true
                }
                self.showLoading(false)
                
               
                
            } else {
                if let t=error{
                    LoggingTree.e("ServicesPresenter/getPrice", t)
                }
                
                self.showLoading(false)
                self.showErrorScreen(true)
            }
        })
    }
    
    
    func filterList(){
        var tmpList : [ServiceResponseIos] = []
        
        if(self.selectedOption == nil || self.selectedOption!.id == -1){
            tmpList = serviceList
        }else{
            serviceList.forEach{i in
                if(i.idSpec != nil && Int(i.idSpec!) == self.selectedOption!.id) {
                    tmpList.append(i)
                    return
                }
            }
        }
        
        if(!textSearch.isEmpty && tmpList.count>0){
            var tmpList2 :[ServiceResponseIos] = []
            tmpList.forEach{ i in
                if(i.title!.lowercased().contains(textSearch.lowercased())){
                    tmpList2.append(i)
                }
            }
            tmpList = tmpList2
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
    
    func showErrorScreen(_ isShow : Bool){
        if isShow {
            showDialogErrorScreen = true
            serviceListForRecy = []
        }else{
            showDialogErrorScreen = false
        }
    }
}
