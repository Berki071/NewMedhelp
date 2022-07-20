//
//  RecodPresenter.swift
//  iosApp
//
//  Created by Михаил Хари on 18.07.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation
import shared

class RecodPresenter : ObservableObject{
    @Published var showDialogLoading: Bool = false
    @Published var showEmptyScreen: Bool = false
    
    let sdk: NetworkManager
    var sharePreferenses : SharedPreferenses
    let netConnection = NetMonitor.shared
    
    var selectServis: ServiceResponseIos? = nil
    var selctDoctor: AllDoctorsResponseIos? = nil
    
    var selectBrtanch : SettingsAllBranchHospitalResponse? = nil
    
    init(selectServis: ServiceResponseIos?, selctDoctor: AllDoctorsResponseIos?){
        self.selectServis = selectServis
        self.selctDoctor = selctDoctor
        
        sdk=NetworkManager()
        sharePreferenses = SharedPreferenses()
        netConnection.startMonitoring()
    
    }
    
    func selectNeqBranch(item : SettingsAllBranchHospitalResponse){
        self.selectBrtanch = item
        
        getDataFrom()
    }
    
    func getDataFrom(){
        if(selectServis != nil){
            if(selctDoctor != nil){
                getCurrentDateFromResponse("doc")
            }else{
                getCurrentDateFromResponse("serv")
            }
        }
    }
    
    func getCurrentDateFromResponse(_ type : String){
        
        let apiKey = String.init(self.sharePreferenses.currentUserInfo!.apiKey ?? "0")
        let idUser=String(Int.init(truncating: self.sharePreferenses.currentUserInfo!.idUser!))
        let idBranch=String(Int.init(truncating: self.sharePreferenses.currentUserInfo!.idBranch!))
        let h_dbName = self.sharePreferenses.centerInfo!.db_name!
        
        sdk.getCurrentDateApiCall(h_Auth: apiKey, h_dbName: h_dbName, h_idKl: idUser, h_idFilial:  idBranch,
                                  completionHandler: { response, error in
            if let res : DateList = response {
       
                let adm = String(Int.init(truncating: self.selectServis!.admission!))
                let selectBranch = String(Int.init(truncating: self.selectBrtanch!.idBranch!))
                
                if(type == "doc"){
                    let idDoc = String(Int.init(truncating: self.selctDoctor!.id!))
                    self.getScheduleByDoctor(idDoc, res.response!, adm, selectBranch)
                }else{
                    let serviceId = String(Int.init(truncating: self.selectServis!.id!))
                    self.getScheduleByService(serviceId, res.response!, adm, selectBranch)
                }
                
            } else {
                if let t=error{
                    LoggingTree.e("RecodPresenter/getCurrentDateFromResponse", t)
                }
                self.showLoading(false)
            }
        })
    }
    
    func getScheduleByService(_ idService: String, _ date: DateResponse, _ adm: String, _ idBranch: String){
        
    }
    
    func getScheduleByDoctor(_ idDoctor: String, _ date: DateResponse, _ adm: String, _ idBranch: String){
        
    }
    
    
    
    
    
    func showLoading(_ isShow : Bool){
        if(isShow){
            self.showDialogLoading = true
        }else{
            self.showDialogLoading = false
        }
    }
}
