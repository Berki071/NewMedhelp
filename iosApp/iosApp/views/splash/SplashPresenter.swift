//
//  SplashPresenter.swift
//  iosApp
//
//  Created by Михаил Хари on 26.05.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation
import SwiftUI
import shared
import FirebaseMessaging

extension SplashUIView {
    
    class SplashPresenter : ObservableObject {
        @Published var nextPage : String = "" //переход на след страницу
        @Published var selectedShow: AlertAttention?
        
        let sdk: NetworkManager
        var sharePreferenses : SharedPreferenses
        let netConnection = NetMonitor.shared
    
    
        init(){
            sdk=NetworkManager()
            sharePreferenses = SharedPreferenses()

            netConnection.startMonitoring()
            
            testOpenNextActivity ()
        }
        
        func testOpenNextActivity (){
            //let sig = "EwXFAQoz6ca1uJAQxbeU6YD4Eps"
            var login = sharePreferenses.currentLogin ?? nil
            var pass = sharePreferenses.currentPassword ?? nil
            
            let status = netConnection.connType
            if status == NetMonitor.ConnectionType.unknown{
                self.showAlert("Отсутствует соединение с интернетом")
            }else{
                if(login==nil || pass==nil){
                    self.showNextpage("Login")
                    return
                }else{
                    verifyUser (Constants.sig ,login,pass)
                }
            }
            
//            if(login == nil){
//                login = "9888888888"
//            }
//            if(pass==nil){
//                pass = "635088"
//            }
//            verifyUser (sig,login,pass)
        }
        
        func verifyUser (_ sig:String,_ login:String?,_ pass:String? ){
            
            sdk.doLoginApiCall(signature: sig ,username: login!, password:pass!, completionHandler: { response, error in
                if let res : UserList = response {
                    if(res.response[0].login != nil ){
                        self.sharePreferenses.usersLogin=res.response
         
                        // var tmp=String(describing: type(of: res.response))
                        //var tmp2=String(describing: type(of:  self.sharePreferenses.usersLogin))
                        
                        let tmpCurrentUserInfo = self.sharePreferenses.currentUserInfo
                        if(tmpCurrentUserInfo == nil || tmpCurrentUserInfo?.apiKey == nil){
                            self.sharePreferenses.currentUserInfo = res.response[0]
                        }
                        
                        self.updateHeaderInfo()
                    }else{
                        //self.sharePreferenses.currentPassword = ""
                        // self.sharePreferenses.usersLogin = nil
                        self.showNextpage("Login")
                    }
                    
                } else {
                    if let t=error{
                        LoggingTree.e("SplashPresenter/verifyUser", t)
                        self.showNextpage("Login")
                    }
                }
            })
        }
        
        func updateHeaderInfo(){
            let idCent=String(Int.init(truncating: self.sharePreferenses.currentUserInfo!.idCenter!))
            
            sdk.getCenterApiCall(idCenter: idCent, completionHandler: { response, error in
                if let res : CenterList = response {
                    self.sharePreferenses.centerInfo = res.response[0]
                    self.currentUserInfo()
                } else {
                    if let t=error{
                        LoggingTree.e("SplashPresenter/updateHeaderInfo", t)
                        self.showNextpage("Login")
                    }
                }
            })
        }
        
        func currentUserInfo(){
            
            let userLogin = self.sharePreferenses.usersLogin
            
            
            if userLogin.count == 0 {
                LoggingTree.e("SplashPresenter/currentUserInfo 0 , Ошибка загрузки информации о пользователе")
                self.showNextpage("Login")
                return
            }
            
            let apiKey = String.init(self.sharePreferenses.currentUserInfo!.apiKey!)
            let h_dbName = String.init(self.sharePreferenses.centerInfo!.db_name!)
            let idUser=String(Int.init(self.sharePreferenses.currentUserInfo!.idUser!))
            let idBranch=String(Int.init(self.sharePreferenses.currentUserInfo!.idBranch!))
            
            for i in  userLogin {
                let idUserItem=String(Int.init(i.idUser!))
                let idBranchItem=String(Int.init(i.idBranch!))
                
                sdk.getCurrentUserInfoInCenter(idUser: idUserItem, idBranch: idBranchItem, h_Auth: apiKey, h_dbName: h_dbName, h_idKl: idUser, h_idFilial:  idBranch, completionHandler: { response, error in
                    if let res : CurrentUserInfoList = response {
                        
                        i.surname = MUtils.companion.encodeDecodeWord(word: res.responses[0].surname!, key: res.responses[0].keySurname!)
                        i.name = res.responses[0].name
                        i.patronymic = res.responses[0].patronymic
                        i.phone = MUtils.companion.encodeDecodeWord(word: res.responses[0].phone!, key: res.responses[0].keyPhone!)
                        i.birthday = res.responses[0].birthday
                        i.email = res.responses[0].email
                        
                        if self.sharePreferenses.currentUserInfo!.idUser == i.idUser {
                                self.sharePreferenses.currentUserInfo = i
                        }
                        
                        var boo = true
                        
                        for tm in userLogin {
                            if tm.name == nil {
                                boo = false
                                break
                            }
                        }
                        
                        if boo {
                            self.sharePreferenses.usersLogin = userLogin
                            self.firebaseId()
                        }
                        
                    } else {
                        if let t=error{
                            LoggingTree.e("SplashPresenter/currentUserInfo", t)
                            self.showNextpage("Login")
                        }
                    }
                })
            }
        }
        
        func firebaseId (){
            Messaging.messaging().token { token, error in
              if let error = error {
                  LoggingTree.e("SplashPresenter/firebaseId", error)
                  self.showNextpage("Login")
              } else if let token = token {
              
                  self.sendFcmToken(token: token)
              }
            }
        }
        
        private var countFcmSend=0
        func sendFcmToken(token : String){
            let userLogin = self.sharePreferenses.usersLogin
            
            if userLogin.count == 0 {
                allHospitalBranc()
                return
            }
            
            countFcmSend=userLogin.count
            
            let apiKey = String.init(self.sharePreferenses.currentUserInfo!.apiKey!)
            let h_dbName = String.init(self.sharePreferenses.centerInfo!.db_name!)
            let idUser=String(Int.init(self.sharePreferenses.currentUserInfo!.idUser!))
            let idBranch=String(Int.init(self.sharePreferenses.currentUserInfo!.idBranch!))
            
            for i in userLogin {
                let idUserItem=String(Int.init(i.idUser!))
                let idBranchItem=String(Int.init(i.idBranch!))
                
                sdk.sendFcmId(idUser: idUserItem, idFilial: idBranchItem, idFcm: token, h_Auth: apiKey, h_dbName: h_dbName, h_idKl: idUser, h_idFilial:  idBranch, completionHandler: { response, error in
                    if let res : SimpleResBoolean = response {
                        self.countFcmSend -= 1
                        if self.countFcmSend <= 0 {
                            self.allHospitalBranc()
                        }
                        
                    } else {
                        if let t=error{
                            LoggingTree.e("LoginPresenter/restorePass", t)
                            self.showAlert("Произошла непредвиденная ошибка")
                            self.showNextpage("Main")
                        }
                    }
                })
            }
        }
        
        func allHospitalBranc(){
            let idCent = String(Int.init(self.sharePreferenses.currentUserInfo!.idCenter!))
            
            sdk.getAllHospitalBranch(idCenter: idCent, completionHandler: { response, error in
                if let res : SettingsAllBaranchHospitalList = response {
                    self.processingYandexKey(resp: res.response)
                    self.showNextpage("Main")
                } else {
                    if let t=error{
                        LoggingTree.e("SplashPresenter/getAllHospitalBranch", t)
                        self.showNextpage("Main")
                    }
                }
            })
        }
   
        func processingYandexKey(resp: [SettingsAllBranchHospitalResponse]){
            var keyExit = false
            var allExist = true
            
            for i in resp {
                if i.idShop == nil || i.idShop!.isEmpty{
                    allExist = false
                }else{
                    keyExit = true
                }
            }
            
            if allExist{
                self.sharePreferenses.yandexStoreIsWorks = true
            }else{
                self.sharePreferenses.yandexStoreIsWorks = false
                if keyExit {
                    LoggingTree.e("Не у всех филиалов прописан Yandex IdShop")
                }
            }
            
        }
        
        func showNextpage(_ page : String){
            //showAlert("nesxttttttttttttt")
            netConnection.stopMonitoring()
            self.nextPage = page
        }
        
        func showAlert(_ str: String ){
            selectedShow = AlertAttention(name: str)
        }
        
    }
}


