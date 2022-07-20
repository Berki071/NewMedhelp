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
    @Published var iuImageLogo : UIImage =  UIImage(named: "sh_center1")!
    @Published var nameBranch : String = ""
    @Published var centerPhone : String = ""
    @Published var centerSite : String = ""
    
    @Published var showDialogLoading: Bool = false
    @Published var showDialogErrorScreen: Bool = false
    @Published var showDialogEmptyScreen: Bool = false
    @Published var showDialogCancelReceptionData: ShowDialogCancelReceptionData? = nil
    
    @Published var actualReceptions : [VisitResponseIos] = []
    @Published var latestReceptions : [VisitResponseIos] = []
    
    @Published var isShowAlertRecomend: StandartAlertData? = nil
    
    let sdk: NetworkManager
    var sharePreferenses : SharedPreferenses
    let netConnection = NetMonitor.shared
    
    var currentUser : UserResponse
    var timeAndDateServer: Date? = nil
  

    init(){
        sdk=NetworkManager()
        sharePreferenses = SharedPreferenses()
        netConnection.startMonitoring()
        
        self.currentUser = sharePreferenses.currentUserInfo ?? UserResponse()
        self.updateHeader()
        self.getVisits1()
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
                let imagePathString = centerInfo!.logo! + "&token=" + currentUserInfo!.apiKey!
                
                DownloadManager(imagePathString, resultUiImage: {(tmp : UIImage) -> Void in
                    self.iuImageLogo = tmp
                })
                
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
                    let dateStrCur = res.response!.time! + " " +  res.response!.today!
                    let dateFormatter = DateFormatter()
                    dateFormatter.dateFormat = MDate.DATE_FORMAT_HHmm_ddMMyyyy
                    self.timeAndDateServer = dateFormatter.date(from: dateStrCur)!
                    
                    self.getVisits2()
                } else {
                    if let t=error{
                        LoggingTree.e("ProfilePresenter/getVisits1", t)
                    }
                    
                    self.showLoading(false)
                    self.showErrorScreen(true)
                }
            })
        }else{
            LoggingTree.e("ProfilePresenter/getVisits1 sharePreferenses.currentUserInfo == nil || sharePreferenses.centerInfo == nil")
            self.showErrorScreen(true)
            self.showLoading(false)
            return
        }
    }
    
    func getVisits2(){
        let apiKey = String.init(self.sharePreferenses.currentUserInfo!.apiKey!)
        let h_dbName = self.sharePreferenses.centerInfo!.db_name!
        let idUser=String(Int.init(truncating: self.sharePreferenses.currentUserInfo!.idUser!))
        let idBranch=String(Int.init(truncating: self.sharePreferenses.currentUserInfo!.idBranch!))
        
        sdk.getAllReceptionApiCall(h_Auth: apiKey, h_dbName: h_dbName, h_idKl: idUser, h_idFilial:  idBranch,
                                  completionHandler: { response, error in
            if let res : VisitList = response {
               
                if(!res.error){
                    if res.response.count == 0  || (res.response.count == 1 && res.response[0].nameServices == nil){
                        self.showEmptyScreen(true)
                    }else{
                        self.showEmptyScreen(false)
                        
                        var tmpList : [VisitResponseIos] = []
                        
                        res.response.forEach{ i in
                            tmpList.append(VisitResponseIos(i))
                        }

                        self.prosessingVisits(tmpList)
                    }
                    
                }else{
                    self.showErrorScreen(true)
                }
                
                self.showLoading(false)
                
                
            } else {
                print(">>> \(error)")
                
                if let t=error{
                    LoggingTree.e("ProfilePresenter/getVisits2", t)
                }
                
                self.showLoading(false)
                self.showErrorScreen(true)
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
        
        if(actualReceptionsTmp.count > 1){
            actualReceptionsTmp.sort{
                $0.getTimeAndDateInDateFormat()! < $1.getTimeAndDateInDateFormat()!
            }
        }
        
        if(latestReceptionsTmp.count > 1){
            latestReceptionsTmp.sort{
                $0.getTimeAndDateInDateFormat()! > $1.getTimeAndDateInDateFormat()!
            }
        }
        
        actualReceptions = actualReceptionsTmp
        latestReceptions = latestReceptionsTmp
       
    }
    
    func confirmBtnClick(_ item : VisitResponseIos){
        
        showLoading(true)
        
        if(self.sharePreferenses.currentUserInfo != nil &&  self.sharePreferenses.centerInfo != nil){
            let apiKey = String.init(self.sharePreferenses.currentUserInfo!.apiKey!)
            let idUser=String(Int.init(truncating: self.sharePreferenses.currentUserInfo!.idUser!))
            let idBranch=String(Int.init(truncating: self.sharePreferenses.currentUserInfo!.idBranch!))
            let h_dbName = self.sharePreferenses.centerInfo!.db_name!
            
            sdk.sendConfirmationOfVisit(user: String(Int(item.idUser!)), id_zapisi: String(Int(item.idRecord!)), idBranch: String(Int(item.idBranch!)),
                h_Auth: apiKey, h_dbName: h_dbName, h_idKl: idUser, h_idFilial:  idBranch,
                                      completionHandler: { response, error in
                if let res : SimpleResponseBoolean = response {
                    self.showLoading(false)
                    
                    var strTmp : String
                    if(item.comment == nil || item.comment!.isEmpty){
                        strTmp = ""
                    }else{
                        strTmp = "Обратите внимание на рекомендации перед приемом: \(item.comment)"
                    }
                    
                    self.isShowAlertRecomend = StandartAlertData(titel: "Ваш прием подтвержден", text: strTmp, isShowCansel: false ,  someFuncOk: {() -> Void in
                        self.isShowAlertRecomend = nil
                        self.getVisits1()
                    }, someFuncCancel: {() -> Void in})
                    
                    
                } else {
                    if let t=error{
                        LoggingTree.e("ProfilePresenter/confirmBtnClick", t)
                    }
                    
                    self.showLoading(false)
                    self.showStandartAlert("", "Произошла ошибка при выполнении операции, попробуйте повторить")
                }
            })
        }else{
            LoggingTree.e("ProfilePresenter/confirmBtnClick sharePreferenses.currentUserInfo == nil || sharePreferenses.centerInfo == nil")
            self.showStandartAlert("", "Произошла ошибка при выполнении операции, попробуйте повторить")
            self.showLoading(false)
            return
        }
    }
    
    func iAmHereBtnClick(_ item : VisitResponseIos){
        showLoading(true)
        
        if(self.sharePreferenses.currentUserInfo != nil &&  self.sharePreferenses.centerInfo != nil){
            let apiKey = String.init(self.sharePreferenses.currentUserInfo!.apiKey!)
            let idUser=String(Int.init(truncating: self.sharePreferenses.currentUserInfo!.idUser!))
            let idBranch=String(Int.init(truncating: self.sharePreferenses.currentUserInfo!.idBranch!))
            let h_dbName = self.sharePreferenses.centerInfo!.db_name!
            
            sdk.sendIAmHere(user: String(Int(item.idUser!)), id_zapisi: String(Int(item.idRecord!)), idBranch: String(Int(item.idBranch!)),
                h_Auth: apiKey, h_dbName: h_dbName, h_idKl: idUser, h_idFilial:  idBranch,
                                      completionHandler: { response, error in
                if let res : SimpleResponseBoolean = response {
                    self.showLoading(false)
                    
                    var strTmp : String
                    if(self.statusIsPaid(item.status!)){
                        strTmp = "Вы можете сразу пройти к кабинету \(item.cabinet) и ожидать приглашения врача"
                    }else{
                        strTmp = "Необходимо подойти к регистратуре для оформления документов"
                    }
                    
                    self.isShowAlertRecomend = StandartAlertData(titel: "Рады приветствовать Вас в нашем медицинском центре!", text: strTmp, isShowCansel: false ,  someFuncOk: {() -> Void in
                        self.isShowAlertRecomend = nil
                        self.getVisits1()
                    }, someFuncCancel: {() -> Void in})
                    
                    
                } else {
                    if let t=error{
                        LoggingTree.e("ProfilePresenter/iAmHereBtnClick", t)
                    }
                    
                    self.showLoading(false)
                    self.showStandartAlert("", "Произошла ошибка при выполнении операции, попробуйте повторить")
                }
            })
        }else{
            LoggingTree.e("ProfilePresenter/iAmHereBtnClick sharePreferenses.currentUserInfo == nil || sharePreferenses.centerInfo == nil")
            self.showStandartAlert("", "Произошла ошибка при выполнении операции, попробуйте повторить")
            self.showLoading(false)
            return
        }
    }
    

    
    func cancelBtnClick(_ item : VisitResponseIos){
        if(!statusIsPaid(item.status!)){
            self.showDialogCancelReceptionData = ShowDialogCancelReceptionData(item: item,
                                                                               someFuncOk: {(i: String) -> Void in
                self.cancelBtnClickRequest(self.showDialogCancelReceptionData!.item, i)
                self.showDialogCancelReceptionData = nil},
                                                                               someFuncCancel: {() -> Void in
                self.showDialogCancelReceptionData = nil}
            )
        }else{
            self.showStandartAlert("", "Для отмены или переноса оплаченного приема необходимо обратиться к администраторам медицинского центра")
        }
    }
    func cancelBtnClickRequest(_ item : VisitResponseIos, _ reason : String){
        showLoading(true)
        
        if(self.sharePreferenses.currentUserInfo != nil &&  self.sharePreferenses.centerInfo != nil){
            let apiKey = String.init(self.sharePreferenses.currentUserInfo!.apiKey!)
            let idUser=String(Int.init(truncating: self.sharePreferenses.currentUserInfo!.idUser!))
            let idBranch=String(Int.init(truncating: self.sharePreferenses.currentUserInfo!.idBranch!))
            let h_dbName = self.sharePreferenses.centerInfo!.db_name!
            
            let date1 = Date()
            let dateFormatter = DateFormatter()
            dateFormatter.dateFormat = MDate.DATE_FORMAT_ddMMyy
            let date = dateFormatter.string(from: date1)
            
            
            sdk.sendCancellationOfVisit(user: String(Int(item.idUser!)), id_zapisi: String(Int(item.idRecord!)), cause: reason, currentData: date, idBranch: String(Int(item.idBranch!)),
                h_Auth: apiKey, h_dbName: h_dbName, h_idKl: idUser, h_idFilial:  idBranch,
                                      completionHandler: { response, error in
                if let res : SimpleResponseBoolean = response {
                    LoggingTree.v("Отмена приема id записи \(String(Int(item.idRecord!))) причина \(reason)")
                        self.getVisits1()
                } else {
                    if let t=error{
                        LoggingTree.e("ProfilePresenter/cancelBtnClickRequest", t)
                    }
                    
                    self.showLoading(false)
                    self.showStandartAlert("", "Произошла ошибка при выполнении операции, попробуйте повторить")
                }
            })
        }else{
            LoggingTree.e("ProfilePresenter/cancelBtnClickRequest sharePreferenses.currentUserInfo == nil || sharePreferenses.centerInfo == nil")
            self.showStandartAlert("", "Произошла ошибка при выполнении операции, попробуйте повторить")
            self.showLoading(false)
            return
        }
    }
    
    func statusIsPaid(_ status: String) -> Bool {
        return status == "p" || status == "wkp" || status == "kpp"
    }
    
    func showStandartAlert(_ title: String, _ text: String){
        self.isShowAlertRecomend = StandartAlertData(titel: title, text: text, isShowCansel: false ,  someFuncOk: {() -> Void in
            self.isShowAlertRecomend = nil
        }, someFuncCancel: {() -> Void in})
    }
    
    func showEmptyScreen(_ isShow : Bool){
        if isShow {
            showDialogEmptyScreen = true
            actualReceptions = []
            latestReceptions = []
        }else{
            showDialogEmptyScreen = false
        }
    }
    
    func showErrorScreen(_ isShow : Bool){
        if isShow {
            showDialogErrorScreen = true
            actualReceptions = []
            latestReceptions = []
        }else{
            showDialogErrorScreen = false
        }
        
    }
    
    func showLoading(_ isShow : Bool){
        if(isShow){
            self.showDialogLoading = true
        }else{
            self.showDialogLoading = false
        }
    }
    
    func checkCurrentUser(){
        let tt = sharePreferenses.currentUserInfo ?? UserResponse()
        
        if(currentUser.idUser != tt.idUser && !(self.sharePreferenses.currentPassword == nil || self.sharePreferenses.currentPassword!.isEmpty)){
            currentUser = tt
            self.getVisits1()
        }
    }
}
