//
//  ProfilePresenter.swift
//  iosApp
//
//  Created by Михаил Хари on 14.06.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation
import shared
import SwiftUI

class ProfilePresenter: ObservableObject {
    @Published var centerName : String = ""
    @Published var urlImageLogo : String = ""  // получение в  base64
    @Published var nameBranch : String = ""
    @Published var centerPhone : String = ""
    @Published var centerSite : String = ""
    
    @Published var showDialogLoading: Bool = false
    
    //@Published var listRecy: [VisitResponseIos] = []
    @Published var actualReceptions : [VisitResponseIos] = []
    @Published var latestReceptions : [VisitResponseIos] = []
    
    let sdk: NetworkManager
    var sharePreferenses : SharedPreferenses
    let netConnection = NetMonitor.shared

    init(){
        sdk=NetworkManager()
        sharePreferenses = SharedPreferenses()
        netConnection.startMonitoring()
    
        updateHeader()
        getVisits1()
    }
    
    func updateHeader(){
        let centerInfo = self.sharePreferenses.centerInfo
        let currentUserInfo = self.sharePreferenses.currentUserInfo
        
        if(centerInfo != nil){
            centerName = centerInfo!.title!
            
            
            if(centerInfo!.site != nil && !centerInfo!.site!.isEmpty){
                centerSite=centerInfo!.site!
            }else{
                centerSite=""
            }
            
            if(centerInfo!.phone != nil && !centerInfo!.phone!.isEmpty){
                centerPhone=centerInfo!.phone!
            }else{
                centerPhone=""
            }
            
            if(currentUserInfo != nil){
                nameBranch = currentUserInfo!.nameBranch!
                let ttt=centerInfo!.logo! + "&token=" + currentUserInfo!.apiKey!
                urlImageLogo = ttt
            }
        }
        
    }
    
    func getVisits1(){
        showLoading(true)
        
        if(self.sharePreferenses.currentUserInfo != nil &&  self.sharePreferenses.centerInfo != nil){
            let apiKey = String.init(self.sharePreferenses.currentUserInfo!.apiKey!)
            let idUser=String(Int.init(truncating: self.sharePreferenses.currentUserInfo!.idUser!))
            let idBranch=String(Int.init(truncating: self.sharePreferenses.currentUserInfo!.idBranch!))
            let h_dbName = self.sharePreferenses.centerInfo!.db_name!
            
            
            sdk.getCurrentDateApiCall(h_Auth: apiKey, h_dbName: h_dbName, h_idKl: idUser, h_idFilial:  idBranch,
                                      completionHandler: { response, error in
                if let res : DateList = response {
                    self.getVisits2(res.response!.time!, res.response!.today!)
                } else {
                    if let t=error{
                        LoggingTree.e("ProfilePresenter/getVisits1", t)
                    }
                    
                    self.showLoading(false)
                    self.showErrorScreen()
                }
            })
        }else{
            LoggingTree.e("ProfilePresenter/getVisits1 sharePreferenses.currentUserInfo == nil || sharePreferenses.centerInfo == nil")
            self.showErrorScreen()
            self.showLoading(false)
            return
        }
    }
    
    func getVisits2(_ time : String, _ today : String ){
        let apiKey = String.init(self.sharePreferenses.currentUserInfo!.apiKey!)
        let h_dbName = self.sharePreferenses.centerInfo!.db_name!
        let idUser=String(Int.init(truncating: self.sharePreferenses.currentUserInfo!.idUser!))
        let idBranch=String(Int.init(truncating: self.sharePreferenses.currentUserInfo!.idBranch!))
        
        sdk.getAllReceptionApiCall(h_Auth: apiKey, h_dbName: h_dbName, h_idKl: idUser, h_idFilial:  idBranch,
                                  completionHandler: { response, error in
            if let res : VisitList = response {
               
                if(!res.error){
                    if res.response.count == 0{
                        self.showEmptyScreen()
                    }else{
                        var tmpList : [VisitResponseIos] = []
                        
                        res.response.forEach{ i in
                            tmpList.append(VisitResponseIos(i))
                        }

                        self.prosessingVisits(tmpList)
                    }
                    
                }else{
                    self.showErrorScreen()
                }
                
                self.showLoading(false)
                
            } else {
                if let t=error{
                    LoggingTree.e("ProfilePresenter/getVisits2", t)
                }
                
                self.showLoading(false)
                self.showErrorScreen()
            }
        })
    }
    
    func prosessingVisits(_ array : [VisitResponseIos]) {
        var actualReceptionsTmp : [VisitResponseIos] = []
        var latestReceptionsTmp : [VisitResponseIos] = []

        array.forEach{i in
            if(i.isCurrentListVisit()){
                actualReceptionsTmp.append(i)

            }else{
                latestReceptionsTmp.append(i)
            }
        }
        
        let c0 = array.count
        let c1 = actualReceptionsTmp.count
        let c2 = latestReceptionsTmp.count
        actualReceptions = actualReceptionsTmp
        latestReceptions = latestReceptionsTmp
       
    }
    
    func showEmptyScreen(){
        
    }
    
    func showErrorScreen(){
        //todo erroe screen
    }
    
    func showLoading(_ isShow : Bool){
        if(isShow){
            self.showDialogLoading = true
        }else{
            self.showDialogLoading = false
        }
    }
}
