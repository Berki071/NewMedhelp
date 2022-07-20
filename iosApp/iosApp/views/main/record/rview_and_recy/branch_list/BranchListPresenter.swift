//
//  BranchListPresenter.swift
//  iosApp
//
//  Created by Михаил Хари on 19.07.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation
import shared
import SwiftUI

class BranchListPresenter : ObservableObject{
    let sdk: NetworkManager
    var sharePreferenses : SharedPreferenses
    let netConnection = NetMonitor.shared
    
    var showDialogLoading: Binding<Bool>
    
    @Published var listBranch : [BranchLineItem] = []
    var selectBrtanch : SettingsAllBranchHospitalResponse? = nil
    
    var selectServis: ServiceResponseIos?
    var selctDoctor: AllDoctorsResponseIos?
    var listenerSelectedBranch: (SettingsAllBranchHospitalResponse) -> Void
    
    init(selectServis: ServiceResponseIos?, selctDoctor: AllDoctorsResponseIos?, showDialogLoading: Binding<Bool>, listenerSelectedBranch: @escaping (SettingsAllBranchHospitalResponse) -> Void){
        self.selectServis = selectServis
        self.selctDoctor = selctDoctor
        self.showDialogLoading = showDialogLoading
        self.listenerSelectedBranch = listenerSelectedBranch
        
        sdk=NetworkManager()
        sharePreferenses = SharedPreferenses()
        netConnection.startMonitoring()
        
        if(selectServis != nil && selectServis!.id != nil){
            let idServis = String(Int.init(truncating: self.selectServis!.id!))
            if(selctDoctor != nil){
                let idDoc = String(Int(self.selctDoctor!.id))
                getBranchByIdServiceIdDoc(idService: idServis, idDoc: idDoc)
            }else{
                getBranchByIdService(idService: idServis)
            }
        }
    }
    
    func getBranchByIdService(idService: String){
        showLoading(true)
        
        let apiKey = String.init(self.sharePreferenses.currentUserInfo!.apiKey ?? "0")
        let idUser=String(Int.init(truncating: self.sharePreferenses.currentUserInfo!.idUser!))
        let idBranch=String(Int.init(truncating: self.sharePreferenses.currentUserInfo!.idBranch!))
        let h_dbName = self.sharePreferenses.centerInfo!.db_name!
        
        sdk.getBranchByIdService(idService: idService,
            h_Auth: apiKey, h_dbName: h_dbName, h_idKl: idUser, h_idFilial:  idBranch,
                                  completionHandler: { response, error in
            if let res : SettingsAllBaranchHospitalList = response {
                if(res.response.count>1 || res.response[0].nameBranch != nil){
                    self.processingGetBranch(list: res.response)
                }
                
                self.showLoading(false)
            } else {
                if let t=error{
                    LoggingTree.e("BranchListPresenter/getBranchByIdService", t)
                }
                self.showLoading(false)
            }
        })
    }
    func getBranchByIdServiceIdDoc (idService: String, idDoc: String){
        showLoading(true)
        
        let apiKey = String.init(self.sharePreferenses.currentUserInfo!.apiKey!)
        let idUser=String(Int.init(truncating: self.sharePreferenses.currentUserInfo!.idUser!))
        let idBranch=String(Int.init(truncating: self.sharePreferenses.currentUserInfo!.idBranch!))
        let h_dbName = self.sharePreferenses.centerInfo!.db_name!
        
        sdk.getBranchByIdServiceIdDoc(idService: idService, idDoc: idDoc,
            h_Auth: apiKey, h_dbName: h_dbName, h_idKl: idUser, h_idFilial:  idBranch,
                                  completionHandler: { response, error in
            if let res : SettingsAllBaranchHospitalList = response {
                if(res.response.count>1 || res.response[0].nameBranch != nil){
                    self.processingGetBranch(list: res.response)
                }
                
                self.showLoading(false)
            } else {
                if let t=error{
                    LoggingTree.e("BranchListPresenter/getBranchByIdServiceIdDoc", t)
                }
                self.showLoading(false)
            }
        })
        
    }
    
    func processingGetBranch(list: [SettingsAllBranchHospitalResponse]){
        var tmpList :[BranchLineItem] = []
        let cuerrentFBranch = self.sharePreferenses.currentUserInfo?.idBranch
        var isSerch = false
        
        
        for i in stride(from: 0, to: list.count-1, by: 2){

            if(list[i].idBranch == cuerrentFBranch){
                isSerch = true
                list[i].isFavorite = true
                selectBrtanch = list[i]
                listenerSelectedBranch(selectBrtanch!)
            }else if(list[i+1].idBranch == cuerrentFBranch){
                isSerch = true
                list[i+1].isFavorite = true
                selectBrtanch = list[i+1]
                listenerSelectedBranch(selectBrtanch!)
            }
            
            tmpList.append(BranchLineItem(item1: list[i], item2: (list[i+1] ?? nil)))
        }
        
        if(!isSerch){
            tmpList[0].item1.isFavorite = true
            selectBrtanch = tmpList[0].item1
            listenerSelectedBranch(selectBrtanch!)
        }
        
        self.listBranch = tmpList
    }
    
    
    func clickFavoriteBranch(item : SettingsAllBranchHospitalResponse){
        if(item.idBranch == selectBrtanch!.idBranch){
            return
        }
        
        selectBrtanch = item
        self.listenerSelectedBranch(selectBrtanch!)
        
        listBranch.forEach{i in
            if(i.item1.idBranch == item.idBranch){
                i.item1.isFavorite = true
            }else{
                i.item1.isFavorite = false
            }
            
            if(i.item2 != nil){
                if(i.item2!.idBranch == item.idBranch){
                    i.item2!.isFavorite = true
                }else{
                    i.item2!.isFavorite = false
                }
            }
        }
        
        objectWillChange.send()
    }
    
    func showLoading(_ isShow : Bool){
        if(isShow){
            self.showDialogLoading.wrappedValue = true
        }else{
            self.showDialogLoading.wrappedValue = false
        }
    }
    
}
